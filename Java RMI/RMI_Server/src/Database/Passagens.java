/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Felipe Lopes Zem
 */
@Entity
//@Table(name = "passagens", catalog = "DISTRIBUIDOSRMI", schema = "public")
@Table(name = "", catalog = "", schema = "")
@NamedQueries({
    @NamedQuery(name = "Passagens.findAll", query = "SELECT p FROM Passagens p"),
    @NamedQuery(name = "Passagens.findById", query = "SELECT p FROM Passagens p WHERE p.id = :id"),
    @NamedQuery(name = "Passagens.findByTipo", query = "SELECT p FROM Passagens p WHERE p.tipo = :tipo"),
    @NamedQuery(name = "Passagens.findByIdLocalorigem", query = "SELECT p FROM Passagens p WHERE p.idLocalorigem = :idLocalorigem"),
    @NamedQuery(name = "Passagens.findByIdLocaldestino", query = "SELECT p FROM Passagens p WHERE p.idLocaldestino = :idLocaldestino"),
    @NamedQuery(name = "Passagens.findByDataida", query = "SELECT p FROM Passagens p WHERE p.dataida = :dataida"),
    @NamedQuery(name = "Passagens.findByDatavolta", query = "SELECT p FROM Passagens p WHERE p.datavolta = :datavolta"),
    @NamedQuery(name = "Passagens.findByPreco", query = "SELECT p FROM Passagens p WHERE p.preco = :preco")})
public class Passagens implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "tipo")
    private Integer tipo;
    @Column(name = "id_localorigem")
    private Integer idLocalorigem;
    @Column(name = "id_localdestino")
    private Integer idLocaldestino;
    @Column(name = "dataida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataida;
    @Column(name = "datavolta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datavolta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "preco")
    private Double preco;

    public Passagens() {
    }

    public Passagens(Integer id) {
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

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        Integer oldTipo = this.tipo;
        this.tipo = tipo;
        changeSupport.firePropertyChange("tipo", oldTipo, tipo);
    }

    public Integer getIdLocalorigem() {
        return idLocalorigem;
    }

    public void setIdLocalorigem(Integer idLocalorigem) {
        Integer oldIdLocalorigem = this.idLocalorigem;
        this.idLocalorigem = idLocalorigem;
        changeSupport.firePropertyChange("idLocalorigem", oldIdLocalorigem, idLocalorigem);
    }

    public Integer getIdLocaldestino() {
        return idLocaldestino;
    }

    public void setIdLocaldestino(Integer idLocaldestino) {
        Integer oldIdLocaldestino = this.idLocaldestino;
        this.idLocaldestino = idLocaldestino;
        changeSupport.firePropertyChange("idLocaldestino", oldIdLocaldestino, idLocaldestino);
    }

    public Date getDataida() {
        return dataida;
    }

    public void setDataida(Date dataida) {
        Date oldDataida = this.dataida;
        this.dataida = dataida;
        changeSupport.firePropertyChange("dataida", oldDataida, dataida);
    }

    public Date getDatavolta() {
        return datavolta;
    }

    public void setDatavolta(Date datavolta) {
        Date oldDatavolta = this.datavolta;
        this.datavolta = datavolta;
        changeSupport.firePropertyChange("datavolta", oldDatavolta, datavolta);
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        Double oldPreco = this.preco;
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
        if (!(object instanceof Passagens)) {
            return false;
        }
        Passagens other = (Passagens) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InterfaceGrafica.Cadastros.Passagens[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
