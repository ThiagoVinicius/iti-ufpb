package jogoshannon.shared;

import java.io.Serializable;

public class UsuarioNaoEncontradoException extends Exception implements
        Serializable {

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

}
