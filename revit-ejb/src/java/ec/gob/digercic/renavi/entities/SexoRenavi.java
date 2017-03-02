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
@Table(name = "SEXO_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SexoRenavi.findAll", query = "SELECT s FROM SexoRenavi s"),
    @NamedQuery(name = "SexoRenavi.findByIdSexo", query = "SELECT s FROM SexoRenavi s WHERE s.idSexo = :idSexo"),
    @NamedQuery(name = "SexoRenavi.findByDescrSexo", query = "SELECT s FROM SexoRenavi s WHERE s.descrSexo = :descrSexo")})
public class SexoRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_SEXO")
    private Integer idSexo;
    @Size(max = 12)
    @Column(name = "DESCR_SEXO")
    private String descrSexo;
    @OneToMany(mappedBy = "fkIdSexoNv", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public SexoRenavi() {
    }

    public SexoRenavi(Integer idSexo) {
        this.idSexo = idSexo;
    }

    public Integer getIdSexo() {
        return idSexo;
    }

    public void setIdSexo(Integer idSexo) {
        this.idSexo = idSexo;
    }

    public String getDescrSexo() {
        return descrSexo;
    }

    public void setDescrSexo(String descrSexo) {
        this.descrSexo = descrSexo;
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
        hash += (idSexo != null ? idSexo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SexoRenavi)) {
            return false;
        }
        SexoRenavi other = (SexoRenavi) object;
        if ((this.idSexo == null && other.idSexo != null) || (this.idSexo != null && !this.idSexo.equals(other.idSexo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descrSexo;
    }
    
}
