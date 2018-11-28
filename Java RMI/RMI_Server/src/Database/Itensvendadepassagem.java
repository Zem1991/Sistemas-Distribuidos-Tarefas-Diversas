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
//@Table(name = "itensvendadepassagem", catalog = "DISTRIBUIDOSRMI", schema = "public")
@Table(name = "", catalog = "", schema = "")
@NamedQueries({
    @NamedQuery(name = "Itensvendadepassagem.findAll", query = "SELECT i FROM Itensvendadepassagem i"),
    @NamedQuery(name = "Itensvendadepassagem.findById", query = "SELECT i FROM Itensvendadepassagem i WHERE i.id = :id"),
    @NamedQuery(name = "Itensvendadepassagem.findByIdVenda", query = "SELECT i FROM Itensvendadepassagem i WHERE i.idVenda = :idVenda"),
    @NamedQuery(name = "Itensvendadepassagem.findByIdPassagem", query = "SELECT i FROM Itensvendadepassagem i WHERE i.idPassagem = :idPassagem"),
    @NamedQuery(name = "Itensvendadepassagem.findByUsuario", query = "SELECT i FROM Itensvendadepassagem i WHERE i.usuario = :usuario")})
public class Itensvendadepassagem implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_venda")
    private Integer idVenda;
    @Column(name = "id_passagem")
    private Integer idPassagem;
    @Column(name = "usuario")
    private String usuario;

    public Itensvendadepassagem() {
    }

    public Itensvendadepassagem(Integer id) {
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

    public Integer getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Integer idVenda) {
        Integer oldIdVenda = this.idVenda;
        this.idVenda = idVenda;
        changeSupport.firePropertyChange("idVenda", oldIdVenda, idVenda);
    }

    public Integer getIdPassagem() {
        return idPassagem;
    }

    public void setIdPassagem(Integer idPassagem) {
        Integer oldIdPassagem = this.idPassagem;
        this.idPassagem = idPassagem;
        changeSupport.firePropertyChange("idPassagem", oldIdPassagem, idPassagem);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        String oldUsuario = this.usuario;
        this.usuario = usuario;
        changeSupport.firePropertyChange("usuario", oldUsuario, usuario);
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
        if (!(object instanceof Itensvendadepassagem)) {
            return false;
        }
        Itensvendadepassagem other = (Itensvendadepassagem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InterfaceGrafica.Operacoes.Itensvendadepassagem[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
