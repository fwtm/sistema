/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "TBL_SAUREG_ESTADOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregEstados.findAll", query = "SELECT t FROM TblSauregEstados t"),
    @NamedQuery(name = "TblSauregEstados.findByIdEstados", query = "SELECT t FROM TblSauregEstados t WHERE t.idEstados = :idEstados"),
    @NamedQuery(name = "TblSauregEstados.findByDescripcion", query = "SELECT t FROM TblSauregEstados t WHERE t.descripcion = :descripcion")})
public class TblSauregEstados implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ESTADOS")
    private Long idEstados;
    @Size(max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(mappedBy = "idEstados", fetch = FetchType.LAZY)
    private List<TblSauregUsuario> tblSauregUsuarioList;

    public TblSauregEstados() {
    }

    public TblSauregEstados(Long idEstados) {
        this.idEstados = idEstados;
    }

    public Long getIdEstados() {
        return idEstados;
    }

    public void setIdEstados(Long idEstados) {
        this.idEstados = idEstados;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<TblSauregUsuario> getTblSauregUsuarioList() {
        return tblSauregUsuarioList;
    }

    public void setTblSauregUsuarioList(List<TblSauregUsuario> tblSauregUsuarioList) {
        this.tblSauregUsuarioList = tblSauregUsuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstados != null ? idEstados.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregEstados)) {
            return false;
        }
        TblSauregEstados other = (TblSauregEstados) object;
        if ((this.idEstados == null && other.idEstados != null) || (this.idEstados != null && !this.idEstados.equals(other.idEstados))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregEstados[ idEstados=" + idEstados + " ]";
    }
    
}
