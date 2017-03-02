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
@Table(name = "TBL_SAUREG_PERMISO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregPermiso.findAll", query = "SELECT t FROM TblSauregPermiso t"),
    @NamedQuery(name = "TblSauregPermiso.findByIdPermiso", query = "SELECT t FROM TblSauregPermiso t WHERE t.idPermiso = :idPermiso"),
    @NamedQuery(name = "TblSauregPermiso.findByDescripcion", query = "SELECT t FROM TblSauregPermiso t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TblSauregPermiso.findByCodCertificado", query = "SELECT t FROM TblSauregPermiso t WHERE t.codCertificado = :codCertificado"),
    @NamedQuery(name = "TblSauregPermiso.findByCertificado", query = "SELECT t FROM TblSauregPermiso t WHERE t.certificado = :certificado")})
public class TblSauregPermiso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PERMISO")
    private Long idPermiso;
    @Size(max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 7)
    @Column(name = "COD_CERTIFICADO")
    private String codCertificado;
    @Size(max = 50)
    @Column(name = "CERTIFICADO")
    private String certificado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblSauregPermiso", fetch = FetchType.LAZY)
    private List<TblSauregCompRol> tblSauregCompRolList;

    public TblSauregPermiso() {
    }

    public TblSauregPermiso(Long idPermiso) {
        this.idPermiso = idPermiso;
    }

    public Long getIdPermiso() {
        return idPermiso;
    }

    public void setIdPermiso(Long idPermiso) {
        this.idPermiso = idPermiso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodCertificado() {
        return codCertificado;
    }

    public void setCodCertificado(String codCertificado) {
        this.codCertificado = codCertificado;
    }

    public String getCertificado() {
        return certificado;
    }

    public void setCertificado(String certificado) {
        this.certificado = certificado;
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
        hash += (idPermiso != null ? idPermiso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregPermiso)) {
            return false;
        }
        TblSauregPermiso other = (TblSauregPermiso) object;
        if ((this.idPermiso == null && other.idPermiso != null) || (this.idPermiso != null && !this.idPermiso.equals(other.idPermiso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregPermiso[ idPermiso=" + idPermiso + " ]";
    }
    
}
