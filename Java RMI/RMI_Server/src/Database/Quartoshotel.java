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
 * @author Felipe Lopes Zem
 */
@Entity
//@Table(name = "quartoshotel", catalog = "DISTRIBUIDOSRMI", schema = "public")
@Table(name = "", catalog = "", schema = "")
@NamedQueries({
    @NamedQuery(name = "Quartoshotel.findAll", query = "SELECT q FROM Quartoshotel q"),
    @NamedQuery(name = "Quartoshotel.findById", query = "SELECT q FROM Quartoshotel q WHERE q.id = :id"),
    @NamedQuery(name = "Quartoshotel.findByIdHotel", query = "SELECT q FROM Quartoshotel q WHERE q.idHotel = :idHotel"),
    @NamedQuery(name = "Quartoshotel.findByNome", query = "SELECT q FROM Quartoshotel q WHERE q.nome = :nome"),
    @NamedQuery(name = "Quartoshotel.findByPreco", query = "SELECT q FROM Quartoshotel q WHERE q.preco = :preco")})
public class Quartoshotel implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_hotel")
    private Integer idHotel;
    @Column(name = "nome")
    private String nome;
    @Column(name = "preco")
    private Integer preco;

    public Quartoshotel() {
    }

    public Quartoshotel(Integer id) {
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

    public Integer getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(Integer idHotel) {
        Integer oldIdHotel = this.idHotel;
        this.idHotel = idHotel;
        changeSupport.firePropertyChange("idHotel", oldIdHotel, idHotel);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        String oldNome = this.nome;
        this.nome = nome;
        changeSupport.firePropertyChange("nome", oldNome, nome);
    }

    public Integer getPreco() {
        return preco;
    }

    public void setPreco(Integer preco) {
        Integer oldPreco = this.preco;
        this.preco = preco;
        changeSupport.firePropertyChange("preco", oldPreco, preco);
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
        if (!(object instanceof Quartoshotel)) {
            return false;
        }
        Quartoshotel other = (Quartoshotel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InterfaceGrafica.Cadastros.Quartoshotel[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
