package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.NacidoVivoRenavi;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal;
import ec.gob.digercic.renavi.entities.EstadoFirmaRenavi;
import ec.gob.digercic.renavi.entities.EstadoRegistroRenavi;
import ec.gob.digercic.renavi.excepciones.EntidadException;
import java.io.File;
import java.io.FileOutputStream;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

@ManagedBean(name = "nacidoVivoRenaviCDLController")
@ViewScoped
public class NacidoVivoRenaviCDLController implements Serializable {

    private NacidoVivoRenavi current;
    private List<NacidoVivoRenavi> items;
    @EJB
    private ec.gob.digercic.renavi.ejb.NacidoVivoRenaviFacadeLocal ejbFacade;
    private String tempSelecComboProvCantParr;
    //Fecha para el registro de auditoria y para validación de calendario
    //en los que se deba ingresar maxdate la fecha actual
    private Date fechaActual = new Date();

    public NacidoVivoRenaviCDLController() {
    }

    public NacidoVivoRenavi getSelected() {
        if (current == null) {
            current = new NacidoVivoRenavi();
        }
        return current;
    }

    private NacidoVivoRenaviFacadeLocal getFacade() {
        return ejbFacade;
    }

    public List<NacidoVivoRenavi> getItems() {
        try {
            items = getFacade().findAll();
            return items;
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeRegistros"), JsfUtil.ERROR_MESSAGE);
            return null;
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        try {
            return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeCatalogos"), JsfUtil.ERROR_MESSAGE);
            return null;
        }
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        try {
            return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeCatalogos"), JsfUtil.ERROR_MESSAGE);
            return null;
        }
    }

    public String prepareList() {
        return "List";
    }

    public String prepareView() {
        return "View";
    }

    public String prepareCreate() {
        return "Create";
    }

    public String prepareEdit() {
        return "Edit";
    }

    public String create() {
        try {
            current.setFkIdEstFir((EstadoFirmaRenavi) ejbFacade.
                    findByNamedQuerySingleResult("EstadoFirmaRenavi.findByIdEstFir1"));
            current.setFkIdEstReg((EstadoRegistroRenavi) ejbFacade.
                    findByNamedQuerySingleResult("EstadoRegistroRenavi.findByIdEstReg2"));
            getFacade().create(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NacidoVivoRenaviCreado"), JsfUtil.INFO_MESSAGE);
            current = new NacidoVivoRenavi();
            return "Create";
        } catch (Exception ee) {
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "Create";
        }
    }

    public String destroy() {
        try {
            Integer id = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            current = getFacade().find(id);
            getFacade().remove(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NacidoVivoRenaviEliminado"), JsfUtil.INFO_MESSAGE);
            return "List";
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "List";
        }
    }

    @FacesConverter(forClass = NacidoVivoRenavi.class)
    public static class NacidoVivoRenaviControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try {
                if (value == null || value.length() == 0) {
                    return null;
                }
                NacidoVivoRenaviCDLController controller = (NacidoVivoRenaviCDLController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "nacidoVivoRenaviCDLController");
                return controller.ejbFacade.find(getKey(value));
            } catch (EntidadException ee) {
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                return null;
            }
        }

        java.math.BigDecimal getKey(String value) {
            java.math.BigDecimal key;
            key = new java.math.BigDecimal(value);
            return key;
        }

        String getStringKey(Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof NacidoVivoRenavi) {
                NacidoVivoRenavi o = (NacidoVivoRenavi) object;
                return getStringKey(o.getIdNacViv());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + NacidoVivoRenavi.class.getName());
            }
        }

    }

    public String getTempSelecComboProvCantParr() {
        return tempSelecComboProvCantParr;
    }

    public void setTempSelecComboProvCantParr(String tempSelecComboProvCantParr) {
        this.tempSelecComboProvCantParr = tempSelecComboProvCantParr;
    }

    public Date getFechaActual() {
        return fechaActual;
    }
    
    public void recuperaPDFs() throws Exception{
        try{
            String query = "select * from nacido_vivo_renavi n where n.id_nac_viv in(";
            query += "182,185,186,199,206,209,210,216,218,229,230,231,232,238,240,241,244,246,247,281,310,311,313,320,321,324,325,336,354,355,356,358,357,359,360,361,362,364,363,365,379,384,385,386,390,391,393,394,396,397,403,402,401,404,407,406,405,408,409,415,418,431,435,436,437,438,439,446,441,443,442,450,449,448,447,451,456,455,454,457,460,462,463,465,468,470,471,472,473,476,474,478,480,481,482,484,485,488,492,494,498,499,500,501,502,503,552,591,649,651,652,669,692,697,698,700,699,701,713,714,717,718,719,734,739,740,745,747,748,749,752,750,753,754,755,756,751,766,764,765,777,778,779,780,781,782,784,787,786,794,791,788,796,797,798,799,800,801,802,805,807,810,809,815,816,817,818,814,811,825,827,819,822,820,821,823,828,826,833,831,834,830,836,835,837,832,869,868,871,870,872,873,874,875,876,877,883,886,893,892,891,887,897,899,896,894,895,907,906,915,916,917,919,920,923,925,936,941,942,945,943,949,946,948,953,952,960,962,955,967,968,970,969,973,974,975,980,987,988,989,990,991,992,993,994,1008,1006,1007,1005,1004,1003,1001,1000,1025,1028,1029,1030,1031,1032,1034,1035,1036,1037,1039,1038,1041,1042,1052,1054,1055,1056,1057,1059,1060,1066,1068)";
            items = ejbFacade.executeNativeQueryListResultGenerico(query, NacidoVivoRenavi.class);
            int i = 1;
            for(NacidoVivoRenavi n : items){
                String namepdfsin = "E:\\DANIEL\\DOCUMENTOS DGRCIC\\2014\\Sistema Nacido Vivo\\Reportes\\sinfirma\\" + n.getCedulNacViv() + "_sinfirma.pdf";
                writeToFile(namepdfsin, n.getPdfSinFirmaNacViv());
                String namepdfcon = "E:\\DANIEL\\DOCUMENTOS DGRCIC\\2014\\Sistema Nacido Vivo\\Reportes\\confirma\\" + n.getCedulNacViv() + "_confirma.pdf";
                writeToFile(namepdfcon, n.getPdfConFirmaNacViv());
                System.out.println("--->" + i);
                i++;
            }
        }catch(EntidadException ee){
            ee.printStackTrace();
        }
    }
    
    private void writeToFile(String name, byte[] data) throws Exception {
        File someFile = new File(name);
        FileOutputStream fos = new FileOutputStream(someFile);
        fos.write(data);
        fos.flush();
        fos.close();
    }

}
