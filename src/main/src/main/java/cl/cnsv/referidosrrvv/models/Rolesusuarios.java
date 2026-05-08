/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author cow
 */
@Entity
@Table(name = "RFD_ROLESUSUARIOS")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Rolesusuarios.findAll", query = "SELECT r FROM Rolesusuarios r"),
        @NamedQuery(name = "Rolesusuarios.findById", query = "SELECT r FROM Rolesusuarios r WHERE r.id = :id"),
        @NamedQuery(name = "Rolesusuarios.findByVersion", query = "SELECT r FROM Rolesusuarios r WHERE r.version = :version"),
        @NamedQuery(name = "Rolesusuarios.findByNombre", query = "SELECT r FROM Rolesusuarios r WHERE r.nombre = :nombre"),
        @NamedQuery(name = "Rolesusuarios.findByRol", query = "SELECT r FROM Rolesusuarios r WHERE r.rol = :rol"),
        @NamedQuery(name = "Rolesusuarios.findByAst", query = "SELECT r FROM Rolesusuarios r WHERE r.ast = :ast"),
        @NamedQuery(name = "Rolesusuarios.findBySup", query = "SELECT r FROM Rolesusuarios r WHERE r.sup = :sup"), })
public class Rolesusuarios implements Serializable {

    private static final long serialVersionUID = 1L;

    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator7")
    @SequenceGenerator(name = "book_generator7", sequenceName = "RFD_ROLESUSUARIOS_SEQ", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private BigDecimal id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "VERSION")
    private BigInteger version;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NOMBRE")
    private String nombre;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ROL")
    private String rol;

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "AST")
    private String ast;

    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "SUP")
    private String sup;

    public Rolesusuarios() {
        // constructor vacio
    }

    public Rolesusuarios(BigDecimal id) {
        this.id = id;
    }

    public Rolesusuarios(
            BigDecimal id,
            BigInteger version,
            String nombre,
            String rol) {
        this.id = id;
        this.version = version;
        this.nombre = nombre;
        this.rol = rol;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getVersion() {
        return version;
    }

    public void setVersion(BigInteger version) {
        this.version = version;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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
        if (!(object instanceof Rolesusuarios)) {
            return false;
        }
        Rolesusuarios other = (Rolesusuarios) object;
        if ((this.id == null && other.id != null)
                || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.cnsv.referidosrrvv.models.Rolesusuarios[ id=" + id + " ]";
    }

    /**
     * @return String return the ast
     */
    public String getAst() {
        return ast;
    }

    /**
     * @param ast the ast to set
     */
    public void setAst(String ast) {
        this.ast = ast;
    }

    /**
     * @return String return the sup
     */
    public String getSup() {
        return sup;
    }

    /**
     * @param sup the sup to set
     */
    public void setSup(String sup) {
        this.sup = sup;
    }
}
