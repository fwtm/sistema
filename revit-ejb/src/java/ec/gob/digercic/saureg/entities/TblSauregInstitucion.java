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
@Table(name = "TBL_SAUREG_INSTITUCION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregInstitucion.findAll", query = "SELECT t FROM TblSauregInstitucion t"),
    @NamedQuery(name = "TblSauregInstitucion.findByIdInstituc", query = "SELECT t FROM TblSauregInstitucion t WHERE t.idInstituc = :idInstituc"),
    @NamedQuery(name = "TblSauregInstitucion.findByNomInstituc", query = "SELECT t FROM TblSauregInstitucion t WHERE t.nomInstituc = :nomInstituc"),
    @NamedQuery(name = "TblSauregInstitucion.findByPaginaWeb", query = "SELECT t FROM TblSauregInstitucion t WHERE t.paginaWeb = :paginaWeb"),
    @NamedQuery(name = "TblSauregInstitucion.findByEMail", query = "SELECT t FROM TblSauregInstitucion t WHERE t.eMail = :eMail"),
    @NamedQuery(name = "TblSauregInstitucion.findByTelefono", query = "SELECT t FROM TblSauregInstitucion t WHERE t.telefono = :telefono"),
    @NamedQuery(name = "TblSauregInstitucion.findByStatus", query = "SELECT t FROM TblSauregInstitucion t WHERE t.status = :status"),
    @NamedQuery(name = "TblSauregInstitucion.findByNomCorto", query = "SELECT t FROM TblSauregInstitucion t WHERE t.nomCorto = :nomCorto"),
    @NamedQuery(name = "TblSauregInstitucion.findByCodRevit", query = "SELECT t FROM TblSauregInstitucion t WHERE t.codRevit = :codRevit")})
public class TblSauregInstitucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_INSTITUC")
    private Long idInstituc;
    @Size(max = 150)
    @Column(name = "NOM_INSTITUC")
    private String nomInstituc;
    @Size(max = 200)
    @Column(name = "PAGINA_WEB")
    private String paginaWeb;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 60)
    @Column(name = "E_MAIL")
    private String eMail;
    @Column(name = "TELEFONO")
    private Long telefono;
    @Column(name = "STATUS")
    private Character status;
    @Size(max = 30)
    @Column(name = "NOM_CORTO")
    private String nomCorto;
    @Size(max = 10)
    @Column(name = "COD_REVIT")
    private String codRevit;
    @OneToMany(mappedBy = "idInstituc", fetch = FetchType.LAZY)
    private List<TblSauregConfig> tblSauregConfigList;
    @OneToMany(mappedBy = "idInstituc", fetch = FetchType.LAZY)
    private List<TblSauregAgencia> tblSauregAgenciaList;
    @OneToMany(mappedBy = "idInstituc", fetch = FetchType.LAZY)
    private List<TblSauregSistInstitucion> tblSauregSistInstitucionList;

    public TblSauregInstitucion() {
    }

    public TblSauregInstitucion(Long idInstituc) {
        this.idInstituc = idInstituc;
    }

    public Long getIdInstituc() {
        return idInstituc;
    }

    public void setIdInstituc(Long idInstituc) {
        this.idInstituc = idInstituc;
    }

    public String getNomInstituc() {
        return nomInstituc;
    }

    public void setNomInstituc(String nomInstituc) {
        this.nomInstituc = nomInstituc;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getNomCorto() {
        return nomCorto;
    }

    public void setNomCorto(String nomCorto) {
        this.nomCorto = nomCorto;
    }

    public String getCodRevit() {
        return codRevit;
    }

    public void setCodRevit(String codRevit) {
        this.codRevit = codRevit;
    }

    @XmlTransient
    public List<TblSauregConfig> getTblSauregConfigList() {
        return tblSauregConfigList;
    }

    public void setTblSauregConfigList(List<TblSauregConfig> tblSauregConfigList) {
        this.tblSauregConfigList = tblSauregConfigList;
    }

    @XmlTransient
    public List<TblSauregAgencia> getTblSauregAgenciaList() {
        return tblSauregAgenciaList;
    }

    public void setTblSauregAgenciaList(List<TblSauregAgencia> tblSauregAgenciaList) {
        this.tblSauregAgenciaList = tblSauregAgenciaList;
    }

    @XmlTransient
    public List<TblSauregSistInstitucion> getTblSauregSistInstitucionList() {
        return tblSauregSistInstitucionList;
    }

    public void setTblSauregSistInstitucionList(List<TblSauregSistInstitucion> tblSauregSistInstitucionList) {
        this.tblSauregSistInstitucionList = tblSauregSistInstitucionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInstituc != null ? idInstituc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregInstitucion)) {
            return false;
        }
        TblSauregInstitucion other = (TblSauregInstitucion) object;
        if ((this.idInstituc == null && other.idInstituc != null) || (this.idInstituc != null && !this.idInstituc.equals(other.idInstituc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregInstitucion[ idInstituc=" + idInstituc + " ]";
    }
    
}
