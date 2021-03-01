package es.sacyl.gsa.inform.reports.lopd;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.reports.MasterReport;
import es.sacyl.gsa.inform.reports.PdfEventoPagina;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class IncidenciaPdf extends MasterReport implements Serializable {

    private static final Logger logger = LogManager.getLogger(IncidenciaPdf.class);

    private final LopdIncidenciaBean incidencia;

    public IncidenciaPdf(LopdIncidenciaBean inciParamIncidencia) {
        incidencia = inciParamIncidencia;
        nombreDelFicheroPdf = "IncidenciaFicha_" + incidencia.getId() + ".pdf";
        nombrePdfAbsoluto = Constantes.PDFPATHABSOLUTO + nombreDelFicheroPdf;
        nombrePdfRelativo = Constantes.PDFPATHRELATIVO + nombreDelFicheroPdf;
        urlDelPdf = "http://localhost:8080" + nombrePdfRelativo;
        doCreaPdf();
    }

    @Override
    public void doCreaPdf() {

        try {
            /*   normal = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);

            bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);

            document = new Document(pdf, PageSize.A4).setTextAlignment(TextAlignment.LEFT);

            document.setMargins(25, 15, 5, 35);

            PdfFont times = null;
            try {
                times = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            } catch (IOException e) {
                e.printStackTrace();
            }
             */
            PdfEventoPagina evento = new PdfEventoPagina(document,
                    " Incidencia de seguridad de datos  " + incidencia.getTipo().getDescripcion());

            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, evento);

            Paragraph paragraph0 = new Paragraph();
            paragraph0.add("\n   Registros de incidencia de seguridad.").setFontSize(15);
            paragraph0.setTextAlignment(TextAlignment.CENTER);
            paragraph0.setFont(normal);
            document.add(paragraph0);

            Paragraph paragraph = new Paragraph();
            paragraph.add(
                    "\n  La Ley 41/2002, de 14 de noviembre, báisca Reguladora de la Autonómía del paciente de de los "
                    + " derechos y obligaciones en materia de información y documentación clínica incluye en su artículo 15.1"
                    + "que la historia clínica incorporará la información que se considere trancendental para el conocimiento"
                    + "y  veraz actualizado estado del paciente  ")
                    .setFontSize(12);
            paragraph.setTextAlignment(TextAlignment.JUSTIFIED);
            paragraph.setFont(normal);
            document.add(paragraph);

            Paragraph paragraph1 = new Paragraph();
            paragraph1.add(
                    "\n   Por otra parte, el Decreto 101/2005, de 22 de diciembre, por el que se regula la historia clínica, "
                    + " cita en su artículo 4.1 que en la historia clínica, deberá quedar constancia de toda la información sobre su"
                    + " proceso asistencial de modo que permita el conocimiento veraz y actualizado de su estado de aslud.")
                    .setFontSize(12);
            paragraph1.setTextAlignment(TextAlignment.JUSTIFIED);
            paragraph1.setFont(normal);
            document.add(paragraph1);

            Paragraph paragraph2 = new Paragraph();
            paragraph2.add(
                    "\n   En este sentido, la Ley Orgánica 3/2018 de 5 de diciembre, de Protección de Datos Personales, "
                    + " en su artículo 4 determina que los datos de carácter personal serán exactos y si fuese necesario deben ser"
                    + " actualizados.")
                    .setFontSize(12);
            paragraph2.setTextAlignment(TextAlignment.JUSTIFIED);
            paragraph2.setFont(normal);
            document.add(paragraph2);

            Float altura = new Float(30f);
            int sizeFont = 7;

            float[] anchos = {130f, 400f};
            Table tabla = new Table(anchos);

            // String[] tituloSrings = { "Nhc", "Apellidos y nombre", "Fecha I,", "Motivo",
            // "Fecha Fin ", "Motivo Baja." };
            tabla.setMarginTop(10);
            Cell cell = new Cell();
            cell.add(new Paragraph("Número").setFont(normal).setFontSize(11));
            tabla.addCell(cell);
            Cell cell1 = new Cell();
            cell1.add(new Paragraph(Long.toString(incidencia.getId())).setFont(normal).setFontSize(11));
            tabla.addCell(cell1);

            Cell cell2 = new Cell();
            cell2.add(new Paragraph("Fecha").setFont(normal).setFontSize(11));
            tabla.addCell(cell2);
            Cell cell3 = new Cell();
            DateTimeFormatter fechaFormato = DateTimeFormatter.ofPattern("dd/mm/YYYY hh:mm");
            cell3.add(new Paragraph(fechaFormato.format(incidencia.getFechaHora())).setFont(normal).setFontSize(11));
            tabla.addCell(cell3);

            Cell cell4 = new Cell();
            cell4.add(new Paragraph("Solicitante").setFont(normal).setFontSize(11));
            tabla.addCell(cell4);
            Cell cell5 = new Cell();
            cell5.add(new Paragraph(incidencia.getUsuarioRegistra().getDni() + " "
                    + incidencia.getUsuarioRegistra().getApellidosNombre()).setFont(normal).setFontSize(11));
            tabla.addCell(cell5);

            Cell cell6 = new Cell();
            cell6.add(new Paragraph("Descripción").setFont(normal).setFontSize(11));
            tabla.addCell(cell6);
            Cell cell7 = new Cell();
            if (incidencia.getDescripcionError() != null) {
                cell7.add(new Paragraph(incidencia.getDescripcionError()).setFont(normal).setFontSize(11));
            }

            tabla.addCell(cell7);

            Cell cell8 = new Cell();

            cell8.add(new Paragraph("Fecha resolución.").setFont(normal).setFontSize(11));

            tabla.addCell(cell8);
            Cell cell9 = new Cell();
            if (incidencia.getFechaSolucion() != null) {
                cell9.add(new Paragraph(incidencia.getFechaSolucionString()).setFont(normal).setFontSize(11));
            }
            tabla.addCell(cell9);

            Cell cell10 = new Cell();
            cell10.add(new Paragraph("Técnico").setFont(normal).setFontSize(11));
            tabla.addCell(cell10);
            Cell cell11 = new Cell();
            if (incidencia.getUsuCambio() != null) {
                cell11.add(new Paragraph(incidencia.getUsuCambio().getApellidosNombre()).setFont(normal).setFontSize(11));
            }
            tabla.addCell(cell11);

            Cell cell12 = new Cell();
            cell12.add(new Paragraph("Solución").setFont(normal).setFontSize(11));
            tabla.addCell(cell12);
            Cell cell13 = new Cell();
            if (incidencia.getDescripcionSolucion() != null) {
                cell13.add(new Paragraph(incidencia.getDescripcionSolucion()).setFont(normal).setFontSize(11));
            }
            tabla.addCell(cell13);

            document.add(tabla);

            float[] anchosf = {260f, 260f};
            Table tablaf = new Table(anchosf);
            Cell cell14 = new Cell();
            cell14.add(new Paragraph("Solicitante \n\n\n\n").setFont(normal).setFontSize(11));
            tablaf.addCell(cell14);

            Cell cell15 = new Cell();
            cell15.add(new Paragraph("Técnico \n\n\n\n").setFont(normal).setFontSize(11));
            tablaf.addCell(cell15);

            Cell cell16 = new Cell();
            if (incidencia.getUsuarioRegistra() != null) {
                cell16.add(
                        new Paragraph(incidencia.getUsuarioRegistra().getApellidosNombre()).setFont(normal).setFontSize(11));
            }
            tablaf.addCell(cell16);

            Cell cell17 = new Cell();
            if (incidencia.getUsuCambio() != null) {
                cell17.add(new Paragraph(incidencia.getUsuCambio().getApellidosNombre()).setFont(normal).setFontSize(11));
            }
            tablaf.addCell(cell17);
            document.add(tablaf);

        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            document.close();
        }
    }

}
