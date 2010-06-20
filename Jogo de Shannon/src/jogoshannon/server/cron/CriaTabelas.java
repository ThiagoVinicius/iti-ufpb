package jogoshannon.server.cron;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jogoshannon.server.Desafio;
import jogoshannon.server.FraseStore;
import jogoshannon.server.GestorPersistencia;
import jogoshannon.server.JuizSoletrandoImpl;
import jogoshannon.server.Usuario;
import jogoshannon.shared.Frase;

public class CriaTabelas extends HttpServlet {

	private static final Frase frases[] = {
		new Frase("ERROR JOGO DE SHANNON"),
		new Frase("HAVIA UMA PEDRA NO MEIO"),
		new Frase("PEDRO ME DA MEU CHIPE"),
	};

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Logger.getLogger(CriaTabelas.class.getName()).log(
				Level.WARNING, "Executando script de criacao de tabelas");
		
		PersistenceManager pm = GestorPersistencia.get()
				.getPersistenceManager();

		Query consulta = pm.newQuery(FraseStore.class);
		
		List<FraseStore> fraseStore;
		List<Usuario> usuario;
		try {
			
			fraseStore = (List<FraseStore>) consulta.execute();
			if (fraseStore.size() == 0) {
				Logger.getLogger(CriaTabelas.class.getName()).log(
						Level.WARNING, "Banco (Frases) estava vazio.");
				for (Frase f : frases) {
					FraseStore frase = new FraseStore(f);
					pm.makePersistent(frase);
				}
			} else {
				Logger.getLogger(CriaTabelas.class.getName()).log(
						Level.WARNING, "Banco (Frases) possuia dados.");
			}
			
			usuario = (List<Usuario>) pm.newQuery(Usuario.class).execute();
			if (usuario.size() == 0) {
				Logger.getLogger(CriaTabelas.class.getName()).log(
						Level.WARNING, "Banco (Usuarios) estava vazio.");
				
				Usuario novo = new Usuario();
				novo.getDesafios().add(new Desafio(0));
				pm.makePersistent(novo);
				
			} else {
				Logger.getLogger(CriaTabelas.class.getName()).log(
						Level.WARNING, "Banco (Usuarios) possuia dados.");
			}
			
			Logger.getLogger(CriaTabelas.class.getName()).log(
					Level.WARNING, "Executado com sucesso");
			
		} catch (Exception e) {
			Logger.getLogger(CriaTabelas.class.getName()).log(
					Level.WARNING, "Execução falhou", e);
		} finally {
			pm.close();	
		}
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.print("<html><body></body></html>");

		
	}

}
