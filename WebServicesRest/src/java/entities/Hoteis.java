/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Felipe Lopes Zem
 */
@Entity
@Table(name = "hoteis")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Hoteis.findAll", query = "SELECT h FROM Hoteis h"),
    @NamedQuery(name = "Hoteis.findById", query = "SELECT h FROM Hoteis h WHERE h.id = :id"),
    @NamedQuery(name = "Hoteis.findByNome", query = "SELECT h FROM Hoteis h WHERE h.nome = :nome"),
    @NamedQuery(name = "Hoteis.findByIdLocal", query = "SELECT h FROM Hoteis h WHERE h.idLocal = :idLocal")})
public class Hoteis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "nome")
    private String nome;
    @Column(name = "id_local")
    private Integer idLocal;
    @OneToMany(mappedBy = "idHotel")
    private Collection<Quartoshotel> quartoshotelCollection;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Locais locais;

    public Hoteis() {
    }

    public Hoteis(Integer id) {
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

    public Integer getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(Integer idLocal) {
        this.idLocal = idLocal;
    }

    @XmlTransient
    public Collection<Quartoshotel> getQuartoshotelCollection() {
        return quartoshotelCollection;
    }

    public void setQuartoshotelCollection(Collection<Quartoshotel> quartoshotelCollection) {
        this.quartoshotelCollection = quartoshotelCollection;
    }

    public Locais getLocais() {
        return locais;
    }

    public void setLocais(Locais locais) {
        this.locais = locais;
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
        if (!(object instanceof Hoteis)) {
            return false;
        }
        Hoteis other = (Hoteis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Hoteis[ id=" + id + " ]";
    }
    
}
