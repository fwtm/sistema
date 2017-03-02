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
@Table(name = "TIPO_INSCRIPCION_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoInscripcionRenavi.findAll", query = "SELECT t FROM TipoInscripcionRenavi t"),
    @NamedQuery(name = "TipoInscripcionRenavi.findByIdTipIns", query = "SELECT t FROM TipoInscripcionRenavi t WHERE t.idTipIns = :idTipIns"),
    @NamedQuery(name = "TipoInscripcionRenavi.findByDescrTipIns", query = "SELECT t FROM TipoInscripcionRenavi t WHERE t.descrTipIns = :descrTipIns")})
public class TipoInscripcionRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIP_INS")
    private Integer idTipIns;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "DESCR_TIP_INS")
    private String descrTipIns;
    @OneToMany(mappedBy = "fkIdTipIns", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public TipoInscripcionRenavi() {
    }

    public TipoInscripcionRenavi(Integer idTipIns) {
        this.idTipIns = idTipIns;
    }

    public TipoInscripcionRenavi(Integer idTipIns, String descrTipIns) {
        this.idTipIns = idTipIns;
        this.descrTipIns = descrTipIns;
    }

    public Integer getIdTipIns() {
        return idTipIns;
    }

    public void setIdTipIns(Integer idTipIns) {
        this.idTipIns = idTipIns;
    }

    public String getDescrTipIns() {
        return descrTipIns;
    }

    public void setDescrTipIns(String descrTipIns) {
        this.descrTipIns = descrTipIns;
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
        hash += (idTipIns != null ? idTipIns.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoInscripcionRenavi)) {
            return false;
        }
        TipoInscripcionRenavi other = (TipoInscripcionRenavi) object;
        if ((this.idTipIns == null && other.idTipIns != null) || (this.idTipIns != null && !this.idTipIns.equals(other.idTipIns))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descrTipIns;
    }
    
}
