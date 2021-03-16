package es.sacyl.gsa.inform.util;

import com.vaadin.flow.component.UI;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.ui.tablas.FrmCentro;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author JuanNieto
 */
public class Utilidades {

    private static final Logger LOGGER = LogManager.getLogger(Utilidades.class);

    public static final DateTimeFormatter formatterdd_mm_yyyy = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    public static final DateTimeFormatter formatteryyyymmdd = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter formatterdd_mm_yyyy_hh_mm = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");

    /**
     * Checks if is numero.
     *
     * @param cadena the cadena
     * @return true, if is numero
     */
    public static boolean isNumero(String cadena) {
        if (cadena == null || cadena.isEmpty()) {
            return false;
        } else {
            int i = 0;
            for (i = 0; i < cadena.length(); i++) {
                if (!Character.isDigit(cadena.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
    public static File blobTofile1(Blob blob, String pathname) {
        //    https://gist.github.com/sbandavaram/78bba6df5d892465ab7a3bff3ec7205c
        File file = null;
        FileOutputStream outputStream = null;
        try {
            InputStream is = null;

            is = blob.getBinaryStream();

            String str = convert(is);
            file = new File(pathname);
            outputStream = new FileOutputStream(file);
            outputStream.write(str.getBytes());
            outputStream.close();
            LOGGER.debug("File Generated: " + pathname);
            return file;
        } catch (SQLException ex) {

            java.util.logging.Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                outputStream.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return file;
    }
     */
    private static String convert(InputStream is) {
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result;
        String str = null;
        try {
            result = bis.read();

            while (result != -1) {
                buf.write((byte) result);
                result = bis.read();
            }
            str = buf.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static File bloBtoFile(Blob blob, String pathname) {
        File file = null;
        try {
            FileOutputStream outpu = null;
            file = new File(pathname);
            outpu = new FileOutputStream(file);

            InputStream inStream = blob.getBinaryStream();
            int size = (int) blob.length();
            byte[] buffer = new byte[size];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outpu.write(buffer, 0, length);
            }
            outpu.close();
            LOGGER.debug("Fichero imagen generado" + pathname);
        } catch (Exception ioe) {
            LOGGER.error(ioe);
        }
        return file;
    }

    public static File iStoFile(InputStream inputStream, String nombreFichero) {

        OutputStream outputStream = null;
        File fichero = null;
        try {
            // InputStream inputStream = new FileInputStream(nombreFichero);
            fichero = new File(nombreFichero);
            if (fichero.exists() && fichero.isFile()) {
                fichero.delete();
            }

            fichero = new File(nombreFichero);
            outputStream = new FileOutputStream(fichero);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Fichero no encontrado:" + nombreFichero, e);
        } catch (IOException e) {
            LOGGER.error("Fallo al leer o escribir" + nombreFichero, e);
        } finally {
            try {
                outputStream.close();

            } catch (java.io.IOException e) {
                e.printStackTrace();
                LOGGER.error("Fallo IO cerrando outputStream", e);
            }
        }
        return fichero;
    }

    public static File bytesToFile(byte[] bytes, String nombreAbsoluto) {
        File file = new File(nombreAbsoluto);
        try {
            FileOutputStream stream = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        try (FileOutputStream stream = new FileOutputStream("Hello world.txt")) {
            stream.write(bytes);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return file;
    }

    public static String getStackTrace(Exception e) {
        StringWriter sWriter = new StringWriter();
        PrintWriter pWriter = new PrintWriter(sWriter);
        e.printStackTrace(pWriter);
        return sWriter.toString();
    }

    public static int getHora(String unahora) {
        int valor;
        String cadena1;
        cadena1 = unahora.substring(0, 2);
        valor = Integer.parseInt(cadena1);
        return valor;
    }

    public static String getHoraHH_MM(Long hora) {
        String hhmmString = null;
        String hh = null;
        String mm = null;
        hhmmString = Long.toString(hora);
        if (hhmmString.length() == 4) {
            hh = hhmmString.substring(0, 2);
            mm = hhmmString.substring(2, 4);
        } else if (hhmmString.length() == 3) {
            hh = "0" + hhmmString.substring(0, 1);
            mm = hhmmString.substring(1, 3);
        } else if (hhmmString.length() == 2) {
            hh = "00";
            mm = hhmmString.substring(1, 1);
        } else if (hhmmString.length() == 1) {
            hh = "00";
            mm = "0" + hhmmString;
        }
        return hh + ":" + mm;
    }

    public static Integer getHoraInt(LocalDateTime fecha) {
        Integer unaHecha = null;
        int hh, mm, ss;
        if (fecha != null) {
            hh = fecha.getHour();
            mm = fecha.getMinute();
            ss = fecha.getSecond();
            unaHecha = new Integer(hh * 10000 + mm * 100 + ss);
        }
        return unaHecha;
    }

    public static LocalDateTime getFechaHoraLocalDateTime(Long fecha, Integer hora) {
        if (fecha == null || hora == null) {
            return null;
        }

        int y, m, d, h = 0, mi = 0;
        String fechaString = Long.toString(fecha);
        String horaString = Long.toString(hora);
        LocalDateTime fechaHoraDateTime = null;
        if (fechaString.length() != 8) {
            return null;
        }
        try {
            y = Integer.parseInt(fechaString.substring(0, 4));
            m = Integer.parseInt(fechaString.substring(4, 6));
            d = Integer.parseInt(fechaString.substring(6, 8));
            if (horaString.length() == 4) {
                h = Integer.parseInt(horaString.substring(0, 2));
                mi = Integer.parseInt(horaString.substring(2, 4));
            } else if (horaString.length() == 3) {
                h = Integer.parseInt(horaString.substring(0, 1));
                mi = Integer.parseInt(horaString.substring(1, 3));
            } else if (horaString.length() <= 2) {
                h = 0;
                mi = Integer.parseInt(horaString);
            }
            fechaHoraDateTime = LocalDateTime.of(y, m, d, h, mi);
        } catch (NumberFormatException e) {
            LOGGER.error("Error convirtiendo en fecha hora " + fecha + " " + hora);
        }
        return fechaHoraDateTime;
    }

    public static String getIPClaseC(String ip) {
        String punto = "\\.";
        String[] dir = ip.split(punto);
        if (dir.length > 3) {
            return dir[0] + "." + dir[1] + "." + dir[2] + ".";
        } else {
            return "";
        }
    }

    public static int binarioToDecimal(int binario) {
        return binarioToDecimal(new Long(binario));
    }

    public static int binarioToDecimal(long binario) {
        int decimal = 0;
        int digito;
        final long DIVISOR = 10;
        for (long i = binario, j = 0; i > 0; i /= DIVISOR, j++) {
            digito = (int) (i % DIVISOR);
            if (digito != 0 && digito != 1) {
                return -1;
            }
            decimal += digito * Math.pow(2, j);
        }
        return decimal;
    }

    public static String binarioToDecimalString(long binario) {
        return Integer.toString(binarioToDecimal(binario));
    }

    public static int binarioToDecimal(String binario) {

        return binarioToDecimal(Long.parseLong(binario));
    }

    public static String binarioToDecimalString(String binario) {

        return binarioToDecimalString(Long.parseLong(binario));
    }

    public static String integerToBiario(Integer numero) {
        //Declaramos el número decimal que deseamos pasar a binario.
        String binario = ""; //Variable aux para guardar la cadena de binarios.
        while (numero > 0) {
            //Utilización de if simplificado en el cual se verifica si el residuo de la división es 0 en caso de cumplirse se agrega 0 a la var aux.
            binario = numero % 2 == 0 ? "0" + binario : "1" + binario;
            numero = numero / 2; //Esta condición es la permite salir del bucle.
        }
        return binario;
        //System.err.print(binario+"\n"); //Imprimes el resultado almacenado en la variable "binario"
    }

    public static String stringToBinario(String numero) {
        //Declaramos el número decimal que deseamos pasar a binario.
        return integerToBiario(Integer.parseInt(numero));
    }

    public static Long getFechaActualLong() {
        return Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    public static String getFechaActualString() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static Integer getHoraActualInt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        String hora = LocalDateTime.now().format(formatter);
        return Integer.parseInt(hora);
    }

    public static LocalDate getFechaLocalDate(Long valor) {
        LocalDate date = null;
        try {
            if (!valor.equals(new Long(0))) {
                date = LocalDate.parse(Long.toString(valor), formatteryyyymmdd);
            }
        } catch (Exception ex) {
            LOGGER.error("Error convirtiendo fecha " + valor + " " + Utilidades.getStackTrace(ex));
        }
        return date;
    }

    public static String getFechadd_mm_yyyy(String cadena) {
        String texto = "";
        if (cadena != null) {
            if (cadena.length() == 8) {
                texto = cadena.substring(0, 4);
                texto = texto.concat("/" + cadena.substring(4, 6));
                texto = texto.concat("/" + cadena.substring(6, 8));
            }
        }
        return texto;

    }

    /**
     *
     * @param date
     * @return fecha en formato dd/mm/yyyy Por ´algún motivo que no se la
     * sentencia DateTimeFormatter.ofPattern("dd/mm/yyyy") del método anterior
     * no funciona el 01/01/2021 y la cambio por este método estático en todo el
     * programa
     */
    public static String getFechadd_mm_yyyy(LocalDate date) {

        String cadena = "";
        if (date != null) {

            if (date.getDayOfMonth() < 10) {
                cadena = "0";
            }
            cadena = cadena.concat(date.getDayOfMonth() + "/");
            if (date.getMonthValue() < 10) {
                cadena = cadena.concat("0");
            }
            cadena = cadena.concat(Integer.toString(date.getMonthValue()) + "/");

            cadena = cadena.concat(Integer.toString(date.getYear()));
        }
        return cadena;

    }

    /**
     * Gets the fecha local date.
     *
     * @param fecha the fecha
     * @return the fecha local date
     */
    public static LocalDate getFechaLocalDaten(Long fecha) {
        LocalDate date = null;
        try {
            if (fecha != null) {
                if (fecha > 19000000) {
                    String cadena = Long.toString(fecha);
                    int year = Integer.parseInt(cadena.substring(0, 4));
                    int month = Integer.parseInt(cadena.substring(4, 6));
                    if (month == 0) {
                        month = 1;
                    }

                    int day = Integer.parseInt(cadena.substring(6, 8));
                    if (day == 0) {
                        day = 1;
                    }
                    date = LocalDate.of(year, month, day);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error convirtiendo fecha " + fecha.toString());
        }
        return date;
    }

    public static LocalDateTime getFechaHoraLas15horas() {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
            String str = LocalDate.now() + " 15:00:00";
            LocalDateTime localDateTime = LocalDateTime.parse(str, dtf);
            return localDateTime;
        } catch (Exception e) {

        }
        return null;
        /*
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMMM-dd HH:mm:ss a")
                                        .withLocale(Locale.FRENCH);
            LocalDateTime date = LocalDateTime.parse("2019-mai-29 10:15:30 AM", formatter);
         */
    }

    /**
     *
     * @param fecha
     * @return
     */
    public static Long getFechaLong(LocalDate fecha) {
        Long unaFecha = null;
        int dd, mm, yyyy;
        if (fecha != null) {
            dd = fecha.getDayOfMonth();
            mm = fecha.getMonthValue();
            yyyy = fecha.getYear();
            unaFecha = new Long(yyyy * 10000 + mm * 100 + dd);
        }
        return unaFecha;
    }

    /**
     *
     * @param fecha
     * @return
     */
    public static Long getFechaLong(LocalDateTime fecha) {
        Long unaFecha = null;
        int dd, mm, yyyy;
        if (fecha != null) {
            dd = fecha.getDayOfMonth();
            mm = fecha.getMonthValue();
            yyyy = fecha.getYear();
            unaFecha = new Long(yyyy * 10000 + mm * 100 + dd);
        }
        return unaFecha;
    }

    public static LocalDate getFechaLocalDate(String cadena) {
        LocalDate date = null;
        if (cadena != null) {
            if (cadena.length() == 8) {
                int year = Integer.parseInt(cadena.substring(0, 4));
                int month = Integer.parseInt(cadena.substring(4, 6));
                int day = Integer.parseInt(cadena.substring(6, 8));
                date = LocalDate.of(year, month, day);
                // return date;
            }
        }
        return date;
    }

    public static LocalDateTime getFechaLocalDateTime(Long fecha) {
        LocalDateTime date = null;
        if (fecha != null && fecha != 0) {

            String cadena = Long.toString(fecha);
            /* Para 14 caracteres
                20200417101344
                yyyymmddmmssdd
                para 12 caracteres
                202004151415
                yyyymmddhhss
             */
            if (cadena.length() == 14 || cadena.length() == 12) {
                int year = Integer.parseInt(cadena.substring(0, 4));
                int month = Integer.parseInt(cadena.substring(4, 6));
                int day = Integer.parseInt(cadena.substring(6, 8));
                int hour = Integer.parseInt(cadena.substring(8, 10));
                int min = Integer.parseInt(cadena.substring(10, 12));
                date = LocalDateTime.of(year, month, day, hour, min);
            } else if (cadena.length() == 8) {
                /*
                20110412
                yyyymmdd
                 */
                int year = Integer.parseInt(cadena.substring(0, 4));
                int month = Integer.parseInt(cadena.substring(4, 6));
                int day = Integer.parseInt(cadena.substring(6, 8));
                date = LocalDateTime.of(year, month, day, 0, 0);
            }

        }
        return date;
    }

    /**
     *
     * @param pathAbsoluto
     *
     * Borra el fichero indicado
     */
    public static void borraFichero(String pathAbsoluto) {
        try {
            File file = new File(pathAbsoluto);
            if (file.isFile()) {
                file.delete();
            } else {
                LOGGER.error("Error borrando fichero. No encontrado".concat(pathAbsoluto));
            }
        } catch (Exception e) {
            LOGGER.error("Error borrando fichero. ".concat(pathAbsoluto));
        }
    }

    /**
     * Checks if is numeric.
     *
     * @param cadena the cadena
     * @return true, if is numeric
     */
    public static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isNumericDecimal(String cadena) {
        try {
            cadena.replaceAll(".", ",");
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     *
     * @param nombre nombre del fichero de imagen
     * @param ficheroBlob objto blob que lo contiene
     * @return
     *
     * ://www.tutorialspoint.com/pdfbox/pdfbox_extracting_image.htm
     */
    public static String getMinitauraDeUnPdf(String nombre, Blob ficheroBlob) {
        //
        String salida = null;
        try {
            String filename = Constantes.PDFPATHABSOLUTO + nombre;;
            Utilidades.bloBtoFile(ficheroBlob, filename);
            File file = new File(filename);
            salida = Constantes.PDFPATHABSOLUTO + nombre;
            PDDocument document = null;
            document = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImage(0);
            ImageIO.write(image, Constantes.MINIATURAEXTENSION, new File(salida));

            System.out.println("Image created" + salida);

            document.close();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FrmCentro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nombre;
    }

    public static String getExtensionFichero(String ficheroNombre) {
        int posPunto = ficheroNombre.lastIndexOf(".");
        String extension = "";
        if (posPunto != -1) {
            extension = ficheroNombre.substring(posPunto, ficheroNombre.length());
        }
        return extension;
    }

    public static ArrayList<DatoGenericoBean> getInformacionCliente(UI ui) {
        ArrayList<DatoGenericoBean> lista = new ArrayList<>();
        /*
        final com.vaadin.server.WebBrowser navegador = ui.getCurrent();

        DatoGenericoBean dato = new DatoGenericoBean();
        dato.setTipoDato("Navegador");
        dato.setValor(navegador.getBrowserApplication());
        lista.add(dato);

        dato = new DatoGenericoBean();
        dato.setTipoDato("Versión");
        dato.setValor(navegador.getBrowserVersion());
        lista.add(dato);

        dato = new DatoGenericoBean();
        dato.setTipoDato("Ventana");
        dato.setValor(navegador.getScreenHeight() + "x" + navegador.getScreenWidth());
        lista.add(dato);

        dato = new DatoGenericoBean();
        dato.setTipoDato("Locale");
        dato.setValor(navegador.getLocale().getDisplayName());
        lista.add(dato);

        dato = new DatoGenericoBean();
        dato.setTipoDato("Fecha");
        dato.setValor(navegador.getCurrentDate().toString());
         */
        return lista;
    }
}
