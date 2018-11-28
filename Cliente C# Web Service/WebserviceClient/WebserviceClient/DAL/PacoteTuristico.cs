using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebserviceClient.DAL.Pacote
{
    public class PacoteTuristico
    {
        int idPacote, numeroPessoas, valorTotal, status;
        string nomeCliente;

        public PacoteTuristico(int idPacote, int numeroPessoas, int valorTotal, int status, string nomeCliente)
        {
            this.idPacote = idPacote;
            this.numeroPessoas = numeroPessoas;
            this.valorTotal = valorTotal;
            this.status = status;
            this.nomeCliente = nomeCliente;
        }

        public int IdPacote
        {
            get
            {
                return idPacote;
            }

            set
            {
                idPacote = value;
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

        public int NumeroPessoas
        {
            get
            {
                return numeroPessoas;
            }

            set
            {
                numeroPessoas = value;
            }
        }

        public int Status
        {
            get
            {
                return status;
            }

            set
            {
                status = value;
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