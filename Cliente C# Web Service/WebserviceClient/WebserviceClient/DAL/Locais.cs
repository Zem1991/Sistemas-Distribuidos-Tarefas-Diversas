using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebserviceClient.DAL.Local
{
    public class Locais
    {
        int idLocal;
        string nomeLocal;

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

        public Locais(int id, string nome)
        {
            idLocal = id;
            nomeLocal = nome;
        }
    }
}