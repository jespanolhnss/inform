/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.reports.recursos;

import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.bean.EquipoAplicacionBean;
import es.sacyl.gsa.inform.reports.MasterReport;
import es.sacyl.gsa.inform.reports.PdfEventoPagina;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author 06551256M
 */
public class AplicacionPDF extends MasterReport implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(AplicacionPDF.class);

    ArrayList<AplicacionBean> lista = new ArrayList<>();

    public AplicacionPDF(ArrayList<AplicacionBean> lista) {
        this.lista = lista;
        doCreaPdf();
    }

    public AplicacionPDF(AplicacionBean app) {
        this.lista.add(app);
        doCreaPdf();
    }

    @Override
    public void doCreaPdf() {
        nombreDelFicheroPdf = "Aplicaciones.pdf";
        this.setFontSize(9);
        this.getDocument().setTopMargin(75);
        this.getDocument().setBottomMargin(50);
        this.doActualizaNombreFicheros();
        try {
            PdfEventoPagina evento = new PdfEventoPagina(document,
                    " Detalle de aplicaciones ");
            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, evento);
            float[] anchos = {50f, 50f, 50f, 350f};
            float[] anchos1 = {70f, 100f, 70f, 100f, 70f, 100f};

            for (AplicacionBean app : lista) {
                Table tabla = new Table(anchos1);
                Paragraph parrafo = new Paragraph(app.getNombre() + "(" + app.getId() + ")").setFontSize(this.getFontSize() + 3);
                tabla.addCell(new Cell(0, 6).setBorder(new SolidBorder(BLANCO, 1)).setBackgroundColor(GRISCLARO).add(parrafo).setTextAlignment(TextAlignment.CENTER));

                parrafo = new Paragraph("Proveedor").setFontSize(this.getFontSize());
                tabla.addCell(new Cell().setBorder(new SolidBorder(BLANCO, 1)).setBackgroundColor(GRISCLARO).add(parrafo).setTextAlignment(TextAlignment.CENTER));
                parrafo = new Paragraph(app.getProveedor().getNombre()).setFontSize(this.getFontSize());
                tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));

                parrafo = new Paragraph("Usuarios").setFontSize(this.getFontSize());
                tabla.addCell(new Cell().setBorder(new SolidBorder(BLANCO, 1)).setBackgroundColor(GRISCLARO).add(parrafo).setTextAlignment(TextAlignment.CENTER));
                if (app.getGestionUsuarios() != null) {
                    parrafo = new Paragraph(app.getGestionUsuarios()).setFontSize(this.getFontSize());
                    tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));
                } else {
                    parrafo = new Paragraph("");
                    tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));
                }

                parrafo = new Paragraph("Ámbito").setFontSize(this.getFontSize());
                tabla.addCell(new Cell().setBorder(new SolidBorder(BLANCO, 1)).setBackgroundColor(GRISCLARO).add(parrafo).setTextAlignment(TextAlignment.CENTER));
                if (app.getAmbito() != null) {
                    parrafo = new Paragraph(app.getAmbito()).setFontSize(this.getFontSize());
                    tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));
                } else {
                    parrafo = new Paragraph("");
                    tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));
                }

                parrafo = new Paragraph("Descripción").setFontSize(this.getFontSize());
                tabla.addCell(new Cell().setBorder(new SolidBorder(BLANCO, 1)).setBackgroundColor(GRISCLARO).add(parrafo).setTextAlignment(TextAlignment.CENTER));
                if (app.getDescripcion() != null) {
                    parrafo = new Paragraph(app.getDescripcion()).setFontSize(this.getFontSize());
                    tabla.addCell(new Cell(0, 5).setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));
                } else {
                    parrafo = new Paragraph("");
                    tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));
                }
                if (app.getListaDatosGenerico().size() > 0) {
                    parrafo = new Paragraph("Características y  datos asociados ").setFontSize(this.getFontSize());
                    tabla.addCell(new Cell(0, 6).setBorder(new SolidBorder(BLANCO, 1)).setBackgroundColor(GRISCLARO).add(parrafo).setTextAlignment(TextAlignment.CENTER));
                    for (DatoGenericoBean dato : app.getListaDatosGenerico()) {
                        parrafo = new Paragraph(dato.getTipoDato()).setFontSize(this.getFontSize());
                        tabla.addCell(new Cell().setBorder(new SolidBorder(BLANCO, 1)).setBackgroundColor(GRISCLARO).add(parrafo).setTextAlignment(TextAlignment.CENTER));
                        parrafo = new Paragraph(dato.getValor()).setFontSize(this.getFontSize());
                        tabla.addCell(new Cell(0, 5).setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));

                    }
                }
                if (app.getListaEquipoBeans().size() > 0) {
                    parrafo = new Paragraph("Equipos instalados ").setFontSize(this.getFontSize());
                    tabla.addCell(new Cell(0, 6).setBorder(new SolidBorder(BLANCO, 1)).setBackgroundColor(GRISCLARO).add(parrafo).setTextAlignment(TextAlignment.CENTER));
                    for (EquipoAplicacionBean equipopp : app.getListaEquipoBeans()) {
                        parrafo = new Paragraph(equipopp.getEquipo().getId().toString()).setFontSize(this.getFontSize());
                        tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));

                        if (equipopp.getEquipo() != null && equipopp.getEquipo().getIpsCadena() != null) {
                            parrafo = new Paragraph(equipopp.getEquipo().getIpsCadena().toString()).setFontSize(this.getFontSize());
                            tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));
                        } else {
                            parrafo = new Paragraph("");
                            tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));
                        }
                        if (equipopp.getEquipo() != null && equipopp.getEquipo().getServicio() != null && equipopp.getEquipo().getServicio().getCodigo() != null) {
                            parrafo = new Paragraph(equipopp.getEquipo().getServicio().getCodigo().toString()).setFontSize(this.getFontSize());
                            tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));
                        } else {
                            parrafo = new Paragraph("");
                            tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));
                        }
                        if (equipopp.getEquipo() != null && equipopp.getEquipo().getUbicacion() != null && equipopp.getEquipo().getUbicacion().getDescripcionFull() != null) {
                            parrafo = new Paragraph(equipopp.getEquipo().getServicio().getCodigo().toString()).setFontSize(this.getFontSize());
                            tabla.addCell(new Cell().setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));
                        } else {
                            parrafo = new Paragraph("");
                            tabla.addCell(new Cell(0, 3).setBorder(new SolidBorder(GRISCLARO, 1)).add(parrafo));
                        }

                    }

                }
                document.add(tabla);
                //salto pagina
                document.add(new AreaBreak());
            }

        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            document.close();
        }
    }

}
