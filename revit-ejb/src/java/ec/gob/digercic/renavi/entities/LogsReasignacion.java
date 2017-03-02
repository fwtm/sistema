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
@Table(name = "LOGS_REASIGNACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogsReasignacion.findAll", query = "SELECT l FROM LogsReasignacion l"),
    @NamedQuery(name = "LogsReasignacion.findByIdLogsReasig", query = "SELECT l FROM LogsReasignacion l WHERE l.idLogsReasig = :idLogsReasig"),
    @NamedQuery(name = "LogsReasignacion.findByIdNacViv", query = "SELECT l FROM LogsReasignacion l WHERE l.idNacViv = :idNacViv"),
    @NamedQuery(name = "LogsReasignacion.findByUsuarioRegAnt", query = "SELECT l FROM LogsReasignacion l WHERE l.usuarioRegAnt = :usuarioRegAnt"),
    @NamedQuery(name = "LogsReasignacion.findByUsuarioRegAsig", query = "SELECT l FROM LogsReasignacion l WHERE l.usuarioRegAsig = :usuarioRegAsig"),
    @NamedQuery(name = "LogsReasignacion.findByUsuarioEjecAsig", query = "SELECT l FROM LogsReasignacion l WHERE l.usuarioEjecAsig = :usuarioEjecAsig"),
    @NamedQuery(name = "LogsReasignacion.findByFechaAsigReg", query = "SELECT l FROM LogsReasignacion l WHERE l.fechaAsigReg = :fechaAsigReg"),
    @NamedQuery(name = "LogsReasignacion.findByFkAgenciaSaureg", query = "SELECT l FROM LogsReasignacion l WHERE l.fkAgenciaSaureg = :fkAgenciaSaureg")})
public class LogsReasignacion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "SEQ_RENAVI_LOG_REASIG", sequenceName = "SEQ_RENAVI_LOG_REASIG", allocationSize = 1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RENAVI_LOG_REASIG")
    @NotNull
    @Column(name = "ID_LOGS_REASIG")
    private Long idLogsReasig;
    @Column(name = "ID_NAC_VIV")
    private Long idNacViv;
    @Size(max = 15)
    @Column(name = "USUARIO_REG_ANT")
    private String usuarioRegAnt;
    @Size(max = 15)
    @Column(name = "USUARIO_REG_ASIG")
    private String usuarioRegAsig;
    @Size(max = 15)
    @Column(name = "USUARIO_EJEC_ASIG")
    private String usuarioEjecAsig;
    @Column(name = "FECHA_ASIG_REG")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAsigReg;
    @Column(name = "FK_AGENCIA_SAUREG")
    private Long fkAgenciaSaureg;

    public LogsReasignacion() {
    }

    public LogsReasignacion(Long idLogsReasig) {
        this.idLogsReasig = idLogsReasig;
    }

    public Long getIdLogsReasig() {
        return idLogsReasig;
    }

    public void setIdLogsReasig(Long idLogsReasig) {
        this.idLogsReasig = idLogsReasig;
    }

    public Long getIdNacViv() {
        return idNacViv;
    }

    public void setIdNacViv(Long idNacViv) {
        this.idNacViv = idNacViv;
    }

    public String getUsuarioRegAnt() {
        return usuarioRegAnt;
    }

    public void setUsuarioRegAnt(String usuarioRegAnt) {
        this.usuarioRegAnt = usuarioRegAnt;
    }

    public String getUsuarioRegAsig() {
        return usuarioRegAsig;
    }

    public void setUsuarioRegAsig(String usuarioRegAsig) {
        this.usuarioRegAsig = usuarioRegAsig;
    }

    public String getUsuarioEjecAsig() {
        return usuarioEjecAsig;
    }

    public void setUsuarioEjecAsig(String usuarioEjecAsig) {
        this.usuarioEjecAsig = usuarioEjecAsig;
    }

    public Date getFechaAsigReg() {
        return fechaAsigReg;
    }

    public void setFechaAsigReg(Date fechaAsigReg) {
        this.fechaAsigReg = fechaAsigReg;
    }

    public Long getFkAgenciaSaureg() {
        return fkAgenciaSaureg;
    }

    public void setFkAgenciaSaureg(Long fkAgenciaSaureg) {
        this.fkAgenciaSaureg = fkAgenciaSaureg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLogsReasig != null ? idLogsReasig.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LogsReasignacion)) {
            return false;
        }
        LogsReasignacion other = (LogsReasignacion) object;
        if ((this.idLogsReasig == null && other.idLogsReasig != null) || (this.idLogsReasig != null && !this.idLogsReasig.equals(other.idLogsReasig))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.LogsReasignacion[ idLogsReasig=" + idLogsReasig + " ]";
    }
    
}
