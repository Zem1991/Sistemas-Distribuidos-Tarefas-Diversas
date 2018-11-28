using System.Collections.Generic;
using HttpUtils;
using RestSharp;
using System.Net;
using System.IO;
using RestSharp.Serializers;
using System.Xml.Linq;
using System.Xml;
using System;
using System.Web.Mvc;
using System.Linq;

namespace WebserviceClient.DAL.Passagem
{
    public class Passagens : Controller
    {
        int id, tipo, idOrigem, idDestino, preco;
        string dataIda, dataVolta,nomeOrigem, nomeDestino;
        //Getters and Setters - gerado pelo Visual Studio
        public int Id
        {
            get
            {
                return id;
            }

            set
            {
                id = value;
            }
        }
        public int Tipo
        {
            get
            {
                return tipo;
            }

            set
            {
                tipo = value;
            }
        }
        public int IdOrigem
        {
            get
            {
                return idOrigem;
            }

            set
            {
                idOrigem = value;
            }
        }
        public int IdDestino
        {
            get
            {
                return idDestino;
            }

            set
            {
                idDestino = value;
            }
        }
        public int Preco
        {
            get
            {
                return preco;
            }

            set
            {
                preco = value;
            }
        }
        public string DataIda
        {
            get
            {
                return dataIda;
            }

            set
            {
                dataIda = value;
            }
        }
        public string DataVolta
        {
            get
            {
                return dataVolta;
            }

            set
            {
                dataVolta = value;
            }
        }
        public string NomeOrigem
        {
            get
            {
                return nomeOrigem;
            }

            set
            {
                nomeOrigem = value;
            }
        }

        public string NomeDestino
        {
            get
            {
                return nomeDestino;
            }

            set
            {
                nomeDestino = value;
            }
        }

        public Passagens()
        {

        }
        /// <summary>
        /// Instancia o Objeto passagem - construtor
        /// </summary>
        /// <param name="id"></param>
        /// <param name="tipo"></param>
        /// <param name="idOrigem"></param>
        /// <param name="idDestino"></param>
        /// <param name="dataIda"></param>
        /// <param name="dataVolta"></param>
        /// <param name="preco"></param>
        /// <param name="nomeOrigem"></param>
        /// <param name="nomeDestino"></param>
        public Passagens(int id, int tipo, int idOrigem, int idDestino, string dataIda, string dataVolta, int preco, string nomeOrigem, string nomeDestino)
        {
            this.id = id;
            this.tipo = tipo;
            this.idOrigem = idOrigem;
            this.idDestino = idDestino;
            this.dataIda = dataIda;
            this.dataVolta = dataVolta;
            this.preco = preco;
            this.nomeDestino = nomeDestino;
            this.nomeOrigem = nomeOrigem;
        }

        /// <summary>
        /// Método que invoca o GET Passagens do WebServive Passagens do Servidor Java
        /// </summary>
        /// <returns>Lista contendo as passagens  cadastradas no banco de dados do servidor</returns>
        public List<Passagens> preencheListaPassagens()
        {
            List<Passagens> listaPassagens = new List<Passagens>();
            //URL de conexão com o GET
            string url = "http://localhost:8080/PassagensApplication/webresources/entities.passagens";
            url = System.Web.HttpUtility.UrlDecode(url);
            HttpWebRequest req = WebRequest.Create(url) as HttpWebRequest;
            string result = null;
            //Faz a requisição da Resposta
            using (HttpWebResponse resp = req.GetResponse() as HttpWebResponse)
            {
                StreamReader reader = new StreamReader(resp.GetResponseStream());
                result = reader.ReadToEnd();
            }
            //codifica resposta como um xdocument para facilitar o tratamento da string
            XDocument xdoc = new XDocument();
            xdoc = XDocument.Parse(result);
            //Corta na tag <passagens>
            var element = xdoc.Descendants("passagens").Elements();

            //para cada tag <passagens> será criado um novo objeto Passagens, que é adicionado a Lista de Todas as passagens

            int i = 0;
            Passagens pass = new Passagens();
            foreach (var iten in element)
            {
                //ifs usados para separar e tratar string xml
                if (i == 0)
                {
                    // data ida
                    pass.DataIda = iten.Value;
                }
                if(i == 1)
                {
                    //data volta
                    pass.DataVolta = iten.Value;
                }
                if(i == 2)
                {
                    // id ?
                }
                if(i == 3)
                {
                    string id = XDocument.Parse(iten.FirstNode.ToString()).ToString();
                    id = id.Trim('<','i','d','>','/');
                    pass.IdDestino = Int32.Parse(id);

                    string nome = XDocument.Parse(iten.LastNode.ToString()).ToString();
                    nome = nome.Remove(0, 6);
                    nome = nome.Replace("</nome>","");
                    pass.NomeDestino = nome;                  
                    //id local destino
                }
                if (i == 4)
                {
                    string id = XDocument.Parse(iten.FirstNode.ToString()).ToString();
                    id = id.Trim('<', 'i', 'd', '>', '/');
                    pass.IdOrigem = Int32.Parse(id);

                    string nome = XDocument.Parse(iten.LastNode.ToString()).ToString();
                    nome = nome.Remove(0, 6);
                    nome = nome.Replace("</nome>", "");
                    pass.NomeOrigem = nome;
                }
                if(i == 5)
                {
                    pass.Preco = Int32.Parse(iten.Value);
                    // preco
                }
                if(i == 6)
                {
                    pass.Tipo = Int32.Parse(iten.Value);
                    //tipo
                }
                i++;
                if(i == 7)
                {
                    listaPassagens.Add(pass);

                    pass = new Passagens();
                    i = 0;
                }
            }

            return listaPassagens;

        }
    }
}