/*
 * Ce programme exporte les agences d'un service d'urgence dans un fichier au
 * format XML.
 * @version Mai 2016.
 * @author Thierry Baribaud.
 */
package expagency;

import agency.Fagency;
import agency.FagencyDAO;
import liba2pi.ApplicationProperties;
import liba2pi.DBServer;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import liba2pi.DBManager;
import liba2pi.DBServerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Comment;

public class ExpAgency {

    /**
     * SourceServer : prod pour le serveur de production, dev pour le serveur de
     * développement. Valeur par défaut : dev.
     */
    private String SourceServer = "dev";

    /**
     * Unum : Référence du client.
     * Valeur par défaut : doit être spécifié en ligne de commande.
     */
    private int unum;

    /**
     * FileOut : fichier qui recevra les résultats du chargement.
     * Valeur par défaut : agences.xml.
     */
    private String FileOut = "agences.xml";

    /**
     * debugMode : fonctionnement du programme en mode debug (true/false).
     * Valeur par défaut : false.
     */
    private boolean debugMode = false;

    /**
     * testMode : fonctionnement du programme en mode test (true/false). Valeur
     * par défaut : false.
     */
    private boolean testMode = false;

    /**
     * MyDocument : référence au document XML.
     */
    private Document MyDocument;
    
    /**
     * MyAgencies : Liste des agences à exporter.
     */
    private Element MyAgencies;

  /**
   * @return SourceServer : retourne la valeur pour le serveur source.
   */
  private String getSourceServer() {
    return(SourceServer);
  }

  /**
   * @return FileOut : retourne le nom du fichier où envoyer les résultats.
   */
  private String getFileOut() {
    return(FileOut);
  }

  /**
   * @return Unum : retourne la référence du client.
   */
  private int getUnum() {
    return(unum);
  }

  /**
   * @return daemonMode : retourne le mode de fonctionnement debug.
   */
  private boolean getDebugMode() {
    return(debugMode);
  }
  
  /**
   * @return testMode : retourne le mode de fonctionnement test.
   */
  private boolean getTestMode() {
    return(testMode);
  }
  
  /**
   * @param MySourceServer : définit le serveur source.
   */
  private void setSourceServer(String MySourceServer) {
    this.SourceServer = MySourceServer;
  }

  /**
   * @param MyFileOut : définit le fichier où envoyer les résultats.
   */
  private void setFileOut(String MyFileOut) {
    this.FileOut = MyFileOut;
  }

  /**
   * @param MyUnum : définit la référence client.
   */
  private void setUnum(int myUnum) {
    this.unum = myUnum;
  }

    /**
    * debugMode : fonctionnement du programme en mode debug (true/false).
   */
  private void setDebugMode(boolean myDebugMode) {
    this.debugMode = myDebugMode;
  }

  /**
    * testMode : fonctionnement du programme en mode test (true/false).
   */
  private void setTestMode(boolean myTestMode) {
    this.testMode = myTestMode;
  }

    /**
     * Initialise le document XML.
     */
    private void StartXMLDocument() {

        DocumentBuilderFactory MyFactory;
        DocumentBuilder MyBuilder;

        MyFactory = DocumentBuilderFactory.newInstance();
        try {
            MyBuilder = MyFactory.newDocumentBuilder();
            MyDocument = MyBuilder.newDocument();

            MyAgencies = MyDocument.createElement("agences");
            MyDocument.appendChild(MyAgencies);

        } catch (ParserConfigurationException MyException) {
            Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, MyException);
            System.out.println("Problem creating XML document " + MyException);
        }

    }
  
    /**
     * Ajoute une agence au document XML.
     */
    private void AddToXMLDocument(Fagency MyFagency) {

        Comment MyComment;
        Element MyElement;
        Element Phones;
        String MyString;
        int myInt;
        Timestamp MyTimestamp;
        
        Element Agency;
        
        TimeZone MyTimeZone = TimeZone.getTimeZone("Europe/Paris");
        DateFormat MyDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        MyString = MyFagency.getA6name();
        if (MyString != null) {
            MyComment = MyDocument.createComment(MyString);
            MyAgencies.appendChild(MyComment);
        }

        Agency = MyDocument.createElement("agence");
        MyAgencies.appendChild(Agency);

        // Agency ID
        myInt = MyFagency.getA6num();
        if (myInt >= 0) {
            MyElement = MyDocument.createElement("id");
            MyElement.appendChild(MyDocument.createTextNode(String.valueOf(myInt)));
            Agency.appendChild(MyElement);
        }
        
        // Customer ID
        myInt = MyFagency.getA6unum();
        if (myInt >= 0) {
            MyElement = MyDocument.createElement("client");
            MyElement.appendChild(MyDocument.createTextNode(String.valueOf(myInt)));
            Agency.appendChild(MyElement);
        }

        // Agency name
        MyString = MyFagency.getA6name();
        if (MyString != null) {
            MyElement = MyDocument.createElement("nom");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            Agency.appendChild(MyElement);
        }

        // Agency abbreviated name
        MyString = MyFagency.getA6abbname();
        if (MyString != null) {
            MyElement = MyDocument.createElement("codeAgence");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            Agency.appendChild(MyElement);
        }

        // Agency external name
        MyString = MyFagency.getA6extname();
        if (MyString != null) {
            MyElement = MyDocument.createElement("appellationClient");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            Agency.appendChild(MyElement);
        }

        // Agency address
        MyString = MyFagency.getA6daddress();
        if (MyString != null) {
            MyElement = MyDocument.createElement("adresse");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            Agency.appendChild(MyElement);
        }
        MyString = MyFagency.getA6daddress2();
        if (MyString != null) {
            MyElement = MyDocument.createElement("complement");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            Agency.appendChild(MyElement);
        }
        MyString = MyFagency.getA6dposcode();
        if (MyString != null) {
            MyElement = MyDocument.createElement("codePostal");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            Agency.appendChild(MyElement);
        }
        MyString = MyFagency.getA6dcity();
        if (MyString != null) {
            MyElement = MyDocument.createElement("ville");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            Agency.appendChild(MyElement);
        }

        // Agency email
        MyString = MyFagency.getA6email();
        if (MyString != null) {
            MyElement = MyDocument.createElement("email");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            Agency.appendChild(MyElement);
        }

        // Agency phones
        Phones = MyDocument.createElement("telephones");
        Agency.appendChild(Phones);

        MyString = MyFagency.getA6teloff();
        if (MyString != null) {
            MyElement = MyDocument.createElement("telephone");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyElement.setAttribute("type", "bureau");
            Phones.appendChild(MyElement);
        }

        MyString = MyFagency.getA6teldir();
        if (MyString != null) {
            MyElement = MyDocument.createElement("telephone");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyElement.setAttribute("type", "direct");
            Phones.appendChild(MyElement);
        }

        MyString = MyFagency.getA6telfax();
        if (MyString != null) {
            MyElement = MyDocument.createElement("telephone");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyElement.setAttribute("type", "fax");
            Phones.appendChild(MyElement);
        }

        // Agency activity
        MyElement = MyDocument.createElement("actif");
        MyElement.appendChild(MyDocument.createTextNode((MyFagency.getA6active() == 1) ? "OUI" : "NON"));
        Agency.appendChild(MyElement);
        
        MyTimestamp = MyFagency.getA6begactive();
        if (MyTimestamp != null) {
            MyElement = MyDocument.createElement("debut");
            MyElement.appendChild(MyDocument.createTextNode(MyDateFormat.format(MyTimestamp)));
            Agency.appendChild(MyElement);
        }

        MyTimestamp = MyFagency.getA6endactive();
        if (MyTimestamp != null) {
            MyElement = MyDocument.createElement("fin");
            MyElement.appendChild(MyDocument.createTextNode(MyDateFormat.format(MyTimestamp)));
            Agency.appendChild(MyElement);
        }
    }
  
  /**
   * Finalise le document XML.
   */
  private void FinalizeXMLDocument() {

    TransformerFactory MyTransformerFactory;
    Transformer MyTransformer;
    DOMSource MySource;
    StreamResult MyOutput;

    try {
        MyTransformerFactory = TransformerFactory.newInstance();
        MyTransformer = MyTransformerFactory.newTransformer();

        MySource = new DOMSource(MyDocument);
//          MyOutput = new StreamResult(new File("agence_" + MyFagency.getA6unum() + ".xml"));
        MyOutput = new StreamResult(new File(FileOut));
//          MyOutput = new StreamResult(System.out);

        // Prologue
        MyTransformer.setOutputProperty(OutputKeys.VERSION, "1.0");
//        MyTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        MyTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-15");
        MyTransformer.setOutputProperty(OutputKeys.STANDALONE, "no");
        MyTransformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,"agences.dtd");

        // Formatting results
        MyTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        MyTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        // Output
        MyTransformer.transform(MySource, MyOutput);

        System.out.println("Fichier des résultats : " + FileOut);
    } catch (TransformerConfigurationException MyException) {
        Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, MyException);
        System.out.println("Problem configuring XML document " + MyException);
    } catch (TransformerException MyException) {
        Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, MyException);
        System.out.println("Problem writing XML document " + MyException);
    }
  }
  
  /**
   * Récupère les arguments de la ligne de commande.
   * @param Args arguments de la ligne de commande.
   * @throws ImpAgencyException en cas d'erreur.
   */
    private void getArgs(String[] Args) throws ExpAgencyException {

        String[] Errmsg = {"Erreur n°1 : Mauvaise source de données",
            "Erreur n°2 : Mauvaise référence client",
            "Erreur n°3 : Mauvais fichier résultat",
            "Erreur n°4 : Mauvais argument"};
        String ErrorValue = "";
        int errNo = Errmsg.length;
        int i;
        int n;
        int ip1;

        n = Args.length;

        System.out.println("nargs=" + n);
//    for(i=0; i<n; i++) System.out.println("args["+i+"]="+Args[i]);
        i = 0;
        while (i < n) {
//            System.out.println("args[" + i + "]=" + Args[i]);
            ip1 = i + 1;
            if (Args[i].equals("-dbserver")) {
                if (ip1 < n) {
                    if (Args[ip1].equals("dev") || Args[ip1].equals("prod") || Args[ip1].equals("mysql")) {
                        setSourceServer(Args[ip1]);
                    } else {
                        errNo = 0;
                        ErrorValue = Args[ip1];
                    }
                    i = ip1;
                } else {
                    errNo = 0;
                    ErrorValue = "base de données non définie";
                }
            } else if (Args[i].equals("-u")) {
                if (ip1 < n) {
                    try {
                        setUnum(Integer.parseInt(Args[ip1]));
                        i = ip1;
                    }
                    catch (Exception MyException) {
                        errNo = 1;
                        ErrorValue = "La référence client doit être numérique";
                    }
                } else {
                    errNo = 1;
                    ErrorValue = "référence client non définie";
                }
            } else if (Args[i].equals("-o")) {
                if (ip1 < n) {
                    setFileOut(Args[ip1]);
                    i = ip1;
                } else {
                    errNo = 2;
                    ErrorValue = "nom de fichier non défini";
                }
            } else if (Args[i].equals("-d")) {
                setDebugMode(true);
            } else if (Args[i].equals("-t")) {
                setTestMode(true);
            } else {
                errNo = 3;
                ErrorValue = Args[i];
            }
            i++;
        }
//        System.out.println("errNo=" + errNo);
        if (errNo != Errmsg.length) {
            throw new ExpAgencyException(Errmsg[errNo] + " : " + ErrorValue);
        }
    }

    /**
     * Les arguments en ligne de commande permettent de changer le mode de 
     * fonctionnement.
     * -u unum : identifiant du service d'urgence (obligatoire).
     * -o fichier : fichier vers lequel exporter les données de l'agence 
     *              (optionnel, nom par défaut agences.xml).
     * -d : le programme fonctionne en mode débug, il est plus verbeux (optionnel).
     * -t : le programme fonctionne en mode de test, les transactions en base 
     *      de données ne sont pas exécutées (optionnel).
     * @param Args arguments de la ligne de commande. 
     * @throws expagency.ExpAgencyException 
     * @throws java.io.IOException 
     * @throws liba2pi.DBServerException 
     */
    public ExpAgency(String[] Args) throws ExpAgencyException, IOException, DBServerException {
        Fagency MyFagency;
        FagencyDAO MyFagencyDAO;

        ApplicationProperties MyApplicationProperties;
        DBServer MyDBServer;
        DBManager MyDBManager;

        String MyA6name = null;
        int i;

//        MyFagency = new Fagency();
//        
////        MyFagency.setA6num(1234);
//        MyFagency.setA6unum(99999);
//        MyFagency.setA6extname("terra incognita");
//        MyFagency.setA6name("utopia");
//        MyFagency.setA6abbname("UTOPIA");
//        MyFagency.setA6email("utopia@gmail.com");
//        MyFagency.setA6daddress("12, rue des rèves");
//        MyFagency.setA6daddress2("bâtiment B");
//        MyFagency.setA6dposcode("92400");
//        MyFagency.setA6dcity("UTOPIA CITY");
//        MyFagency.setA6teloff("01.01.01.01.01");
//        MyFagency.setA6teldir("02.02.02.02.02");
//        MyFagency.setA6telfax("03.03.03.03.03");
//        MyFagency.setA6active(1);
//        MyFagency.setA6begactive(new Timestamp(new java.util.Date().getTime()));
//        MyFagency.setA6endactive(Timestamp.valueOf("2050-12-31 23:59:59.0"));
//        System.out.println("MyFagency=" + MyFagency);
        // On récupère les arguments de la ligne de commande.
        System.out.println("Récupération des arguments en ligne de commande ...");
        getArgs(Args);

        System.out.println("Lecture du fichier de paramètres ...");
        MyApplicationProperties = new ApplicationProperties("ExpAgencyPublic.prop");

        System.out.println("Lecture des paramètres de base de données ...");
        MyDBServer = new DBServer(getSourceServer(), MyApplicationProperties);
        System.out.println("  " + MyDBServer);

        StartXMLDocument();

        try {
            MyDBManager = new DBManager(MyDBServer);

            MyFagencyDAO = new FagencyDAO(MyDBManager.getConnection(), unum, MyA6name);
            i = 0;
            while ((MyFagency = MyFagencyDAO.select()) != null) {
                i++;
                System.out.println("Fagency(" + i + ")=" + MyFagency);
                AddToXMLDocument(MyFagency);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, ex);
        }

        FinalizeXMLDocument();
    }

    /**
     * Methode pour lancer le programme.
     * Les arguments en ligne de commande permettent de changer le mode de 
     * fonctionnement.
     * -u unum : identifiant du service d'urgence (obligatoire).
     * -o fichier : fichier vers lequel exporter les données de l'agence 
     *              (optionnel, nom par défaut agences.xml).
     * -d : le programme fonctionne en mode débug, il est plus verbeux (optionnel).
     * -t : le programme fonctionne en mode de test, les transactions en base 
     *      de données ne sont pas exécutées (optionnel).
     * @param Args arguments de la ligne de commande. 
     */
    public static void main(String[] Args){
        ExpAgency MyExpAgency;
        
        System.out.println("Lancement de ExpAgency ...");
        
        try {
            MyExpAgency = new ExpAgency(Args);
        }
        catch (Exception MyException) {
            System.out.println("Problème lors du lancement de ExpAgency" + MyException);
        }
        
        System.out.println("Traitement terminé.");
    }
}
