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
@Table(name = "TBL_SAUREG_SISTEMA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregSistema.findAll", query = "SELECT t FROM TblSauregSistema t"),
    @NamedQuery(name = "TblSauregSistema.findByIdSistema", query = "SELECT t FROM TblSauregSistema t WHERE t.idSistema = :idSistema"),
    @NamedQuery(name = "TblSauregSistema.findByCodSist", query = "SELECT t FROM TblSauregSistema t WHERE t.codSist = :codSist"),
    @NamedQuery(name = "TblSauregSistema.findByNomSist", query = "SELECT t FROM TblSauregSistema t WHERE t.nomSist = :nomSist"),
    @NamedQuery(name = "TblSauregSistema.findByDescripcion", query = "SELECT t FROM TblSauregSistema t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TblSauregSistema.findByUrlSistema", query = "SELECT t FROM TblSauregSistema t WHERE t.urlSistema = :urlSistema")})
public class TblSauregSistema implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_SISTEMA")
    private Long idSistema;
    @Size(max = 20)
    @Column(name = "COD_SIST")
    private String codSist;
    @Size(max = 50)
    @Column(name = "NOM_SIST")
    private String nomSist;
    @Size(max = 20)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 100)
    @Column(name = "URL_SISTEMA")
    private String urlSistema;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblSauregSistema", fetch = FetchType.LAZY)
    private List<TblSauregRolSist> tblSauregRolSistList;
    @OneToMany(mappedBy = "idSistema", fetch = FetchType.LAZY)
    private List<TblSauregSistInstitucion> tblSauregSistInstitucionList;
    @OneToMany(mappedBy = "idSistema", fetch = FetchType.LAZY)
    private List<TblSauregTipUsu> tblSauregTipUsuList;

    public TblSauregSistema() {
    }

    public TblSauregSistema(Long idSistema) {
        this.idSistema = idSistema;
    }

    public Long getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(Long idSistema) {
        this.idSistema = idSistema;
    }

    public String getCodSist() {
        return codSist;
    }

    public void setCodSist(String codSist) {
        this.codSist = codSist;
    }

    public String getNomSist() {
        return nomSist;
    }

    public void setNomSist(String nomSist) {
        this.nomSist = nomSist;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlSistema() {
        return urlSistema;
    }

    public void setUrlSistema(String urlSistema) {
        this.urlSistema = urlSistema;
    }

    @XmlTransient
    public List<TblSauregRolSist> getTblSauregRolSistList() {
        return tblSauregRolSistList;
    }

    public void setTblSauregRolSistList(List<TblSauregRolSist> tblSauregRolSistList) {
        this.tblSauregRolSistList = tblSauregRolSistList;
    }

    @XmlTransient
    public List<TblSauregSistInstitucion> getTblSauregSistInstitucionList() {
        return tblSauregSistInstitucionList;
    }

    public void setTblSauregSistInstitucionList(List<TblSauregSistInstitucion> tblSauregSistInstitucionList) {
        this.tblSauregSistInstitucionList = tblSauregSistInstitucionList;
    }

    @XmlTransient
    public List<TblSauregTipUsu> getTblSauregTipUsuList() {
        return tblSauregTipUsuList;
    }

    public void setTblSauregTipUsuList(List<TblSauregTipUsu> tblSauregTipUsuList) {
        this.tblSauregTipUsuList = tblSauregTipUsuList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSistema != null ? idSistema.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregSistema)) {
            return false;
        }
        TblSauregSistema other = (TblSauregSistema) object;
        if ((this.idSistema == null && other.idSistema != null) || (this.idSistema != null && !this.idSistema.equals(other.idSistema))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregSistema[ idSistema=" + idSistema + " ]";
    }
    
}
