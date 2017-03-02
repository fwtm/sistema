/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "TBL_SAUREG_ESTRUC_SIST")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregEstrucSist.findAll", query = "SELECT t FROM TblSauregEstrucSist t"),
    @NamedQuery(name = "TblSauregEstrucSist.findByIdEstrucSist", query = "SELECT t FROM TblSauregEstrucSist t WHERE t.idEstrucSist = :idEstrucSist"),
    @NamedQuery(name = "TblSauregEstrucSist.findByIdPermiso", query = "SELECT t FROM TblSauregEstrucSist t WHERE t.idPermiso = :idPermiso"),
    @NamedQuery(name = "TblSauregEstrucSist.findByDescripcion", query = "SELECT t FROM TblSauregEstrucSist t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TblSauregEstrucSist.findByTipoSistEstruct", query = "SELECT t FROM TblSauregEstrucSist t WHERE t.tipoSistEstruct = :tipoSistEstruct"),
    @NamedQuery(name = "TblSauregEstrucSist.findByCodEsctructSist", query = "SELECT t FROM TblSauregEstrucSist t WHERE t.codEsctructSist = :codEsctructSist"),
    @NamedQuery(name = "TblSauregEstrucSist.findByOrdenEstruct", query = "SELECT t FROM TblSauregEstrucSist t WHERE t.ordenEstruct = :ordenEstruct"),
    @NamedQuery(name = "TblSauregEstrucSist.findByPaginas", query = "SELECT t FROM TblSauregEstrucSist t WHERE t.paginas = :paginas"),
    @NamedQuery(name = "TblSauregEstrucSist.findByStatus", query = "SELECT t FROM TblSauregEstrucSist t WHERE t.status = :status"),
    @NamedQuery(name = "TblSauregEstrucSist.findByIdSistema", query = "SELECT t FROM TblSauregEstrucSist t WHERE t.idSistema = :idSistema")})
public class TblSauregEstrucSist implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ESTRUC_SIST")
    private Long idEstrucSist;
    @Column(name = "ID_PERMISO")
    private Long idPermiso;
    @Size(max = 40)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 8)
    @Column(name = "TIPO_SIST_ESTRUCT")
    private String tipoSistEstruct;
    @Size(max = 30)
    @Column(name = "COD_ESCTRUCT_SIST")
    private String codEsctructSist;
    @Size(max = 20)
    @Column(name = "IMAGEN")
    private String imagen;
    @Column(name = "ORDEN_ESTRUCT")
    private BigInteger ordenEstruct;
    @Size(max = 150)
    @Column(name = "PAGINAS")
    private String paginas;
    @Column(name = "STATUS")
    private Character status;
    @Column(name = "ID_SISTEMA")
    private Long idSistema;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblSauregEstrucSist", fetch = FetchType.LAZY)
    private List<TblSauregCompRol> tblSauregCompRolList;
    @OneToMany(mappedBy = "codEstructura", fetch = FetchType.LAZY)
    private List<TblSauregEstrucSist> tblSauregEstrucSistList;
    @JoinColumn(name = "COD_ESTRUCTURA", referencedColumnName = "ID_ESTRUC_SIST")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblSauregEstrucSist codEstructura;

    public TblSauregEstrucSist() {
    }

    public TblSauregEstrucSist(Long idEstrucSist) {
        this.idEstrucSist = idEstrucSist;
    }

    public Long getIdEstrucSist() {
        return idEstrucSist;
    }

    public void setIdEstrucSist(Long idEstrucSist) {
        this.idEstrucSist = idEstrucSist;
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

    public String getTipoSistEstruct() {
        return tipoSistEstruct;
    }

    public void setTipoSistEstruct(String tipoSistEstruct) {
        this.tipoSistEstruct = tipoSistEstruct;
    }

    public String getCodEsctructSist() {
        return codEsctructSist;
    }

    public void setCodEsctructSist(String codEsctructSist) {
        this.codEsctructSist = codEsctructSist;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public BigInteger getOrdenEstruct() {
        return ordenEstruct;
    }

    public void setOrdenEstruct(BigInteger ordenEstruct) {
        this.ordenEstruct = ordenEstruct;
    }

    public String getPaginas() {
        return paginas;
    }

    public void setPaginas(String paginas) {
        this.paginas = paginas;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Long getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(Long idSistema) {
        this.idSistema = idSistema;
    }

    @XmlTransient
    public List<TblSauregCompRol> getTblSauregCompRolList() {
        return tblSauregCompRolList;
    }

    public void setTblSauregCompRolList(List<TblSauregCompRol> tblSauregCompRolList) {
        this.tblSauregCompRolList = tblSauregCompRolList;
    }

    @XmlTransient
    public List<TblSauregEstrucSist> getTblSauregEstrucSistList() {
        return tblSauregEstrucSistList;
    }

    public void setTblSauregEstrucSistList(List<TblSauregEstrucSist> tblSauregEstrucSistList) {
        this.tblSauregEstrucSistList = tblSauregEstrucSistList;
    }

    public TblSauregEstrucSist getCodEstructura() {
        return codEstructura;
    }

    public void setCodEstructura(TblSauregEstrucSist codEstructura) {
        this.codEstructura = codEstructura;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstrucSist != null ? idEstrucSist.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregEstrucSist)) {
            return false;
        }
        TblSauregEstrucSist other = (TblSauregEstrucSist) object;
        if ((this.idEstrucSist == null && other.idEstrucSist != null) || (this.idEstrucSist != null && !this.idEstrucSist.equals(other.idEstrucSist))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregEstrucSist[ idEstrucSist=" + idEstrucSist + " ]";
    }
    
}
