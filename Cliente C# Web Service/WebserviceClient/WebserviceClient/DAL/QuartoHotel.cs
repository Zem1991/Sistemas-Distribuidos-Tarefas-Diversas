using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebserviceClient.DAL.Quarto
{
    public class QuartoHotel
    {
        int idQuarto, idHotel, precoEstadia;
        string nomeQuarto, nomeHotel;

        public int IdHotel
        {
            get
            {
                return idHotel;
            }

            set
            {
                idHotel = value;
            }
        }

        public int IdQuarto
        {
            get
            {
                return idQuarto;
            }

            set
            {
                idQuarto = value;
            }
        }

        public string NomeHotel
        {
            get
            {
                return nomeHotel;
            }

            set
            {
                nomeHotel = value;
            }
        }

        public string NomeQuarto
        {
            get
            {
                return nomeQuarto;
            }

            set
            {
                nomeQuarto = value;
            }
        }

        public int PrecoEstadia
        {
            get
            {
                return precoEstadia;
            }

            set
            {
                precoEstadia = value;
            }
        }

        public QuartoHotel(int idQuarto, int idHotel, int preco, string nomeQuarto, string nomeHotel)
        {
            this.idQuarto = idQuarto;
            this.idHotel = idHotel;
            precoEstadia = preco;
            this.nomeQuarto = nomeQuarto;
            this.nomeHotel = nomeHotel;
        }
    }
}