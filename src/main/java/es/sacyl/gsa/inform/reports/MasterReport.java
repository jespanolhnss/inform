package es.sacyl.gsa.inform.reports;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.property.TextAlignment;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author juannietopajares
 */
public abstract class MasterReport {

    protected final ByteArrayOutputStream out = new ByteArrayOutputStream();

    protected final PdfWriter writer = new PdfWriter(out);

    protected PdfDocument pdf = new PdfDocument(writer);

    protected Document document = new Document(pdf);

    protected final DateTimeFormatter fechadma = DateTimeFormatter.ofPattern("dd/MM/YYYY");

    protected PdfFont normal;

    protected PdfFont bold;

    protected int fontSize = 13;

    protected int altoFila = 12;

    protected File file;
    protected String nombreDelFicheroPdf = null;
    protected String nombrePdfAbsoluto = null;

    protected String nombrePdfRelativo = null;

    protected String urlDelPdf = null;

    public MasterReport() {

        document = new Document(pdf, PageSize.A4).setTextAlignment(TextAlignment.LEFT);

        document.setMargins(25, 15, 5, 35);

        try {
            normal = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);

            bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void doCreaPdf();

    public String getNombrePdfAbsoluto() {
        return nombrePdfAbsoluto;
    }

    public void setNombrePdfAbsoluto(String nombrePdfAbsoluto) {
        this.nombrePdfAbsoluto = nombrePdfAbsoluto;
    }

    public String getNombrePdfRelativo() {
        return nombrePdfRelativo;
    }

    public void setNombrePdfRelativo(String nombrePdfRelativo) {
        this.nombrePdfRelativo = nombrePdfRelativo;
    }

    public String getUrlDelPdf() {
        return urlDelPdf;
    }

    public void setUrlDelPdf(String urlDelPdf) {
        this.urlDelPdf = urlDelPdf;
    }

    public InputStream getStream() {
        // Here we return the pdf contents as a byte-array
        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayOutputStream getOut() {
        return out;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void doCreaFicheroPdf() {
        Utilidades.iStoFile(getStream(), nombrePdfAbsoluto);
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public PdfFont getNormal() {
        return normal;
    }

    public void setNormal(PdfFont normal) {
        this.normal = normal;
    }

    public PdfFont getBold() {
        return bold;
    }

    public void setBold(PdfFont bold) {
        this.bold = bold;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getAltoFila() {
        return altoFila;
    }

    public void setAltoFila(int altoFila) {
        this.altoFila = altoFila;
    }

    public String getNombreDelFicheroPdf() {
        return nombreDelFicheroPdf;
    }

    public void setNombreDelFicheroPdf(String nombreDelFicheroPdf) {
        this.nombreDelFicheroPdf = nombreDelFicheroPdf;
    }

    public PdfDocument getPdf() {
        return pdf;
    }

    public void setPdf(PdfDocument pdf) {
        this.pdf = pdf;
    }

}
