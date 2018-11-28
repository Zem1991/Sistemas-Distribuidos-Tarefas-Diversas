using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using HttpUtils;
using WebserviceClient.DAL.Passagem;
using WebserviceClient.DAL.VPassagem;
using System.Net;
using System.Text;
using System.IO;
using RestSharp;

namespace WebserviceClient.Controllers
{
    public class HomeController : Controller
    {
        public HomeController()
        {

        }
        public ActionResult Index()
        {
            ViewBag.Title = "Home Page";

            //var texte = new HttpUtils.RestClient();

            ////basic call
            //string endPoint = @"http:\\myRestService.com\api\";
            //var client = new RestClient(endPoint);
            //var json = client.MakeRequest();

            ////If you want to append parameters you can pass them into the make request method like so.
            //var json2 = client.MakeRequest("?param=0");

            ////To set the HttpVerb (i.e. GET, POST, PUT, or DELETE), 
            ////simply use the provided HttpVerb enumeration. Here is an expample of making a POST request:

            //var client2 = new RestClient(endpoint: endPoint,
            //                method: HttpVerb.POST,
            //                postData: "{'someValueToPost': 'The Value being Posted'}");

            ////You can also just assign the values in line if you want:
            //var client3 = new RestClient();
            //client.EndPoint = @"http:\\myRestService.com\api\"; ;
            //client.Method = HttpVerb.POST;
            //client.PostData = "{postData: value}";
            //var json3 = client.MakeRequest();
            return View();
        }
        public ActionResult Passagens()
        {
            return View();
        }
        /// <summary>
        /// Método que Recebe parametros da View passagens.cshtml, por meio do clique em um link.
        /// Os Parametros são passados atravez da URL 
        /// </summary>
        /// <param name="Id">Id da passagem a ser comprada</param>
        /// <param name="DataIda">Data de Ida</param>
        /// <param name="DataVolta">Data de Volta</param>
        /// <param name="IdDestino">Id do Destino</param>
        /// <param name="NomeDestino">Nome do Destino</param>
        /// <param name="IdOrigem">Id da Origem</param>
        /// <param name="NomeOrigem">Nome da Origem</param>
        /// <param name="Preco">Preço da passagem</param>
        /// <param name="Tipo">Tipo da passagem: 0 para ida, 1 para ida e volta</param>
        /// <returns>Outra view, contendo os Detalhes da passagem selecionada e campos extras para serem preenchidos</returns>
        public ActionResult comprarPassagem(int Id = 0, string DataIda = "", string DataVolta = "",
            int IdDestino = 0, string NomeDestino = "",
            int IdOrigem = 0, string NomeOrigem = "", int Preco = 0, int Tipo = 0)
        {
            Passagens passagem = new Passagens(Id, Tipo, IdOrigem, IdDestino, DataIda, DataVolta, Preco, NomeOrigem, NomeDestino);

            ViewBag.passagem = passagem;
            ViewData["passag"] = passagem;
            return View(passagem);
        }
        /// <summary>
        /// Método que invoca o Post do WebService Java para efetuar a Compra da Passagem
        /// </summary>
        /// <param name="form">Formulário web que contem os parametros digitados pelo usuário(cliente)</param>
        /// <returns>Redireciona para o Index em caso de sucesso, Retorna a exceção em caso de erro</returns>
        [HttpPost]
        public ActionResult CadastrarCompra(FormCollection form)
        {
            //numero cartao, numero parcelas, numero pessoas, valor total, idPacote, nome cliente
            VendaPassagem venda = new VendaPassagem(Int32.Parse(form["NumeroCartao"]), Int32.Parse(form["NumeroParcelas"]), 
                Int32.Parse(form["NumeroPessoas"]), Int32.Parse(form["Valor"]), form["NomeCliente"]);

            HttpWebRequest req = WebRequest.Create(new Uri("http://localhost:8080/PassagensVendaApplication/webresources/entities.vendasdepassagem/Post")) as HttpWebRequest;
            req.Method = "POST";
            req.ContentType = "text/plain";


            // Build a string with all the params, properly encoded.
            // We assume that the arrays paramName and paramVal aret
            // of equal length:
            string[] paramName = new string[5] { "NumeroCartao", "NumeroParcelas","NumeroPessoas","Valor","NomeCliente" } ;
            string[] paramVal = new string[5] { form["NumeroCartao"], form["NumeroParcelas"], form["NumeroPessoas"], form["Valor"], form["NomeCliente"] };

            StringBuilder paramz = new StringBuilder();
            String postParameter = "";
            for (int i = 0; i < paramName.Length; i++)
            {
                paramz.Append(paramName[i]);
                paramz.Append("=");
                paramz.Append(HttpUtility.UrlEncode(paramVal[i]));
                paramz.Append("&");
                postParameter = paramz.ToString();
            }

            
            // Codifica os dados
            byte[] formData = UTF8Encoding.UTF8.GetBytes(paramz.ToString());
            req.ContentLength = formData.Length;
            //Envio dos dados para o Post
            Stream newStream = req.GetRequestStream();
            newStream.Write(formData, 0, formData.Length);
            newStream.Close();
            WebResponse resp = req.GetResponse();

            StreamReader reader = new StreamReader(resp.GetResponseStream());
            string result = reader.ReadToEnd();
            

            return RedirectToAction("Index");
        }
    }
}
