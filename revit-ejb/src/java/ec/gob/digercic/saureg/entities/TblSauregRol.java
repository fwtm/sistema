/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author daniel.porras
 */
@Entity
@Table(name = "TBL_SAUREG_ROL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregRol.findAll", query = "SELECT t FROM TblSauregRol t"),
    @NamedQuery(name = "TblSauregRol.findByIdRol", query = "SELECT t FROM TblSauregRol t WHERE t.idRol = :idRol"),
    @NamedQuery(name = "TblSauregRol.findByDescripcion", query = "SELECT t FROM TblSauregRol t WHERE t.descripcion = :descripcion")
})
public class TblSauregRol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ROL")
    private Long idRol;
    @Size(max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "STATUS")
    private Character status;
    @OneToMany(mappedBy = "idRol", fetch = FetchType.LAZY)
    private List<TblSauregSegAcceso> tblSauregSegAccesoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblSauregRol", fetch = FetchType.LAZY)
    private List<TblSauregRolSist> tblSauregRolSistList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblSauregRol", fetch = FetchType.LAZY)
    private List<TblSauregCompRol> tblSauregCompRolList;

    public TblSauregRol() {
    }

    public TblSauregRol(Long idRol) {
        this.idRol = idRol;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    @XmlTransient
    public List<TblSauregSegAcceso> getTblSauregSegAccesoList() {
        return tblSauregSegAccesoList;
    }

    public void setTblSauregSegAccesoList(List<TblSauregSegAcceso> tblSauregSegAccesoList) {
        this.tblSauregSegAccesoList = tblSauregSegAccesoList;
    }

    @XmlTransient
    public List<TblSauregRolSist> getTblSauregRolSistList() {
        return tblSauregRolSistList;
    }

    public void setTblSauregRolSistList(List<TblSauregRolSist> tblSauregRolSistList) {
        this.tblSauregRolSistList = tblSauregRolSistList;
    }

    @XmlTransient
    public List<TblSauregCompRol> getTblSauregCompRolList() {
        return tblSauregCompRolList;
    }

    public void setTblSauregCompRolList(List<TblSauregCompRol> tblSauregCompRolList) {
        this.tblSauregCompRolList = tblSauregCompRolList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRol != null ? idRol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregRol)) {
            return false;
        }
        TblSauregRol other = (TblSauregRol) object;
        if ((this.idRol == null && other.idRol != null) || (this.idRol != null && !this.idRol.equals(other.idRol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregRol[ idRol=" + idRol + " ]";
    }
    
}
