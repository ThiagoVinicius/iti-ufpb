package jogoshannon.server.persistent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.listener.StoreCallback;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Obra implements StoreCallback {
    
    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
    private Key key;
    
    public Key getKey() {
        return key;
    }

    @Persistent
    private Date lastModified;
    
    public String getCodificacao() {
        return codificacao;
    }

    public void setCodificacao(String codificacao) {
        this.codificacao = codificacao;
    }

    public long getCharLen() {
        return charLen;
    }

    public void setCharLen(long charLen) {
        this.charLen = charLen;
    }

    @Persistent
    private String titulo;
    
    @Persistent
    private String autor;
    
    @Persistent
    private Text descricao;
    
    @Persistent(serialized="true")
    private byte conteudo[];
    
    @NotPersistent
    private ByteArrayOutputStream tmpConteudo;
    
    @NotPersistent 
    private OutputStream tmpout;
    
    @Persistent
    private String uploadUrl;
    
    @Persistent
    private String codificacao;
    
    @Persistent
    private long charLen;
    
    private Obra() {
    }
    
    public Obra(String titulo, String autor, String descricao, String uploadUrl) {
        this.titulo = titulo;
        this.autor = autor;
        this.descricao = new Text(descricao);
        this.uploadUrl = uploadUrl;
    }
    
    public Obra(String titulo, String autor, String descricao) {
        this.titulo = titulo;
        this.autor = autor;
        this.descricao = new Text(descricao);
    }

    @Override
    public void jdoPreStore() {
        updateConteudo(false);
        setLastModified(new Date());
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDescricao() {
        return descricao.getValue();
    }

    public void setDescricao(String descricao) {
        this.descricao = new Text(descricao);
    }
    
    public Date getLastModified() {
        return lastModified;
    }

    private void setLastModified(Date newValue) {
        lastModified = newValue;
    }
    
    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
    
    public synchronized OutputStream getOutput () throws IOException {
        tmpConteudo = new ByteArrayOutputStream();
        tmpout = new GZIPOutputStream(tmpConteudo) {
            @Override
            public void close() throws IOException {
                super.close();
                updateConteudo(true);
            }
        };
        return tmpout;
    }
    
    public synchronized InputStream open (long offset) throws IOException {
        return new GZIPInputStream(new ByteArrayInputStream(conteudo));
    }
    
    private synchronized void updateConteudo (boolean callback) {
        if (callback) {
            conteudo = tmpConteudo.toByteArray();
            tmpConteudo = null;
            tmpout = null;
        }
        else if (tmpConteudo != null && tmpout != null) {
            try {
                tmpout.close();
            } catch (IOException e) {
            }
        }
    }
    
    
    
    @Override
    public String toString () {
        return "Titulo: " + titulo + "\n" +
        	   "Autor: " + autor + "\n" +
        	   "Descricao: " + descricao.getValue() + "\n" +
        	   "Upload url: " + (uploadUrl == null ? "Upload efetuado" : uploadUrl);
    }
    
}
