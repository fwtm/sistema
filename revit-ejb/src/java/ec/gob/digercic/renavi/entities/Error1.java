/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.digercic.renavi.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author santiago.tapia
 */
@javax.persistence.Entity
@Table(name = "ERROR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Error.findAll", query = "SELECT e FROM Error1 e"),
    @NamedQuery(name = "Error.findById", query = "SELECT e FROM Error1 e WHERE e.id = :id"),
    @NamedQuery(name = "Error.findByDescErrorAnul", query = "SELECT e FROM Error1 e WHERE e.descErrorAnul = :descErrorAnul")})
public class Error1 implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 50)
    @Column(name = "DESC_ERROR_ANUL")
    private String descErrorAnul;
    @ManyToMany(mappedBy = "err")
    private List<Anulacion> an;

    public Error1() {
    }

    public Error1(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescErrorAnul() {
        return descErrorAnul;
    }

    public void setDescErrorAnul(String descErrorAnul) {
        this.descErrorAnul = descErrorAnul;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Error1)) {
            return false;
        }
        Error1 other = (Error1) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descErrorAnul;
    }

    /**
     * @return the anulaciones
     */
    public List<Anulacion> getAn() {
        return an;
    }

    /**
     * @param anulaciones the anulaciones to set
     */
    public void setAn(List<Anulacion> an) {
        this.setAn(an);
    }

   
}
