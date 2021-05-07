package es.sacyl.gsa.inform.reports.indicadores;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import es.sacyl.gsa.inform.bean.DWIndicadorValorAno;
import es.sacyl.gsa.inform.dao.DWQuerysDao;
import es.sacyl.gsa.inform.reports.MasterReport;
import es.sacyl.gsa.inform.reports.PdfEventoPagina;
import es.sacyl.gsa.inform.util.Utilidades;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class ServicioEvolucionAnoPdf extends MasterReport {

    private static final Logger LOGGER = LogManager.getLogger(ServicioEvolucionAnoPdf.class);

    private final String area;
    private final Integer ano;
    private final String servicio;

    public static ArrayList<String> CABECERAS = new ArrayList<String>() {
        {
            add("HOSPITALIZACION");
            add("Ener");
            add("Febr");
            add("Marz");
            add("Abri");
            add("Mayo");
            add("Juni");
            add("Juli");
            add("Agos");
            add("Sept");
            add("Octu");
            add("Novi");
            add("Dicc");
            add("TOTAL");

        }
    };

    private DeviceRgb GRISCLARO = new DeviceRgb(239, 238, 238);
    private DeviceRgb BLANCO = new DeviceRgb(254, 254, 254);

    public ServicioEvolucionAnoPdf(Integer anoparam, String servicioParm) {
        this.ano = anoparam;
        this.area = "HOSPITALIZACION";
        this.servicio = servicioParm;
        nombreDelFicheroPdf = "indicadoresservicio_" + servicio + "_" + ano + ".pdf";
        this.doActualizaNombreFicheros();
        doCreaPdf();
    }

    @Override
    public void doCreaPdf() {
        Paragraph parrafo;
        try {
            this.setFontSize(8);
            this.getDocument().setTopMargin(75);
            PdfEventoPagina evento = new PdfEventoPagina(document,
                    " Evlución anual de indicadores del servicio de  " + servicio + " Año  " + ano.toString());
            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, evento);

            ArrayList<DWIndicadorValorAno> lista = new DWQuerysDao().getListaHospitalizacionAnoServicio(ano, servicio);
            float[] anchos = {140f, 43f, 43f, 43f, 43f, 43f, 43f, 43f, 43f, 43f, 43f, 43f, 43f, 43f};
            Table tabla = new Table(anchos).setHorizontalAlignment(HorizontalAlignment.RIGHT);
            for (String cabecaera : CABECERAS) {
                parrafo = new Paragraph(cabecaera).setHorizontalAlignment(HorizontalAlignment.RIGHT).setFontSize(this.getFontSize());
                tabla.addCell(new Cell().setBorder(new SolidBorder(BLANCO, 1)).setBackgroundColor(GRISCLARO).add(parrafo).setTextAlignment(TextAlignment.CENTER));
            }
            for (DWIndicadorValorAno indicador : lista) {
                tabla.addCell(new Cell().setBorder(new SolidBorder(BLANCO, 1)).setBackgroundColor(GRISCLARO).add(new Paragraph(indicador.getDwindicador().getNombre()).setFontSize(this.getFontSize())));
                int[] meses = indicador.getMesesTotales();
                for (int i = 0; i < meses.length; i++) {
                    int val = meses[i];
                    String valor = Integer.toString(val);
                    Text texto = new Text(valor).setHorizontalAlignment(HorizontalAlignment.RIGHT);
                    texto.setTextAlignment(TextAlignment.RIGHT);
                    parrafo = new Paragraph(texto).setHorizontalAlignment(HorizontalAlignment.RIGHT).setFontSize(this.getFontSize());
                    tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo).setTextAlignment(TextAlignment.RIGHT));
                }
            }
            document.add(tabla);
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            document.close();
        }
    }

}
