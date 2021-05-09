package es.sacyl.gsa.inform.bean;

/**
 *
 * @author 06551256M
 */
public class FicheroNombre {

    private String nombreExtension;

    public FicheroNombre(String nombreExtension) {
        this.nombreExtension = nombreExtension;
    }

    public String getNombreSinExtension() {
        if (this.nombreExtension != null && this.nombreExtension.length() > 4) {
            return this.nombreExtension.substring(0, this.nombreExtension.length() - 4);
        } else {
            return "";
        }
    }

    public String getExtension() {
        String extension = "";
        int posPunto = this.nombreExtension.lastIndexOf(".");
        if (posPunto != -1) {
            extension = this.nombreExtension.substring(posPunto, this.nombreExtension.length());
        }
        return extension;
    }
}
