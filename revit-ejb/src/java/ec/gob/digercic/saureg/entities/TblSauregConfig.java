/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hernan.inga
 */
@Entity
@Table(name = "TBL_SAUREG_CONFIG")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregConfig.findAll", query = "SELECT t FROM TblSauregConfig t"),
    @NamedQuery(name = "TblSauregConfig.findByIdConfiguracion", query = "SELECT t FROM TblSauregConfig t WHERE t.idConfiguracion = :idConfiguracion"),
    @NamedQuery(name = "TblSauregConfig.findByCodConfig", query = "SELECT t FROM TblSauregConfig t WHERE t.codConfig = :codConfig AND t.idInstituc.idInstituc = :codInstituc"),
    @NamedQuery(name = "TblSauregConfig.findByDescripcion", query = "SELECT t FROM TblSauregConfig t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TblSauregConfig.findByValorEntero", query = "SELECT t FROM TblSauregConfig t WHERE t.valorEntero = :valorEntero"),
    @NamedQuery(name = "TblSauregConfig.findByValorNumerico", query = "SELECT t FROM TblSauregConfig t WHERE t.valorNumerico = :valorNumerico"),
    @NamedQuery(name = "TblSauregConfig.findByValorTexto", query = "SELECT t FROM TblSauregConfig t WHERE t.valorTexto = :valorTexto"),
    @NamedQuery(name = "TblSauregConfig.findByOrigen", query = "SELECT t FROM TblSauregConfig t WHERE t.origen = :origen")})
public class TblSauregConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "SEQ_CONFIGURACION", sequenceName = "SEQ_CONFIGURACION", allocationSize=1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONFIGURACION")
    @NotNull
    @Column(name = "ID_CONFIGURACION")
    private BigDecimal idConfiguracion;
    @Size(max = 32)
    @Column(name = "COD_CONFIG")
    private String codConfig;
    @Size(max = 100)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "VALOR_ENTERO")
    private BigInteger valorEntero;
    @Column(name = "VALOR_NUMERICO")
    private BigInteger valorNumerico;
    @Size(max = 50)
    @Column(name = "VALOR_TEXTO")
    private String valorTexto;
    @Size(max = 8)
    @Column(name = "ORIGEN")
    private String origen;
    @JoinColumn(name = "ID_INSTITUC", referencedColumnName = "ID_INSTITUC")
    @ManyToOne
    private TblSauregInstitucion idInstituc;

    public TblSauregConfig() {
    }

    public TblSauregConfig(BigDecimal idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public BigDecimal getIdConfiguracion() {
        return idConfiguracion;
    }

    public void setIdConfiguracion(BigDecimal idConfiguracion) {
        this.idConfiguracion = idConfiguracion;
    }

    public String getCodConfig() {
        return codConfig;
    }

    public void setCodConfig(String codConfig) {
        this.codConfig = codConfig;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigInteger getValorEntero() {
        return valorEntero;
    }

    public void setValorEntero(BigInteger valorEntero) {
        this.valorEntero = valorEntero;
    }

    public BigInteger getValorNumerico() {
        return valorNumerico;
    }

    public void setValorNumerico(BigInteger valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public String getValorTexto() {
        return valorTexto;
    }

    public void setValorTexto(String valorTexto) {
        this.valorTexto = valorTexto;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public TblSauregInstitucion getIdInstituc() {
        return idInstituc;
    }

    public void setIdInstituc(TblSauregInstitucion idInstituc) {
        this.idInstituc = idInstituc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConfiguracion != null ? idConfiguracion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregConfig)) {
            return false;
        }
        TblSauregConfig other = (TblSauregConfig) object;
        if ((this.idConfiguracion == null && other.idConfiguracion != null) || (this.idConfiguracion != null && !this.idConfiguracion.equals(other.idConfiguracion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsinformacion.modelo.entities.TblSauregConfig[ idConfiguracion=" + idConfiguracion + " ]";
    }
    
}
