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
@Table(name = "LUGAR_MUERTE_VIOLENTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LugarMuerteViolenta.findAll", query = "SELECT l FROM LugarMuerteViolenta l"),
    @NamedQuery(name = "LugarMuerteViolenta.findByIdLugMueVio", query = "SELECT l FROM LugarMuerteViolenta l WHERE l.idLugMueVio = :idLugMueVio"),
    @NamedQuery(name = "LugarMuerteViolenta.findByNombreLugMueVio", query = "SELECT l FROM LugarMuerteViolenta l WHERE l.nombreLugMueVio = :nombreLugMueVio")})
public class LugarMuerteViolenta implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_LUG_MUE_VIO")
    private Integer idLugMueVio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE_LUG_MUE_VIO")
    private String nombreLugMueVio;
    @OneToMany(mappedBy = "fkIdLugMueVio", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;

    public LugarMuerteViolenta() {
    }

    public LugarMuerteViolenta(Integer idLugMueVio) {
        this.idLugMueVio = idLugMueVio;
    }

    public LugarMuerteViolenta(Integer idLugMueVio, String nombreLugMueVio) {
        this.idLugMueVio = idLugMueVio;
        this.nombreLugMueVio = nombreLugMueVio;
    }

    public Integer getIdLugMueVio() {
        return idLugMueVio;
    }

    public void setIdLugMueVio(Integer idLugMueVio) {
        this.idLugMueVio = idLugMueVio;
    }

    public String getNombreLugMueVio() {
        return nombreLugMueVio;
    }

    public void setNombreLugMueVio(String nombreLugMueVio) {
        this.nombreLugMueVio = nombreLugMueVio;
    }

    @XmlTransient
    public List<Fallecido> getFallecidoList() {
        return fallecidoList;
    }

    public void setFallecidoList(List<Fallecido> fallecidoList) {
        this.fallecidoList = fallecidoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLugMueVio != null ? idLugMueVio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LugarMuerteViolenta)) {
            return false;
        }
        LugarMuerteViolenta other = (LugarMuerteViolenta) object;
        if ((this.idLugMueVio == null && other.idLugMueVio != null) || (this.idLugMueVio != null && !this.idLugMueVio.equals(other.idLugMueVio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.LugarMuerteViolenta[ idLugMueVio=" + idLugMueVio + " ]";
    }
    
}
