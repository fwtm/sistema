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
@Table(name = "MORTALIDAD_MATERNA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MortalidadMaterna.findAll", query = "SELECT m FROM MortalidadMaterna m"),
    @NamedQuery(name = "MortalidadMaterna.findByIdMorMat", query = "SELECT m FROM MortalidadMaterna m WHERE m.idMorMat = :idMorMat"),
    @NamedQuery(name = "MortalidadMaterna.findByNombreMorMat", query = "SELECT m FROM MortalidadMaterna m WHERE m.nombreMorMat = :nombreMorMat")})
public class MortalidadMaterna implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_MOR_MAT")
    private Integer idMorMat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE_MOR_MAT")
    private String nombreMorMat;
    @OneToMany(mappedBy = "fkIdMorMat", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;

    public MortalidadMaterna() {
    }

    public MortalidadMaterna(Integer idMorMat) {
        this.idMorMat = idMorMat;
    }

    public MortalidadMaterna(Integer idMorMat, String nombreMorMat) {
        this.idMorMat = idMorMat;
        this.nombreMorMat = nombreMorMat;
    }

    public Integer getIdMorMat() {
        return idMorMat;
    }

    public void setIdMorMat(Integer idMorMat) {
        this.idMorMat = idMorMat;
    }

    public String getNombreMorMat() {
        return nombreMorMat;
    }

    public void setNombreMorMat(String nombreMorMat) {
        this.nombreMorMat = nombreMorMat;
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
        hash += (idMorMat != null ? idMorMat.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MortalidadMaterna)) {
            return false;
        }
        MortalidadMaterna other = (MortalidadMaterna) object;
        if ((this.idMorMat == null && other.idMorMat != null) || (this.idMorMat != null && !this.idMorMat.equals(other.idMorMat))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombreMorMat;
    }
    
}
