/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author daniel.porras
 */
@Entity
@Table(name = "TBL_SAUREG_SEG_ACCESO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregSegAcceso.findAll", query = "SELECT t FROM TblSauregSegAcceso t"),
    @NamedQuery(name = "TblSauregSegAcceso.findByIdSegAcceso", query = "SELECT t FROM TblSauregSegAcceso t WHERE t.idSegAcceso = :idSegAcceso")
})
public class TblSauregSegAcceso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_SEG_ACCESO")
    private Long idSegAcceso;
     @Size(max = 1)
    @Column(name = "STATUS")
    private String status;
    @JoinColumn(name = "NOM_USUARIO", referencedColumnName = "NOM_USUARIO")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblSauregUsuario nomUsuario;
    @JoinColumn(name = "ID_ROL", referencedColumnName = "ID_ROL")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblSauregRol idRol;

    public TblSauregSegAcceso() {
    }

    public TblSauregSegAcceso(Long idSegAcceso) {
        this.idSegAcceso = idSegAcceso;
    }

    public Long getIdSegAcceso() {
        return idSegAcceso;
    }

    public void setIdSegAcceso(Long idSegAcceso) {
        this.idSegAcceso = idSegAcceso;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TblSauregUsuario getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(TblSauregUsuario nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public TblSauregRol getIdRol() {
        return idRol;
    }

    public void setIdRol(TblSauregRol idRol) {
        this.idRol = idRol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSegAcceso != null ? idSegAcceso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregSegAcceso)) {
            return false;
        }
        TblSauregSegAcceso other = (TblSauregSegAcceso) object;
        if ((this.idSegAcceso == null && other.idSegAcceso != null) || (this.idSegAcceso != null && !this.idSegAcceso.equals(other.idSegAcceso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregSegAcceso[ idSegAcceso=" + idSegAcceso + " ]";
    }
    
}
