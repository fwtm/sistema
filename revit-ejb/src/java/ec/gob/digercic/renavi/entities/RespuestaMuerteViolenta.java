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
@Table(name = "RESPUESTA_MUERTE_VIOLENTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RespuestaMuerteViolenta.findAll", query = "SELECT r FROM RespuestaMuerteViolenta r"),
    @NamedQuery(name = "RespuestaMuerteViolenta.findByIdResMueVio", query = "SELECT r FROM RespuestaMuerteViolenta r WHERE r.idResMueVio = :idResMueVio"),
    @NamedQuery(name = "RespuestaMuerteViolenta.findByValorResMueVio", query = "SELECT r FROM RespuestaMuerteViolenta r WHERE r.valorResMueVio = :valorResMueVio")})
public class RespuestaMuerteViolenta implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_RES_MUE_VIO")
    private Integer idResMueVio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "VALOR_RES_MUE_VIO")
    private String valorResMueVio;
    @OneToMany(mappedBy = "fkIdResMueVio", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;

    public RespuestaMuerteViolenta() {
    }

    public RespuestaMuerteViolenta(Integer idResMueVio) {
        this.idResMueVio = idResMueVio;
    }

    public RespuestaMuerteViolenta(Integer idResMueVio, String valorResMueVio) {
        this.idResMueVio = idResMueVio;
        this.valorResMueVio = valorResMueVio;
    }

    public Integer getIdResMueVio() {
        return idResMueVio;
    }

    public void setIdResMueVio(Integer idResMueVio) {
        this.idResMueVio = idResMueVio;
    }

    public String getValorResMueVio() {
        return valorResMueVio;
    }

    public void setValorResMueVio(String valorResMueVio) {
        this.valorResMueVio = valorResMueVio;
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
        hash += (idResMueVio != null ? idResMueVio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RespuestaMuerteViolenta)) {
            return false;
        }
        RespuestaMuerteViolenta other = (RespuestaMuerteViolenta) object;
        if ((this.idResMueVio == null && other.idResMueVio != null) || (this.idResMueVio != null && !this.idResMueVio.equals(other.idResMueVio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return valorResMueVio;
    }
    
}
