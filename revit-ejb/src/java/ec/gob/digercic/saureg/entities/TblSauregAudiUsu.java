/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author daniel.porras
 */
@Entity
@Table(name = "TBL_SAUREG_AUDI_USU")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregAudiUsu.findAll", query = "SELECT t FROM TblSauregAudiUsu t"),
    @NamedQuery(name = "TblSauregAudiUsu.findByIdAudiUsu", query = "SELECT t FROM TblSauregAudiUsu t WHERE t.idAudiUsu = :idAudiUsu"),
    @NamedQuery(name = "TblSauregAudiUsu.findByFecha", query = "SELECT t FROM TblSauregAudiUsu t WHERE t.fecha = :fecha"),
    @NamedQuery(name = "TblSauregAudiUsu.findByIdUsuCambio", query = "SELECT t FROM TblSauregAudiUsu t WHERE t.idUsuCambio = :idUsuCambio"),
    @NamedQuery(name = "TblSauregAudiUsu.findByTipoTransac", query = "SELECT t FROM TblSauregAudiUsu t WHERE t.tipoTransac = :tipoTransac")})
public class TblSauregAudiUsu implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_AUDI_USU")
    private BigDecimal idAudiUsu;
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "ID_USU_CAMBIO")
    private Long idUsuCambio;
    @Size(max = 20)
    @Column(name = "TIPO_TRANSAC")
    private String tipoTransac;
    @Lob
    @Column(name = "TRANSACCION")
    private Serializable transaccion;

    public TblSauregAudiUsu() {
    }

    public TblSauregAudiUsu(BigDecimal idAudiUsu) {
        this.idAudiUsu = idAudiUsu;
    }

    public BigDecimal getIdAudiUsu() {
        return idAudiUsu;
    }

    public void setIdAudiUsu(BigDecimal idAudiUsu) {
        this.idAudiUsu = idAudiUsu;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Long getIdUsuCambio() {
        return idUsuCambio;
    }

    public void setIdUsuCambio(Long idUsuCambio) {
        this.idUsuCambio = idUsuCambio;
    }

    public String getTipoTransac() {
        return tipoTransac;
    }

    public void setTipoTransac(String tipoTransac) {
        this.tipoTransac = tipoTransac;
    }

    public Serializable getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(Serializable transaccion) {
        this.transaccion = transaccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAudiUsu != null ? idAudiUsu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregAudiUsu)) {
            return false;
        }
        TblSauregAudiUsu other = (TblSauregAudiUsu) object;
        if ((this.idAudiUsu == null && other.idAudiUsu != null) || (this.idAudiUsu != null && !this.idAudiUsu.equals(other.idAudiUsu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregAudiUsu[ idAudiUsu=" + idAudiUsu + " ]";
    }
    
}
