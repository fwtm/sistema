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
@Table(name = "TIPO_EDAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoEdad.findAll", query = "SELECT t FROM TipoEdad t"),
    @NamedQuery(name = "TipoEdad.findByIdTipEda", query = "SELECT t FROM TipoEdad t WHERE t.idTipEda = :idTipEda"),
    @NamedQuery(name = "TipoEdad.findByNombreTipEda", query = "SELECT t FROM TipoEdad t WHERE t.nombreTipEda = :nombreTipEda")})
public class TipoEdad implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIP_EDA")
    private Integer idTipEda;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMBRE_TIP_EDA")
    private String nombreTipEda;
    @OneToMany(mappedBy = "fkIdTipEda", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;

    public TipoEdad() {
    }

    public TipoEdad(Integer idTipEda) {
        this.idTipEda = idTipEda;
    }

    public TipoEdad(Integer idTipEda, String nombreTipEda) {
        this.idTipEda = idTipEda;
        this.nombreTipEda = nombreTipEda;
    }

    public Integer getIdTipEda() {
        return idTipEda;
    }

    public void setIdTipEda(Integer idTipEda) {
        this.idTipEda = idTipEda;
    }

    public String getNombreTipEda() {
        return nombreTipEda;
    }

    public void setNombreTipEda(String nombreTipEda) {
        this.nombreTipEda = nombreTipEda;
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
        hash += (idTipEda != null ? idTipEda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoEdad)) {
            return false;
        }
        TipoEdad other = (TipoEdad) object;
        if ((this.idTipEda == null && other.idTipEda != null) || (this.idTipEda != null && !this.idTipEda.equals(other.idTipEda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombreTipEda;
    }
    
}
