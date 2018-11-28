/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Felipe
 */
@Entity
//@Table(name = "pacotesturisticos", catalog = "DISTRIBUIDOSRMI", schema = "public")
@Table(name = "", catalog = "", schema = "")
@NamedQueries({
    @NamedQuery(name = "Pacotesturisticos.findAll", query = "SELECT p FROM Pacotesturisticos p"),
    @NamedQuery(name = "Pacotesturisticos.findById", query = "SELECT p FROM Pacotesturisticos p WHERE p.id = :id"),
    @NamedQuery(name = "Pacotesturisticos.findByCliente", query = "SELECT p FROM Pacotesturisticos p WHERE p.cliente = :cliente"),
    @NamedQuery(name = "Pacotesturisticos.findByNumeropessoas", query = "SELECT p FROM Pacotesturisticos p WHERE p.numeropessoas = :numeropessoas"),
    @NamedQuery(name = "Pacotesturisticos.findByValortotal", query = "SELECT p FROM Pacotesturisticos p WHERE p.valortotal = :valortotal"),
    @NamedQuery(name = "Pacotesturisticos.findByStatus", query = "SELECT p FROM Pacotesturisticos p WHERE p.status = :status")})
public class Pacotesturisticos implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "cliente")
    private String cliente;
    @Column(name = "numeropessoas")
    private Integer numeropessoas;
    @Column(name = "valortotal")
    private Integer valortotal;
    @Column(name = "status")
    private Integer status;

    public Pacotesturisticos() {
    }

    public Pacotesturisticos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Integer oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        String oldCliente = this.cliente;
        this.cliente = cliente;
        changeSupport.firePropertyChange("cliente", oldCliente, cliente);
    }

    public Integer getNumeropessoas() {
        return numeropessoas;
    }

    public void setNumeropessoas(Integer numeropessoas) {
        Integer oldNumeropessoas = this.numeropessoas;
        this.numeropessoas = numeropessoas;
        changeSupport.firePropertyChange("numeropessoas", oldNumeropessoas, numeropessoas);
    }

    public Integer getValortotal() {
        return valortotal;
    }

    public void setValortotal(Integer valortotal) {
        Integer oldValortotal = this.valortotal;
        this.valortotal = valortotal;
        changeSupport.firePropertyChange("valortotal", oldValortotal, valortotal);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        Integer oldStatus = this.status;
        this.status = status;
        changeSupport.firePropertyChange("status", oldStatus, status);
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
        if (!(object instanceof Pacotesturisticos)) {
            return false;
        }
        Pacotesturisticos other = (Pacotesturisticos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InterfaceGrafica.Operacoes.Pacotesturisticos[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
