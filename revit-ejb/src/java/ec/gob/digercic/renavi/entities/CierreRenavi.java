/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author henry.aguilar
 */
@Entity
@Table(name = "CIERRE_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CierreRenavi.findAll", query = "SELECT c FROM CierreRenavi c"),
    @NamedQuery(name = "CierreRenavi.findByIdCierre", query = "SELECT c FROM CierreRenavi c WHERE c.idCierre = :idCierre"),
    @NamedQuery(name = "CierreRenavi.findByUsuario", query = "SELECT c FROM CierreRenavi c WHERE c.usuario = :usuario"),
    @NamedQuery(name = "CierreRenavi.findByFkUsuSaureg", query = "SELECT c FROM CierreRenavi c WHERE c.fkUsuSaureg = :fkUsuSaureg"),
    @NamedQuery(name = "CierreRenavi.findByFechaCierre", query = "SELECT c FROM CierreRenavi c WHERE c.fechaCierre = :fechaCierre"),
    @NamedQuery(name = "CierreRenavi.findByNumPartos", query = "SELECT c FROM CierreRenavi c WHERE c.numPartos = :numPartos"),
    @NamedQuery(name = "CierreRenavi.findByNumNacimientos", query = "SELECT c FROM CierreRenavi c WHERE c.numNacimientos = :numNacimientos"),
    @NamedQuery(name = "CierreRenavi.findByNumNacVivFe", query = "SELECT c FROM CierreRenavi c WHERE c.numNacVivFe = :numNacVivFe"),
    @NamedQuery(name = "CierreRenavi.findByNumRegFisicos", query = "SELECT c FROM CierreRenavi c WHERE c.numRegFisicos = :numRegFisicos"),
    @NamedQuery(name = "CierreRenavi.findByNumRegIncompletos", query = "SELECT c FROM CierreRenavi c WHERE c.numRegIncompletos = :numRegIncompletos"),
    @NamedQuery(name = "CierreRenavi.findByNumRegAnulNacVivFe", query = "SELECT c FROM CierreRenavi c WHERE c.numRegAnulNacVivFe = :numRegAnulNacVivFe"),
    @NamedQuery(name = "CierreRenavi.findByDiferPartos", query = "SELECT c FROM CierreRenavi c WHERE c.diferPartos = :diferPartos"),
    @NamedQuery(name = "CierreRenavi.findByDiferNacViv", query = "SELECT c FROM CierreRenavi c WHERE c.diferNacViv = :diferNacViv"),
    @NamedQuery(name = "CierreRenavi.findByObservacion", query = "SELECT c FROM CierreRenavi c WHERE c.observacion = :observacion"),
    @NamedQuery(name = "CierreRenavi.findByEstatus", query = "SELECT c FROM CierreRenavi c WHERE c.estatus = :estatus"),
    @NamedQuery(name = "CierreRenavi.findByFkInstSaureg", query = "SELECT c FROM CierreRenavi c WHERE c.fkInstSaureg = :fkInstSaureg"),
    @NamedQuery(name = "CierreRenavi.findByFkAgenSaureg", query = "SELECT c FROM CierreRenavi c WHERE c.fkAgenSaureg = :fkAgenSaureg order by c.idCierre DESC"),
    @NamedQuery(name = "CierreRenavi.findByMesOperacionAgen", query = "SELECT c FROM CierreRenavi c WHERE c.mesOperacionAgen = :mesOperacionAgen"),
    @NamedQuery(name = "CierreRenavi.findByUltMesCierre", query = "SELECT c FROM CierreRenavi c WHERE c.ultMesCierre = :ultMesCierre"),
    @NamedQuery(name = "CierreRenavi.findByObservacionInc", query = "SELECT c FROM CierreRenavi c WHERE c.observacionInc = :observacionInc"),
    @NamedQuery(name = "CierreRenavi.findByNumPartosSistema", query = "SELECT c FROM CierreRenavi c WHERE c.numPartosSistema = :numPartosSistema"),
    @NamedQuery(name = "CierreRenavi.findByNumNacimientosSistema", query = "SELECT c FROM CierreRenavi c WHERE c.numNacimientosSistema = :numNacimientosSistema"),
    @NamedQuery(name = "CierreRenavi.findByFechaUltCierre", query = "SELECT c FROM CierreRenavi c WHERE c.fechaUltCierre = :fechaUltCierre"),
    @NamedQuery(name = "CierreRenavi.findAllByUsuarioCierre", query = "SELECT c FROM CierreRenavi c WHERE c.fkAgenSaureg = :fkAgenciaSaureg AND c.fkUsuSaureg = :fkUsuSaureg"),
    @NamedQuery(name = "CierreRenavi.findAllByUsuarioUltMesCierre", query = "SELECT c FROM CierreRenavi c WHERE c.estatus = :status AND c.fkAgenSaureg = :fkAgenciaSaureg AND c.fkUsuSaureg = :fkUsuSaureg AND c.ultMesCierre =:ultMesCierre")})
public class CierreRenavi implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "SEQ_RENAVI_CIERRE_NV", sequenceName = "SEQ_RENAVI_CIERRE_NV", allocationSize = 1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RENAVI_CIERRE_NV")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_CIERRE")
    private BigDecimal idCierre;
    @Size(max = 100)
    @Column(name = "USUARIO")
    private String usuario;
    @Size(max = 10)
    @Column(name = "FK_USU_SAUREG")
    private String fkUsuSaureg;
    @Column(name = "FECHA_CIERRE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCierre;
    @Column(name = "NUM_PARTOS")
    private BigInteger numPartos;
    @Column(name = "NUM_NACIMIENTOS")
    private BigInteger numNacimientos;
    @Column(name = "NUM_NAC_VIV_FE")
    private BigInteger numNacVivFe;
    @Column(name = "NUM_REG_FISICOS")
    private BigInteger numRegFisicos;
    @Column(name = "NUM_REG_INCOMPLETOS")
    private BigInteger numRegIncompletos;
    @Column(name = "NUM_REG_ANUL_NAC_VIV_FE")
    private BigInteger numRegAnulNacVivFe;
    @Column(name = "DIFER_PARTOS")
    private BigInteger diferPartos;
    @Column(name = "DIFER_NAC_VIV")
    private BigInteger diferNacViv;
    @Size(max = 300)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Size(max = 2)
    @Column(name = "ESTATUS")
    private String estatus;
    @Size(max = 150)
    @Column(name = "FK_INST_SAUREG")
    private String fkInstSaureg;
    @Size(max = 150)
    @Column(name = "FK_AGEN_SAUREG")
    private String fkAgenSaureg;
    @Column(name = "MES_OPERACION_AGEN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mesOperacionAgen;
    @Size(max = 50)
    @Column(name = "ULT_MES_CIERRE")
    private String ultMesCierre;
    @Size(max = 300)
    @Column(name = "OBSERVACION_INC")
    private String observacionInc;
    @Column(name = "NUM_PARTOS_SISTEMA")
    private BigInteger numPartosSistema;
    @Column(name = "NUM_NACIMIENTOS_SISTEMA")
    private BigInteger numNacimientosSistema;
    @Column(name = "FECHA_ULT_CIERRE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaUltCierre;
    @Column(name = "NUM_PARTOS_SIS_INDOC")
    private BigInteger numPartosSisIndoc;
    @Column(name = "NUM_NAC_VIV_FE_INDOC")
    private BigInteger numNacVivFeIndoc;
    @Column(name = "NUM_REG_INCOMPLETOS_INDOC")
    private BigInteger numRegIncompletosIndoc;
    @Column(name = "NUM_TOTAL_CALCULADO")
    private BigInteger numTotalCalculado;
    @Lob
    @Column(name = "PDF_SIN_FIRMAR")
    private byte[] pdfSinFirmar;
    @Lob
    @Column(name = "PDF_CON_FIRMA")
    private byte[] pdfConFirma;

    public CierreRenavi() {
    }

    public CierreRenavi(BigDecimal idCierre) {
        this.idCierre = idCierre;
    }

    public BigDecimal getIdCierre() {
        return idCierre;
    }

    public void setIdCierre(BigDecimal idCierre) {
        this.idCierre = idCierre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getFkUsuSaureg() {
        return fkUsuSaureg;
    }

    public void setFkUsuSaureg(String fkUsuSaureg) {
        this.fkUsuSaureg = fkUsuSaureg;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public BigInteger getNumPartos() {
        return numPartos;
    }

    public void setNumPartos(BigInteger numPartos) {
        this.numPartos = numPartos;
    }

    public BigInteger getNumNacimientos() {
        return numNacimientos;
    }

    public void setNumNacimientos(BigInteger numNacimientos) {
        this.numNacimientos = numNacimientos;
    }

    public BigInteger getNumNacVivFe() {
        return numNacVivFe;
    }

    public void setNumNacVivFe(BigInteger numNacVivFe) {
        this.numNacVivFe = numNacVivFe;
    }

    public BigInteger getNumRegFisicos() {
        return numRegFisicos;
    }

    public void setNumRegFisicos(BigInteger numRegFisicos) {
        this.numRegFisicos = numRegFisicos;
    }

    public BigInteger getNumRegIncompletos() {
        return numRegIncompletos;
    }

    public void setNumRegIncompletos(BigInteger numRegIncompletos) {
        this.numRegIncompletos = numRegIncompletos;
    }

    public BigInteger getNumRegAnulNacVivFe() {
        return numRegAnulNacVivFe;
    }

    public void setNumRegAnulNacVivFe(BigInteger numRegAnulNacVivFe) {
        this.numRegAnulNacVivFe = numRegAnulNacVivFe;
    }

    public BigInteger getDiferPartos() {
        return diferPartos;
    }

    public void setDiferPartos(BigInteger diferPartos) {
        this.diferPartos = diferPartos;
    }

    public BigInteger getDiferNacViv() {
        return diferNacViv;
    }

    public void setDiferNacViv(BigInteger diferNacViv) {
        this.diferNacViv = diferNacViv;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFkInstSaureg() {
        return fkInstSaureg;
    }

    public void setFkInstSaureg(String fkInstSaureg) {
        this.fkInstSaureg = fkInstSaureg;
    }

    public String getFkAgenSaureg() {
        return fkAgenSaureg;
    }

    public void setFkAgenSaureg(String fkAgenSaureg) {
        this.fkAgenSaureg = fkAgenSaureg;
    }

    public Date getMesOperacionAgen() {
        return mesOperacionAgen;
    }

    public void setMesOperacionAgen(Date mesOperacionAgen) {
        this.mesOperacionAgen = mesOperacionAgen;
    }

    public String getUltMesCierre() {
        return ultMesCierre;
    }

    public void setUltMesCierre(String ultMesCierre) {
        this.ultMesCierre = ultMesCierre;
    }

    public String getObservacionInc() {
        return observacionInc;
    }

    public void setObservacionInc(String observacionInc) {
        this.observacionInc = observacionInc;
    }

    public BigInteger getNumPartosSistema() {
        return numPartosSistema;
    }

    public void setNumPartosSistema(BigInteger numPartosSistema) {
        this.numPartosSistema = numPartosSistema;
    }

    public BigInteger getNumNacimientosSistema() {
        return numNacimientosSistema;
    }

    public void setNumNacimientosSistema(BigInteger numNacimientosSistema) {
        this.numNacimientosSistema = numNacimientosSistema;
    }

    public Date getFechaUltCierre() {
        return fechaUltCierre;
    }

    public void setFechaUltCierre(Date fechaUltCierre) {
        this.fechaUltCierre = fechaUltCierre;
    }

    public BigInteger getNumPartosSisIndoc() {
        return numPartosSisIndoc;
    }

    public void setNumPartosSisIndoc(BigInteger numPartosSisIndoc) {
        this.numPartosSisIndoc = numPartosSisIndoc;
    }

    public BigInteger getNumNacVivFeIndoc() {
        return numNacVivFeIndoc;
    }

    public void setNumNacVivFeIndoc(BigInteger numNacVivFeIndoc) {
        this.numNacVivFeIndoc = numNacVivFeIndoc;
    }

    public BigInteger getNumRegIncompletosIndoc() {
        return numRegIncompletosIndoc;
    }

    public void setNumRegIncompletosIndoc(BigInteger numRegIncompletosIndoc) {
        this.numRegIncompletosIndoc = numRegIncompletosIndoc;
    }

    public BigInteger getNumTotalCalculado() {
        return numTotalCalculado;
    }

    public void setNumTotalCalculado(BigInteger numTotalCalculado) {
        this.numTotalCalculado = numTotalCalculado;
    }

    public byte[] getPdfSinFirmar() {
        return pdfSinFirmar;
    }

    public void setPdfSinFirmar(byte[] pdfSinFirmar) {
        this.pdfSinFirmar = pdfSinFirmar;
    }

    public byte[] getPdfConFirma() {
        return pdfConFirma;
    }

    public void setPdfConFirma(byte[] pdfConFirma) {
        this.pdfConFirma = pdfConFirma;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCierre != null ? idCierre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CierreRenavi)) {
            return false;
        }
        CierreRenavi other = (CierreRenavi) object;
        if ((this.idCierre == null && other.idCierre != null) || (this.idCierre != null && !this.idCierre.equals(other.idCierre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CierreRenavi[ idCierre=" + idCierre + " ]";
    }

}
