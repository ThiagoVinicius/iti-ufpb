package jogoshannon.shared;

import java.io.Serializable;

public class SessaoInvalidaException extends Exception implements Serializable {

    public SessaoInvalidaException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public SessaoInvalidaException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public SessaoInvalidaException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public SessaoInvalidaException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
