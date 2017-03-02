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
@Table(name = "TIPO_PARTO_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPartoRenavi.findAll", query = "SELECT t FROM TipoPartoRenavi t"),
    @NamedQuery(name = "TipoPartoRenavi.findByIdTipPar", query = "SELECT t FROM TipoPartoRenavi t WHERE t.idTipPar = :idTipPar"),
    @NamedQuery(name = "TipoPartoRenavi.findByDescrTipPar", query = "SELECT t FROM TipoPartoRenavi t WHERE t.descrTipPar = :descrTipPar")})
public class TipoPartoRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIP_PAR")
    private Integer idTipPar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "DESCR_TIP_PAR")
    private String descrTipPar;
    @OneToMany(mappedBy = "fkIdTipPar", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public TipoPartoRenavi() {
    }

    public TipoPartoRenavi(Integer idTipPar) {
        this.idTipPar = idTipPar;
    }

    public TipoPartoRenavi(Integer idTipPar, String descrTipPar) {
        this.idTipPar = idTipPar;
        this.descrTipPar = descrTipPar;
    }

    public Integer getIdTipPar() {
        return idTipPar;
    }

    public void setIdTipPar(Integer idTipPar) {
        this.idTipPar = idTipPar;
    }

    public String getDescrTipPar() {
        return descrTipPar;
    }

    public void setDescrTipPar(String descrTipPar) {
        this.descrTipPar = descrTipPar;
    }

    @XmlTransient
    public List<NacidoVivoRenavi> getNacidoVivoRenaviList() {
        return nacidoVivoRenaviList;
    }

    public void setNacidoVivoRenaviList(List<NacidoVivoRenavi> nacidoVivoRenaviList) {
        this.nacidoVivoRenaviList = nacidoVivoRenaviList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipPar != null ? idTipPar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPartoRenavi)) {
            return false;
        }
        TipoPartoRenavi other = (TipoPartoRenavi) object;
        if ((this.idTipPar == null && other.idTipPar != null) || (this.idTipPar != null && !this.idTipPar.equals(other.idTipPar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descrTipPar;
    }
    
}
