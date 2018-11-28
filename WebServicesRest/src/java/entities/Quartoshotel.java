/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe Lopes Zem
 */
@Entity
@Table(name = "quartoshotel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Quartoshotel.findAll", query = "SELECT q FROM Quartoshotel q"),
    @NamedQuery(name = "Quartoshotel.findById", query = "SELECT q FROM Quartoshotel q WHERE q.id = :id"),
    @NamedQuery(name = "Quartoshotel.findByNome", query = "SELECT q FROM Quartoshotel q WHERE q.nome = :nome"),
    @NamedQuery(name = "Quartoshotel.findByPreco", query = "SELECT q FROM Quartoshotel q WHERE q.preco = :preco")})
public class Quartoshotel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "nome")
    private String nome;
    @Column(name = "preco")
    private Integer preco;
    @JoinColumn(name = "id_hotel", referencedColumnName = "id")
    @ManyToOne
    private Hoteis idHotel;

    public Quartoshotel() {
    }

    public Quartoshotel(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getPreco() {
        return preco;
    }

    public void setPreco(Integer preco) {
        this.preco = preco;
    }

    public Hoteis getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(Hoteis idHotel) {
        this.idHotel = idHotel;
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
        return "entities.Quartoshotel[ id=" + id + " ]";
    }
    
}
