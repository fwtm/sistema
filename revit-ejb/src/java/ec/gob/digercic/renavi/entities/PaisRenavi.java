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
@Table(name = "PAIS_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaisRenavi.findAll", query = "SELECT p FROM PaisRenavi p"),
    @NamedQuery(name = "PaisRenavi.findByIdPais", query = "SELECT p FROM PaisRenavi p WHERE p.idPais = :idPais"),
    @NamedQuery(name = "PaisRenavi.findByDescPais", query = "SELECT p FROM PaisRenavi p WHERE p.descPais = :descPais"),
    @NamedQuery(name = "PaisRenavi.findByCodigPais", query = "SELECT p FROM PaisRenavi p WHERE p.codigPais = :codigPais"),
    //por henrry
    @NamedQuery(name = "PaisRenavi.findByCodigPaisMe", query = "SELECT p FROM PaisRenavi p WHERE p.idPais <> :idPais")
})
public class PaisRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PAIS")
    private Integer idPais;
    @Size(max = 100)
    @Column(name = "DESC_PAIS")
    private String descPais;
    @Size(max = 2)
    @Column(name = "CODIG_PAIS")
    private String codigPais;
    @OneToMany(mappedBy = "fkIdPais", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;
    @OneToMany(mappedBy = "fkIdPaisMad", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;
    @OneToMany(mappedBy = "fkIdPaisResidnMad", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList1;

    public PaisRenavi() {
    }

    public PaisRenavi(Integer idPais) {
        this.idPais = idPais;
    }

    public Integer getIdPais() {
        return idPais;
    }

    public void setIdPais(Integer idPais) {
        this.idPais = idPais;
    }

    public String getDescPais() {
        return descPais;
    }

    public void setDescPais(String descPais) {
        this.descPais = descPais;
    }

    public String getCodigPais() {
        return codigPais;
    }

    public void setCodigPais(String codigPais) {
        this.codigPais = codigPais;
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

    @XmlTransient
    public List<NacidoVivoRenavi> getNacidoVivoRenaviList1() {
        return nacidoVivoRenaviList1;
    }

    public void setNacidoVivoRenaviList1(List<NacidoVivoRenavi> nacidoVivoRenaviList1) {
        this.nacidoVivoRenaviList1 = nacidoVivoRenaviList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPais != null ? idPais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaisRenavi)) {
            return false;
        }
        PaisRenavi other = (PaisRenavi) object;
        if ((this.idPais == null && other.idPais != null) || (this.idPais != null && !this.idPais.equals(other.idPais))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descPais;
    }
    
}
