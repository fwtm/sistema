/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hernan.inga
 */
@Entity
@Table(name = "TBL_SAUREG_AGENCIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregAgencia.findAll", query = "SELECT t FROM TblSauregAgencia t"),
    @NamedQuery(name = "TblSauregAgencia.findByIdAgencia", query = "SELECT t FROM TblSauregAgencia t WHERE t.idAgencia = :idAgencia AND t.status = :status"),
    @NamedQuery(name = "TblSauregAgencia.findByNomAgencia", query = "SELECT t FROM TblSauregAgencia t WHERE t.nomAgencia = :nomAgencia"),
    @NamedQuery(name = "TblSauregAgencia.findByInstitucion", query = "SELECT t FROM TblSauregAgencia t WHERE t.idInstituc.idInstituc = :codInstituc AND t.status = :status AND t.idAgencia NOT IN (47)")
})
public class TblSauregAgencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "SEQ_AGENCIA", sequenceName = "SEQ_AGENCIA", allocationSize=1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AGENCIA")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_AGENCIA")
    private Long idAgencia;
    @Size(max = 100)
    @Column(name = "NOM_AGENCIA")
    private String nomAgencia;
    @Size(max = 200)
    @Column(name = "DIRECCION")
    private String direccion;
    @Size(max = 15)
    @Column(name = "TELEFONO")
    private String telefono;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 60)
    @Column(name = "E_MAIL")
    private String eMail;
    @Size(max = 150)
    @Column(name = "CONTACTO")
    private String contacto;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 20)
    @Column(name = "COD_MSP")
    private String codMsp;
    @JoinColumn(name = "ID_CANTON", referencedColumnName = "ID_UBICACION")
    @ManyToOne
    private TblSauregUbicacion idCanton;
    @JoinColumn(name = "ID_PARROQUIA", referencedColumnName = "ID_UBICACION")
    @ManyToOne
    private TblSauregUbicacion idParroquia;
    @JoinColumn(name = "ID_PROVINCIA", referencedColumnName = "ID_UBICACION")
    @ManyToOne
    private TblSauregUbicacion idProvincia;
    @JoinColumn(name = "ID_INSTITUC", referencedColumnName = "ID_INSTITUC")
    @ManyToOne
    private TblSauregInstitucion idInstituc;
    @Size(max = 10)
    @Column(name = "COD_AGENCIA")
    private String codAgencia;
    @Size(max = 10)
    @Column(name = "LOCALIDAD")
    private String localidad;
    @Size(max = 10)
    @Column(name = "EXTENCION")
    private String extencion;
    //
    @Transient
    private String eMailUsuario;
    @Transient
    private String celularUsuario;
    @Transient
    private String telefonoUsuario;
    @Transient
    private String telfExtensionUsuario;

    public TblSauregAgencia() {
    }

    public TblSauregAgencia(Long idAgencia) {
        this.idAgencia = idAgencia;
    }

    public Long getIdAgencia() {
        return idAgencia;
    }

    public void setIdAgencia(Long idAgencia) {
        this.idAgencia = idAgencia;
    }

    public String getNomAgencia() {
        return nomAgencia;
    }

    public void setNomAgencia(String nomAgencia) {
        this.nomAgencia = nomAgencia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public TblSauregUbicacion getIdCanton() {
        return idCanton;
    }

    public void setIdCanton(TblSauregUbicacion idCanton) {
        this.idCanton = idCanton;
    }

    public TblSauregUbicacion getIdParroquia() {
        return idParroquia;
    }

    public void setIdParroquia(TblSauregUbicacion idParroquia) {
        this.idParroquia = idParroquia;
    }

    public TblSauregUbicacion getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(TblSauregUbicacion idProvincia) {
        this.idProvincia = idProvincia;
    }

    public TblSauregInstitucion getIdInstituc() {
        return idInstituc;
    }

    public void setIdInstituc(TblSauregInstitucion idInstituc) {
        this.idInstituc = idInstituc;
    }

    public String getCodAgencia() {
        return codAgencia;
    }

    public void setCodAgencia(String codAgencia) {
        this.codAgencia = codAgencia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getExtencion() {
        return extencion;
    }

    public void setExtencion(String extencion) {
        this.extencion = extencion;
    }

    public String geteMailUsuario() {
        return eMailUsuario;
    }

    public void seteMailUsuario(String eMailUsuario) {
        this.eMailUsuario = eMailUsuario;
    }

    public String getCelularUsuario() {
        return celularUsuario;
    }

    public void setCelularUsuario(String celularUsuario) {
        this.celularUsuario = celularUsuario;
    }

    public String getTelefonoUsuario() {
        return telefonoUsuario;
    }

    public void setTelefonoUsuario(String telefonoUsuario) {
        this.telefonoUsuario = telefonoUsuario;
    }

    public String getTelfExtensionUsuario() {
        return telfExtensionUsuario;
    }

    public void setTelfExtensionUsuario(String telfExtensionUsuario) {
        this.telfExtensionUsuario = telfExtensionUsuario;
    }

    public String getCodMsp() {
        return codMsp;
    }

    public void setCodMsp(String codMsp) {
        this.codMsp = codMsp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAgencia != null ? idAgencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregAgencia)) {
            return false;
        }
        TblSauregAgencia other = (TblSauregAgencia) object;
        if ((this.idAgencia == null && other.idAgencia != null) || (this.idAgencia != null && !this.idAgencia.equals(other.idAgencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsinformacion.modelo.entities.TblSauregAgencia[ idAgencia=" + idAgencia + " ]";
    }
    
}
