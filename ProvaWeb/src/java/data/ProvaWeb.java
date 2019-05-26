/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package data;

import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author Marco Maspes
 *
 */
public class ProvaWeb extends HttpServlet {
    
    final private String driver = "com.mysql.jdbc.Driver";
    final private String dbms_url = "jdbc:mysql://localhost/";
    final private String database = "agenda";
    final private String user = "root";
    final private String password = "";
    private Connection circolari;
    private boolean connected;
    
    // attivazione servlet (connessione a DBMS)
    public void init() {
        String url = dbms_url + database;
        try {
            Class.forName(driver);
            circolari = DriverManager.getConnection(url, user, password);
            connected = true;
        } catch (SQLException e) {
            connected = false;
        } catch (ClassNotFoundException e) {
            connected = false;
        }
    }
    
    // disattivazione servlet (disconnessione da DBMS)
    public void destroy() {
        try {
            circolari.close();
        } catch (SQLException e) {
        }
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet WS_Phone</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet WS_Phone at " + request.getContextPath() + "</h1>");
            out.println("<p> Prova</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     * examples of use:
     *    http://localhost:8080/ProvaWeb
     *    http://localhost:8080/ProvaWeb/visualizzaUtenti
     */
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String operazione;
        String nome, cognome;
        String url;
        String[] url_section;
        // verifica stato connessione a DBMS
        if (!connected) {
            response.sendError(500, "DBMS server error!");
            return;
        }
        // estrazione nominativo da URL
        url = request.getRequestURL().toString();
        url_section = url.split("/");
        operazione = url_section[url_section.length - 1];
        
        if (operazione == null) {
            response.sendError(400, "Request syntax error!");
            return;
        }
        if (operazione.isEmpty()) {
            response.sendError(400, "Request syntax error!");
            return;
        }
        
        if (operazione.equals("visualizzaUtenti")) {
            response.setContentType("text/xml;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try
            {
                out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                
                Statement statement = circolari.createStatement();
                
                String sql = "SELECT Nome,Cognome FROM utenti";
                ResultSet result = statement.executeQuery(sql);
                
                out.println("<entry>");
                
                for(;;)
                {
                    if (result.next()) {
                        nome = result.getString(1);
                        cognome = result.getString(2);
                        out.println("<persona>");
                        out.print("<nome>");
                        out.print(nome);
                        out.println("</nome>");
                        out.print("<cognome>");
                        out.print(cognome);
                        out.println("</cognome>");
                        out.println("</persona>");
                    }
                    else
                        break;
                }
                
                out.println("</entry>");
            }catch(Exception ex){}
            finally{out.close();}
            
            response.setStatus(200); // OK
        }
        else
        {
            response.setContentType("text/xml;charset=UTF-8");
            
            PrintWriter out = response.getWriter();
            try
            {
                out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                
                out.println("<entry>");
                out.println("<p> Queste sono le possibili estensioni da aggiungere all'URL: </p>");
                out.println("<estensione> visualizzaUtenti - Permette di vedere tutta la lista utenti presenti</estensione>");
                out.println("<estensione> inserisciUtente - Permette di inserire un utente || Attributi: nome - cognome - mail - user - psw </estensione>");
                out.println("<estensione> inserisciCircolare - Permette di inserire una circolare || Attributi: titolo - desc - tag - tip - ril - liv </estensione>");
                out.println("</entry>");
                
            }
            catch(Exception ex)
            {
                response.sendError(400, "Generic error arrived!");
                return;
            }
            finally{out.close();}
        }
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String operazione = "";
        String line = "";
        
        if (!connected) {
            response.sendError(500, "DBMS server error!");
            return;
        }
        
        try {
            
            BufferedReader input = request.getReader();
            BufferedWriter file = new BufferedWriter(new FileWriter("entry.xml"));
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
            
            NodeList list = root.getElementsByTagName("operazione");
            if (list != null && list.getLength() > 0) {
                operazione = list.item(0).getFirstChild().getNodeValue();
            }
            
            if(operazione.equals("inserisciUtente"))
            {
                PrintWriter out = response.getWriter();
                try
                {
                    list = root.getElementsByTagName("nome");
                    String nome = null;
                    if (list != null && list.getLength() > 0) {
                        nome = list.item(0).getFirstChild().getNodeValue();
                    }
                    
                    list = root.getElementsByTagName("cognome");
                    String cognome = null;
                    if (list != null && list.getLength() > 0) {
                        cognome = list.item(0).getFirstChild().getNodeValue();
                    }
                    
                    list = root.getElementsByTagName("mail");
                    String mail = null;
                    if (list != null && list.getLength() > 0) {
                        mail = list.item(0).getFirstChild().getNodeValue();
                    }
                    
                    list = root.getElementsByTagName("user");
                    String user = null;
                    if (list != null && list.getLength() > 0) {
                        user = list.item(0).getFirstChild().getNodeValue();
                    }
                    
                    list = root.getElementsByTagName("psw");
                    String psw = null;
                    if (list != null && list.getLength() > 0) {
                        psw = list.item(0).getFirstChild().getNodeValue();
                    }
                    
                    list = root.getElementsByTagName("classe");
                    String classe = null;
                    if (list != null && list.getLength() > 0) {
                        classe = list.item(0).getFirstChild().getNodeValue();
                    }
                    
                    if (nome == null || cognome == null || mail == null || user == null || psw == null) {
                        response.sendError(400, "Malformed XML!");
                        return;
                    }
                    if (nome.isEmpty() || cognome.isEmpty() || mail.isEmpty() || user.isEmpty() || psw.isEmpty()) {
                        response.sendError(400, "Malformed XML!");
                        return;
                    }
                    
                    try {
                        Statement statement = circolari.createStatement();
                        String stringaSql = "INSERT INTO `utenti`(`Username`, `Nome`, `Cognome`, `Mail`, `Password`, `Classe`) VALUES ('"+user+"','"+nome+"','"+cognome+"','"+mail+"','"+psw+"','"+classe+"')";
                        if (statement.executeUpdate(stringaSql) <= 0) {
                            statement.close();
                            return;
                        }
                        statement.close();
                    } catch (SQLException e) {
                        response.sendError(500, "DBMS server error!");
                        return;
                    }
                    response.setStatus(200);
                    
                }catch(Exception ex){}
                finally{out.close();}
            }
            else if(operazione.equals("addCalendar"))
            {
                PrintWriter out = response.getWriter();
                try
                {
                    list = root.getElementsByTagName("titolo");
                    String titolo = null;
                    if (list != null && list.getLength() > 0) {
                        titolo = list.item(0).getFirstChild().getNodeValue();
                    }
                    
                    list = root.getElementsByTagName("descrizione");
                    String descrizione = null;
                    if (list != null && list.getLength() > 0) {
                        descrizione = list.item(0).getFirstChild().getNodeValue();
                    }
                    
                    list = root.getElementsByTagName("tag");
                    String tag = null;
                    if (list != null && list.getLength() > 0) {
                        tag = list.item(0).getFirstChild().getNodeValue();
                    }
                    
                    list = root.getElementsByTagName("tipologia");
                    String tipologia = null;
                    if (list != null && list.getLength() > 0) {
                        tipologia = list.item(0).getFirstChild().getNodeValue();
                    }
                    
                    list = root.getElementsByTagName("rilevante");
                    String rilevante = null;
                    if (list != null && list.getLength() > 0) {
                        rilevante = list.item(0).getFirstChild().getNodeValue();
                    }
                    
                    list = root.getElementsByTagName("livelloAutorizzativo");
                    String livelloAutorizzativo = null;
                    if (list != null && list.getLength() > 0) {
                        livelloAutorizzativo = list.item(0).getFirstChild().getNodeValue();
                    }
                    
                    if (titolo == null || descrizione == null || tag == null || tipologia == null || rilevante == null || livelloAutorizzativo == null) {
                        response.sendError(400, "Malformed XML!");
                        return;
                    }
                    if (titolo.isEmpty() || descrizione.isEmpty() || tag.isEmpty() || tipologia.isEmpty() || rilevante.isEmpty() || livelloAutorizzativo.isEmpty()) {
                        response.sendError(400, "Malformed XML!");
                        return;
                    }
                    
                    try {
                        Statement statement = circolari.createStatement();
                        if (statement.executeUpdate("INSERT INTO `circolari`(`CodCircolare`, `Titolo`, `Descrizione`, `TagIdentificativo`, `Tipologia`, `Rilevante`, `LivelloAutorizzativo`) "
                                + "VALUES (NULL,'" + titolo + "','" + descrizione + "','" + tag + "','" + tipologia + "','" + rilevante + "','" + livelloAutorizzativo + "')") <= 0) {
                            statement.close();
                            return;
                        }
                        statement.close();
                    } catch (SQLException e) {
                        response.sendError(500, "DBMS server error!");
                        return;
                    }
                    response.setStatus(200);
                    
                }catch(Exception ex){}
                finally{out.close();}
            }
            
        } catch (ParserConfigurationException e) {
            response.sendError(500, "XML parser error!");
        } catch (SAXException e) {
            response.sendError(500, "XML parser error!");
        }
    }
    
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url_name;
        String url;
        String line;
        String[] url_section;
        
        // verifica stato connessione a DBMS
        if (!connected) {
            response.sendError(500, "DBMS server error!");
            return;
        }
        // estrazione nominativo da URL
        url = request.getRequestURL().toString();
        url_section = url.split("/");
        url_name = url_section[url_section.length - 1];
        if (url_name == null) {
            response.sendError(400, "Request syntax error!");
            return;
        }
        if (url_name.isEmpty()) {
            response.sendError(400, "Request syntax error!");
            return;
        }
        try {
            // scrittura nel file "entry.xml" del body della richiesta
            BufferedReader input = request.getReader();
            BufferedWriter file = new BufferedWriter(new FileWriter("entry.xml"));
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
            
            NodeList list = root.getElementsByTagName("titolo");
            String titolo = null;
            if (list != null && list.getLength() > 0) {
                titolo = list.item(0).getFirstChild().getNodeValue();
            }
            
            list = root.getElementsByTagName("descrizione");
            String descrizione = null;
            if (list != null && list.getLength() > 0) {
                descrizione = list.item(0).getFirstChild().getNodeValue();
            }
            
            list = root.getElementsByTagName("tagIdentificativo");
            String tagIdentificativo = null;
            if (list != null && list.getLength() > 0) {
                tagIdentificativo = list.item(0).getFirstChild().getNodeValue();
            }
            
            list = root.getElementsByTagName("rilevante");
            String rilevante = null;
            if (list != null && list.getLength() > 0) {
                rilevante = list.item(0).getFirstChild().getNodeValue();
            }
            
            list = root.getElementsByTagName("livelloAutorizzativo");
            String livelloAutorizzativo = null;
            if (list != null && list.getLength() > 0) {
                livelloAutorizzativo = list.item(0).getFirstChild().getNodeValue();
            }
            
            if (titolo == null || descrizione == null || tagIdentificativo == null || rilevante == null || livelloAutorizzativo == null) {
                response.sendError(400, "Malformed XML!");
                return;
            }
            if (titolo.isEmpty() || descrizione.isEmpty() || tagIdentificativo.isEmpty() || rilevante.isEmpty() || livelloAutorizzativo.isEmpty()) {
                response.sendError(400, "Malformed XML!");
                return;
            }
            if (!titolo.equalsIgnoreCase(url_name)) {
                response.sendError(400, "URL name mismtach XML name!");
                return;
            }
            try {
                Statement statement = circolari.createStatement();
                if (statement.executeUpdate("UPDATE circolari SET Number='" + "number" + "'WHERE Name = '" + "name" + "';") <= 0) {
                    response.sendError(404, "Entry not found!");
                    statement.close();
                    return;
                }
                statement.close();
            } catch (SQLException e) {
                response.sendError(500, "DBMS server error!");
                return;
            }
            response.setStatus(204); // OK
        } catch (ParserConfigurationException e) {
            response.sendError(500, "XML parser error!");
        } catch (SAXException e) {
            response.sendError(500, "XML parser error!");
        }
    }
    
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name;
        String url;
        String[] url_section;
        
        // verifica stato connessione a DBMS
        if (!connected) {
            response.sendError(500, "DBMS server error!");
            return;
        }
        // estrazione nominativo da URL
        url = request.getRequestURL().toString();
        url_section = url.split("/");
        name = url_section[url_section.length - 1];
        if (name == null) {
            response.sendError(400, "Request syntax error!");
            return;
        }
        if (name.isEmpty()) {
            response.sendError(400, "Request syntax error!");
            return;
        }
        try {
            Statement statement = circolari.createStatement();
            if (statement.executeUpdate("DELETE FROM Phonebook WHERE Name = '" + name + "';") <= 0) {
                response.sendError(404, "Entry not found!");
                statement.close();
                return;
            }
            statement.close();
            response.setStatus(204); // OK
        } catch (SQLException e) {
            response.sendError(500, "DBMS server error!");
            return;
        }
    }
    
    @Override
    public String getServletInfo() {
        return "CircolariDB";
    }// </editor-fold>
    
}
