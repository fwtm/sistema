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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
@Table(name = "PRODUCTO_EMBARAZO_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProductoEmbarazoRenavi.findAll", query = "SELECT p FROM ProductoEmbarazoRenavi p ORDER BY p.idProEmb"),
    @NamedQuery(name = "ProductoEmbarazoRenavi.findByIdProEmb", query = "SELECT p FROM ProductoEmbarazoRenavi p WHERE p.idProEmb = :idProEmb"),
    @NamedQuery(name = "ProductoEmbarazoRenavi.findByDescrProEmb", query = "SELECT p FROM ProductoEmbarazoRenavi p WHERE p.descrProEmb = :descrProEmb")})
public class ProductoEmbarazoRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "SEQ_RENAVI_PRODUCTO_EMBARAZO", sequenceName = "SEQ_RENAVI_PRODUCTO_EMBARAZO", allocationSize=1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RENAVI_PRODUCTO_EMBARAZO")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PRO_EMB")
    private Integer idProEmb;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "DESCR_PRO_EMB")
    private String descrProEmb;
    @OneToMany(mappedBy = "fkIdProEmb", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public ProductoEmbarazoRenavi() {
    }

    public ProductoEmbarazoRenavi(Integer idProEmb) {
        this.idProEmb = idProEmb;
    }

    public ProductoEmbarazoRenavi(Integer idProEmb, String descrProEmb) {
        this.idProEmb = idProEmb;
        this.descrProEmb = descrProEmb;
    }

    public Integer getIdProEmb() {
        return idProEmb;
    }

    public void setIdProEmb(Integer idProEmb) {
        this.idProEmb = idProEmb;
    }

    public String getDescrProEmb() {
        return descrProEmb;
    }

    public void setDescrProEmb(String descrProEmb) {
        this.descrProEmb = descrProEmb;
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
        hash += (idProEmb != null ? idProEmb.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoEmbarazoRenavi)) {
            return false;
        }
        ProductoEmbarazoRenavi other = (ProductoEmbarazoRenavi) object;
        if ((this.idProEmb == null && other.idProEmb != null) || (this.idProEmb != null && !this.idProEmb.equals(other.idProEmb))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descrProEmb;
    }

    
}
