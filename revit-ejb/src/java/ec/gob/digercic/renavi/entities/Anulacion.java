/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author santiago.tapia
 */
@Entity
@Table(name = "ANULACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Anulacion.findAll", query = "SELECT a FROM Anulacion a"),
    @NamedQuery(name = "Anulacion.findByCedNacViv", query = "SELECT a FROM Anulacion a WHERE a.fkCedNacViv.cedulNacViv = :cedNacViv"),
    @NamedQuery(name = "Anulacion.findById", query = "SELECT a FROM Anulacion a WHERE a.id = :id"),
    @NamedQuery(name = "Anulacion.findByNomMedicoAnulNacViv", query = "SELECT a FROM Anulacion a WHERE a.nomMedicoAnulNacViv = :nomMedicoAnulNacViv"),
    @NamedQuery(name = "Anulacion.findByApellMedicoAnulNacViv", query = "SELECT a FROM Anulacion a WHERE a.apellMedicoAnulNacViv = :apellMedicoAnulNacViv"),
    @NamedQuery(name = "Anulacion.findByUserMedicoAnulNacViv", query = "SELECT a FROM Anulacion a WHERE a.userMedicoAnulNacViv = :userMedicoAnulNacViv"),
    @NamedQuery(name = "Anulacion.findByFechaAnul", query = "SELECT a FROM Anulacion a WHERE a.fechaAnul = :fechaAnul"),
    @NamedQuery(name = "Anulacion.findByJustificacion", query = "SELECT a FROM Anulacion a WHERE a.justificacion = :justificacion"),
    @NamedQuery(name = "Anulacion.findByCedSupervisorAnulNacViv", query = "SELECT a FROM Anulacion a WHERE a.cedSupervisorAnulNacViv = :cedSupervisorAnulNacViv"),
    @NamedQuery(name = "Anulacion.findByEstAnulNacViv", query = "SELECT a FROM Anulacion a WHERE a.estAnulNacViv = :estAnulNacViv"),
    @NamedQuery(name = "Anulacion.findByFkCedNacViv", query = "SELECT a FROM Anulacion a WHERE a.fkCedNacViv.idNacViv = :idNacViv")
})
public class Anulacion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "SEQ_RENAVI_ANULA_NV", sequenceName = "SEQ_RENAVI_ANULA_NV", allocationSize = 1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RENAVI_ANULA_NV")
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Size(max = 50)
    @Column(name = "NOM_MEDICO_ANUL_NAC_VIV")
    private String nomMedicoAnulNacViv;
    @Size(max = 50)
    @Column(name = "APELL_MEDICO_ANUL_NAC_VIV")
    private String apellMedicoAnulNacViv;
    @Size(max = 10)
    @Column(name = "USER_MEDICO_ANUL_NAC_VIV")
    private String userMedicoAnulNacViv;
    @Column(name = "FECHA_ANUL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnul;
    @Size(max = 256)
    @Column(name = "JUSTIFICACION")
    private String justificacion;
    @Size(max = 256)
    @Column(name = "OBS_RECH")
    private String obsRech;
    @Size(max = 10)
    @Column(name = "CED_SUPERVISOR_ANUL_NAC_VIV")
    private String cedSupervisorAnulNacViv;
    @Size(max = 1)
    @Column(name = "EST_ANUL_NAC_VIV")
    private String estAnulNacViv;
    @Column(name = "FECHA_APROBACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAprobacion;
    @Column(name = "FECHA_RECHAZO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRechazo;
    @Column(name = "FECHA_FINALIZACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinalizacion;
    @Size(max = 10)
    @Column(name = "OBSERVACION_RECHAZO_LEIDO")
    private String observacionRechazoLeido;
    @ManyToMany()
    private List<Error1> err;
    @JoinColumn(name = "FK_CED_NAC_VIV", referencedColumnName = "ID_NAC_VIV")
    @ManyToOne
    private NacidoVivoRenavi fkCedNacViv;
    @Column(name = "ID_AGENCIA_ANUL")
    private BigDecimal idAgenciaAnul;

    public Anulacion() {
    }

    public Anulacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomMedicoAnulNacViv() {
        return nomMedicoAnulNacViv;
    }

    public void setNomMedicoAnulNacViv(String nomMedicoAnulNacViv) {
        this.nomMedicoAnulNacViv = nomMedicoAnulNacViv;
    }

    public String getApellMedicoAnulNacViv() {
        return apellMedicoAnulNacViv;
    }

    public void setApellMedicoAnulNacViv(String apellMedicoAnulNacViv) {
        this.apellMedicoAnulNacViv = apellMedicoAnulNacViv;
    }

    public String getUserMedicoAnulNacViv() {
        return userMedicoAnulNacViv;
    }

    public void setUserMedicoAnulNacViv(String userMedicoAnulNacViv) {
        this.userMedicoAnulNacViv = userMedicoAnulNacViv;
    }

    public Date getFechaAnul() {
        return fechaAnul;
    }

    public void setFechaAnul(Date fechaAnul) {
        this.fechaAnul = fechaAnul;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public String getCedSupervisorAnulNacViv() {
        return cedSupervisorAnulNacViv;
    }

    public void setCedSupervisorAnulNacViv(String cedSupervisorAnulNacViv) {
        this.cedSupervisorAnulNacViv = cedSupervisorAnulNacViv;
    }

    public String getEstAnulNacViv() {
        return estAnulNacViv;
    }

    public void setEstAnulNacViv(String estAnulNacViv) {
        this.estAnulNacViv = estAnulNacViv;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Date getFechaRechazo() {
        return fechaRechazo;
    }

    public void setFechaRechazo(Date fechaRechazo) {
        this.fechaRechazo = fechaRechazo;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getObservacionRechazoLeido() {
        return observacionRechazoLeido;
    }

    public void setObservacionRechazoLeido(String observacionRechazoLeido) {
        this.observacionRechazoLeido = observacionRechazoLeido;
    } 
    
    /**
     * Ordenar lista***************************
     */
    private static Comparator<Error1> COMPARATOR = new Comparator<Error1>() {
        // This is where the sorting happens.
        @Override
        public int compare(Error1 e1, Error1 e2) {
            return e1.getId().compareTo(e2.getId());
        }
    };
    
    
    @XmlTransient
    public List<Error1> getErr() {
        Collections.sort(err, COMPARATOR);
        return err;
    }

    public void setErr(List<Error1> err) {
        this.err = err;
    }

    public NacidoVivoRenavi getFkCedNacViv() {
        return fkCedNacViv;
    }

    public void setFkCedNacViv(NacidoVivoRenavi fkCedNacViv) {
        this.fkCedNacViv = fkCedNacViv;
    }

    /**
     * @return the ObsRech
     */
    public String getObsRech() {
        return obsRech;
    }

    /**
     * @param ObsRech the ObsRech to set
     */
    public void setObsRech(String obsRech) {
        this.obsRech = obsRech;
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
        if (!(object instanceof Anulacion)) {
            return false;
        }
        Anulacion other = (Anulacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.Anulacion[ id=" + id + " ]";
    }

    /**
     * @return the idAgenciaAnul
     */
    public BigDecimal getIdAgenciaAnul() {
        return idAgenciaAnul;
    }

    /**
     * @param idAgenciaAnul the idAgenciaAnul to set
     */
    public void setIdAgenciaAnul(BigDecimal idAgenciaAnul) {
        this.idAgenciaAnul = idAgenciaAnul;
    }

    
}
