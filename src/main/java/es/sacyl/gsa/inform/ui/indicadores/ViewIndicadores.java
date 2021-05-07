package es.sacyl.gsa.inform.ui.indicadores;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import es.sacyl.gsa.inform.bean.DWIndicadorValorAno;
import es.sacyl.gsa.inform.dao.DWQuerysDao;
import es.sacyl.gsa.inform.reports.indicadores.GraficoBarrasIngresosAA;
import es.sacyl.gsa.inform.reports.indicadores.GraficosUrgencias;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author 06551256M
 */
//@Route("")
@StyleSheet("styles/styles.css")
public class ViewIndicadores extends VerticalLayout {

    public static ArrayList<String> OPCIONES = new ArrayList<String>() {
        {
            add("Inicio");
            add("Hospital");
            add("Servicios");
        }
    };
    HorizontalLayout header = new HorizontalLayout();
    VerticalLayout sideMenu = new VerticalLayout();
    private VerticalLayout workspace = new VerticalLayout();
    private HorizontalLayout footer = new HorizontalLayout();

    public ViewIndicadores() {
        doHeader();

        // WORKSPACE
        //  workspace.add(createCard(), createCard(), createCard(), createCard());
        workspace.addClassName("workspace");
        workspace.setSizeFull();

        // doFooter();
        // CONTAINER
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        setPadding(false);
        add(sideMenu, header, workspace);
    }

    private void doHeader() {
        // HEADER
        Icon drawer = VaadinIcon.MENU.create();
        Span title = new Span("");
        Icon help = VaadinIcon.QUESTION_CIRCLE.create();
        Icon home = VaadinIcon.HOME.create();
        home.addClickListener(ev -> doOpcion(OPCIONES.get(0)));
        Icon hospital = VaadinIcon.HOSPITAL.create();
        hospital.addClickListener(ev -> doOpcion(OPCIONES.get(1)));
        Icon servicios = VaadinIcon.PACKAGE.create();
        servicios.addClickListener(ev -> doOpcion(OPCIONES.get(2)));

        Tab actionButton1 = new Tab(home, new Span(OPCIONES.get(0)));
        Tab actionButton2 = new Tab(hospital, new Span(OPCIONES.get(1)));
        Tab actionButton3 = new Tab(servicios, new Span(OPCIONES.get(2)));

        actionButton1.setClassName("tab");
        actionButton2.setClassName("tab");
        actionButton3.setClassName("tab");

        header.add(drawer, title, actionButton1, actionButton2, actionButton3, help);
        header.expand(title);
        header.setPadding(true);
        header.setWidth("100%");

        // MENU
        sideMenu.addClassName("sideMenu");
        sideMenu.setHeight("100%");
        sideMenu.setWidth("auto");
        sideMenu.setSpacing(false);
        drawer.getElement().addEventListener("click", ev -> sideMenu.getStyle().set("left", "0px"));
        Icon avatar = VaadinIcon.USER.create();
        avatar.setSize("4em");
        sideMenu.add(avatar, new Span("Menu1"), createMenuOption("Menu2"), createMenuOption("Menu3"), createMenuOption("menu4"));
        sideMenu.setAlignItems(Alignment.CENTER);

    }

    private void doFooter() {
        // FOOTER
        Icon home = VaadinIcon.HOME.create();
        home.addClickListener(ev -> doOpcion(OPCIONES.get(0)));
        Icon hospital = VaadinIcon.HOSPITAL.create();
        hospital.addClickListener(ev -> doOpcion(OPCIONES.get(1)));
        Icon servicios = VaadinIcon.PACKAGE.create();
        servicios.addClickListener(ev -> doOpcion(OPCIONES.get(2)));

        Tab actionButton1 = new Tab(home, new Span(OPCIONES.get(0)));
        Tab actionButton2 = new Tab(hospital, new Span(OPCIONES.get(1)));
        Tab actionButton3 = new Tab(servicios, new Span(OPCIONES.get(2)));

        actionButton1.setClassName("tab");
        actionButton2.setClassName("tab");
        actionButton3.setClassName("tab");

        Tabs buttonBar = new Tabs(actionButton1, actionButton2, actionButton3);
        footer = new HorizontalLayout(buttonBar);
        footer.setJustifyContentMode(JustifyContentMode.CENTER);
        footer.setWidth("100%");
    }

    private void doOpcion(String op) {
        Notification.show("Menu " + op + " clicked.");
        workspace.removeAll();
        //   FlexLayout cajaFormulario = (FlexLayout) new ObjetosComunes().getCaja();
        switch (op) {
            case "Inicio":
                FlexLayout cajaGraficoU = (FlexLayout) new ObjetosComunes().getCaja();
                workspace.add(cajaGraficoU);
                cajaGraficoU.add(new GraficosUrgencias().urgActuales());
                cajaGraficoU = (FlexLayout) new ObjetosComunes().getCaja();
                workspace.add(cajaGraficoU);
                cajaGraficoU.add(new GraficosUrgencias().ultimos30dias());
                break;
            case "Hospital":
                FlexLayout cajaGrafico = (FlexLayout) new ObjetosComunes().getCaja();
                workspace.add(cajaGrafico);
                ComboBox<Integer> ano = new CombosUi().getAnoCombo(new Integer(2000), LocalDate.now().getYear());
                cajaGrafico.add(ano);
                ano.addValueChangeListener(event -> {
                    DWIndicadorValorAno acutal = new DWQuerysDao().geIndicadorHospitalizacionAnoServicio(ano.getValue(), "CAR", "HOS002");
                    DWIndicadorValorAno anterior = new DWQuerysDao().geIndicadorHospitalizacionAnoServicio(ano.getValue() - 1, "CAR", "HOS002");
                    cajaGrafico.add(new GraficoBarrasIngresosAA(acutal, anterior, "Ingresos ", ano.getValue(),
                            ano.getValue() - 1));
                });
                break;
            case "Servicios":

                break;
        }
    }

    private Button createMenuOption(String title) {
        Button m1 = new Button(title);
        m1.setWidth("100%");
        m1.addClickListener(ev -> m1.getElement().getParent().getStyle().set("left", "-1000px"));
        //m1.addClickListener(ev -> Notification.show("Button " + title + " clicked."));
        m1.addClickListener(ev -> doOpcion(title));
        return m1;
    }

    private Component createCard(String titulo, Component component) {
        Span card1Label = new Span(titulo);
        FlexLayout card = new FlexLayout(card1Label);
        card.addClassName("card");
        card.setAlignItems(Alignment.CENTER);
        card.setJustifyContentMode(JustifyContentMode.CENTER);
        card.setWidth("100%");
        card.setHeight("200px");
        card.add(component);
        return card;
    }
}
