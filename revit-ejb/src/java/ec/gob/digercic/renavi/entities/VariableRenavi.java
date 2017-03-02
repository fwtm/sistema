/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author daniel.porras
 */
@Entity
@Table(name = "VARIABLE_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VariableRenavi.findAll", query = "SELECT v FROM VariableRenavi v"),
    @NamedQuery(name = "VariableRenavi.findByVarId", query = "SELECT v FROM VariableRenavi v WHERE v.varId = :varId"),
    @NamedQuery(name = "VariableRenavi.findByVarNombre", query = "SELECT v FROM VariableRenavi v WHERE v.varNombre = :varNombre"),
    @NamedQuery(name = "VariableRenavi.findByVarValor", query = "SELECT v FROM VariableRenavi v WHERE v.varValor = :varValor")})
public class VariableRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "VAR_ID")
    private Integer varId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "VAR_NOMBRE")
    private String varNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "VAR_VALOR")
    private String varValor;
    @Size(max = 100)
    @Column(name = "VAR_MARGEN_ERROR")
    private String varMargenError;
    @Size(max = 250)
    @Column(name = "VAR_SIS_DESCRIPCION")
    private String varSisDescripcion;

    public VariableRenavi() {
    }

    public VariableRenavi(Integer varId) {
        this.varId = varId;
    }

    public VariableRenavi(Integer varId, String varNombre, String varValor) {
        this.varId = varId;
        this.varNombre = varNombre;
        this.varValor = varValor;
    }

    public Integer getVarId() {
        return varId;
    }

    public void setVarId(Integer varId) {
        this.varId = varId;
    }

    public String getVarNombre() {
        return varNombre;
    }

    public void setVarNombre(String varNombre) {
        this.varNombre = varNombre;
    }

    public String getVarValor() {
        return varValor;
    }

    public void setVarValor(String varValor) {
        this.varValor = varValor;
    }

    public String getVarMargenError() {
        return varMargenError;
    }

    public void setVarMargenError(String varMargenError) {
        this.varMargenError = varMargenError;
    }

    public String getVarSisDescripcion() {
        return varSisDescripcion;
    }

    public void setVarSisDescripcion(String varSisDescripcion) {
        this.varSisDescripcion = varSisDescripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (varId != null ? varId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VariableRenavi)) {
            return false;
        }
        VariableRenavi other = (VariableRenavi) object;
        if ((this.varId == null && other.varId != null) || (this.varId != null && !this.varId.equals(other.varId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return varNombre;
    }
    
}
