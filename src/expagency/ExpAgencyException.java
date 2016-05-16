package expagency;

/**
 * Classe qui définit une exception lancée en cas d'erreur lors de
 * l'instanciation de la classe ImpAgency.
 *
 * @version Mai 2016.
 * @author Thierry Baribaud.
 */
class ExpAgencyException extends Exception {

    private final static String ERRMSG
            = "Problème lors de l'instanciation de ExpAgency";

    public ExpAgencyException() {
        System.out.println(ERRMSG);
    }

    public ExpAgencyException(String ErrMsg) {
        System.out.println(ERRMSG + " : " + ErrMsg);
    }
}
