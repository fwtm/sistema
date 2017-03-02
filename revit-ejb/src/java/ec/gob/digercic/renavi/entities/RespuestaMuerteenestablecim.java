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
@Table(name = "RESPUESTA_MUERTEENESTABLECIM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RespuestaMuerteenestablecim.findAll", query = "SELECT r FROM RespuestaMuerteenestablecim r"),
    @NamedQuery(name = "RespuestaMuerteenestablecim.findByIdResMueEnest", query = "SELECT r FROM RespuestaMuerteenestablecim r WHERE r.idResMueEnest = :idResMueEnest"),
    @NamedQuery(name = "RespuestaMuerteenestablecim.findByValorReMueEnest", query = "SELECT r FROM RespuestaMuerteenestablecim r WHERE r.valorReMueEnest = :valorReMueEnest")})
public class RespuestaMuerteenestablecim implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_RES_MUE_ENEST")
    private Integer idResMueEnest;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "VALOR_RE_MUE_ENEST")
    private String valorReMueEnest;
    @OneToMany(mappedBy = "fkIdResMueEnest", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;

    public RespuestaMuerteenestablecim() {
    }

    public RespuestaMuerteenestablecim(Integer idResMueEnest) {
        this.idResMueEnest = idResMueEnest;
    }

    public RespuestaMuerteenestablecim(Integer idResMueEnest, String valorReMueEnest) {
        this.idResMueEnest = idResMueEnest;
        this.valorReMueEnest = valorReMueEnest;
    }

    public Integer getIdResMueEnest() {
        return idResMueEnest;
    }

    public void setIdResMueEnest(Integer idResMueEnest) {
        this.idResMueEnest = idResMueEnest;
    }

    public String getValorReMueEnest() {
        return valorReMueEnest;
    }

    public void setValorReMueEnest(String valorReMueEnest) {
        this.valorReMueEnest = valorReMueEnest;
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
        hash += (idResMueEnest != null ? idResMueEnest.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RespuestaMuerteenestablecim)) {
            return false;
        }
        RespuestaMuerteenestablecim other = (RespuestaMuerteenestablecim) object;
        if ((this.idResMueEnest == null && other.idResMueEnest != null) || (this.idResMueEnest != null && !this.idResMueEnest.equals(other.idResMueEnest))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return valorReMueEnest;
    }
    
}
