package ec.gob.digercic.renavi.utilitario.generico;

import java.io.Serializable;

/**
 * @author Daniel Porras
 */
public class ParametroConsulta implements Serializable {

    private static final long serialVersionUID = 3823440874951355517L;
    private String nombreParametro;
    private Object valorParametro;

    /**
     * Constructor
     */
    public ParametroConsulta() {
    }

    /**
     *
     * @param nombreParametro nombre del par�metro asignado a la consulta
     * @param valorParametro valor del par�metro asignado a la consulta
     */
    public ParametroConsulta(String nombreParametro, Object valorParametro) {
        super();
        this.nombreParametro = nombreParametro;
        this.valorParametro = valorParametro;
    }

    public String getNombreParametro() {
        return nombreParametro;
    }

    public void setNombreParametro(String nombreParametro) {
        this.nombreParametro = nombreParametro;
    }

    public Object getValorParametro() {
        return valorParametro;
    }

    public void setValorParametro(Object valorParametro) {
        this.valorParametro = valorParametro;
    }
}
