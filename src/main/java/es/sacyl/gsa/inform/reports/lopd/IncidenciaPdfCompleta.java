package es.sacyl.gsa.inform.reports.lopd;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.bean.LopdNotaBean;
import es.sacyl.gsa.inform.dao.LopdNotaDao;
import es.sacyl.gsa.inform.reports.MasterReport;
import es.sacyl.gsa.inform.reports.PdfEventoPagina;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class IncidenciaPdfCompleta extends MasterReport {

    private static final Logger logger = LogManager.getLogger(IncidenciaPdfCompleta.class);

    private final LopdIncidenciaBean incidencia;

    public IncidenciaPdfCompleta(LopdIncidenciaBean inciParamIncidencia) {
        super();
        incidencia = inciParamIncidencia;
        nombreDelFicheroPdf = "IncidenciaCompleta_" + incidencia.getId() + ".pdf";
        nombrePdfAbsoluto = Constantes.PDFPATHABSOLUTO + nombreDelFicheroPdf;
        nombrePdfRelativo = Constantes.PDFPATHRELATIVO + nombreDelFicheroPdf;
        urlDelPdf = "http://localhost:8080" + nombrePdfRelativo;
        doCreaPdf();
    }

    @Override
    public void doCreaPdf() {

        try {
            ArrayList<LopdNotaBean> listaNotas = new LopdNotaDao().getNostasIncidencia(incidencia);
            PdfEventoPagina evento = new PdfEventoPagina(document,
                    " Informe completo sobre la Incidencia de seguridad " + incidencia.getTipo().getDescripcion());

            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, evento);

            Paragraph paragraph0 = new Paragraph();
            paragraph0.add("\n Informe completo sobre la Incidencia de seguridad").setFontSize(15);
            paragraph0.setTextAlignment(TextAlignment.CENTER);
            paragraph0.setFont(normal);
            document.add(paragraph0);

            float[] anchos = {90f, 450f};
            Table tabla = new Table(anchos);

            tabla.setMarginTop(10);
            Cell cell = new Cell();
            cell.add(new Paragraph("Número").setFont(normal).setFontSize(11));
            tabla.addCell(cell);
            Cell cell0 = new Cell();
            cell0.add(new Paragraph(Long.toString(incidencia.getId())).setFont(normal).setFontSize(11));
            tabla.addCell(cell0);

            tabla.addCell(new Cell().add(new Paragraph("Tipo")).setFont(normal).setFontSize(11));
            Cell cell1 = new Cell();
            cell1.add(new Paragraph(incidencia.getTipoDescripcion()).setFont(normal).setFontSize(11));
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
            cell5.add(
                    new Paragraph(incidencia.getUsuarioRegistra().getApellidosNombre()).setFont(normal).setFontSize(11));
            tabla.addCell(cell5);

            Cell cell6 = new Cell();
            cell6.add(new Paragraph("Descripción").setFont(normal).setFontSize(11));
            tabla.addCell(cell6);
            Cell cell7 = new Cell();
            if (incidencia.getDescripcionError() != null) {
                cell7.add(new Paragraph(incidencia.getDescripcionError()).setFont(normal).setFontSize(11));
            } else {
                cell6.add(new Paragraph());
            }

            tabla.addCell(cell7);

            Cell cell8 = new Cell();
            cell8.add(new Paragraph("Fecha resolución.").setFont(normal).setFontSize(11));
            tabla.addCell(cell8);
            Cell cell9 = new Cell();
            if (incidencia.getFechaSolucion() != null) {
                cell9.add(new Paragraph(incidencia.getFechaSolucionString()).setFont(normal).setFontSize(11));
            } else {
                cell9.add(new Paragraph());
            }

            tabla.addCell(cell9);

            Cell cell10 = new Cell();
            cell10.add(new Paragraph("Técnico").setFont(normal).setFontSize(11));
            tabla.addCell(cell10);
            Cell cell11 = new Cell();
            if (incidencia.getUsuCambio() != null) {
                cell11.add(
                        new Paragraph(incidencia.getUsuCambio().getApellidosNombre()).setFont(normal).setFontSize(11));
            } else {
                cell11.add(new Paragraph());
            }
            tabla.addCell(cell11);

            Cell cell12 = new Cell();
            cell12.add(new Paragraph("Solución").setFont(normal).setFontSize(11));
            tabla.addCell(cell12);
            Cell cell13 = new Cell();
            if (incidencia.getDescripcionSolucion() != null) {
                cell13.add(new Paragraph(incidencia.getDescripcionSolucion()).setFont(normal).setFontSize(11));
            } else {
                cell13.add(new Paragraph());
            }
            tabla.addCell(cell13);
            document.add(tabla);

            if (listaNotas.size() > 0) {
                float[] anchoTabla = {50f, 120f, 460f};
                Table tablaNotas = new Table(anchoTabla);
                tablaNotas.addCell(new Cell().add(new Paragraph("Fecha")).setFont(normal).setFontSize(9));
                tablaNotas.addCell(new Cell().add(new Paragraph("Usuario")).setFont(normal).setFontSize(9));
                tablaNotas.addCell(new Cell().add(new Paragraph("Texto de la nota")).setFont(normal).setFontSize(9));

                for (LopdNotaBean nota : listaNotas) {
                    tablaNotas.addCell(
                            new Cell().add(new Paragraph(nota.getFechaHoraFormato())).setFont(normal).setFontSize(9));
                    tablaNotas.addCell(new Cell().add(new Paragraph(nota.getUsucambio().getApellidosNombre()))
                            .setFont(normal).setFontSize(9));
                    tablaNotas.addCell(
                            new Cell().add(new Paragraph(nota.getDescripcion())).setFont(normal).setFontSize(9));
                }
                document.add(tablaNotas);
            }

            Paragraph paragraph1 = new Paragraph();
            paragraph1.add("\n\n\n fecha impresión:" + fechaFormato.format(LocalDateTime.now())).setFontSize(8);
            paragraph1.setTextAlignment(TextAlignment.LEFT);
            paragraph1.setFont(normal);
            document.add(paragraph1);

        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            document.close();
        }
    }

}
