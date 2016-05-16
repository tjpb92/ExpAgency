/*
 * This programme exports agency in xml format for a given customer.
 * @version May 2016
 * @author Thierry Baribaud
 */
package expagency;

import agency.Fagency;
import java.io.File;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Comment;

public class ExpAgency {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Fagency MyFagency;
        DocumentBuilderFactory MyFactory;
        DocumentBuilder MyBuilder;
        Document MyDocument;
        Element MyAgencies;
        Comment MyComment;
        Element Agency;
        Element A6num;
        Element A6unum;
        Element A6name;
        Element A6extname;
        Element A6abbname;
        Element A6address;
        Element A6address2;
        Element A6poscode;
        Element A6city;
        Element A6email;
        Element Phones;
        Element A6teloff;
        Element A6teldir;
        Element A6telfax;
        Element A6active;
        Element A6begactive;
        Element A6endactive;
        TimeZone MyTimeZone = TimeZone.getTimeZone("Europe/Paris");
        DateFormat MyDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        TransformerFactory MyTransformerFactory;
        Transformer MyTransformer;
        DOMSource MySource;
        StreamResult MyOutput;

        MyFagency = new Fagency();
        MyFagency.setA6num(1234);
        MyFagency.setA6unum(99999);
        MyFagency.setA6extname("terra incognita");
        MyFagency.setA6name("utopia");
        MyFagency.setA6abbname("UTOPIA");
        MyFagency.setA6email("utopia@gmail.com");
        MyFagency.setA6daddress("12, rue des rèves");
        MyFagency.setA6daddress2("bâtiment B");
        MyFagency.setA6dposcode("92400");
        MyFagency.setA6dcity("UTOPIA CITY");
        MyFagency.setA6teloff("01.01.01.01.01");
        MyFagency.setA6teldir("02.02.02.02.02");
        MyFagency.setA6telfax("03.03.03.03.03");
        MyFagency.setA6active(1);
        MyFagency.setA6begactive(new Timestamp(new java.util.Date().getTime()));
        MyFagency.setA6endactive(Timestamp.valueOf("2050-12-31 23:59:59.0"));
        System.out.println("MyFagency=" + MyFagency);

        try {
            MyFactory = DocumentBuilderFactory.newInstance();
            MyBuilder = MyFactory.newDocumentBuilder();
            MyDocument = MyBuilder.newDocument();

            MyAgencies = MyDocument.createElement("agences");
            MyDocument.appendChild(MyAgencies);

            MyComment = MyDocument.createComment(MyFagency.getA6name());
            MyAgencies.appendChild(MyComment);

            Agency = MyDocument.createElement("agence");
            MyAgencies.appendChild(Agency);

            // Agency ID
            A6num = MyDocument.createElement("id");
            Agency.appendChild(A6num);
            A6num.appendChild(MyDocument.createTextNode(String.valueOf(MyFagency.getA6num())));

            // Customer ID
            A6unum = MyDocument.createElement("client");
            Agency.appendChild(A6unum);
            A6unum.appendChild(MyDocument.createTextNode(String.valueOf(MyFagency.getA6unum())));

            // Agency name
            A6name = MyDocument.createElement("nom");
            Agency.appendChild(A6name);
            A6name.appendChild(MyDocument.createTextNode(MyFagency.getA6name()));

            // Agency abbreviated name
            A6abbname = MyDocument.createElement("codeAgence");
            Agency.appendChild(A6abbname);
            A6abbname.appendChild(MyDocument.createTextNode(MyFagency.getA6abbname()));

            // Agency external name
            A6extname = MyDocument.createElement("appellationClient");
            Agency.appendChild(A6extname);
            A6extname.appendChild(MyDocument.createTextNode(MyFagency.getA6extname()));

            // Agency address
            A6address = MyDocument.createElement("adresse");
            Agency.appendChild(A6address);
            A6address.appendChild(MyDocument.createTextNode(MyFagency.getA6daddress()));
            A6address2 = MyDocument.createElement("complement");
            Agency.appendChild(A6address2);
            A6address2.appendChild(MyDocument.createTextNode(MyFagency.getA6daddress2()));
            A6poscode = MyDocument.createElement("codePostal");
            Agency.appendChild(A6poscode);
            A6poscode.appendChild(MyDocument.createTextNode(MyFagency.getA6dposcode()));
            A6city = MyDocument.createElement("ville");
            Agency.appendChild(A6city);
            A6city.appendChild(MyDocument.createTextNode(MyFagency.getA6dcity()));

            // Agency email
            A6email = MyDocument.createElement("email");
            Agency.appendChild(A6email);
            A6email.appendChild(MyDocument.createTextNode(MyFagency.getA6email()));

            // Agency phones
            Phones = MyDocument.createElement("telephones");

            A6teloff = MyDocument.createElement("telephone");
            A6teloff.appendChild(MyDocument.createTextNode(MyFagency.getA6teloff()));
            A6teloff.setAttribute("type", "bureau");

            A6teldir = MyDocument.createElement("telephone");
            A6teldir.appendChild(MyDocument.createTextNode(MyFagency.getA6teldir()));
            A6teldir.setAttribute("type", "direct");

            A6telfax = MyDocument.createElement("telephone");
            A6telfax.appendChild(MyDocument.createTextNode(MyFagency.getA6telfax()));
            A6telfax.setAttribute("type", "fax");

            Phones.appendChild(A6teloff);
            Phones.appendChild(A6teldir);
            Phones.appendChild(A6telfax);
            Agency.appendChild(Phones);

            // Agency activity
            A6active = MyDocument.createElement("actif");
            Agency.appendChild(A6active);
//          A6active.appendChild(MyDocument.createTextNode(String.valueOf(MyFagency.getA6active())));
            A6active.appendChild(MyDocument.createTextNode((MyFagency.getA6active() == 1) ? "OUI" : "NON"));
            A6begactive = MyDocument.createElement("debut");
            Agency.appendChild(A6begactive);
            A6begactive.appendChild(MyDocument.createTextNode(MyDateFormat.format(MyFagency.getA6begactive())));
            A6endactive = MyDocument.createElement("fin");
            Agency.appendChild(A6endactive);
            A6endactive.appendChild(MyDocument.createTextNode(MyDateFormat.format(MyFagency.getA6endactive())));

            MyTransformerFactory = TransformerFactory.newInstance();
            MyTransformer = MyTransformerFactory.newTransformer();

            MySource = new DOMSource(MyDocument);
//          MyOutput = new StreamResult(new File("agence_" + MyFagency.getA6unum() + ".xml"));
            MyOutput = new StreamResult(new File("agences.xml"));
//          MyOutput = new StreamResult(System.out);

            // Prologue
            MyTransformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            MyTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            MyTransformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
//        Marche mieux, va savoir pourquoi ???          
//          MyDocument.setXmlStandalone(true);

            // Formatting results
            MyTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            MyTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            // Output
            MyTransformer.transform(MySource, MyOutput);
        } catch (ParserConfigurationException MyException) {
            Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, MyException);
            System.out.println("Problem creating XML document " + MyException);
        } catch (TransformerConfigurationException MyException) {
            Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, MyException);
            System.out.println("Problem configuring XML document " + MyException);
        } catch (TransformerException MyException) {
            Logger.getLogger(ExpAgency.class.getName()).log(Level.SEVERE, null, MyException);
            System.out.println("Problem writing XML document " + MyException);
        }
    }

}
