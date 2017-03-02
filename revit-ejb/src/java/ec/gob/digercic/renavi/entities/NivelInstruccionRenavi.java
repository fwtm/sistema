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
@Table(name = "NIVEL_INSTRUCCION_RENAVI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelInstruccionRenavi.findAll", query = "SELECT n FROM NivelInstruccionRenavi n ORDER BY n.idNivIns asc"),
    @NamedQuery(name = "NivelInstruccionRenavi.findByIdNivIns", query = "SELECT n FROM NivelInstruccionRenavi n WHERE n.idNivIns = :idNivIns"),
    //Por Daniel
    @NamedQuery(name = "NivelInstruccionRenavi.findByEdad10And11", query = "SELECT n FROM NivelInstruccionRenavi n WHERE n.idNivIns IN (0, 2, 4) ORDER BY n.idNivIns asc"),
    @NamedQuery(name = "NivelInstruccionRenavi.findByEdad12And14", query = "SELECT n FROM NivelInstruccionRenavi n WHERE n.idNivIns IN (0,2,3,4) ORDER BY n.idNivIns asc"),
    @NamedQuery(name = "NivelInstruccionRenavi.findByEdad15And16", query = "SELECT n FROM NivelInstruccionRenavi n WHERE n.idNivIns IN (0,1,2,3,4,5) ORDER BY n.idNivIns asc"),
    @NamedQuery(name = "NivelInstruccionRenavi.findByEdad17And22", query = "SELECT n FROM NivelInstruccionRenavi n WHERE n.idNivIns IN (0,1,2,3,4,5,6,7) ORDER BY n.idNivIns asc"),
    @NamedQuery(name = "NivelInstruccionRenavi.findByEdad23And49", query = "SELECT n FROM NivelInstruccionRenavi n WHERE n.idNivIns IN (0,1,2,3,4,5,6,7,8) ORDER BY n.idNivIns asc")
})
public class NivelInstruccionRenavi implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "SEQ_RENAVI_NIVEL_INSTRUCCION", sequenceName = "SEQ_RENAVI_NIVEL_INSTRUCCION", allocationSize=1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RENAVI_NIVEL_INSTRUCCION")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_NIV_INS")
    private Integer idNivIns;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "DESCR_NIV_INST")
    private String descrNivInst;
    @OneToMany(mappedBy = "fkIdNivelInstruccion", fetch = FetchType.LAZY)
    private List<Fallecido> fallecidoList;
    @OneToMany(mappedBy = "fkIdNivelInstr", fetch = FetchType.LAZY)
    private List<NacidoVivoRenavi> nacidoVivoRenaviList;

    public NivelInstruccionRenavi() {
    }

    public NivelInstruccionRenavi(Integer idNivIns) {
        this.idNivIns = idNivIns;
    }

    public NivelInstruccionRenavi(Integer idNivIns, String descrNivInst) {
        this.idNivIns = idNivIns;
        this.descrNivInst = descrNivInst;
    }

    public int getIdNivIns() {
        return idNivIns;
    }

    public void setIdNivIns(Integer idNivIns) {
        this.idNivIns = idNivIns;
    }

    public String getDescrNivInst() {
        return descrNivInst;
    }

    public void setDescrNivInst(String descrNivInst) {
        this.descrNivInst = descrNivInst;
    }

    @XmlTransient
    public List<Fallecido> getFallecidoList() {
        return fallecidoList;
    }

    public void setFallecidoList(List<Fallecido> fallecidoList) {
        this.fallecidoList = fallecidoList;
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
        hash += (idNivIns != null ? idNivIns.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NivelInstruccionRenavi)) {
            return false;
        }
        NivelInstruccionRenavi other = (NivelInstruccionRenavi) object;
        if ((this.idNivIns == null && other.idNivIns != null) || (this.idNivIns != null && !this.idNivIns.equals(other.idNivIns))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descrNivInst;
    }
    
}
