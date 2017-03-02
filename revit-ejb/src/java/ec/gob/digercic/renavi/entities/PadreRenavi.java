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
import javax.persistence.Id;
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
@Table(name = "PADRE_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PadreRenavi.findAll", query = "SELECT p FROM PadreRenavi p"),
    @NamedQuery(name = "PadreRenavi.findByIdPad", query = "SELECT p FROM PadreRenavi p WHERE p.idPad = :idPad"),
    @NamedQuery(name = "PadreRenavi.findByCedulPad", query = "SELECT p FROM PadreRenavi p WHERE p.cedulPad = :cedulPad")})
public class PadreRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PAD")
    private Long idPad;
    @Size(max = 10)
    @Column(name = "CEDUL_PAD")
    private String cedulPad;
    @Size(max = 100)
    @Column(name = "NOMBR_PAD")
    private String nombrPad;
    @Size(max = 13)
    @Column(name = "PASAP_PAD")
    private String pasapPad;
    @Column(name = "FECHA_NACIM_PAD")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimPad;
    @OneToMany(mappedBy = "fkCedulPad", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public PadreRenavi() {
    }

    public PadreRenavi(Long idPad) {
        this.idPad = idPad;
    }

    public Long getIdPad() {
        return idPad;
    }

    public void setIdPad(Long idPad) {
        this.idPad = idPad;
    }

    public String getCedulPad() {
        return cedulPad;
    }

    public void setCedulPad(String cedulPad) {
        this.cedulPad = cedulPad;
    }

    public String getNombrPad() {
        return nombrPad;
    }

    public void setNombrPad(String nombrPad) {
        this.nombrPad = nombrPad;
    }

    public String getPasapPad() {
        return pasapPad;
    }

    public void setPasapPad(String pasapPad) {
        this.pasapPad = pasapPad;
    }

    public Date getFechaNacimPad() {
        return fechaNacimPad;
    }

    public void setFechaNacimPad(Date fechaNacimPad) {
        this.fechaNacimPad = fechaNacimPad;
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
        hash += (idPad != null ? idPad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PadreRenavi)) {
            return false;
        }
        PadreRenavi other = (PadreRenavi) object;
        if ((this.idPad == null && other.idPad != null) || (this.idPad != null && !this.idPad.equals(other.idPad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.PadreRenavi[ idPad=" + idPad + " ]";
    }
    
}
