/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*cambio de Juan Hernandez*/
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
 * @author juan.hernandez
 */
@Entity
@Table(name = "LOGS_ELIMINACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LogsEliminacion.findAll", query = "SELECT l FROM LogsEliminacion l"),
    @NamedQuery(name = "LogsEliminacion.findByIdEli", query = "SELECT l FROM LogsEliminacion l WHERE l.idEli = :idEli"),
    @NamedQuery(name = "LogsEliminacion.findByFechaEli", query = "SELECT l FROM LogsEliminacion l WHERE l.fechaEli = :fechaEli"),
    @NamedQuery(name = "LogsEliminacion.findByUsuarioEli", query = "SELECT l FROM LogsEliminacion l WHERE l.usuarioEli = :usuarioEli"),
    @NamedQuery(name = "LogsEliminacion.findByAgenIdEli", query = "SELECT l FROM LogsEliminacion l WHERE l.agenIdEli = :agenIdEli"),
    @NamedQuery(name = "LogsEliminacion.findByNomUsuEli", query = "SELECT l FROM LogsEliminacion l WHERE l.nomUsuEli = :nomUsuEli"),
    @NamedQuery(name = "LogsEliminacion.findByApeUsuEli", query = "SELECT l FROM LogsEliminacion l WHERE l.apeUsuEli = :apeUsuEli"),
    @NamedQuery(name = "LogsEliminacion.findByInstIdEli", query = "SELECT l FROM LogsEliminacion l WHERE l.instIdEli = :instIdEli"),
    @NamedQuery(name = "LogsEliminacion.findByIpEli", query = "SELECT l FROM LogsEliminacion l WHERE l.ipEli = :ipEli"),
    @NamedQuery(name = "LogsEliminacion.findByIdMadre", query = "SELECT l FROM LogsEliminacion l WHERE l.idMadre = :idMadre"),
    @NamedQuery(name = "LogsEliminacion.findByCedulMadre", query = "SELECT l FROM LogsEliminacion l WHERE l.cedulMadre = :cedulMadre"),
    @NamedQuery(name = "LogsEliminacion.findByNomMadre", query = "SELECT l FROM LogsEliminacion l WHERE l.nomMadre = :nomMadre"),
    @NamedQuery(name = "LogsEliminacion.findByPasapMadre", query = "SELECT l FROM LogsEliminacion l WHERE l.pasapMadre = :pasapMadre"),
    @NamedQuery(name = "LogsEliminacion.findByStatusRegistro", query = "SELECT l FROM LogsEliminacion l WHERE l.statusRegistro = :statusRegistro"),
   })
public class LogsEliminacion implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
     @SequenceGenerator(name = "SEQ_RENAVI_LOG_ELIMINACION", sequenceName = "SEQ_RENAVI_LOG_ELIMINACION", allocationSize = 1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RENAVI_LOG_ELIMINACION")
    @Column(name = "ID_LOG_ELI", nullable = false, precision = 22)
    private BigDecimal idEli;
    @Column(name = "FECHA_LOG_ELI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEli;
    @Column(name = "USUARIO_LOG_ELI", length = 15)
    private String usuarioEli;
    @Column(name = "AGEN_ID_LOG_ELI", length = 15)
    private String agenIdEli;
    @Column(name = "NOM_USU_LOG_ELI", length = 60)
    private String nomUsuEli;
    @Column(name = "APE_USU_LOG_ELI", length = 60)
    private String apeUsuEli;
    @Column(name = "INST_ID_LOG_ELI" , length = 15)
    private String instIdEli;
    @Column(name = "IP_LOG_ELI", length = 25)
    private String ipEli;
    @Column(name = "ID_MAD_ELI", length = 25)
    private Long idMadre;
    @Column(name = "CEDUL_MAD_ELI",length = 15)
    private String cedulMadre;
    @Column(name = "NOMBR_MAD_ELI", length = 120)
    private String nomMadre;
    @Column(name = "PASAP_MAD_ELI", length = 20)
    private String pasapMadre;
    @Column(name = "ESTADO_REGISTRO_ELI", length = 1)
    private String statusRegistro;
    
    

    public LogsEliminacion() {
    }

    public BigDecimal getIdEli() {
        return idEli;
    }

    public void setIdEli(BigDecimal idEli) {
        this.idEli = idEli;
    }

    public Date getFechaEli() {
        return fechaEli;
    }

    public void setFechaEli(Date fechaEli) {
        this.fechaEli = fechaEli;
    }

    public String getUsuarioEli() {
        return usuarioEli;
    }

    public void setUsuarioEli(String usuarioEli) {
        this.usuarioEli = usuarioEli;
    }

    public String getAgenIdEli() {
        return agenIdEli;
    }

    public void setAgenIdEli(String agenIdEli) {
        this.agenIdEli = agenIdEli;
    }

    public String getNomUsuEli() {
        return nomUsuEli;
    }

    public void setNomUsuEli(String nomUsuEli) {
        this.nomUsuEli = nomUsuEli;
    }

    public String getApeUsuEli() {
        return apeUsuEli;
    }

    public void setApeUsuEli(String apeUsuEli) {
        this.apeUsuEli = apeUsuEli;
    }

    public String getInstIdEli() {
        return instIdEli;
    }

    public void setInstIdEli(String instIdEli) {
        this.instIdEli = instIdEli;
    }

    public String getIpEli() {
        return ipEli;
    }

    public void setIpEli(String ipEli) {
        this.ipEli = ipEli;
    }

    public Long getIdMadre() {
        return idMadre;
    }

    public void setIdMadre(Long idMadre) {
        this.idMadre = idMadre;
    }

 
  

    public String getCedulMadre() {
        return cedulMadre;
    }

    public void setCedulMadre(String cedulMadre) {
        this.cedulMadre = cedulMadre;
    }

    public String getNomMadre() {
        return nomMadre;
    }

    public void setNomMadre(String nomMadre) {
        this.nomMadre = nomMadre;
    }

    public String getPasapMadre() {
        return pasapMadre;
    }

    public void setPasapMadre(String pasapMadre) {
        this.pasapMadre = pasapMadre;
    }

    public String getStatusRegistro() {
        return statusRegistro;
    }

    public void setStatusRegistro(String statusRegistro) {
        this.statusRegistro = statusRegistro;
    }






    @Override
    public int hashCode() {
        int hash = 0;
    //    hash += (id != null ? idEli.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LogsEliminacion)) {
            return false;
        }
        LogsEliminacion other = (LogsEliminacion) object;
        if ((this.idEli == null && other.idEli != null) || (this.idEli != null && !this.idEli.equals(other.idEli))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Registro de LogEliminacion[ idEli=" + idEli + " ]";
    }
    
}
