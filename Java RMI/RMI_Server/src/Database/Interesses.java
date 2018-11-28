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
//@Table(name = "interesses", catalog = "DISTRIBUIDOSRMI", schema = "public")
@Table(name = "", catalog = "", schema = "")
@NamedQueries({
    @NamedQuery(name = "Interesses.findAll", query = "SELECT i FROM Interesses i"),
    @NamedQuery(name = "Interesses.findById", query = "SELECT i FROM Interesses i WHERE i.id = :id"),
    @NamedQuery(name = "Interesses.findByInteressado", query = "SELECT i FROM Interesses i WHERE i.interessado = :interessado"),
    @NamedQuery(name = "Interesses.findByIdLocal", query = "SELECT i FROM Interesses i WHERE i.idLocal = :idLocal"),
    @NamedQuery(name = "Interesses.findByDatalimite", query = "SELECT i FROM Interesses i WHERE i.datalimite = :datalimite"),
    @NamedQuery(name = "Interesses.findByPreco", query = "SELECT i FROM Interesses i WHERE i.preco = :preco"),
    @NamedQuery(name = "Interesses.findByTipo", query = "SELECT i FROM Interesses i WHERE i.tipo = :tipo")})
public class Interesses implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "interessado")
    private String interessado;
    @Column(name = "id_local")
    private Integer idLocal;
    @Column(name = "datalimite")
    private String datalimite;
    @Column(name = "preco")
    private Integer preco;
    @Column(name = "tipo")
    private Integer tipo;

    public Interesses() {
    }

    public Interesses(Integer id) {
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

    public String getInteressado() {
        return interessado;
    }

    public void setInteressado(String interessado) {
        String oldInteressado = this.interessado;
        this.interessado = interessado;
        changeSupport.firePropertyChange("interessado", oldInteressado, interessado);
    }

    public Integer getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Integer idLocal) {
        Integer oldIdLocal = this.idLocal;
        this.idLocal = idLocal;
        changeSupport.firePropertyChange("idLocal", oldIdLocal, idLocal);
    }

    public String getDatalimite() {
        return datalimite;
    }

    public void setDatalimite(String datalimite) {
        String oldDatalimite = this.datalimite;
        this.datalimite = datalimite;
        changeSupport.firePropertyChange("datalimite", oldDatalimite, datalimite);
    }

    public Integer getPreco() {
        return preco;
    }

    public void setPreco(Integer preco) {
        Integer oldPreco = this.preco;
        this.preco = preco;
        changeSupport.firePropertyChange("preco", oldPreco, preco);
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        Integer oldTipo = this.tipo;
        this.tipo = tipo;
        changeSupport.firePropertyChange("tipo", oldTipo, tipo);
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
        if (!(object instanceof Interesses)) {
            return false;
        }
        Interesses other = (Interesses) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InterfaceGrafica.Operacoes.Interesses[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
