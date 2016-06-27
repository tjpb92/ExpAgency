package expagency;

import bdd.Fagency;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import utils.XMLDocument;

/**
 * Classe pour générer un fichier au format XML décrivant des agences.
 *
 * @version Juin 2016
 * @author Thierry Baribaud
 */
public class AgencyXMLDocument extends XMLDocument {

    public AgencyXMLDocument(String RootName, String XsdFile) {
        super(RootName, XsdFile);
    }

    /**
     * Ajoute une agence au document XML. L'agence est décrite d'une manière
     * structurée
     *
     * @param MyFagency agence à transcrire en XML.
     */
    public void AddToXMLDocument(Fagency MyFagency) {

        Comment MyComment;
        Element MyElement;
        Element MyGroup;
        String MyString;
        int myInt;
        Timestamp MyTimestamp;

        Element Agency;

        DateFormat MyDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        MyString = MyFagency.getA6name();
        if (MyString != null) {
            MyComment = MyDocument.createComment(MyString);
            MyElements.appendChild(MyComment);
        }

        Agency = MyDocument.createElement("agence");
        MyElements.appendChild(Agency);

        // Ids group
        MyGroup = MyDocument.createElement("ids");
        Agency.appendChild(MyGroup);

        // Agency ID
        myInt = MyFagency.getA6num();
        if (myInt >= 0) {
            MyElement = MyDocument.createElement("id");
            MyElement.appendChild(MyDocument.createTextNode(String.valueOf(myInt)));
            MyGroup.appendChild(MyElement);
        }

        // Customer ID
        myInt = MyFagency.getA6unum();
        if (myInt >= 0) {
            MyElement = MyDocument.createElement("idClient");
            MyElement.appendChild(MyDocument.createTextNode(String.valueOf(myInt)));
            MyGroup.appendChild(MyElement);
        }

        // Agency abbreviated name
        MyString = MyFagency.getA6abbname();
        if (MyString != null) {
            MyElement = MyDocument.createElement("codeAgence");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyGroup.appendChild(MyElement);
        }

        // Names group
        MyGroup = MyDocument.createElement("noms");
        Agency.appendChild(MyGroup);

        // Agency name
        MyString = MyFagency.getA6name();
        if (MyString != null) {
            MyElement = MyDocument.createElement("nom");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyGroup.appendChild(MyElement);
        }

        // Agency external name
        MyString = MyFagency.getA6extname();
        if (MyString != null) {
            MyElement = MyDocument.createElement("appellationClient");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyGroup.appendChild(MyElement);
        }

        // Address group
        MyGroup = MyDocument.createElement("adressePostale");
        Agency.appendChild(MyGroup);

        // Agency address
        MyString = MyFagency.getA6daddress();
        if (MyString != null) {
            MyElement = MyDocument.createElement("adresse");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyGroup.appendChild(MyElement);
        }
        MyString = MyFagency.getA6daddress2();
        if (MyString != null) {
            MyElement = MyDocument.createElement("complement");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyGroup.appendChild(MyElement);
        }
        MyString = MyFagency.getA6dposcode();
        if (MyString != null) {
            MyElement = MyDocument.createElement("codePostal");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyGroup.appendChild(MyElement);
        }
        MyString = MyFagency.getA6dcity();
        if (MyString != null) {
            MyElement = MyDocument.createElement("ville");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyGroup.appendChild(MyElement);
        }

        // Agency email
        MyString = MyFagency.getA6email();
        if (MyString != null) {
            MyElement = MyDocument.createElement("email");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            Agency.appendChild(MyElement);
        }

        // Agency phones
        MyGroup = MyDocument.createElement("telephones");
        Agency.appendChild(MyGroup);

        MyString = MyFagency.getA6teloff();
        if (MyString != null) {
            MyElement = MyDocument.createElement("telephone");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyElement.setAttribute("type", "bureau");
            MyGroup.appendChild(MyElement);
        }

        MyString = MyFagency.getA6teldir();
        if (MyString != null) {
            MyElement = MyDocument.createElement("telephone");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyElement.setAttribute("type", "direct");
            MyGroup.appendChild(MyElement);
        }

        MyString = MyFagency.getA6telfax();
        if (MyString != null) {
            MyElement = MyDocument.createElement("telephone");
            MyElement.appendChild(MyDocument.createTextNode(MyString));
            MyElement.setAttribute("type", "fax");
            MyGroup.appendChild(MyElement);
        }

        // Status group
        MyGroup = MyDocument.createElement("etat");
        Agency.appendChild(MyGroup);

        // Agency activity
        MyElement = MyDocument.createElement("actif");
        MyElement.appendChild(MyDocument.createTextNode((MyFagency.getA6active() == 1) ? "OUI" : "NON"));
        MyGroup.appendChild(MyElement);

        MyTimestamp = MyFagency.getA6begactive();
        if (MyTimestamp != null) {
            MyElement = MyDocument.createElement("debut");
            MyElement.appendChild(MyDocument.createTextNode(MyDateFormat.format(MyTimestamp)));
            MyGroup.appendChild(MyElement);
        }

        MyTimestamp = MyFagency.getA6endactive();
        if (MyTimestamp != null) {
            MyElement = MyDocument.createElement("fin");
            MyElement.appendChild(MyDocument.createTextNode(MyDateFormat.format(MyTimestamp)));
            MyGroup.appendChild(MyElement);
        }
    }
}
