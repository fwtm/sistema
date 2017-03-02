/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author daniel.porras
 */
@Entity
@Table(name = "ESTADO_CIVIL_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoCivilRenavi.findAll", query = "SELECT e FROM EstadoCivilRenavi e"),
    @NamedQuery(name = "EstadoCivilRenavi.findByIdEstCiv", query = "SELECT e FROM EstadoCivilRenavi e WHERE e.idEstCiv = :idEstCiv"),
    @NamedQuery(name = "EstadoCivilRenavi.findByDescEstCiv", query = "SELECT e FROM EstadoCivilRenavi e WHERE e.descEstCiv = :descEstCiv")})
public class EstadoCivilRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "SEQ_RENAVI_ESTADO_CIVIL", sequenceName = "SEQ_RENAVI_ESTADO_CIVIL", allocationSize=1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RENAVI_ESTADO_CIVIL")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_EST_CIV")
    private Integer idEstCiv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "DESC_EST_CIV")
    private String descEstCiv;
    @OneToMany(mappedBy = "fkIdEstadoCivilFal", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;
    @OneToMany(mappedBy = "fkIdEstCivMad", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public EstadoCivilRenavi() {
    }

    public EstadoCivilRenavi(Integer idEstCiv) {
        this.idEstCiv = idEstCiv;
    }

    public EstadoCivilRenavi(Integer idEstCiv, String descEstCiv) {
        this.idEstCiv = idEstCiv;
        this.descEstCiv = descEstCiv;
    }

    public Integer getIdEstCiv() {
        return idEstCiv;
    }

    public void setIdEstCiv(Integer idEstCiv) {
        this.idEstCiv = idEstCiv;
    }

    public String getDescEstCiv() {
        return descEstCiv;
    }

    public void setDescEstCiv(String descEstCiv) {
        this.descEstCiv = descEstCiv;
    }

    @XmlTransient
    public List<Fallecido> getFallecidoList() {
        return fallecidoList;
    }

    public void setFallecidoList(List<Fallecido> fallecidoList) {
        this.fallecidoList = fallecidoList;
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
        hash += (idEstCiv != null ? idEstCiv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoCivilRenavi)) {
            return false;
        }
        EstadoCivilRenavi other = (EstadoCivilRenavi) object;
        if ((this.idEstCiv == null && other.idEstCiv != null) || (this.idEstCiv != null && !this.idEstCiv.equals(other.idEstCiv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descEstCiv;
    }
    
}
