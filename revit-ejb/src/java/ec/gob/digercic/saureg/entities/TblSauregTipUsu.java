/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "TBL_SAUREG_TIP_USU")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregTipUsu.findAll", query = "SELECT t FROM TblSauregTipUsu t"),
    @NamedQuery(name = "TblSauregTipUsu.findByIdTipUsu", query = "SELECT t FROM TblSauregTipUsu t WHERE t.idTipUsu = :idTipUsu"),
    @NamedQuery(name = "TblSauregTipUsu.findByDescripcion", query = "SELECT t FROM TblSauregTipUsu t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TblSauregTipUsu.findByAcronimo", query = "SELECT t FROM TblSauregTipUsu t WHERE t.acronimo = :acronimo")})
public class TblSauregTipUsu implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIP_USU")
    private Integer idTipUsu;
    @Size(max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Size(max = 10)
    @Column(name = "ACRONIMO")
    private String acronimo;
    @OneToMany(mappedBy = "idTipUsu", fetch = FetchType.LAZY)
    private List<TblSauregUsuario> tblSauregUsuarioList;
    @JoinColumn(name = "ID_SISTEMA", referencedColumnName = "ID_SISTEMA")
    @ManyToOne(fetch = FetchType.LAZY)
    private TblSauregSistema idSistema;

    public TblSauregTipUsu() {
    }

    public TblSauregTipUsu(Integer idTipUsu) {
        this.idTipUsu = idTipUsu;
    }

    public Integer getIdTipUsu() {
        return idTipUsu;
    }

    public void setIdTipUsu(Integer idTipUsu) {
        this.idTipUsu = idTipUsu;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAcronimo() {
        return acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
    }

    @XmlTransient
    public List<TblSauregUsuario> getTblSauregUsuarioList() {
        return tblSauregUsuarioList;
    }

    public void setTblSauregUsuarioList(List<TblSauregUsuario> tblSauregUsuarioList) {
        this.tblSauregUsuarioList = tblSauregUsuarioList;
    }

    public TblSauregSistema getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(TblSauregSistema idSistema) {
        this.idSistema = idSistema;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipUsu != null ? idTipUsu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregTipUsu)) {
            return false;
        }
        TblSauregTipUsu other = (TblSauregTipUsu) object;
        if ((this.idTipUsu == null && other.idTipUsu != null) || (this.idTipUsu != null && !this.idTipUsu.equals(other.idTipUsu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsrevit.entities.saureg.TblSauregTipUsu[ idTipUsu=" + idTipUsu + " ]";
    }
    
}
