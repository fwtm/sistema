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
@Table(name = "IDENTIFICACION_ETNICA_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IdentificacionEtnicaRenavi.findAll", query = "SELECT i FROM IdentificacionEtnicaRenavi i"),
    @NamedQuery(name = "IdentificacionEtnicaRenavi.findByIdIdeEtn", query = "SELECT i FROM IdentificacionEtnicaRenavi i WHERE i.idIdeEtn = :idIdeEtn"),
    @NamedQuery(name = "IdentificacionEtnicaRenavi.findByDescIdeEtn", query = "SELECT i FROM IdentificacionEtnicaRenavi i WHERE i.descIdeEtn = :descIdeEtn")})
public class IdentificacionEtnicaRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "SEQ_RENAVI_IDENTIF_ETNICA", sequenceName = "SEQ_RENAVI_IDENTIF_ETNICA", allocationSize=1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RENAVI_IDENTIF_ETNICA")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_IDE_ETN")
    private Integer idIdeEtn;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "DESC_IDE_ETN")
    private String descIdeEtn;
    @OneToMany(mappedBy = "fkIdEtnia", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;
    @OneToMany(mappedBy = "fkIdIdeEtnMad", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public IdentificacionEtnicaRenavi() {
    }

    public IdentificacionEtnicaRenavi(Integer idIdeEtn) {
        this.idIdeEtn = idIdeEtn;
    }

    public IdentificacionEtnicaRenavi(Integer idIdeEtn, String descIdeEtn) {
        this.idIdeEtn = idIdeEtn;
        this.descIdeEtn = descIdeEtn;
    }

    public Integer getIdIdeEtn() {
        return idIdeEtn;
    }

    public void setIdIdeEtn(Integer idIdeEtn) {
        this.idIdeEtn = idIdeEtn;
    }

    public String getDescIdeEtn() {
        return descIdeEtn;
    }

    public void setDescIdeEtn(String descIdeEtn) {
        this.descIdeEtn = descIdeEtn;
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
        hash += (idIdeEtn != null ? idIdeEtn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IdentificacionEtnicaRenavi)) {
            return false;
        }
        IdentificacionEtnicaRenavi other = (IdentificacionEtnicaRenavi) object;
        if ((this.idIdeEtn == null && other.idIdeEtn != null) || (this.idIdeEtn != null && !this.idIdeEtn.equals(other.idIdeEtn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descIdeEtn;
    }
    
}
