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
     * Les arguments en ligne de commande permettent de changer le mode de
     * fonctionnement. Voir GetArgs pour plus de détails.
     *
     * @param Args arguments de la ligne de commande.
     * @throws expagency.ExpAgencyException en cas de problème lors du lancement de ExpAgency.
     * @throws java.io.IOException en cas de fichier non lisible ou absent.
     * @throws utils.DBServerException en cas de propriété incorrecte.
     */
    public ExpAgency(String[] Args) throws ExpAgencyException, IOException, DBServerException {
        Fagency MyFagency;
        FagencyDAO MyFagencyDAO;
        AgencyXMLDocument MyXMLDocument;

        GetArgs MyArgs;
        ApplicationProperties MyApplicationProperties;
        DBServer MyDBServer;
        DBManager MyDBManager;

        String MyA6name = null;
        int i;

        // On récupère les arguments de la ligne de commande.
        System.out.println("Récupération des arguments en ligne de commande ...");
        try {
            MyArgs = new GetArgs(Args);
            System.out.println(MyArgs);

            System.out.println("Lecture du fichier de paramètres ...");
            MyApplicationProperties = new ApplicationProperties("MyDatabases.prop");

            System.out.println("Lecture des paramètres de base de données ...");
            MyDBServer = new DBServer(MyArgs.getSourceServer(), MyApplicationProperties);
            System.out.println("  " + MyDBServer);

            MyXMLDocument = new AgencyXMLDocument("agences", "agences.xsd");

            MyDBManager = new DBManager(MyDBServer);

            MyFagencyDAO = new FagencyDAO(MyDBManager.getConnection());
            MyFagencyDAO.filterByName(MyArgs.getUnum(), MyA6name);
            System.out.println("  SelectStatement=" + MyFagencyDAO.getSelectStatement());
            MyFagencyDAO.setSelectPreparedStatement();
            i = 0;
            while ((MyFagency = MyFagencyDAO.select()) != null) {
                i++;
                System.out.println("Fagency(" + i + ")=" + MyFagency);
                MyXMLDocument.AddToXMLDocument(MyFagency);
            }
            MyXMLDocument.FinalizeXMLDocument(MyArgs.getFileOut());
            MyFagencyDAO.closeSelectPreparedStatement();
        } catch (GetArgsException MyException) {
            Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, MyException);
            GetArgs.usage();
        } catch (ClassNotFoundException MyException) {
            Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, MyException);
        } catch (SQLException MyException) {
            Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, MyException);
        }
    }

    /**
     * Les arguments en ligne de commande permettent de changer le mode de
     * fonctionnement. Voir GetArgs pour plus de détails.
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
