package jogoshannon.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jogoshannon.server.persistent.Obra;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class UploadObra extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UploadObra.class);
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        logger.info("Recebido upload de arquivo.");
        
        String uriAtual = req.getRequestURI();
        
        PersistenceManager pm = GestorPersistencia.get().getPersistenceManager();
        
        ServletFileUpload uploadReader = new ServletFileUpload();
        
        try {
            Query query = pm.newQuery(Obra.class);
            query.setFilter("uploadUrl == uriAtual");
            query.declareParameters("String uriAtual");
            List<Obra> result = (List<Obra>) query.execute(uriAtual);
            logger.info("Localizadas {} obras (deve ser <= 1)", result.size());
            if (result.size() != 1) {
                logger.warn("Mais de uma obra (ou nenhuma) foi localizada. Abortando.");
                resp.sendRedirect("/admin/upload/obra?sucesso=0");
            }
            
            FileItemIterator listaArquivos = uploadReader.getItemIterator(req);
            FileItemStream arquivo = listaArquivos.next();
            if (arquivo.isFormField()) {
                logger.warn("Existem campos no formulário que não são arquivos");
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                        "Existem campos no formulário que não são arquivos");
                
            }
            
            byte buf[] = new byte[1 << 16];
            
            InputStream in = arquivo.openStream();
            Obra obra = result.get(0);
            OutputStream out = obra.getOutput();
            for (int read = in.read(buf); read >= 0; read = in.read(buf)) {
                out.write(buf, 0, read);
            }
            in.close();
            out.close();
            obra.setUploadUrl(null);
            logger.info("Persistindo obra:\n{}", obra);
            pm.makePersistent(obra);
        } catch (Exception e) {
            logger.warn("", e);
            resp.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
        } finally {
            pm.close();
        }
        
        resp.getWriter().write("OK");
        
    }
    
}
