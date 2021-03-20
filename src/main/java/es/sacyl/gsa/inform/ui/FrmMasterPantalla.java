/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

/**
 *
 * @author JuanNieto
 */
public abstract class FrmMasterPantalla extends HorizontalLayout {

    /**
     * Botones generales para todas las pantallas
     */
    protected Button botonGrabar, botonBorrar, botonAyuda, botonLimpiar, botonImprimir, botonCancelar;
    /**
     * El título de la pantalla
     */
    protected H4 titulo;
    /**
     * Campo buscador de texto para el grid
     */
    protected TextField buscador;

    protected HorizontalLayout contenedorBotones = new HorizontalLayout();
    protected HorizontalLayout contenedorBuscadores = new HorizontalLayout();

    protected FormLayout contenedorFormulario = new FormLayout();
    protected VerticalLayout contenedorIzquierda = new VerticalLayout();
    protected VerticalLayout contenedorDerecha = new VerticalLayout();

    public FrmMasterPantalla() {
        /**
         * Crea los botones
         */
        botonGrabar = new ObjetosComunes().getBoton("Graba", null, VaadinIcon.CHECK_CIRCLE.create());

        botonBorrar = new ObjetosComunes().getBoton("Borra", null, VaadinIcon.MINUS_CIRCLE.create());

        botonAyuda = new ObjetosComunes().getBoton("Ayuda", null, VaadinIcon.QUESTION_CIRCLE.create());

        botonCancelar = new ObjetosComunes().getBoton("Cancela", null, VaadinIcon.CLOSE_CIRCLE.create());

        botonLimpiar = new ObjetosComunes().getBoton("Limpia", null, VaadinIcon.PAINTBRUSH.create());

        botonImprimir = new ObjetosComunes().getBoton("Imprime", null, VaadinIcon.PRINT.create());

        titulo = new H4();

        buscador = new ObjetosComunes().getTextField("", "dato a buscar ", 200, "200px", "50px");

        buscador.setClearButtonVisible(true);

        doHazVentana();
    }

    public FrmMasterPantalla(String ancho) {

        this.setWidth(ancho);

        doHazVentana();
    }

    /**
     * Construye la pantalla con los conenedores y los objetos en cada
     * contenedor
     */
    public void doHazVentana() {

        /*
      +-------------------------------------------------------------------+
      |  HorizontalLayout                                                 |
      |   +--------------------------+ +--------------------------------+ |
      |   |                          | |                                | |
      |   |   contenedorIzquieda     | |   contenedorDerecha            | |
      |   |                          | |                                | |
      |   | +----------------------+ | |+------------------------------+| |
      |   | |  contenedorBotones   | | ||contenedorBuscadores          || |
      |   | +----------------------+ | |+------------------------------+| |
      |   | +----------------------+ | |+------------------------------+| |
      |   | |  contenedorFormulario| | ||contenedorGrid                || |
      |   | +----------------------+ | |+------------------------------+| |
      |   +--------------------------+ +--------------------------------| |
      +-------------------------------------------------------------------+

         */
        this.setSizeUndefined();
        /**
         * Quita margen y espacio entre objetos
         */
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);
        contenedorBotones.setMargin(false);

        contenedorBuscadores.setMargin(false);

        contenedorIzquierda.setMargin(false);
        contenedorIzquierda.setSpacing(false);
        contenedorIzquierda.getStyle().set("overflow-y", "auto");
        contenedorIzquierda.setHeight("100%");

        contenedorDerecha.setMargin(false);
        contenedorDerecha.setSpacing(false);
        contenedorDerecha.getStyle().set("overflow-y", "auto");
        contenedorDerecha.setHeight("100%");

        /**
         * Añade los objetos botones al contenedor de botones
         */
        this.contenedorBotones.add(botonGrabar, botonBorrar, botonAyuda, botonLimpiar, botonCancelar);
        /**
         * Añade a la pantalla (es un HorizontalLayout) el contendor izquierda y
         * el contendor derecha
         */
        this.add(contenedorIzquierda, contenedorDerecha);
        /**
         * En el contenedor de la izquerda el título, el contenedor de botones y
         * el formulario
         */
        this.contenedorIzquierda.add(titulo, contenedorBotones, contenedorFormulario);
        this.contenedorDerecha.add(contenedorBuscadores);
        /**
         * Eventos click de los botones
         */
        botonGrabar.addClickListener(e -> {
            doGrabar();
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
        botonLimpiar.addClickListener(e -> {
            doLimpiar();
        });
        botonImprimir.addClickListener(e -> {
            doImprimir();
        });
    }

    /**
     * Métodos abtractos asociados a los click de los botones
     */
    public void doCancelar() {
        this.removeAll();
        this.setVisible(false);
    }

    public abstract void doGrabar();

    public abstract void doBorrar();

    public abstract void doAyuda();

    public abstract void doLimpiar();

    public abstract void doImprimir();

    /**
     * Contruye el grid en función de bean que se gestiona
     */
    public abstract void doGrid();

    /**
     * Actualiza la lista de valores del grid en función de los filtros o
     * criterios de búsqueda según se diseñe la pantalla y los campos de
     * selección de búsqueda del contenedor de buscador Ejemplo para la tabla de
     * autonomías: 1.- Carga ArrayList con los datos de la base de datos 2.-
     * Añade la lista al grid
     *
     * @Override public void doActualizaGrid() { autonomiaLista = new
     * AutonomiaDao().getListaAutonomias(buscador.getValue());
     * autonomiaGrid.setItems(autonomiaLista); }
     *
     */
    public abstract void doActualizaGrid();

    /**
     * Para los campos del bean, los que se tienen que almacenar o recuperar
     * define las propiedades del binder: Get y Set del bean Valiaciones Campos
     * reuqeridos etc... Ejemplo de varios tipos de campos Campo códgio de la
     * tabla autonomías con atributos de requerido, y validando el ancho entre 1
     * y 5 caracteres
     */
    /*
      autonomiaBinder.forField(codigo)
      .withNullRepresentation("") .asRequired() .withValidator(new
      StringLengthValidator( FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
      .bind(AutonomiaBean::getCodigo, AutonomiaBean::setCodigo);

     * Ejemplo para campo id. Este campo sólo tiene atributo get ya que no se escribr y
     * convierte de text a long. Podría ser Long pero es textfield

      centroBinder.forField(id)
                .asRequired()
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(CentroBean::getId, null);

     */
    public abstract void doBinderPropiedades();

    /**
     * En este método se definen por ejemplo anchos, colores, títulos y label
     * particulres campos ocultos: por ejemplo ocultar botones que no tienen uso
     * o readonly ....
     */
    /*
     // título de la ventana
     this.titulo.setText("Centros");

       // se oculta el botón umprmir
        botonImprimir.setVisible(false)

        // cambia el label de campo buscador
        buscador.setLabel(" Nombre del centro a buscar");
     */
    public abstract void doComponenesAtributos();

    /**
     * Distribución de los elementos de la pantalla en los contenedores de la
     * pantalla maestra En general: Se añaden nuevos botones si son necesario
     *
     * Se añaden los campos del formulario a contenedor de formulario
     *
     * Se añaden los componentes de la derecha: el buscador, el grid ....
     */
    /*
    Ejemplo para la tabla autonmomías

        @Override
    public void doComponentesOrganizacion() {
        this.contenedorFormulario.add(codigo, nombre);
        this.contenedorBuscadores.add(buscador);
        this.contenedorDerecha.removeAll();
        this.contenedorDerecha.add(contenedorBuscadores, autonomiaGrid);
    }

     */
    public abstract void doComponentesOrganizacion();

    /**
     * Los eventos de los componentes
     *
     * Evento click de grid
     *
     * Evento se relgas del formulario
     *
     * Evento del buscador para haga las gúsquedas automátcias
     *
     */
    /*


     */
    public abstract void doCompentesEventos();

    /**
     *
     * @param obj Activa o desactiva el boton de borrar e imprimir en función
     * que obj Si en la pantalla hay datos se activa Si en la pantalla no hay
     * datos se desactiva
     */
    public void doControlBotones(Object obj) {
        if (obj != null) {
            botonBorrar.setEnabled(true);
            botonImprimir.setEnabled(true);
        } else {
            botonBorrar.setEnabled(false);
            botonImprimir.setEnabled(false);
        }
    }

    /**
     *
     * Geter y Setter de los botones para poder manipular desde fuera
     */
    /**
     *
     * @return
     */
    public Button getBotonGrabar() {
        return botonGrabar;
    }

    /**
     *
     * @param botonGrabar
     */
    public void setBotonGrabar(Button botonGrabar) {
        this.botonGrabar = botonGrabar;
    }

    /**
     *
     * @return
     */
    public Button getBotonBorrar() {
        return botonBorrar;
    }

    /**
     *
     * @param botonBorrar
     */
    public void setBotonBorrar(Button botonBorrar) {
        this.botonBorrar = botonBorrar;
    }

    /**
     *
     * @return
     */
    public Button getBotonAyuda() {
        return botonAyuda;
    }

    /**
     *
     * @param botonAyuda
     */
    public void setBotonAyuda(Button botonAyuda) {
        this.botonAyuda = botonAyuda;
    }

    /**
     *
     * @return
     */
    public Button getBotonLimpiar() {
        return botonLimpiar;
    }

    /**
     *
     * @param botonLimpiar
     */
    public void setBotonLimpiar(Button botonLimpiar) {
        this.botonLimpiar = botonLimpiar;
    }

    /**
     *
     * @return
     */
    public Button getBotonImprimir() {
        return botonImprimir;
    }

    /**
     *
     * @param botonImprimir
     */
    public void setBotonImprimir(Button botonImprimir) {
        this.botonImprimir = botonImprimir;
    }

    /**
     *
     * @return
     */
    public Button getBotonCancelar() {
        return botonCancelar;
    }

    /**
     *
     * @param botonCancelar
     */
    public void setBotonCancelar(Button botonCancelar) {
        this.botonCancelar = botonCancelar;
    }

    /**
     *
     * @return
     */
    public H4 getTitulo() {
        return titulo;
    }

    /**
     *
     * @param titulo
     */
    public void setTitulo(H4 titulo) {
        this.titulo = titulo;
    }

    /**
     *
     * @return
     */
    public TextField getBuscador() {
        return buscador;
    }

    /**
     *
     * @param buscador
     */
    public void setBuscador(TextField buscador) {
        this.buscador = buscador;
    }

    /**
     *
     * @return
     */
    public HorizontalLayout getContenedorBotones() {
        return contenedorBotones;
    }

    /**
     *
     * @param contenedorBotones
     */
    public void setContenedorBotones(HorizontalLayout contenedorBotones) {
        this.contenedorBotones = contenedorBotones;
    }

    /**
     *
     * @return
     */
    public HorizontalLayout getContenedorBuscadores() {
        return contenedorBuscadores;
    }

    /**
     *
     * @param contenedorBuscadores
     */
    public void setContenedorBuscadores(HorizontalLayout contenedorBuscadores) {
        this.contenedorBuscadores = contenedorBuscadores;
    }

    /**
     *
     * @return
     */
    public FormLayout getContenedorFormulario() {
        return contenedorFormulario;
    }

    /**
     *
     * @param contenedorFormulario
     */
    public void setContenedorFormulario(FormLayout contenedorFormulario) {
        this.contenedorFormulario = contenedorFormulario;
    }

    /**
     *
     * @return
     */
    public VerticalLayout getContenedorIzquierda() {
        return contenedorIzquierda;
    }

    /**
     *
     * @param contenedorIzquierda
     */
    public void setContenedorIzquierda(VerticalLayout contenedorIzquierda) {
        this.contenedorIzquierda = contenedorIzquierda;
    }

    /**
     *
     * @return
     */
    public VerticalLayout getContenedorDerecha() {
        return contenedorDerecha;
    }

    /**
     *
     * @param contenedorDerecha
     */
    public void setContenedorDerecha(VerticalLayout contenedorDerecha) {
        this.contenedorDerecha = contenedorDerecha;
    }

}
