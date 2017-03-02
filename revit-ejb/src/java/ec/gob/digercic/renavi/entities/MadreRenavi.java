/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
 * @author daniel.porras
 */
@Entity
@Table(name = "MADRE_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MadreRenavi.findAll", query = "SELECT m FROM MadreRenavi m"),
    @NamedQuery(name = "MadreRenavi.findByIdMad", query = "SELECT m FROM MadreRenavi m WHERE m.idMad = :idMad"),
    @NamedQuery(name = "MadreRenavi.findByCedulMad", query = "SELECT m FROM MadreRenavi m WHERE m.cedulMad = :cedulMad"),
    @NamedQuery(name = "MadreRenavi.findByNombrMad", query = "SELECT m FROM MadreRenavi m WHERE m.nombrMad = :nombrMad"),
    @NamedQuery(name = "MadreRenavi.findByFechaNacimMad", query = "SELECT m FROM MadreRenavi m WHERE m.fechaNacimMad = :fechaNacimMad"),
    /**/
    @NamedQuery(name = "MadreRenavi.findByCedulMadAndStstus", query = "SELECT m FROM MadreRenavi m WHERE m.cedulMad = :cedulMad AND m.status = :status"),
    /**/
    @NamedQuery(name = "MadreRenavi.findByPasapMad", query = "SELECT m FROM MadreRenavi m WHERE m.pasapMad = :pasapMad")
})
public class MadreRenavi implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "SEQ_RENAVI_MADRE", sequenceName = "SEQ_RENAVI_MADRE", allocationSize = 1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RENAVI_MADRE")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_MAD")
    private Long idMad;
    @Size(max = 10)
    @Column(name = "CEDUL_MAD")
    private String cedulMad;
    @Size(max = 120)
    @Column(name = "NOMBR_MAD")
    private String nombrMad;
    @Column(name = "FECHA_NACIM_MAD")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimMad;
    @Size(max = 20)
    @Column(name = "PASAP_MAD")
    private String pasapMad;
    @Column(name = "ANIO_NACIM_MAD")
    private Short anioNacimMad;
    @Column(name = "MES_NACIM_MAD")
    private Short mesNacimMad;
    @Column(name = "DIA_NACIM_MAD")
    private Short diaNacimMad;
    @Size(max = 120)
    @Column(name = "NOM_PADRE_MAD")
    private String nomPadreMad;
    @Size(max = 120)
    @Column(name = "NOM_MADRE_MAD")
    private String nomMadreMad;
    @Size(max = 1)
    @Column(name = "STATUS")
    private String status;
    @Size(max = 10)
    @Column(name = "CELULAR_MAD")
    private String celularMad;
    @Size(max = 100)
    @Column(name = "MAIL_MAD")
    private String mailMad;
    @Column(name = "TELEFONO_MAD")
    private String telefonoMad;
    //  @Size(max = 9)
    @Size(max = 50)
    @Column(name = "APELLIDOS_MAD_INDOC")
    private String apellidosMadIndoc;
    @Size(max = 50)
    @Column(name = "NOMBRES_MAD_INDOC")
    private String nombresMadIndoc;

    @JoinColumn(name = "FK_ID_TIPO_IDENT", referencedColumnName = "ID_TIPO_IDENT")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoIdentRenavi fkIdTipoIdent;
    @OneToMany(mappedBy = "fkCedulMad", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public MadreRenavi() {
    }

    public MadreRenavi(Long idMad) {
        this.idMad = idMad;
    }

    public Long getIdMad() {
        return idMad;
    }

    public void setIdMad(Long idMad) {
        this.idMad = idMad;
    }

    public String getCedulMad() {
        return cedulMad;
    }

    public void setCedulMad(String cedulMad) {
        this.cedulMad = cedulMad;
    }

    public String getNombrMad() {
        return nombrMad;
    }

    public void setNombrMad(String nombrMad) {
        this.nombrMad = nombrMad;
    }

    public Date getFechaNacimMad() {
        return fechaNacimMad;
    }

    public void setFechaNacimMad(Date fechaNacimMad) {
        this.fechaNacimMad = fechaNacimMad;
    }

    public String getPasapMad() {
        return pasapMad;
    }

    public void setPasapMad(String pasapMad) {
        this.pasapMad = pasapMad;
    }

    public Short getAnioNacimMad() {
        return anioNacimMad;
    }

    public void setAnioNacimMad(Short anioNacimMad) {
        this.anioNacimMad = anioNacimMad;
    }

    public Short getMesNacimMad() {
        return mesNacimMad;
    }

    public void setMesNacimMad(Short mesNacimMad) {
        this.mesNacimMad = mesNacimMad;
    }

    public Short getDiaNacimMad() {
        return diaNacimMad;
    }

    public void setDiaNacimMad(Short diaNacimMad) {
        this.diaNacimMad = diaNacimMad;
    }

    public String getNomPadreMad() {
        return nomPadreMad;
    }

    public void setNomPadreMad(String nomPadreMad) {
        this.nomPadreMad = nomPadreMad;
    }

    public String getNomMadreMad() {
        return nomMadreMad;
    }

    public void setNomMadreMad(String nomMadreMad) {
        this.nomMadreMad = nomMadreMad;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TipoIdentRenavi getFkIdTipoIdent() {
        return fkIdTipoIdent;
    }

    public void setFkIdTipoIdent(TipoIdentRenavi fkIdTipoIdent) {
        this.fkIdTipoIdent = fkIdTipoIdent;
    }

    public String getCelularMad() {
        return celularMad;
    }

    public void setCelularMad(String celularMad) {
        this.celularMad = celularMad;
    }

    public String getMailMad() {
        return mailMad;
    }

    public void setMailMad(String mailMad) {
        this.mailMad = mailMad;
    }

    public String getTelefonoMad() {
        return telefonoMad;
    }

    public void setTelefonoMad(String telefonoMad) {
        this.telefonoMad = telefonoMad;
    }

    @XmlTransient
    public List<NacidoVivoRenavi> getNacidoVivoRenaviList() {
        return nacidoVivoRenaviList;
    }

    public void setNacidoVivoRenaviList(List<NacidoVivoRenavi> nacidoVivoRenaviList) {
        this.nacidoVivoRenaviList = nacidoVivoRenaviList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMad != null ? idMad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MadreRenavi)) {
            return false;
        }
        MadreRenavi other = (MadreRenavi) object;
        if ((this.idMad == null && other.idMad != null) || (this.idMad != null && !this.idMad.equals(other.idMad))) {
            return false;
        }
        return true;
    }

    public String getApellidosMadIndoc() {
        return apellidosMadIndoc;
    }

    public void setApellidosMadIndoc(String apellidosMadIndoc) {
        this.apellidosMadIndoc = apellidosMadIndoc;
    }

    public String getNombresMadIndoc() {
        return nombresMadIndoc;
    }

    public void setNombresMadIndoc(String nombresMadIndoc) {
        this.nombresMadIndoc = nombresMadIndoc;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.MadreRenavi[ idMad=" + idMad + " ]";
    }

}
