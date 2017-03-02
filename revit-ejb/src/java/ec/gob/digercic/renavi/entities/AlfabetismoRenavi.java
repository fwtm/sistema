/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "ALFABETISMO_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlfabetismoRenavi.findAll", query = "SELECT a FROM AlfabetismoRenavi a"),
    @NamedQuery(name = "AlfabetismoRenavi.findByIdAlfb", query = "SELECT a FROM AlfabetismoRenavi a WHERE a.idAlfb = :idAlfb"),
    @NamedQuery(name = "AlfabetismoRenavi.findByDescAlfb", query = "SELECT a FROM AlfabetismoRenavi a WHERE a.descAlfb = :descAlfb")})
public class AlfabetismoRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_ALFB")
    private Integer idAlfb;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "DESC_ALFB")
    private String descAlfb;
    @OneToMany(mappedBy = "fkIdAlfabetismo", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;
    @OneToMany(mappedBy = "fkIdSabeLeerMad", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public AlfabetismoRenavi() {
    }

    public AlfabetismoRenavi(Integer idAlfb) {
        this.idAlfb = idAlfb;
    }

    public AlfabetismoRenavi(Integer idAlfb, String descAlfb) {
        this.idAlfb = idAlfb;
        this.descAlfb = descAlfb;
    }

    public Integer getIdAlfb() {
        return idAlfb;
    }

    public void setIdAlfb(Integer idAlfb) {
        this.idAlfb = idAlfb;
    }

    public String getDescAlfb() {
        return descAlfb;
    }

    public void setDescAlfb(String descAlfb) {
        this.descAlfb = descAlfb;
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
        hash += (idAlfb != null ? idAlfb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlfabetismoRenavi)) {
            return false;
        }
        AlfabetismoRenavi other = (AlfabetismoRenavi) object;
        if ((this.idAlfb == null && other.idAlfb != null) || (this.idAlfb != null && !this.idAlfb.equals(other.idAlfb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descAlfb;
    }
    
}
