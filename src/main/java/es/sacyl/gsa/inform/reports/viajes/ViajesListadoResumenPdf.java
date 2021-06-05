package es.sacyl.gsa.inform.reports.viajes;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.bean.ViajeBean;
import es.sacyl.gsa.inform.bean.ViajeCentroBean;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.ViajesDao;
import es.sacyl.gsa.inform.reports.MasterReport;
import es.sacyl.gsa.inform.reports.PdfEventoPagina;
import es.sacyl.gsa.inform.util.Utilidades;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class ViajesListadoResumenPdf extends MasterReport {

    private static final Logger LOGGER = LogManager.getLogger(ViajesListadoResumenPdf.class);

    private LocalDate desde, hasta;
    public static final DateTimeFormatter formatterdd_mm_yyyy = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    public ViajesListadoResumenPdf(LocalDate desde, LocalDate hasta) {
        super();
        this.desde = desde;
        this.hasta = hasta;
        nombreDelFicheroPdf = "Viajes_" + desde + "_" + hasta + ".pdf";
        doActualizaNombreFicheros();
        /*
        nombrePdfAbsoluto = Constantes.PDFPATHABSOLUTO + nombreDelFicheroPdf;
        nombrePdfRelativo = Constantes.PDFPATHRELATIVO + nombreDelFicheroPdf;
        urlDelPdf = "http://localhost:8080" + nombrePdfRelativo;
         */
        document.setMargins(60, 25, 5, 50);
        LOGGER.debug("Generando fichero pdf  viajes " + nombreDelFicheroPdf);
        doCreaPdf();
    }

    /**
     *
     */
    @Override
    public void doCreaPdf() {
        try {
            this.setFontSize(8);
            this.getDocument().setTopMargin(100);
            String cabecera = " Listado de viajes ";
            if (desde != null) {
                cabecera = cabecera.concat(" desde " + formatterdd_mm_yyyy.format(desde));
            }
            if (hasta != null) {
                cabecera = cabecera.concat(" hasta " + formatterdd_mm_yyyy.format(hasta));
            }
            PdfEventoPagina evento = new PdfEventoPagina(document, cabecera);
            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, evento);

            ArrayList<ViajeBean> lista = new ViajesDao().getListaViajes(desde, hasta, null, null, ConexionDao.BBDD_ACTIVOSI);
            float[] anchos = {90f, 250f, 260f};
            Table tabla = new Table(anchos);
            for (ViajeBean viaje : lista) {
                tabla.addCell(new Cell((viaje.getListaCentros().size() * 2), 0).setBorder(new SolidBorder(GRISCLARO, 1)).add(new Paragraph(
                        viaje.getId().toString() + "\n" + viaje.getSalidaString()).setFont(normal).setFontSize(fontSize)
                ));
                //     tabla.addCell(new Cell().add(new Paragraph(viaje.getSalidaString().toString()  ).setFont(normal).setFontSize(fontSize)));
                String cadenaTecnicos = "";

                for (UsuarioBean tecnico : viaje.getListaTecnicos()) {
                    if (cadenaTecnicos.length() > 2) {
                        cadenaTecnicos = cadenaTecnicos.concat("\n");
                    }
                    cadenaTecnicos = cadenaTecnicos.concat(tecnico.getApellidosNombre());
                }
                int contador = 0;
                for (ViajeCentroBean centro : viaje.getListaCentros()) {

                    if (contador == 0) {
                        tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(new Paragraph(centro.getNombreCentro()).setFont(normal).setFontSize(fontSize)));
                        tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(new Paragraph(cadenaTecnicos).setFont(normal).setFontSize(fontSize)));
                    } else {
                        tabla.addCell(new Cell(0, 2).setBorder(new SolidBorder(GRISCLARO, 1)).add(new Paragraph(centro.getNombreCentro()).setFont(normal).setFontSize(fontSize)));
                    }

                    String cadena = "";
                    if (centro.getPreparacion() != null && !centro.getPreparacion().isEmpty()) {
                        cadena = "Preparación:" + centro.getPreparacion() + "\n";
                    }
                    if (centro.getActuacion() != null && !centro.getActuacion().isEmpty()) {
                        cadena = cadena.concat("Tareas Realizada:" + centro.getActuacion());
                    }
                    tabla.addCell(new Cell(0, 2).setBorder(new SolidBorder(GRISCLARO, 1)).add(new Paragraph(cadena).setFont(normal).setFontSize(fontSize)));

                    contador++;
                }

            }
            document.add(tabla);
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            document.close();
        }
        document.close();
    }

}
