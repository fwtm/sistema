/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "ESTADO_FIRMA_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoFirmaRenavi.findAll", query = "SELECT e FROM EstadoFirmaRenavi e"),
    @NamedQuery(name = "EstadoFirmaRenavi.findByIdEstFir", query = "SELECT e FROM EstadoFirmaRenavi e WHERE e.idEstFir = :idEstFir"),
    @NamedQuery(name = "EstadoFirmaRenavi.findByDescEstFir", query = "SELECT e FROM EstadoFirmaRenavi e WHERE e.descEstFir = :descEstFir")})
public class EstadoFirmaRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_EST_FIR")
    private Integer idEstFir;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "DESC_EST_FIR")
    private String descEstFir;
    @OneToMany(mappedBy = "fkIdEstadoFirma", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkIdEstFir", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public EstadoFirmaRenavi() {
    }

    public EstadoFirmaRenavi(Integer idEstFir) {
        this.idEstFir = idEstFir;
    }

    public EstadoFirmaRenavi(Integer idEstFir, String descEstFir) {
        this.idEstFir = idEstFir;
        this.descEstFir = descEstFir;
    }

    public Integer getIdEstFir() {
        return idEstFir;
    }

    public void setIdEstFir(Integer idEstFir) {
        this.idEstFir = idEstFir;
    }

    public String getDescEstFir() {
        return descEstFir;
    }

    public void setDescEstFir(String descEstFir) {
        this.descEstFir = descEstFir;
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
        hash += (idEstFir != null ? idEstFir.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoFirmaRenavi)) {
            return false;
        }
        EstadoFirmaRenavi other = (EstadoFirmaRenavi) object;
        if ((this.idEstFir == null && other.idEstFir != null) || (this.idEstFir != null && !this.idEstFir.equals(other.idEstFir))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descEstFir;
    }
    
}
