/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author maspes_marco
 */
public class ConnessioneWSRest {
    
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
    {
        ConnessioneWSRestClient webService = new ConnessioneWSRestClient("http://localhost:8080/ProvaWeb");
        
        System.out.println("Inserisci l'operazione desiderata");
        System.out.println("1 - Visualizza utenti");
        System.out.println("2 - Inserisci utente");
        System.out.println("3 - Inserisci circolare");
        System.out.println("4 - Vedi estensioni possibili");
        System.out.println("0 - Termina programma");
        
        String scelta = br.readLine();
        System.out.println("");
        
        while(!scelta.equals("0"))
        {
            switch(Integer.parseInt(scelta)){
                case 1:
                    webService.stampaCalendari();
                    
                    webService.printResult();
                    System.out.println("Crea Nuovo Calendario");
                    
                    System.out.println("Nome Calendario");
                    String NomeCalendario = br.readLine();
                    
                    System.out.println("Descrizione");
                    String Descrizione = br.readLine();
                    
                    System.out.println("Tipologia");
                    String Tipologia = br.readLine();
                    
                    System.out.println("Username");
                    String username = br.readLine();
                    
                    int ritorno = webService.inserisciCalendario(NomeCalendario,Descrizione,Tipologia,username);
                    if(ritorno == 200)
                        System.out.println("Good");
                    else
                        System.out.println(ritorno);
                    
                    break;
                case 2:
                    System.out.println("Inserisci il nome");
                    String nome = br.readLine();
                    
                    System.out.println("Inserisci il cognome");
                    String cognome = br.readLine();
                    
                    System.out.println("Inserisci la mail");
                    String mail = br.readLine();
                    
                    System.out.println("Inserisci l' user");
                    String user = br.readLine();
                    
                    System.out.println("Inserisci la psw");
                    String psw = br.readLine();
                    
                    System.out.println("Inserisci la classe");
                    String classe = br.readLine();
                    
                    ritorno = 0;
                    if(ritorno == 200)
                        System.out.println("Inserimento effettuato");
                    else
                        System.out.println(ritorno);
                    
                    break;
                case 3:
                    System.out.println("Inserisci il titolo");
                    String titolo = br.readLine();
                    
                    System.out.println("Inserisci la descrizione");
                    String descrizione = br.readLine();
                    
                    System.out.println("Inserisci il tag");
                    String tag = br.readLine();
                    
                    System.out.println("Inserisci la tipologia");
                    String tipologia = br.readLine();
                    
                    System.out.println("Inserisci se è rilevante (0 - 1)");
                    String rilevante = br.readLine();
                    
                    System.out.println("Inserisci il livelloAutorizzativo");
                    String livAutorizzativo = br.readLine();
                    
                    ritorno = webService.inserisciCircolare(titolo,descrizione,tag,tipologia,rilevante, livAutorizzativo);
                    
                    if(ritorno == 200)
                        System.out.println("Inserimento effettuato");
                    else
                        System.out.println("Inserimento non effettuato");
                    
                    break;
                case 4:
                    webService.ottieniOperazioniPossibili();
                    webService.printResult();
                    break;
                case 5:
                    int id;
                    
                    id = br.read();
                    webService.deleteCalendario(id);
                    webService.printResult();
                    
                    break;
                case 6:
                    System.out.println("Inserisci il Nome calendario");
                    nome = br.readLine();
                    
                    System.out.println("Inserisci la nuova tipologia");
                    tipologia = br.readLine();
                    
                    ritorno = webService.aggiornaCalendario(nome, tipologia);
                    
                    if(ritorno == 200)
                        System.out.println("Inserimento effettuato");
                    else
                        System.out.println("Inserimento non effettuato");
                    
                    
                    break;
                default:
            }
            
            System.out.println("Inserisci l'operazione desiderata");
            System.out.println("1 - Visualizza utenti");
            System.out.println("2 - Inserisci utente");
            System.out.println("3 - Inserisci circolare");
            System.out.println("4 - Vedi estensioni possibili");
            System.out.println("0 - Termina programma");
            
            scelta = br.readLine();
            System.out.println("");
        }
    }
}
