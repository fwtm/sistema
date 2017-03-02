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
@Table(name = "TIPO_REGISTRO_FALLECIMIENTO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoRegistroFallecimiento.findAll", query = "SELECT t FROM TipoRegistroFallecimiento t"),
    @NamedQuery(name = "TipoRegistroFallecimiento.findByIdTipRegFal", query = "SELECT t FROM TipoRegistroFallecimiento t WHERE t.idTipRegFal = :idTipRegFal"),
    @NamedQuery(name = "TipoRegistroFallecimiento.findByNombreTipRegFal", query = "SELECT t FROM TipoRegistroFallecimiento t WHERE t.nombreTipRegFal = :nombreTipRegFal")})
public class TipoRegistroFallecimiento implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIP_REG_FAL")
    private Integer idTipRegFal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE_TIP_REG_FAL")
    private String nombreTipRegFal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkIdTipRegFal", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;

    public TipoRegistroFallecimiento() {
    }

    public TipoRegistroFallecimiento(Integer idTipRegFal) {
        this.idTipRegFal = idTipRegFal;
    }

    public TipoRegistroFallecimiento(Integer idTipRegFal, String nombreTipRegFal) {
        this.idTipRegFal = idTipRegFal;
        this.nombreTipRegFal = nombreTipRegFal;
    }

    public Integer getIdTipRegFal() {
        return idTipRegFal;
    }

    public void setIdTipRegFal(Integer idTipRegFal) {
        this.idTipRegFal = idTipRegFal;
    }

    public String getNombreTipRegFal() {
        return nombreTipRegFal;
    }

    public void setNombreTipRegFal(String nombreTipRegFal) {
        this.nombreTipRegFal = nombreTipRegFal;
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
        hash += (idTipRegFal != null ? idTipRegFal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoRegistroFallecimiento)) {
            return false;
        }
        TipoRegistroFallecimiento other = (TipoRegistroFallecimiento) object;
        if ((this.idTipRegFal == null && other.idTipRegFal != null) || (this.idTipRegFal != null && !this.idTipRegFal.equals(other.idTipRegFal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombreTipRegFal;
    }
    
}
