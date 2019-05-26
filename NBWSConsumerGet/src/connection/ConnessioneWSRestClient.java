/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author maspes_marco
 */
public class ConnessioneWSRestClient {
    
    private String baseUrl;
    private int statusChiamata;
    private Vector<String> valoriRichieste;
    
    ConnessioneWSRestClient(String baseUrl) {
        this.baseUrl = baseUrl;
        
        this.statusChiamata = 0;
        
        valoriRichieste = new Vector<>();
    }
    
    
    
    public int ottieniOperazioniPossibili() throws ParserConfigurationException, SAXException
    {
        try {
            //invio richiesta al web server
            
            URL server = new URL(baseUrl);
            HttpURLConnection service = (HttpURLConnection) server.openConnection();
            
            service.setRequestProperty("Accept-Charset", "UTF-8");
            service.setRequestProperty("Accept", "text/xml");
            
            service.setDoInput(true);
            service.setRequestMethod("GET");
            
            service.connect();
            
            statusChiamata = service.getResponseCode();
            if (statusChiamata != 200) {
                return statusChiamata;
            }
            
            
            //ottenimento informazioni dal web server
            
            BufferedReader input = new BufferedReader(new InputStreamReader(service.getInputStream(), "UTF-8"));
            BufferedWriter file = new BufferedWriter(new FileWriter("entry.xml"));
            
            String line;
            while ((line = input.readLine()) != null) {
                file.write(line);
                file.newLine();
            }
            
            input.close();
            file.flush();
            file.close();
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("entry.xml");
            
            Element root = document.getDocumentElement();
            valoriRichieste.clear();
            
            NodeList list = root.getElementsByTagName("p");
            if (list != null && list.getLength() > 0) {
                valoriRichieste.add(list.item(0).getFirstChild().getNodeValue());
            }
            
            list = root.getElementsByTagName("estensione");
            if (list != null && list.getLength() > 0) {
                for(int i = 0; i< list.getLength(); i++)
                    valoriRichieste.add(list.item(i).getFirstChild().getNodeValue());
            }
            
            valoriRichieste.add("");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ConnessioneWSRestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConnessioneWSRestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConnessioneWSRestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statusChiamata;
    }
    
    public int visualizzaUtenti() throws ParserConfigurationException, SAXException
    {
        try {
            //invio richiesta al web server
            
            URL server = new URL(baseUrl + "/visualizzaUtenti");
            HttpURLConnection service = (HttpURLConnection) server.openConnection();
            
            service.setRequestProperty("Accept-Charset", "UTF-8");
            service.setRequestProperty("Accept", "text/xml");
            
            service.setDoInput(true);
            service.setRequestMethod("GET");
            
            service.connect();
            
            statusChiamata = service.getResponseCode();
            if (statusChiamata != 200) {
                return statusChiamata;
            }
            
            
            //ottenimento informazioni dal web server
            
            BufferedReader input = new BufferedReader(new InputStreamReader(service.getInputStream(), "UTF-8"));
            BufferedWriter file = new BufferedWriter(new FileWriter("entry.xml"));
            
            String line;
            while ((line = input.readLine()) != null) {
                file.write(line);
                file.newLine();
            }
            
            input.close();
            file.flush();
            file.close();
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("entry.xml");
            
            Element root = document.getDocumentElement();
            valoriRichieste.clear();
            
            NodeList list = root.getElementsByTagName("persona");
            if (list != null && list.getLength() > 0) {
                for(int i = 0; i< list.getLength(); i++)
                {
                    Element persona = (Element) list.item(i);
                    
                    valoriRichieste.add("Utente:");
                    
                    NodeList nome = persona.getElementsByTagName("nome");
                    if (nome != null && nome.getLength() > 0) {
                        valoriRichieste.add(nome.item(0).getFirstChild().getNodeValue());
                    }
                    
                    NodeList cognome = persona.getElementsByTagName("cognome");
                    if (cognome != null && cognome.getLength() > 0) {
                        valoriRichieste.add(cognome.item(0).getFirstChild().getNodeValue());
                    }
                    
                    valoriRichieste.add("");
                }
            }
            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ConnessioneWSRestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConnessioneWSRestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConnessioneWSRestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statusChiamata;
    }
    
    public int inserisciUtente(String nome, String cognome, String mail, String user, String psw, String Classe) throws ParserConfigurationException, SAXException
    {
        String doc = "<?xml version = \"1.0\" encoding= \"UTF-8\" ?>";
        
        doc += "<entry>\r\n";
        doc += "<operazione>inserisciUtente</operazione>\r\n";
        doc += "<nome>" + nome + "</nome>\r\n";
        doc += "<cognome>" + cognome + "</cognome>\r\n";
        doc += "<mail>" + mail + "</mail>\r\n";
        doc += "<user>" + user + "</user>\r\n";
        doc += "<psw>" + psw + "</psw>\r\n";
        doc += "<classe>" + Classe + "</classe>\r\n";
        doc += "</entry>\r\n";
        
        int n = doc.length();
        
        try {
            //invio richiesta al web server
            
            URL server = new URL(baseUrl);
            HttpURLConnection service = (HttpURLConnection) server.openConnection();
            
            service.setRequestProperty("Content-type", "application/xml");
            service.setRequestProperty("Content-length", Integer.toString(n));
            
            service.setDoOutput(true);
            service.setRequestMethod("POST");
            
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(service.getOutputStream(), "UTF-8"));
            output.write(doc);
            output.flush();
            output.close();
            
            service.connect();
            
            statusChiamata = service.getResponseCode();

            return statusChiamata;            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ConnessioneWSRestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConnessioneWSRestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConnessioneWSRestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statusChiamata;
    }
    
    public int inserisciCircolare(String titolo, String descrizione, String tag, String tipologia, String rilevante, String livelloAutorizzativo) throws ParserConfigurationException, SAXException
    {
        String doc = "<?xml version = \"1.0\" encoding= \"UTF-8\" ?>";
        
        doc += "<entry>\r\n";
        doc += "<operazione>inserisciCircolare</operazione>\r\n";
        doc += "<titolo>" + titolo + "</titolo>\r\n";
        doc += "<descrizione>" + descrizione + "</descrizione>\r\n";
        doc += "<tag>" + tag + "</tag>\r\n";
        doc += "<tipologia>" + tipologia + "</tipologia>\r\n";
        doc += "<rilevante>" + rilevante + "</rilevante>\r\n";
        doc += "<livelloAutorizzativo>" + livelloAutorizzativo + "</livelloAutorizzativo>\r\n";
        doc += "</entry>\r\n";
        
        int n = doc.length();
        
        try {
            //invio richiesta al web server
            
            URL server = new URL(baseUrl);
            HttpURLConnection service = (HttpURLConnection) server.openConnection();
            
            service.setRequestProperty("Content-type", "application/xml");
            service.setRequestProperty("Content-length", Integer.toString(n));
            
            service.setDoOutput(true);
            service.setRequestMethod("POST");
            
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(service.getOutputStream(), "UTF-8"));
            output.write(doc);
            output.flush();
            output.close();
            
            service.connect();
            
            statusChiamata = service.getResponseCode();

            return statusChiamata;            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ConnessioneWSRestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(ConnessioneWSRestClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConnessioneWSRestClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return statusChiamata;
    }
    
    void printResult() {
        for(String a : valoriRichieste)
            System.out.println(a);
    }
}
