package ec.gob.digercic.renavi.view;

import ec.gob.digercic.renavi.entities.ProductoEmbarazoRenavi;
import ec.gob.digercic.renavi.view.util.JsfUtil;
import ec.gob.digercic.renavi.ejb.ProductoEmbarazoRenaviFacadeLocal;
import ec.gob.digercic.renavi.excepciones.EntidadException;

import java.io.Serializable;
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

@ManagedBean(name = "productoEmbarazoRenaviCDLController")
@ViewScoped
public class ProductoEmbarazoRenaviCDLController implements Serializable{

    private ProductoEmbarazoRenavi current;
    private List<ProductoEmbarazoRenavi> items;
    @EJB
    private ec.gob.digercic.renavi.ejb.ProductoEmbarazoRenaviFacadeLocal ejbFacade;

    public ProductoEmbarazoRenaviCDLController() {
    }

    public ProductoEmbarazoRenavi getSelected() {
        if (current == null) {
            current = new ProductoEmbarazoRenavi();
        }
        return current;
    }

    private ProductoEmbarazoRenaviFacadeLocal getFacade() {
        return ejbFacade;
    }

    public List<ProductoEmbarazoRenavi> getItems() {
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
            return JsfUtil.getSelectItems(ejbFacade.findByNamedQueryResultList("ProductoEmbarazoRenavi.findAll"), false);
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("NoCargaDeCatalogos"), JsfUtil.ERROR_MESSAGE);
            return null;
        }
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        try {
            if (items == null) {
                items = ejbFacade.findByNamedQueryResultList("ProductoEmbarazoRenavi.findAll");
                
               
            }
            return JsfUtil.getSelectItems(items, true);
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
            getFacade().create(current);
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ProductoEmbarazoRenaviCreado"), JsfUtil.INFO_MESSAGE);
            current = new ProductoEmbarazoRenavi();
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
            JsfUtil.displayMessage(ResourceBundle.getBundle("/ec/gob/digercic/renavi/view/resources/Mensajes").getString("ProductoEmbarazoRenaviEliminado"), JsfUtil.INFO_MESSAGE);
            return "List";
        } catch (EntidadException ee) {
            ee.printStackTrace();
            JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
            return "List";
        }
    }


    @FacesConverter(forClass = ProductoEmbarazoRenavi.class)
    public static class ProductoEmbarazoRenaviControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try {
                if (value == null || value.length() == 0) {
                    return null;
                }
                ProductoEmbarazoRenaviCDLController controller = (ProductoEmbarazoRenaviCDLController) facesContext.getApplication().getELResolver().
                        getValue(facesContext.getELContext(), null, "productoEmbarazoRenaviCDLController");
                return controller.ejbFacade.find(getKey(value));
            } catch (EntidadException ee) {
                ee.printStackTrace();
                JsfUtil.displayMessage(ee.getMessage(), JsfUtil.ERROR_MESSAGE);
                return null;
            }
        }

        Integer getKey(String value) {
            Integer key;
            key = new Integer(value);
            return key;
        }

        String getStringKey(Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ProductoEmbarazoRenavi) {
                ProductoEmbarazoRenavi o = (ProductoEmbarazoRenavi) object;
                return getStringKey(o.getIdProEmb());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ProductoEmbarazoRenavi.class.getName());
            }
        }

    }

}
