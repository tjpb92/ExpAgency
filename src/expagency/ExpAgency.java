package expagency;

import bdd.Fagency;
import bdd.FagencyDAO;
import utils.ApplicationProperties;
import utils.DBServer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.DBManager;
import utils.DBServerException;

/**
 * Ce programme exporte les agences d'un service d'urgence dans un fichier au
 * format XML.
 *
 * @version Juin 2016
 * @author Thierry Baribaud
 */
public class ExpAgency {

    /**
     * SourceServer : prod pour le serveur de production, dev pour le serveur de
     * développement. Valeur par défaut : dev.
     */
    private String SourceServer = "dev";

    /**
     * Unum : Référence du client. Valeur par défaut : doit être spécifié en
     * ligne de commande.
     */
    private int unum;

    /**
     * FileOut : fichier qui recevra les résultats du chargement. Valeur par
     * défaut : agences.xml.
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
     * @return SourceServer : la valeur pour le serveur source.
     */
    private String getSourceServer() {
        return (SourceServer);
    }

    /**
     * @return FileOut : le nom du fichier où envoyer les résultats.
     */
    private String getFileOut() {
        return (FileOut);
    }

    /**
     * @return Unum : la référence du client.
     */
    private int getUnum() {
        return (unum);
    }

    /**
     * @return daemonMode : le mode de fonctionnement debug.
     */
    private boolean getDebugMode() {
        return (debugMode);
    }

    /**
     * @return testMode : le mode de fonctionnement test.
     */
    private boolean getTestMode() {
        return (testMode);
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
     * @param myUnum : définit la référence client.
     */
    private void setUnum(int myUnum) {
        this.unum = myUnum;
    }

    /**
     * @param myDebugMode : fonctionnement du programme en mode debug (true/false).
     */
    private void setDebugMode(boolean myDebugMode) {
        this.debugMode = myDebugMode;
    }

    /**
     * @param myTestMode : fonctionnement du programme en mode test (true/false).
     */
    private void setTestMode(boolean myTestMode) {
        this.testMode = myTestMode;
    }

    /**
     * Récupère les arguments de la ligne de commande.
     *
     * @param Args arguments de la ligne de commande.
     * @throws ExpAgencyException en cas d'erreur.
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
                    } catch (Exception MyException) {
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
     * <p>
     * Les arguments en ligne de commande permettent de changer le mode de
     * fonctionnement.</p><ul>
     * <li>-u unum : identifiant du service d'urgence (obligatoire).</li>
     * <li>-o fichier : fichier vers lequel exporter les données de l'agence
     * (optionnel, nom par défaut <i>agences.xml</i>).</li>
     * <li>-d : le programme fonctionne en mode débug, il est plus verbeux
     * (optionnel).</li>
     * <li>-t : le programme fonctionne en mode de test, les transactions en
     * base de données ne sont pas exécutées (optionnel).</li>
     * </ul>
     *
     * @param Args arguments de la ligne de commande.
     * @throws expagency.ExpAgencyException en cas de problème lors du lancement
     * de ExpAgency
     * @throws java.io.IOException en cas de fichier non lisible ou absent.
     * @throws utils.DBServerException en cas de propriété incorrecte.
     */
    public ExpAgency(String[] Args) throws ExpAgencyException, IOException, DBServerException {
        Fagency MyFagency;
        FagencyDAO MyFagencyDAO;
        AgencyXMLDocument MyXMLDocument;

        ApplicationProperties MyApplicationProperties;
        DBServer MyDBServer;
        DBManager MyDBManager;

        String MyA6name = null;
        int i;

        // On récupère les arguments de la ligne de commande.
        System.out.println("Récupération des arguments en ligne de commande ...");
        getArgs(Args);

        System.out.println("Lecture du fichier de paramètres ...");
        MyApplicationProperties = new ApplicationProperties("MyDatabases.prop");

        System.out.println("Lecture des paramètres de base de données ...");
        MyDBServer = new DBServer(getSourceServer(), MyApplicationProperties);
        System.out.println("  " + MyDBServer);

        MyXMLDocument = new AgencyXMLDocument("agences", "agences_plat.xsd");

        try {
            MyDBManager = new DBManager(MyDBServer);

            MyFagencyDAO = new FagencyDAO(MyDBManager.getConnection());
            MyFagencyDAO.filterByName(unum, MyA6name);
            System.out.println("  SelectStatement=" + MyFagencyDAO.getSelectStatement());
            MyFagencyDAO.setSelectPreparedStatement();
            i = 0;
            while ((MyFagency = MyFagencyDAO.select()) != null) {
                i++;
                System.out.println("Fagency(" + i + ")=" + MyFagency);
                MyXMLDocument.AddToXMLDocument(MyFagency);
            }
            MyFagencyDAO.closeSelectPreparedStatement();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, ex);
        }

        MyXMLDocument.FinalizeXMLDocument(getFileOut());
    }

    /**
     * <p>
     * Les arguments en ligne de commande permettent de changer le mode de
     * fonctionnement.</p><ul>
     * <li>-u unum : identifiant du service d'urgence (obligatoire).</li>
     * <li>-o fichier : fichier vers lequel exporter les données de l'agence
     * (optionnel, nom par défaut <i>agences.xml</i>).</li>
     * <li>-d : le programme fonctionne en mode débug, il est plus verbeux
     * (optionnel).</li>
     * <li>-t : le programme fonctionne en mode de test, les transactions en
     * base de données ne sont pas exécutées (optionnel).</li>
     * </ul>
     *
     * @param Args arguments de la ligne de commande.
     */
    public static void main(String[] Args) {
        ExpAgency MyExpAgency;

        System.out.println("Lancement de ExpAgency ...");

        try {
            MyExpAgency = new ExpAgency(Args);
        } catch (Exception MyException) {
            System.out.println("Problème lors du lancement de ExpAgency" + MyException);
        }

        System.out.println("Traitement terminé.");
    }
}
