package jogoshannon.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UsuarioNaoEncontradoException extends Exception implements
        Serializable {

    private long id;
    
    public UsuarioNaoEncontradoException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UsuarioNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public UsuarioNaoEncontradoException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public UsuarioNaoEncontradoException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
    public UsuarioNaoEncontradoException(long id) {
        super();
        this.id = id;
    }
    
    public long getId () {
        return id;
    }

}
