/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author daniel.porras
 */
@Entity
@Table(name = "TBL_SAUREG_UBICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregUbicacion.findAll", query = "SELECT t FROM TblSauregUbicacion t"),
    @NamedQuery(name = "TblSauregUbicacion.findByIdUbicacion", query = "SELECT t FROM TblSauregUbicacion t WHERE t.idUbicacion = :idUbicacion"),
    @NamedQuery(name = "TblSauregUbicacion.findByIdGrupo", query = "SELECT t FROM TblSauregUbicacion t WHERE t.idGrupo.idUbicacion = :idGrupo AND t.status = :status ORDER BY t.derscripcion ASC"),
    @NamedQuery(name = "TblSauregUbicacion.findByDerscripcion", query = "SELECT t FROM TblSauregUbicacion t WHERE t.derscripcion = :derscripcion"),
    @NamedQuery(name = "TblSauregUbicacion.findByCodDpa", query = "SELECT t FROM TblSauregUbicacion t WHERE t.codDpa = :codDpa"),
    @NamedQuery(name = "TblSauregUbicacion.findByIdGrupoCodExterno1", query = "SELECT t FROM TblSauregUbicacion t WHERE t.idGrupo.idUbicacion IN (SELECT t1.idUbicacion FROM TblSauregUbicacion t1 WHERE t1.codExterno1 = :codExterno1) AND t.status = :status ORDER BY t.derscripcion ASC"),
    @NamedQuery(name = "TblSauregUbicacion.findByIdGrupoCodDpa", query = "SELECT t FROM TblSauregUbicacion t WHERE t.idGrupo.idUbicacion IN (SELECT t1.idUbicacion FROM TblSauregUbicacion t1 WHERE t1.codDpa = :codDpa) AND t.status = :status ORDER BY t.derscripcion ASC"),
    @NamedQuery(name = "TblSauregUbicacion.findByCodExterno2", query = "SELECT t FROM TblSauregUbicacion t WHERE t.codExterno2 = :codExterno2")})
public class TblSauregUbicacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_UBICACION")
    private Long idUbicacion;
    @Size(max = 50)
    @Column(name = "DERSCRIPCION")
    private String derscripcion;
    @Size(max = 50)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Size(max = 50)
    @Column(name = "COD_DPA")
    private String codDpa;
    @Column(name = "STATUS")
    private String status;
    @Size(max = 20)
    @Column(name = "COD_EXTERNO1")
    private String codExterno1;
    @Size(max = 20)
    @Column(name = "COD_EXTERNO2")
    private String codExterno2;
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    @OneToMany(mappedBy = "idCanton", fetch = FetchType.LAZY)
    private List<TblSauregAgencia> tblSauregAgenciaList;
    @OneToMany(mappedBy = "idParroquia", fetch = FetchType.LAZY)
    private List<TblSauregAgencia> tblSauregAgenciaList1;
    @OneToMany(mappedBy = "idProvincia", fetch = FetchType.LAZY)
    private List<TblSauregAgencia> tblSauregAgenciaList2;
    @OneToMany(mappedBy = "idGrupo", fetch = FetchType.LAZY)
    private List<TblSauregUbicacion> tblSauregUbicacionList;
    @JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID_UBICACION")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblSauregUbicacion idGrupo;

    public TblSauregUbicacion() {
    }

    public TblSauregUbicacion(Long idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public Long getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(Long idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getDerscripcion() {
        return derscripcion;
    }

    public void setDerscripcion(String derscripcion) {
        this.derscripcion = derscripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getCodDpa() {
        return codDpa;
    }

    public void setCodDpa(String codDpa) {
        this.codDpa = codDpa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCodExterno1() {
        return codExterno1;
    }

    public void setCodExterno1(String codExterno1) {
        this.codExterno1 = codExterno1;
    }

    public String getCodExterno2() {
        return codExterno2;
    }

    public void setCodExterno2(String codExterno2) {
        this.codExterno2 = codExterno2;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @XmlTransient
    public List<TblSauregAgencia> getTblSauregAgenciaList() {
        return tblSauregAgenciaList;
    }

    public void setTblSauregAgenciaList(List<TblSauregAgencia> tblSauregAgenciaList) {
        this.tblSauregAgenciaList = tblSauregAgenciaList;
    }

    @XmlTransient
    public List<TblSauregAgencia> getTblSauregAgenciaList1() {
        return tblSauregAgenciaList1;
    }

    public void setTblSauregAgenciaList1(List<TblSauregAgencia> tblSauregAgenciaList1) {
        this.tblSauregAgenciaList1 = tblSauregAgenciaList1;
    }

    @XmlTransient
    public List<TblSauregAgencia> getTblSauregAgenciaList2() {
        return tblSauregAgenciaList2;
    }

    public void setTblSauregAgenciaList2(List<TblSauregAgencia> tblSauregAgenciaList2) {
        this.tblSauregAgenciaList2 = tblSauregAgenciaList2;
    }

    @XmlTransient
    public List<TblSauregUbicacion> getTblSauregUbicacionList() {
        return tblSauregUbicacionList;
    }

    public void setTblSauregUbicacionList(List<TblSauregUbicacion> tblSauregUbicacionList) {
        this.tblSauregUbicacionList = tblSauregUbicacionList;
    }

    public TblSauregUbicacion getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(TblSauregUbicacion idGrupo) {
        this.idGrupo = idGrupo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUbicacion != null ? idUbicacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregUbicacion)) {
            return false;
        }
        TblSauregUbicacion other = (TblSauregUbicacion) object;
        if ((this.idUbicacion == null && other.idUbicacion != null) || (this.idUbicacion != null && !this.idUbicacion.equals(other.idUbicacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregUbicacion[ idUbicacion=" + idUbicacion + " ]";
    }
    
}
