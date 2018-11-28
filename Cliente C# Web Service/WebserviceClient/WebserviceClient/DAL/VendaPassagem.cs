using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebserviceClient.DAL.VPassagem
{
    public class VendaPassagem
    {
        int idVendaPassagem, numeroCartao, numParcelas, numPessoas, valorTotal, idPacoteTuristico;
        string nomeCliente;
        /// <summary>
        /// Método para instanciar uma Venda de Passagem - construtor do objeto 
        /// </summary>
        /// <param name="numeroCartao"></param>
        /// <param name="numParcelas"></param>
        /// <param name="numPessoas"></param>
        /// <param name="valorTotal"></param>
        /// <param name="nomeCliente"></param>
        public VendaPassagem( int numeroCartao, int numParcelas, int numPessoas, int valorTotal, string nomeCliente)
        { 
           
            this.numeroCartao = numeroCartao;
            this.numParcelas = numParcelas;
            this.numPessoas = numPessoas;
            this.valorTotal = valorTotal;
            this.nomeCliente = nomeCliente;
           
        }
        //Getters and Setters - gerado pelo Visual Studio
        public int IdPacoteTuristico
        {
            get
            {
                return idPacoteTuristico;
            }

            set
            {
                idPacoteTuristico = value;
            }
        }

        public int IdVendaPassagem
        {
            get
            {
                return idVendaPassagem;
            }

            set
            {
                idVendaPassagem = value;
            }
        }

        public string NomeCliente
        {
            get
            {
                return nomeCliente;
            }

            set
            {
                nomeCliente = value;
            }
        }

        public int NumeroCartao
        {
            get
            {
                return numeroCartao;
            }

            set
            {
                numeroCartao = value;
            }
        }

        public int NumParcelas
        {
            get
            {
                return numParcelas;
            }

            set
            {
                numParcelas = value;
            }
        }

        public int NumPessoas
        {
            get
            {
                return numPessoas;
            }

            set
            {
                numPessoas = value;
            }
        }

        public int ValorTotal
        {
            get
            {
                return valorTotal;
            }

            set
            {
                valorTotal = value;
            }
        }
    }
}