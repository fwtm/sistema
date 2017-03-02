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
@Table(name = "TIPO_AREA_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoAreaRenavi.findAll", query = "SELECT t FROM TipoAreaRenavi t"),
    @NamedQuery(name = "TipoAreaRenavi.findByIdTipArea", query = "SELECT t FROM TipoAreaRenavi t WHERE t.idTipArea = :idTipArea"),
    @NamedQuery(name = "TipoAreaRenavi.findByDescTipArea", query = "SELECT t FROM TipoAreaRenavi t WHERE t.descTipArea = :descTipArea")})
public class TipoAreaRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIP_AREA")
    private Integer idTipArea;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "DESC_TIP_AREA")
    private String descTipArea;
    @OneToMany(mappedBy = "fkIdTipoAreaMad", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public TipoAreaRenavi() {
    }

    public TipoAreaRenavi(Integer idTipArea) {
        this.idTipArea = idTipArea;
    }

    public TipoAreaRenavi(Integer idTipArea, String descTipArea) {
        this.idTipArea = idTipArea;
        this.descTipArea = descTipArea;
    }

    public Integer getIdTipArea() {
        return idTipArea;
    }

    public void setIdTipArea(Integer idTipArea) {
        this.idTipArea = idTipArea;
    }

    public String getDescTipArea() {
        return descTipArea;
    }

    public void setDescTipArea(String descTipArea) {
        this.descTipArea = descTipArea;
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
        hash += (idTipArea != null ? idTipArea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoAreaRenavi)) {
            return false;
        }
        TipoAreaRenavi other = (TipoAreaRenavi) object;
        if ((this.idTipArea == null && other.idTipArea != null) || (this.idTipArea != null && !this.idTipArea.equals(other.idTipArea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descTipArea;
    }
    
}
