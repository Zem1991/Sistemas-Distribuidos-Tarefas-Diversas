using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebserviceClient.DAL.Hotel
{
    public class Hoteis
    {
        int idHotel, idLocal;
        string nomeHotel, nomeLocal;

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

        public int IdLocal
        {
            get
            {
                return idLocal;
            }

            set
            {
                idLocal = value;
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

        public string NomeLocal
        {
            get
            {
                return nomeLocal;
            }

            set
            {
                nomeLocal = value;
            }
        }

        public Hoteis(int id, int local, string nome, string nomeLocal)
        {
            idHotel = id;
            local = idLocal;
            nome = nomeHotel;
            this.nomeLocal = nomeLocal;
        }
    }
}