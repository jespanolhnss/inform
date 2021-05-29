package es.sacyl.gsa.inform.ui;

import com.vaadin.componentfactory.Tooltip;
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
    /**
     * Bocadillos para cada boton
     */
    protected Tooltip tooltipGrabar, tooltipBorrar, tooltipAyuda, tooltipLimpiar, tooltipImprimir, tooltipCancelar;

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
        doHazVentana();
    }

    public FrmMasterVentana() {
        doHazVentana();
    }

    public void doHazVentana() {
        this.add(contenedorVentana);
        contenedorVentana.add(contenedorIzquierda, contenedorDerecha);

        botonGrabar = new ObjetosComunes().getBoton("", null, VaadinIcon.CHECK_CIRCLE.create());
        tooltipGrabar = new ObjetosComunes().getTooltip(botonGrabar, "Graba los datos actuales en la base de datos");

        botonBorrar = new ObjetosComunes().getBoton("", null, VaadinIcon.MINUS_CIRCLE.create());
        tooltipBorrar = new ObjetosComunes().getTooltip(botonBorrar, "Borra de la base de datos  los datos actuales");

        botonAyuda = new ObjetosComunes().getBoton("", null, VaadinIcon.QUESTION_CIRCLE.create());
        tooltipAyuda = new ObjetosComunes().getTooltip(botonAyuda, "Ayuda sobre los campos de pantalla");

        botonCancelar = new ObjetosComunes().getBoton("", null, VaadinIcon.CLOSE_CIRCLE.create());
        tooltipCancelar = new ObjetosComunes().getTooltip(botonCancelar, "Cancela la acción y cierra la ventan");

        botonLimpiar = new ObjetosComunes().getBoton("", null, VaadinIcon.PAINTBRUSH.create());
        tooltipLimpiar = new ObjetosComunes().getTooltip(botonLimpiar, "Limpia los campos del formulario");

        botonImprimir = new ObjetosComunes().getBoton("", null, VaadinIcon.PRINT.create());
        tooltipImprimir = new ObjetosComunes().getTooltip(botonImprimir, "Imprime la ficha actual");

        titulo = new H4();
        contenedorBotones.setMargin(false);
        contenedorFiltros.setMargin(false);
        contenedorIzquierda.setMargin(false);
        contenedorIzquierda.setSpacing(false);

        contenedorDerecha.setMargin(false);
        contenedorDerecha.setSpacing(false);

        contenedorIzquierda.add(titulo, contenedorBotones, contenedorFormulario);
        this.contenedorBotones.add(botonGrabar, tooltipGrabar, botonBorrar, tooltipBorrar, botonAyuda, tooltipAyuda,
                botonLimpiar, tooltipLimpiar, botonCancelar, tooltipCancelar);

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
