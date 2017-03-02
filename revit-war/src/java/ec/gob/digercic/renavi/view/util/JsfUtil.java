package ec.gob.digercic.renavi.view.util;

import java.sql.Connection;
import java.lang.reflect.Field;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JsfUtil {

    /*
     * Las siguientes constantes fueron declaradas para el manejo de los
     * mensajes JSF
     */
    public static final String ERROR_MESSAGE = "ERROR";
    public static final String INFO_MESSAGE = "INFO";
    public static final String FATAL_MESSAGE = "FATAL";
    public static final String WARN_MESSAGE = "WARN";

    //Pruebas y Desarrollo
//   public static final String USER_ACCWS_MADRE="renavi";
//   public static final String PASS_ACCWS_MADRE="r3N@vI";
//   public static final String USER_ACCWSNOMBRES_MADRE="renavi2";
//   public static final String PASS_ACCWSNOMBRES_MADRE="r3N@vI2";
//   //Producción
    public static final String USER_ACCWS_MADRE = "renavi";
    public static final String PASS_ACCWS_MADRE = "Ren@Vi3lkJ";
    public static final String USER_ACCWSNOMBRES_MADRE = "renavi2";
    public static final String PASS_ACCWSNOMBRES_MADRE = "Ren@V1EnUY";

    /*Para el nacido Vivo*/
    public static final Integer STAT_DAT_INCOMPLETOS = 1;
    public static final Integer STAT_DAT_MADRE = 2;
    public static final Integer STAT_DAT_NACVIVO = 3;
    public static final Integer STAT_DAT_DOCTOR = 4;
    public static final Integer STAT_DAT_DIGERCIG = 5;
    public static final Integer STAT_ANULADO = 6;
    public static final Integer STAT_PEND_EDIT = 7;
    public static final Integer STAT_ANULADO_FE = 8;
    public static final Integer COD_PAIS_EC = 81;
    /*Para el fallecido*/
    public static final Integer STAT_DAT_INCOMPLETOS_FAL = 1;
    public static final Integer STAT_DAT_FALLECIDO = 2;
    public static final Integer STAT_DAT_DATOS_FALLECIDO = 3;
    public static final Integer STAT_DAT_DOCTOR_FAL = 4;
    public static final Integer STAT_DAT_DIGERCIG_FAL = 5;
    //--para si es certificado por medico o no
    public static final Integer CERTIFICACION_MEDICA_SI = 1;
    public static final Integer CERTIFICACION_MEDICA_NO = 2;

    /*Para la firma*/
    public static final Integer STAT_FIR_SIN = 1;
    public static final Integer STAT_FIR_CON = 2;

    public static final String ESTADO_REG_ACTIVO = "A";
    public static final String ESTADO_REG_INACTIVO = "I";

    //paginas
    public static final String PAG_CREARNV = "9";
    public static final String PAG_FIRMARNV = "11";

    //Para el tipo de identificacion en los registros de madre (identificada y no identificada)
    public static final Integer STAT_IDENT = 1;
    public static final Integer STAT_INDOC = 2;
    //public static final String SEXO_MADRE = "FEMENINO";
    public static final String SEXO_MADRE = "MUJER";
    public static final short STAT_NUM_PART_SIST = 1;

    //Para el cierre 
    public static final String ESTADO_GUARDADO = "G";
    public static final String ESTADO_CERRADO = "C";

    //roles de usuario
    
    public static final Long ROL_DIGITADOR = Long.parseLong("1");
     /*-----------------PRODUCCION----------------*/
    public static final Long ROL_SUPERVISOR = Long.parseLong("186");
    public static final Long ROL_MEDICO = Long.parseLong("166"); 
    public static final Long ROL_OBSTETRIZ = Long.parseLong("167");
    public static final Long ROL_JEFE_SERVICIO = Long.parseLong("149");
    public static final Long ROL_GERENTE_UNIDAD = Long.parseLong("150");
    /*-----------------PRE PRODUCCION-------------*/
//    public static final Long ROL_SUPERVISOR = Long.parseLong("86");
//    public static final Long ROL_MEDICO = Long.parseLong("200000");
//    public static final Long ROL_OBSTETRIZ = Long.parseLong("200001");
//    public static final Long ROL_JEFE_SERVICIO = Long.parseLong("149");
//    public static final Long ROL_GERENTE_UNIDAD = Long.parseLong("150");
    /*-----------------DESARROLLO----------------*/
//    public static final Long ROL_SUPERVISOR = Long.parseLong("46");
//    public static final Long ROL_MEDICO = Long.parseLong("426");
//    public static final Long ROL_OBSTETRIZ = Long.parseLong("409");
//    public static final Long ROL_JEFE_SERVICIO = Long.parseLong("186");
//    public static final Long ROL_GERENTE_UNIDAD = Long.parseLong("187");
    /*------------------------------------------------------------------------*/

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem(null, "SELECCIONE");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static void displayMessage(String message, String severity) {
        FacesMessage facesMessage = null;
        if (severity.equals("ERROR")) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
        } else if (severity.equals("FATAL")) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_FATAL, message, message);
        } else if (severity.equals("INFO")) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, message);
        } else if (severity.equals("WARN")) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, message, message);
        }
        FacesContext.getCurrentInstance().addMessage(FacesMessage.FACES_MESSAGES, facesMessage);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static void removeSessionBackingBean(final String sessionBackingBeanName) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                .remove(sessionBackingBeanName);
    }

    /**
     * DFJ => Retorna un objeto Connection a partir del Pool de Conexiones del
     * Glassfish
     *
     * @return
     */
    public static Connection getConecction() {
        Connection conConexion = null;
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("jdbc/__Oracle");
            conConexion = ds.getConnection(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conConexion;
    }
}
