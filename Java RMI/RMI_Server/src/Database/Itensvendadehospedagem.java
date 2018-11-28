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
 * @author Felipe
 */
@Entity
//@Table(name = "itensvendadehospedagem", catalog = "DISTRIBUIDOSRMI", schema = "public")
@Table(name = "", catalog = "", schema = "")
@NamedQueries({
    @NamedQuery(name = "Itensvendadehospedagem.findAll", query = "SELECT i FROM Itensvendadehospedagem i"),
    @NamedQuery(name = "Itensvendadehospedagem.findById", query = "SELECT i FROM Itensvendadehospedagem i WHERE i.id = :id"),
    @NamedQuery(name = "Itensvendadehospedagem.findByIdVenda", query = "SELECT i FROM Itensvendadehospedagem i WHERE i.idVenda = :idVenda"),
    @NamedQuery(name = "Itensvendadehospedagem.findByIdHospedagem", query = "SELECT i FROM Itensvendadehospedagem i WHERE i.idHospedagem = :idHospedagem"),
    @NamedQuery(name = "Itensvendadehospedagem.findByHospedes", query = "SELECT i FROM Itensvendadehospedagem i WHERE i.hospedes = :hospedes"),
    @NamedQuery(name = "Itensvendadehospedagem.findByDataentrada", query = "SELECT i FROM Itensvendadehospedagem i WHERE i.dataentrada = :dataentrada"),
    @NamedQuery(name = "Itensvendadehospedagem.findByDatasaida", query = "SELECT i FROM Itensvendadehospedagem i WHERE i.datasaida = :datasaida")})
public class Itensvendadehospedagem implements Serializable {

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
    @Column(name = "id_hospedagem")
    private Integer idHospedagem;
    @Column(name = "hospedes")
    private String hospedes;
    @Column(name = "dataentrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataentrada;
    @Column(name = "datasaida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datasaida;

    public Itensvendadehospedagem() {
    }

    public Itensvendadehospedagem(Integer id) {
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

    public Integer getIdHospedagem() {
        return idHospedagem;
    }

    public void setIdHospedagem(Integer idHospedagem) {
        Integer oldIdHospedagem = this.idHospedagem;
        this.idHospedagem = idHospedagem;
        changeSupport.firePropertyChange("idHospedagem", oldIdHospedagem, idHospedagem);
    }

    public String getHospedes() {
        return hospedes;
    }

    public void setHospedes(String hospedes) {
        String oldHospedes = this.hospedes;
        this.hospedes = hospedes;
        changeSupport.firePropertyChange("hospedes", oldHospedes, hospedes);
    }

    public Date getDataentrada() {
        return dataentrada;
    }

    public void setDataentrada(Date dataentrada) {
        Date oldDataentrada = this.dataentrada;
        this.dataentrada = dataentrada;
        changeSupport.firePropertyChange("dataentrada", oldDataentrada, dataentrada);
    }

    public Date getDatasaida() {
        return datasaida;
    }

    public void setDatasaida(Date datasaida) {
        Date oldDatasaida = this.datasaida;
        this.datasaida = datasaida;
        changeSupport.firePropertyChange("datasaida", oldDatasaida, datasaida);
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
        if (!(object instanceof Itensvendadehospedagem)) {
            return false;
        }
        Itensvendadehospedagem other = (Itensvendadehospedagem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InterfaceGrafica.Operacoes.Itensvendadehospedagem[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
