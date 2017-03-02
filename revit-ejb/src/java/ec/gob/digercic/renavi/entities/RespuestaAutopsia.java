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
@Table(name = "RESPUESTA_AUTOPSIA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RespuestaAutopsia.findAll", query = "SELECT r FROM RespuestaAutopsia r"),
    @NamedQuery(name = "RespuestaAutopsia.findByIdResAut", query = "SELECT r FROM RespuestaAutopsia r WHERE r.idResAut = :idResAut"),
    @NamedQuery(name = "RespuestaAutopsia.findByValorResAut", query = "SELECT r FROM RespuestaAutopsia r WHERE r.valorResAut = :valorResAut")})
public class RespuestaAutopsia implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_RES_AUT")
    private Integer idResAut;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "VALOR_RES_AUT")
    private String valorResAut;
    @OneToMany(mappedBy = "fkIdResAut", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;

    public RespuestaAutopsia() {
    }

    public RespuestaAutopsia(Integer idResAut) {
        this.idResAut = idResAut;
    }

    public RespuestaAutopsia(Integer idResAut, String valorResAut) {
        this.idResAut = idResAut;
        this.valorResAut = valorResAut;
    }

    public Integer getIdResAut() {
        return idResAut;
    }

    public void setIdResAut(Integer idResAut) {
        this.idResAut = idResAut;
    }

    public String getValorResAut() {
        return valorResAut;
    }

    public void setValorResAut(String valorResAut) {
        this.valorResAut = valorResAut;
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
        hash += (idResAut != null ? idResAut.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RespuestaAutopsia)) {
            return false;
        }
        RespuestaAutopsia other = (RespuestaAutopsia) object;
        if ((this.idResAut == null && other.idResAut != null) || (this.idResAut != null && !this.idResAut.equals(other.idResAut))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return valorResAut;
    }
    
}
