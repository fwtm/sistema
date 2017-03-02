/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.gob.digercic.saureg.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hernan.inga
 */
@Entity
@Table(name = "TBL_SAUREG_USUARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TblSauregUsuario.findAll", query = "SELECT t FROM TblSauregUsuario t"),
    @NamedQuery(name = "TblSauregUsuario.findByNomUsuario", query = "SELECT t FROM TblSauregUsuario t WHERE t.nomUsuario = :nomUsuario AND t.idEstados.idEstados = :estado"),
    @NamedQuery(name = "TblSauregUsuario.findByIdUsuario", query = "SELECT t FROM TblSauregUsuario t WHERE t.idUsuario = :idUsuario"),
    @NamedQuery(name = "TblSauregUsuario.findByApellido", query = "SELECT t FROM TblSauregUsuario t WHERE t.apellido = :apellido"),
    @NamedQuery(name = "TblSauregUsuario.findByNombre", query = "SELECT t FROM TblSauregUsuario t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TblSauregUsuario.findByUsernameAndPasswordUsu", query = "SELECT u FROM TblSauregUsuario u WHERE u.contrasenia = :passwordUsu AND u.nomUsuario = :usernameUsu AND u.idEstados.idEstados = :estado"),
    @NamedQuery(name = "TblSauregUsuario.updatePasswordByUser", query = "UPDATE TblSauregUsuario set contrasenia = :contrasenia, status = :status WHERE nomUsuario = :nomUsuario")

})
public class TblSauregUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "NOM_USUARIO")
    private String nomUsuario;
    @SequenceGenerator(name = "SEQ_USUARIO", sequenceName = "SEQ_USUARIO", allocationSize=1/*, initialValue=1*/)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO")
    @Column(name = "ID_USUARIO")
    private BigInteger idUsuario;
    @Size(max = 64)
    @Column(name = "APELLIDO")
    private String apellido;
    @Size(max = 64)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 100)
    @Column(name = "CONTRASENIA")
    private String contrasenia;
    @Size(max = 25)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Column(name = "STATUS")
    private String status;
    @OneToMany(mappedBy = "nomUsuario")
    private List<TblSauregSegAcceso> tblSauregSegAccesoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblSauregUsuario")
    private List<TblSauregUsuAgencia> tblSauregUsuAgenciaList;
    @JoinColumn(name = "ID_TIP_USU", referencedColumnName = "ID_TIP_USU")
    @ManyToOne
    private TblSauregTipUsu idTipUsu;
    @JoinColumn(name = "ID_ESTADOS", referencedColumnName = "ID_ESTADOS")
    @ManyToOne
    private TblSauregEstados idEstados;
    
    
    //Campos que no estan en la base
    //Usuario agencia
    @Transient
    private List<TblSauregAgencia> listTblSauregAgencia;
    //Agencia en la que va a trabajar el usuario
    @Transient
    private TblSauregAgencia agenciaInUser;
    //Lista de paginas par el menu
    @Transient
    private List<TblSauregCompRol> tblSauregCompRolList;
    
    public void c (){
        TblSauregUsuario u = new TblSauregUsuario();
        u.tblSauregSegAccesoList.get(0).getIdRol().getTblSauregCompRolList().get(0).getTblSauregPermiso();
    }

    public TblSauregUsuario() {
    }

    public TblSauregUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public String getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public BigInteger getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TblSauregAgencia> getListTblSauregAgencia() {
        return listTblSauregAgencia;
    }

    public void setListTblSauregAgencia(List<TblSauregAgencia> listTblSauregAgencia) {
        
        this.listTblSauregAgencia = listTblSauregAgencia;
    }

    public TblSauregAgencia getAgenciaInUser() {
        return agenciaInUser;
    }

    public void setAgenciaInUser(TblSauregAgencia agenciaInUser) {
        this.agenciaInUser = agenciaInUser;
    }

    @XmlTransient
    public List<TblSauregSegAcceso> getTblSauregSegAccesoList() {
        return tblSauregSegAccesoList;
    }

    public void setTblSauregSegAccesoList(List<TblSauregSegAcceso> tblSauregSegAccesoList) {
        this.tblSauregSegAccesoList = tblSauregSegAccesoList;
    }

    @XmlTransient
    public List<TblSauregUsuAgencia> getTblSauregUsuAgenciaList() {
        return tblSauregUsuAgenciaList;
    }

    public void setTblSauregUsuAgenciaList(List<TblSauregUsuAgencia> tblSauregUsuAgenciaList) {
        this.tblSauregUsuAgenciaList = tblSauregUsuAgenciaList;
    }

    public TblSauregTipUsu getIdTipUsu() {
        return idTipUsu;
    }

    public void setIdTipUsu(TblSauregTipUsu idTipUsu) {
        this.idTipUsu = idTipUsu;
    }

    public TblSauregEstados getIdEstados() {
        return idEstados;
    }

    public void setIdEstados(TblSauregEstados idEstados) {
        this.idEstados = idEstados;
    }

    public List<TblSauregCompRol> getTblSauregCompRolList() {
        return tblSauregCompRolList;
    }

    public void setTblSauregCompRolList(List<TblSauregCompRol> tblSauregCompRolList) {
        this.tblSauregCompRolList = tblSauregCompRolList;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nomUsuario != null ? nomUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TblSauregUsuario)) {
            return false;
        }
        TblSauregUsuario other = (TblSauregUsuario) object;
        if ((this.nomUsuario == null && other.nomUsuario != null) || (this.nomUsuario != null && !this.nomUsuario.equals(other.nomUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.digercic.wsinformacion.modelo.entities.TblSauregUsuario[ nomUsuario=" + nomUsuario + " ]";
    }
    
}
