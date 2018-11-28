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
//@Table(name = "vendasdepassagem", catalog = "DISTRIBUIDOSRMI", schema = "public")
@Table(name = "", catalog = "", schema = "")
@NamedQueries({
    @NamedQuery(name = "Vendasdepassagem.findAll", query = "SELECT v FROM Vendasdepassagem v"),
    @NamedQuery(name = "Vendasdepassagem.findById", query = "SELECT v FROM Vendasdepassagem v WHERE v.id = :id"),
    @NamedQuery(name = "Vendasdepassagem.findByCliente", query = "SELECT v FROM Vendasdepassagem v WHERE v.cliente = :cliente"),
    @NamedQuery(name = "Vendasdepassagem.findByCartao", query = "SELECT v FROM Vendasdepassagem v WHERE v.cartao = :cartao"),
    @NamedQuery(name = "Vendasdepassagem.findByParcelas", query = "SELECT v FROM Vendasdepassagem v WHERE v.parcelas = :parcelas"),
    @NamedQuery(name = "Vendasdepassagem.findByNumeropessoas", query = "SELECT v FROM Vendasdepassagem v WHERE v.numeropessoas = :numeropessoas"),
    @NamedQuery(name = "Vendasdepassagem.findByValortotal", query = "SELECT v FROM Vendasdepassagem v WHERE v.valortotal = :valortotal"),
    @NamedQuery(name = "Vendasdepassagem.findByIdPacoteturistico", query = "SELECT v FROM Vendasdepassagem v WHERE v.idPacoteturistico = :idPacoteturistico")})
public class Vendasdepassagem implements Serializable {

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
    @Column(name = "cartao")
    private String cartao;
    @Column(name = "parcelas")
    private Integer parcelas;
    @Column(name = "numeropessoas")
    private Integer numeropessoas;
    @Column(name = "valortotal")
    private Integer valortotal;
    @Column(name = "id_pacoteturistico")
    private Integer idPacoteturistico;

    public Vendasdepassagem() {
    }

    public Vendasdepassagem(Integer id) {
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

    public String getCartao() {
        return cartao;
    }

    public void setCartao(String cartao) {
        String oldCartao = this.cartao;
        this.cartao = cartao;
        changeSupport.firePropertyChange("cartao", oldCartao, cartao);
    }

    public Integer getParcelas() {
        return parcelas;
    }

    public void setParcelas(Integer parcelas) {
        Integer oldParcelas = this.parcelas;
        this.parcelas = parcelas;
        changeSupport.firePropertyChange("parcelas", oldParcelas, parcelas);
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

    public Integer getIdPacoteturistico() {
        return idPacoteturistico;
    }

    public void setIdPacoteturistico(Integer idPacoteturistico) {
        Integer oldIdPacoteturistico = this.idPacoteturistico;
        this.idPacoteturistico = idPacoteturistico;
        changeSupport.firePropertyChange("idPacoteturistico", oldIdPacoteturistico, idPacoteturistico);
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
        if (!(object instanceof Vendasdepassagem)) {
            return false;
        }
        Vendasdepassagem other = (Vendasdepassagem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InterfaceGrafica.Operacoes.Vendasdepassagem[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
