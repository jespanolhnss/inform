package es.sacyl.gsa.inform.util;

/**
 *
 * @author JuanNieto
 */
public class Constantes {

    /**
     * DIRECTORIOPDF: Nombre del directorio donde se guardan los PDF
     */
    public static final String PDFDIRECTORIO = "pdf";
    /**
     * PDFPATHRELATIVO: Ruta relativa para guardar los pdf.
     */
    public static final String PDFPATHRELATIVO
            = System.getProperty("file.separator") + PDFDIRECTORIO + System.getProperty("file.separator");
    /**
     * PDFPATHABSOLUTO: Ruta absoluta para guardar los pdf.
     */
    public static final String PDFPATHABSOLUTO = System.getProperty("catalina.home") + System.getProperty("file.separator") + PDFDIRECTORIO + System.getProperty("file.separator");
    /**
     * PDFURL: Url del fichero accesible desde el navegador
     */
    public static final String PDFURL = System.getProperty("file.separator") + PDFDIRECTORIO + System.getProperty("file.separator");

    /**
     * SESSION_USERNAME: Nombre del parámetro de la sesión del usuario
     */
    public static final String SESSION_USERNAME = "sesionusuario";

    public static final String MINIATURAEXTENSION = "jpeg";

    public static final String HTMLESPACIO1 = "&nbsp";
    public static final String HTMLESPACIO2 = HTMLESPACIO1 + HTMLESPACIO1;
    public static final String HTMLESPACIO3 = HTMLESPACIO1 + HTMLESPACIO1 + HTMLESPACIO1;
    public static final String HTMLESPACIO4 = HTMLESPACIO1 + HTMLESPACIO1 + HTMLESPACIO1 + HTMLESPACIO1;

    public Constantes() {

    }

    /*
    public String getPathAbsoluto() {
        String path = "";
        String sSistemaOperativo = System.getProperty("os.name");
        if (sSistemaOperativo.equals("Windows 10")) {
            path = System.getProperty("catalina.home")
                    + System.getProperty("file.separator") + "webapps" + System.getProperty("file.separator") + "pdf" + System.getProperty("file.separator");
        } else {
            path = "/Users/JuanNieto/NetBeansProjects/farmacia/src/main/webapp/";
        }
        return path;
    }
     */
}
