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
@Table(name = "PARENTESCO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Parentesco.findAll", query = "SELECT p FROM Parentesco p"),
    @NamedQuery(name = "Parentesco.findByIdPar", query = "SELECT p FROM Parentesco p WHERE p.idPar = :idPar"),
    @NamedQuery(name = "Parentesco.findByNombrePar", query = "SELECT p FROM Parentesco p WHERE p.nombrePar = :nombrePar")})
public class Parentesco implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PAR")
    private Integer idPar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE_PAR")
    private String nombrePar;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentesco", fetch = FetchType.LAZY)
    private List<SolicitanteFallecimiento> solicitanteFallecimientoList;

    public Parentesco() {
    }

    public Parentesco(Integer idPar) {
        this.idPar = idPar;
    }

    public Parentesco(Integer idPar, String nombrePar) {
        this.idPar = idPar;
        this.nombrePar = nombrePar;
    }

    public Integer getIdPar() {
        return idPar;
    }

    public void setIdPar(Integer idPar) {
        this.idPar = idPar;
    }

    public String getNombrePar() {
        return nombrePar;
    }

    public void setNombrePar(String nombrePar) {
        this.nombrePar = nombrePar;
    }

    @XmlTransient
    public List<SolicitanteFallecimiento> getSolicitanteFallecimientoList() {
        return solicitanteFallecimientoList;
    }

    public void setSolicitanteFallecimientoList(List<SolicitanteFallecimiento> solicitanteFallecimientoList) {
        this.solicitanteFallecimientoList = solicitanteFallecimientoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPar != null ? idPar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Parentesco)) {
            return false;
        }
        Parentesco other = (Parentesco) object;
        if ((this.idPar == null && other.idPar != null) || (this.idPar != null && !this.idPar.equals(other.idPar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.renavi.entities.Parentesco[ idPar=" + idPar + " ]";
    }
    
}
