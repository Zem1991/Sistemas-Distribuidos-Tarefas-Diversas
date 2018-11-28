using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebserviceClient.DAL.VHospedagem
{
    public class VendaHospedagem
    {
        int idVendaHosp, numeroCartao, numParcelas, numPessoas, valorTotal, idPacoteTuristico;
        string nomeCliente;

        public VendaHospedagem(int idVendaHosp, int numeroCartao, int numParcelas, int numPessoas, int valorTotal, int idPacote, string nomeCliente)
        {
            this.idVendaHosp = idVendaHosp;
            this.numeroCartao = numeroCartao;
            this.numParcelas = numParcelas;
            this.numPessoas = numPessoas;
            this.valorTotal = valorTotal;
            this.nomeCliente = nomeCliente;
            idPacoteTuristico = idPacote;
        }
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

        public int IdVendaHosp
        {
            get
            {
                return idVendaHosp;
            }

            set
            {
                idVendaHosp = value;
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