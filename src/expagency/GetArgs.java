package expagency;

import java.util.Date;

/**
 * Cette classe sert à vérifier et à récupérer les arguments passés en ligne de
 * commande à un programme.
 *
 * @version Juin 2016
 * @author Thierry Baribaud
 */
public class GetArgs {

    /**
     * SourceServer : prod pour le serveur de production, dev pour le serveur de
     * développement. Valeur par défaut : dev.
     */
    private String SourceServer = "dev";

    /**
     * Unum : Référence du client. Valeur par défaut : doit être spécifié en
     * ligne de commande.
     */
    private int unum = 0;

    /**
     * FileOut : fichier qui recevra les résultats de l'export. Valeur par
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
    public String getSourceServer() {
        return (SourceServer);
    }

    /**
     * @return FileOut : le nom du fichier où envoyer les résultats.
     */
    public String getFileOut() {
        return (FileOut);
    }

    /**
     * @return Unum : la référence du client.
     */
    public int getUnum() {
        return (unum);
    }

    /**
     * @return debugMode : le mode de fonctionnement debug.
     */
    public boolean getDebugMode() {
        return (debugMode);
    }

    /**
     * @return testMode : le mode de fonctionnement test.
     */
    public boolean getTestMode() {
        return (testMode);
    }

    /**
     * @param SourceServer : définit le serveur source.
     */
    public void setSourceServer(String SourceServer) {
        this.SourceServer = SourceServer;
    }

    /**
     * @param FileOut : définit le fichier où envoyer les résultats.
     */
    public void setFileOut(String FileOut) {
        this.FileOut = FileOut;
    }

    /**
     * @param unum : définit la référence client.
     */
    public void setUnum(int unum) {
        this.unum = unum;
    }

    /**
     * @param debugMode : fonctionnement du programme en mode debug
     * (true/false).
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * @param testMode : fonctionnement du programme en mode test (true/false).
     */
    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    /**
     * <p>
     * Les arguments en ligne de commande permettent de changer le mode de
     * fonctionnement.</p><ul>
     * <li>-dbserver : référence à la base de donnée, par défaut fait référence
     * à la base de développement, cf. fichier de paramètres
     * <i>myDatabases.prop</i> (optionnel)</li>
     * <li>-u unum : identifiant du service d'urgence (obligatoire).</li>
     * <li>-o fichier : fichier vers lequel exporter les données de l'agence,
     * nom par défaut <i>agences.xml</i>(optionnel).</li>
     * <li>-d : le programme fonctionne en mode débug le rendant plus verbeux,
     * désactivé par défaut (optionnel).</li>
     * <li>-t : le programme fonctionne en mode de test, les transactions en
     * base de données ne sont pas exécutées, désactivé par défaut
     * (optionnel).</li>
     * </ul>
     *
     * @param Args arguments de la ligne de commande.
     * @throws GetArgsException erreur sur les paramètres.
     */
    public GetArgs(String Args[]) throws GetArgsException {

        int i;
        int n;
        int ip1;
        Date MyDate;

        // Demande une analyse d'une date valide
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
                        throw new GetArgsException("Mauvaise source de données : " + Args[ip1]);
                    }
                    i = ip1;
                } else {
                    throw new GetArgsException("Base de données non définie");
                }
            } else if (Args[i].equals("-u")) {
                if (ip1 < n) {
                    try {
                        setUnum(Integer.parseInt(Args[ip1]));
                        i = ip1;
                    } catch (Exception MyException) {
                        throw new GetArgsException("La référence client doit être numérique : " + Args[ip1]);
                    }
                } else {
                    throw new GetArgsException("Référence client non définie");
                }
            } else if (Args[i].equals("-o")) {
                if (ip1 < n) {
                    setFileOut(Args[ip1]);
                    i = ip1;
                } else {
                    throw new GetArgsException("Nom de fichier non défini");
                }
            } else if (Args[i].equals("-d")) {
                setDebugMode(true);
            } else if (Args[i].equals("-t")) {
                setTestMode(true);
            } else {
                throw new GetArgsException("Mauvais argument : " + Args[i]);
            }
            i++;
        }
    }

    /**
     * Affiche le mode d'utilisation du programme.
     */
    public static void usage() {
        System.out.println("Usage : java ExpCalls -dbserver prod -u unum "
                + " [-o fichier.xml] [-d] [-t]");
    }

    /**
     * Affiche le contenu de GetArgs.
     *
     * @return retourne le contenu de GetArgs.
     */
    @Override
    public String toString() {
        return this.getClass().getName()
                + " : {dbServer=" + SourceServer
                + ", unum=" + unum
                + ", fichier=" + FileOut
                + ", debugMode=" + debugMode
                + ", testMode=" + testMode
                + "}";
    }
}
