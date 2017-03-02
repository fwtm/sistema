/*
 * To change this template, choose Tools | Templates
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santiago.tapia
 */
@Entity
@Table(name = "LOGS_ACCESO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogsAcceso.findAll", query = "SELECT l FROM LogsAcceso l"),
    @NamedQuery(name = "LogsAcceso.findById", query = "SELECT l FROM LogsAcceso l WHERE l.id = :id"),
    @NamedQuery(name = "LogsAcceso.findByFechaAcceso", query = "SELECT l FROM LogsAcceso l WHERE l.fechaAcceso = :fechaAcceso"),
    @NamedQuery(name = "LogsAcceso.findByUsuario", query = "SELECT l FROM LogsAcceso l WHERE l.usuario = :usuario"),
    @NamedQuery(name = "LogsAcceso.findByAgenId", query = "SELECT l FROM LogsAcceso l WHERE l.agenId = :agenId"),
    @NamedQuery(name = "LogsAcceso.findByNomUsu", query = "SELECT l FROM LogsAcceso l WHERE l.nomUsu = :nomUsu"),
    @NamedQuery(name = "LogsAcceso.findByApeUsu", query = "SELECT l FROM LogsAcceso l WHERE l.apeUsu = :apeUsu"),
    @NamedQuery(name = "LogsAcceso.findByAgenNom", query = "SELECT l FROM LogsAcceso l WHERE l.agenNom = :agenNom"),
    @NamedQuery(name = "LogsAcceso.findByInstId", query = "SELECT l FROM LogsAcceso l WHERE l.instId = :instId"),
    @NamedQuery(name = "LogsAcceso.findByInstNombre", query = "SELECT l FROM LogsAcceso l WHERE l.instNombre = :instNombre"),
    @NamedQuery(name = "LogsAcceso.findByAccion", query = "SELECT l FROM LogsAcceso l WHERE l.accion = :accion"),
    @NamedQuery(name = "LogsAcceso.findByIp", query = "SELECT l FROM LogsAcceso l WHERE l.ip = :ip")})
public class LogsAcceso implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "SEQ_RENAVI_LOG_ACCIONES", sequenceName = "SEQ_RENAVI_LOG_ACCIONES", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RENAVI_LOG_ACCIONES")
    @Column(name = "ID", nullable = false, precision = 20)
    private Long id;
    @Column(name = "FECHA_ACCESO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAcceso;
    @Column(name = "USUARIO", length = 15)
    private String usuario;
    @Column(name = "AGEN_ID")
    private String agenId;
    @Column(name = "NOM_USU", length = 60)
    private String nomUsu;
    @Column(name = "APE_USU", length = 60)
    private String apeUsu;
    @Column(name = "AGEN_NOM", length = 100)
    private String agenNom;
    @Column(name = "INST_ID")
    private String instId;
    @Column(name = "INST_NOMBRE", length = 100)
    private String instNombre;
    @Column(name = "ACCION", length = 200)
    private String accion;
    @Column(name = "IP", length = 50)
    private String ip;

    public LogsAcceso() {
    }

    public LogsAcceso(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaAcceso() {
        return fechaAcceso;
    }

    public void setFechaAcceso(Date fechaAcceso) {
        this.fechaAcceso = fechaAcceso;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAgenId() {
        return agenId;
    }

    public void setAgenId(String agenId) {
        this.agenId = agenId;
    }

    public String getNomUsu() {
        return nomUsu;
    }

    public void setNomUsu(String nomUsu) {
        this.nomUsu = nomUsu;
    }

    public String getApeUsu() {
        return apeUsu;
    }

    public void setApeUsu(String apeUsu) {
        this.apeUsu = apeUsu;
    }

    public String getAgenNom() {
        return agenNom;
    }

    public void setAgenNom(String agenNom) {
        this.agenNom = agenNom;
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getInstNombre() {
        return instNombre;
    }

    public void setInstNombre(String instNombre) {
        this.instNombre = instNombre;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LogsAcceso)) {
            return false;
        }
        LogsAcceso other = (LogsAcceso) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.LogAcceso[ id=" + id + " ]";
    }
    
}
