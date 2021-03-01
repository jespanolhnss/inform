package es.sacyl.gsa.inform.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class Pdf2JpegConverter {

    private String tmpFilePath = "/tmp/tmp.pdf";

    public ByteArrayOutputStream[] converter2jpeg(ByteArrayOutputStream baos, float DPI) throws Exception {

        System.out.println("Needs to convert pdf to jpeg...");

        // Write pdf to file
        try (OutputStream outputStream = new FileOutputStream(this.tmpFilePath)) {
            baos.writeTo(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        // return format
        ByteArrayOutputStream[] imageBaos;

        // Read pdf file from tmp file
        try (final PDDocument document = PDDocument.load(new File(this.tmpFilePath))) {

            // read file from local
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // set up page and array length
            int pageCount = document.getNumberOfPages();

            imageBaos = new ByteArrayOutputStream[pageCount];

            for (int page = 0; page < pageCount; page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, DPI, ImageType.RGB);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(image, "jpeg", stream);
                imageBaos[page] = stream;
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return imageBaos;
    }
}
