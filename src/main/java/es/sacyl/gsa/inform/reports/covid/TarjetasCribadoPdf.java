/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.reports.covid;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import es.sacyl.gsa.inform.reports.MasterReport;
import es.sacyl.gsa.inform.reports.lopd.IncidenciaPdf;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class TarjetasCribadoPdf extends MasterReport {

    private static final Logger LOGGER = LogManager.getLogger(IncidenciaPdf.class);

    private final Integer desde;
    private final Integer hasta;

    public TarjetasCribadoPdf(Integer desde, Integer hasta) {
        super();
        this.desde = desde;
        this.hasta = hasta;
        nombreDelFicheroPdf = "Tarjetas_" + desde + "_" + hasta + ".pdf";
        nombrePdfAbsoluto = Constantes.PDFPATHABSOLUTO + nombreDelFicheroPdf;
        nombrePdfRelativo = Constantes.PDFPATHRELATIVO + nombreDelFicheroPdf;
        urlDelPdf = "http://localhost:8080" + nombrePdfRelativo;
        document.setMargins(60, 25, 5, 50);
        LOGGER.debug("Generando fichero pdf  tarjetas " + nombreDelFicheroPdf);
        doCreaPdf();
    }

    @Override
    public void doCreaPdf() {
        try {
            float[] anchos = {350f, 350f};
            float[] anchos4 = {100f, 264f, 100f, 250f};

            String imageFile = System.getProperty("catalina.home")
                    + System.getProperty("file.separator")
                    + "webapps"
                    + System.getProperty("file.separator")
                    + "inform"
                    + System.getProperty("file.separator")
                    + "WEB-INF"
                    + System.getProperty("file.separator")
                    + "icons"
                    + System.getProperty("file.separator")
                    + "logosacyl200x100.png";

            imageFile = "C:\\Users\\06551256M\\Documents\\NetBeansProjects\\inform\\src\\main\\webapp\\icons\\logosacyl100x50.png";

            Image image = null;
            try {
                ImageData data = ImageDataFactory.create(imageFile);
                image = new Image(data);
            } catch (Exception e) {
                LOGGER.error(Utilidades.getStackTrace(e));
            }

            for (int i = desde; i <= hasta; i++) {
                Table tabla = new Table(anchos);
                tabla.addCell(new Cell().add(new Paragraph("Nombre y Apellidos \n \n\n\n\n\n").setFont(normal).setFontSize(fontSize)));
                tabla.addCell(new Cell(3, 3).add(new Paragraph("Espacio para colocar el cassette").setFont(normal).setFontSize(fontSize)));
                tabla.addCell(new Cell().add(new Paragraph("DNI \n\n\n").setFont(normal).setFontSize(fontSize)));
                tabla.addCell(new Cell().add(new Paragraph("Teléfono \n\n\n").setFont(normal).setFontSize(fontSize)));

                Paragraph p1 = new Paragraph("Nº CRIBADO ").setFont(normal).setFontSize(fontSize);
                Text text = new Text(Integer.toString(i)).setFont(bold).setFontSize(15);
                p1.add(text);
                Cell cell = new Cell().add(p1).setTextAlignment(TextAlignment.CENTER).setHorizontalAlignment(HorizontalAlignment.CENTER);
                cell.setBorderBottom(Border.NO_BORDER);
                tabla.addCell(cell).setHorizontalAlignment(HorizontalAlignment.CENTER);

                cell = new Cell().add(new Paragraph(" RESULTADO TEST \n ").setFont(normal).setFontSize(fontSize).setTextAlignment(TextAlignment.LEFT));
                cell.setBorderBottom(Border.NO_BORDER);
                tabla.addCell(cell);
                document.add(tabla);
                // Artesanía tecnológica ya que no soy capaz de centrar una imagen en un celda
                tabla = new Table(anchos4);
                cell = new Cell();
                cell.setBorderRight(Border.NO_BORDER);
                cell.setBorderTop(Border.NO_BORDER);
                tabla.addCell(cell);
                cell = new Cell();
                cell.setBorderLeft(Border.NO_BORDER);
                cell.setBorderTop(Border.NO_BORDER);
                cell.add(createBarcodeImg(Integer.toString(i), getPdf()));
                tabla.addCell(cell);
                cell = new Cell();
                cell.setBorderRight(Border.NO_BORDER);
                cell.setBorderTop(Border.NO_BORDER);
                tabla.addCell(cell);
                cell = new Cell();
                cell.setBorderTop(Border.NO_BORDER);
                cell.setBorderLeft(Border.NO_BORDER);
                if (image != null) {
                    cell.add(image);
                } else {
                    cell.add(new Paragraph("\n"));
                }

                tabla.addCell(cell);
                document.add(tabla);

                document.add(new Paragraph("\n \n \n"));
                if (i % 2 == 0) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }
            }
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            document.close();
        }
        document.close();
    }

    private static Cell createBarcode(String code, PdfDocument pdfDoc) {
        //   BarcodeEAN barcode = new BarcodeEAN(pdfDoc);
        Barcode128 barcode = new Barcode128(pdfDoc);
        // barcode.setCodeType(BarcodeEAN.EAN13);
        barcode.setCode(code);
        //    PdfFormXObject barcodeObject = barcode.createFormXObject(pdfDoc);
        PdfFormXObject barcodeObject = barcode.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdfDoc);

        Cell cell = new Cell().add(new Image(barcodeObject));
        cell.setPaddingTop(10);
        cell.setPaddingRight(10);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(10);
        return cell;
    }

    private Image createBarcodeImg(String code, PdfDocument pdfDoc) {
        Barcode128 barcode = new Barcode128(pdfDoc);
        barcode.setCode(code);
        PdfFormXObject barcodeObject = barcode.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdfDoc);
        return new Image(barcodeObject);
    }
}
