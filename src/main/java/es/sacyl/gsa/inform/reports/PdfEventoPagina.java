package es.sacyl.gsa.inform.reports;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author david
 */
public class PdfEventoPagina implements IEventHandler {

    private final Document documento;
    private final String cabeceraString;

    public PdfEventoPagina(Document doc, String cabecera) {
        documento = doc;
        cabeceraString = cabecera;
    }

    /**
     * Crea el rectangulo donde pondremos el encabezado
     *
     * @param docEvent Evento de documento
     * @return Area donde colocaremos el encabezado
     */
    private Rectangle crearRectanguloEncabezado(PdfDocumentEvent docEvent) {

        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();

        float xEncabezado = pdfDoc.getDefaultPageSize().getX() + documento.getLeftMargin();
        float yEncabezado = pdfDoc.getDefaultPageSize().getTop() - documento.getTopMargin();
        float anchoEncabezado = page.getPageSize().getWidth() - 72;
        float altoEncabezado = 50F;

        Rectangle rectanguloEncabezado = new Rectangle(xEncabezado, yEncabezado, anchoEncabezado, altoEncabezado);

        return rectanguloEncabezado;
    }

    /**
     * Crea el rectangulo donde pondremos el pie de pagina
     *
     * @param docEvent Evento del documento
     * @return Area donde colocaremos el pie de pagina
     */
    private Rectangle crearRectanguloPie(PdfDocumentEvent docEvent) {
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();

        float xPie = pdfDoc.getDefaultPageSize().getX() + documento.getLeftMargin();
        float yPie = pdfDoc.getDefaultPageSize().getBottom();
        float anchoPie = page.getPageSize().getWidth() - 72;
        float altoPie = 50F;

        Rectangle rectanguloPie = new Rectangle(xPie, yPie, anchoPie, altoPie);

        return rectanguloPie;
    }

    /**
     * Crea la tabla que contendra el mensaje del encabezado
     *
     * @param mensaje Mensaje que desplegaremos
     * @return Tabla con el mensaje de encabezado
     */
    private Table crearTablaEncabezado(String mensaje) {
        float[] anchos = {1F};
        Table tablaEncabezado = new Table(anchos);
        tablaEncabezado.setWidth(527F);

        tablaEncabezado.addCell(new Cell().setHorizontalAlignment(HorizontalAlignment.CENTER)
                .add(new Paragraph(mensaje).setHorizontalAlignment(HorizontalAlignment.CENTER).setFontSize(14)));

        return tablaEncabezado;
    }

    /**
     * Crea la tabla de pie de pagina, con el numero de pagina
     *
     * @param docEvent Evento del documento
     * @return Pie de pagina con el numero de pagina
     */
    private Table crearTablaPie(PdfDocumentEvent docEvent) {
        PdfPage page = docEvent.getPage();
        float[] anchos = {1F};
        Table tablaPie = new Table(anchos);
        tablaPie.setWidth(527F);

        PdfFont times = null;
        try {
            times = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Paragraph textopieParagraph = new Paragraph(
                "Complejo Asistencial de Ávila. Avd. Juan Carlos Primero s/n. Ávila 05004. Tf: 920358000")
                .setFontSize(7);

        textopieParagraph.setTextAlignment(TextAlignment.CENTER);// textopieParagraph.setFont("size=8");
        textopieParagraph.setFont(times);
        Cell cell = new Cell();
        cell.add(textopieParagraph);
        tablaPie.addCell(cell);

        Integer pageNum = docEvent.getDocument().getPageNumber(page);
        Paragraph nParagraph = new Paragraph(" -" + pageNum + "-").setFontSize(7);
        nParagraph.setFont(times);
        nParagraph.setTextAlignment(TextAlignment.CENTER);
        Cell cell1 = new Cell();
        cell1.add(nParagraph);
        tablaPie.addCell(cell1);

        return tablaPie;
    }

    /**
     * Manejador del evento de cambio de pagina, agrega el encabezado y pie de
     * pagina
     *
     * @param event Evento de pagina
     */
    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

        Table tablaEncabezado = this.crearTablaEncabezado(cabeceraString);

        File imageFile = new File(
                VaadinService.getCurrent().getContextRootRelativePath(VaadinRequest.getCurrent()) + "/images/logosacyl.jpg");

        Image image = null;
        /*
        try {
            image = new Image(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageData imageData = null;
        try {
            imageData = ImageDataFactory.create(image, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
 /*
        Image pdfImg = new Image(imageData);
        pdfImg.setHeight(50);
        pdfImg.setWidth(100);
         */
        Rectangle rectanguloEncabezado = this.crearRectanguloEncabezado(docEvent);
        Canvas canvasEncabezado = new Canvas(canvas, pdfDoc, rectanguloEncabezado);
        /* canvasEncabezado.add(pdfImg);
         */
        canvasEncabezado.add(tablaEncabezado);

        PdfFont times = null;
        try {
            times = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Table tablaNumeracion = this.crearTablaPie(docEvent);
        Rectangle rectanguloPie = this.crearRectanguloPie(docEvent);
        Canvas canvasPie = new Canvas(canvas, pdfDoc, rectanguloPie);
        canvasPie.add(tablaNumeracion);

    }
}
