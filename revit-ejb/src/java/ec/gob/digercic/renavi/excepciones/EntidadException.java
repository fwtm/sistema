package ec.gob.digercic.renavi.excepciones;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class EntidadException extends Exception {

    private static final long serialVersionUID = 1L;

    public EntidadException() {
        super();
    }

    public EntidadException(String message) {
        super(message);
    }

    public EntidadException(Throwable cause) {
        super(cause);
    }

    public EntidadException(String message, Throwable cause) {
        super(message, cause);
    }
}
