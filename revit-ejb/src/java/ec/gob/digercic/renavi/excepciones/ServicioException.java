package ec.gob.digercic.renavi.excepciones;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ServicioException extends Exception {

    private static final long serialVersionUID = 1L;

    public ServicioException() {
        super();
    }

    public ServicioException(String message) {
        super(message);
    }

    public ServicioException(Throwable cause) {
        super(cause);
    }

    public ServicioException(String message, Throwable cause) {
        super(message, cause);
    }
}
