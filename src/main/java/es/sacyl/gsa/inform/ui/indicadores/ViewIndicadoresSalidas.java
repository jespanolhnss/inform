package es.sacyl.gsa.inform.ui.indicadores;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import es.sacyl.gsa.inform.bean.DWIndicadorValorAno;
import es.sacyl.gsa.inform.dao.DWQuerysDao;
import es.sacyl.gsa.inform.reports.indicadores.GraficoBarrasIngresosAA;
import es.sacyl.gsa.inform.reports.indicadores.ServicioEvolucionAnoPdf;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.time.LocalDate;

/**
 *
 * @author 06551256M
 */
public final class ViewIndicadoresSalidas extends HorizontalLayout {

    FormLayout formulario = new FormLayout();
    VerticalLayout izquierda = new VerticalLayout();
    VerticalLayout derecha = new VerticalLayout();

    HorizontalLayout botones = new HorizontalLayout();

    private final IntegerField ano = new ObjetosComunes().getAno();
    private final IntegerField mes = new ObjetosComunes().getMes();

    private final RadioButtonGroup<String> areaRadio = new ObjetosComunes().getAreasIndicadores();

    private final Image pdf = new Image("icons/pdf.png", "Pdf");
    private final Image excel = new Image("icons/excel.jpg", "Excel");
    private final Image barras = new Image("icons/barras.jpg", "Barras");

    public ViewIndicadoresSalidas() {
        doComponentesOrganizacion();
        doComponenesAtributos();
        doCompentesEventos();
    }

    public void doComponenesAtributos() {
        this.setMargin(true);
        ano.setValue(LocalDate.now().getYear());
        mes.setValue(LocalDate.now().getMonthValue());

    }

    public void doComponentesOrganizacion() {
        this.setSizeUndefined();

        this.setMargin(false);
        this.setSpacing(false);

        izquierda.setMargin(false);
        izquierda.setSpacing(false);

        derecha.setMargin(false);
        derecha.setSpacing(false);

        formulario.setSizeUndefined();

        formulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("200px", 1),
                new FormLayout.ResponsiveStep("200px", 2));
        formulario.add(ano, mes, areaRadio);
        Label titulo = new Label("Consulta indicadores ");

        botones.add(pdf, excel, barras);

        izquierda.setMaxWidth("450px");
        izquierda.add(titulo, botones, formulario);
        derecha.setSizeUndefined();
        derecha.setWidthFull();
        this.add(izquierda, derecha);
    }

    public void doCompentesEventos() {
        pdf.addClickListener(event -> {
            ServicioEvolucionAnoPdf incidenciaPdf = new ServicioEvolucionAnoPdf(ano.getValue(), "CAR");
            incidenciaPdf.doCreaFicheroPdf();
            Page page = new Page(getUI().get());
            page.open(incidenciaPdf.getUrlDelPdf(), "_blank");
        });

        barras.addClickListener(event -> {
            DWIndicadorValorAno acutal = new DWQuerysDao().geIndicadorHospitalizacionAnoServicio(ano.getValue(), "CAR", "HOS002");
            DWIndicadorValorAno anterior = new DWQuerysDao().geIndicadorHospitalizacionAnoServicio(ano.getValue() - 1, "CAR", "HOS002");
            derecha.add(new GraficoBarrasIngresosAA(acutal, anterior, "Ingresos ", ano.getValue(),
                    ano.getValue() - 1));
        });
    }

}
