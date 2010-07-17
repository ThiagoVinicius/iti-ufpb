package jogoshannon.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SessaoInvalidaException extends Exception implements Serializable {

    public SessaoInvalidaException() {
        super();
    }

    public SessaoInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessaoInvalidaException(String message) {
        super(message);
    }

    public SessaoInvalidaException(Throwable cause) {
        super(cause);
    }

}
