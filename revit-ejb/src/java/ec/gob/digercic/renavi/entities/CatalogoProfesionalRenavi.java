/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author fabricio.toapanta
 */
@Entity
@Table(name = "CATALOGOPROFESIONAL_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CatalogoProfesionalRenavi.finfAll", query = "SELECT c FROM CatalogoProfesionalRenavi c"),
    @NamedQuery(name = "CatalogoProfesionalRenavi.findByNombreProfeRenavi", query = "SELECT c FROM CatalogoProfesionalRenavi c WHERE c.nombreCatProf = :nombreCatProf AND c.statusCatProf = :statusPrfo"),
    @NamedQuery(name = "CatalogoProfesionalRenavi.findByRoles", query = "SELECT c FROM CatalogoProfesionalRenavi c WHERE c.nombreCatProf IN :lstProfesion AND c.statusCatProf = :statusProf")
    })
public class CatalogoProfesionalRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CAT_PROF")
    private String idCatProf;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOM_CAT_PROF")
    private String nombreCatProf;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "DESC_CAT_PROF")
    private String descripcionCatProf;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1)
    @Column(name = "STATUS_CAT_PROF")
    private String statusCatProf;

    public String getIdCatProf() {
        return idCatProf;
    }

    public void setIdCatProf(String idCatProf) {
        this.idCatProf = idCatProf;
    }

    public String getNombreCatProf() {
        return nombreCatProf;
    }

    public void setNombreCatProf(String nombreCatProf) {
        this.nombreCatProf = nombreCatProf;
    }

    public String getDescripcionCatProf() {
        return descripcionCatProf;
    }

    public void setDescripcionCatProf(String descripcionCatProf) {
        this.descripcionCatProf = descripcionCatProf;
    }

    public String getStatusCatProf() {
        return statusCatProf;
    }

    public void setStatusCatProf(String statusCatProf) {
        this.statusCatProf = statusCatProf;
    }
    
     @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCatProf != null ? idCatProf.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatalogoProfesionalRenavi)) {
            return false;
        }
        CatalogoProfesionalRenavi other = (CatalogoProfesionalRenavi) object;
        if ((this.idCatProf == null && other.idCatProf != null) || (this.idCatProf != null && !this.idCatProf.equals(other.idCatProf))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return nombreCatProf;
    }
}
