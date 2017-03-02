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
@Table(name = "ESTADO_REGISTRO_FALLECIDO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoRegistroFallecido.findAll", query = "SELECT e FROM EstadoRegistroFallecido e"),
    @NamedQuery(name = "EstadoRegistroFallecido.findByIdEstRegFal", query = "SELECT e FROM EstadoRegistroFallecido e WHERE e.idEstRegFal = :idEstRegFal"),
    @NamedQuery(name = "EstadoRegistroFallecido.findByNombreEstRegFal", query = "SELECT e FROM EstadoRegistroFallecido e WHERE e.nombreEstRegFal = :nombreEstRegFal")})
public class EstadoRegistroFallecido implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_EST_REG_FAL")
    private Integer idEstRegFal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE_EST_REG_FAL")
    private String nombreEstRegFal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fkIdEstRegFal", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;

    public EstadoRegistroFallecido() {
    }

    public EstadoRegistroFallecido(Integer idEstRegFal) {
        this.idEstRegFal = idEstRegFal;
    }

    public EstadoRegistroFallecido(Integer idEstRegFal, String nombreEstRegFal) {
        this.idEstRegFal = idEstRegFal;
        this.nombreEstRegFal = nombreEstRegFal;
    }

    public Integer getIdEstRegFal() {
        return idEstRegFal;
    }

    public void setIdEstRegFal(Integer idEstRegFal) {
        this.idEstRegFal = idEstRegFal;
    }

    public String getNombreEstRegFal() {
        return nombreEstRegFal;
    }

    public void setNombreEstRegFal(String nombreEstRegFal) {
        this.nombreEstRegFal = nombreEstRegFal;
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
        hash += (idEstRegFal != null ? idEstRegFal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoRegistroFallecido)) {
            return false;
        }
        EstadoRegistroFallecido other = (EstadoRegistroFallecido) object;
        if ((this.idEstRegFal == null && other.idEstRegFal != null) || (this.idEstRegFal != null && !this.idEstRegFal.equals(other.idEstRegFal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombreEstRegFal;
    }
    
}
