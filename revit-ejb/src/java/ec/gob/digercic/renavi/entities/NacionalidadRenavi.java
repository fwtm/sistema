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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "NACIONALIDAD_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NacionalidadRenavi.findAll", query = "SELECT n FROM NacionalidadRenavi n"),
    @NamedQuery(name = "NacionalidadRenavi.findByIdNac", query = "SELECT n FROM NacionalidadRenavi n WHERE n.idNac = :idNac"),
    @NamedQuery(name = "NacionalidadRenavi.findByDescNac", query = "SELECT n FROM NacionalidadRenavi n WHERE n.descNac = :descNac")})
public class NacionalidadRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_NAC")
    private Integer idNac;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "DESC_NAC")
    private String descNac;
    @OneToMany(mappedBy = "fkIdNacionalidad", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;
    @OneToMany(mappedBy = "fkIdNacMad", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public NacionalidadRenavi() {
    }

    public NacionalidadRenavi(Integer idNac) {
        this.idNac = idNac;
    }

    public NacionalidadRenavi(Integer idNac, String descNac) {
        this.idNac = idNac;
        this.descNac = descNac;
    }

    public Integer getIdNac() {
        return idNac;
    }

    public void setIdNac(Integer idNac) {
        this.idNac = idNac;
    }

    public String getDescNac() {
        return descNac;
    }

    public void setDescNac(String descNac) {
        this.descNac = descNac;
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
        hash += (idNac != null ? idNac.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NacionalidadRenavi)) {
            return false;
        }
        NacionalidadRenavi other = (NacionalidadRenavi) object;
        if ((this.idNac == null && other.idNac != null) || (this.idNac != null && !this.idNac.equals(other.idNac))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descNac;
    }
    
}
