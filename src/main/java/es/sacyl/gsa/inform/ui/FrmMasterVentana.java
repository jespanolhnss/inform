package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 *
 * @author JuanNieto
 *
 */
/*
      +-------------------------------------------------------------------+
      |  VerticalLayout                                                  |
      |   +--------------------------++--------------------------------+ |
      |   |                          ||                                | |
      |   |   contenedorIzquieda     ||   contenedorDerecha            | |
      |   |                          ||                                | |
      |   | +----------------------+ ||+------------------------------+| |
      |   | |  contenedorBotones   | |||contenedorBuscadores          || |
      |   | +----------------------+ ||+------------------------------+| |
      |   | +----------------------+ ||+------------------------------+| |
      |   | |  contenedorFormulario| |||contenedorGrid                || |
      |   | +----------------------+ ||+------------------------------+| |
      |   +--------------------------++--------------------------------| |
      +-------------------------------------------------------------------+
 */
public abstract class FrmMasterVentana extends Dialog {

    protected Button botonGrabar, botonBorrar, botonAyuda, botonLimpiar, botonImprimir, botonCancelar;

    protected H4 titulo;

    protected VerticalLayout contenedorTitulo = new VerticalLayout();

    protected HorizontalLayout contenedorBotones = new HorizontalLayout();
    protected HorizontalLayout contenedorFiltros = new HorizontalLayout();
    protected FormLayout contenedorFormulario = new FormLayout();

    protected HorizontalLayout contenedorVentana = new HorizontalLayout();
    protected VerticalLayout contenedorIzquierda = new VerticalLayout();
    protected VerticalLayout contenedorDerecha = new VerticalLayout();

    public FrmMasterVentana(String ancho) {
        this.setWidth(ancho);
        this.setSizeUndefined();
        this.add(contenedorVentana);
        contenedorVentana.add(contenedorIzquierda, contenedorDerecha);

        botonGrabar = new ObjetosComunes().getBoton("Graba", null, VaadinIcon.CHECK_CIRCLE.create());

        botonBorrar = new ObjetosComunes().getBoton("Borra", null, VaadinIcon.MINUS_CIRCLE.create());

        botonAyuda = new ObjetosComunes().getBoton("Ayuda", null, VaadinIcon.QUESTION_CIRCLE.create());

        botonCancelar = new ObjetosComunes().getBoton("Cancela", null, VaadinIcon.CLOSE_CIRCLE.create());

        botonLimpiar = new ObjetosComunes().getBoton("Limpia", null, VaadinIcon.PAINTBRUSH.create());

        botonImprimir = new ObjetosComunes().getBoton("Imprime", null, VaadinIcon.PRINT.create());
        titulo = new H4();
        contenedorBotones.setMargin(false);
        contenedorFiltros.setMargin(false);
        contenedorIzquierda.setMargin(false);
        contenedorIzquierda.setSpacing(false);

        contenedorDerecha.setMargin(false);
        contenedorDerecha.setSpacing(false);

        contenedorIzquierda.add(titulo, contenedorBotones, contenedorFormulario);
        contenedorBotones.add(botonGrabar, botonBorrar, botonAyuda, botonLimpiar, botonCancelar);

        contenedorDerecha.add(contenedorFiltros);

        botonGrabar.addClickListener(e -> {
            doGrabar();
        });
        botonBorrar.addClickListener(e -> {
            doBorrar();
        });
        botonCancelar.addClickListener(e -> {
            doCancelar();
        });
        botonAyuda.addClickListener(e -> {
            doAyuda();
        });
        botonBorrar.addClickListener(e -> {
            doBorrar();
        });

    }

    public abstract void doGrabar();

    public abstract void doBorrar();

    public abstract void doCancelar();

    public abstract void doCerrar();

    public abstract void doAyuda();

    public abstract void doLimpiar();

    public abstract void doGrid();

    public abstract void doActualizaGrid();

    public abstract void doBinderPropiedades();

    public abstract void doComponenesAtributos();

    public abstract void doComponentesOrganizacion();

    public abstract void doCompentesEventos();

    public void doControlBotones(Object obj) {
        if (obj != null) {
            botonBorrar.setEnabled(true);
        } else {
            botonBorrar.setEnabled(false);
        }
    }
}
