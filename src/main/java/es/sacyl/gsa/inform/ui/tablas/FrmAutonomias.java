package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.dao.AutonomiaDao;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author juannietopajares
 */
public final class FrmAutonomias extends FrmMasterPantalla {

    /**
     * Componentes (campos) del formulario
     */
    private final TextField codigo = new ObjetosComunes().getTextField("Código", "codigo", 10, "150px", "50px");
    private final TextField nombre = new ObjetosComunes().getTextField("Nombre", "nombre", 10, "300px", "50px");
    private RadioButtonGroup<String> estadoRadio;
    /**
     * Objetos para gestionar el bean, el binder, el grid y el arraylist
     */
    private AutonomiaBean autonomiaBean = null;
    private final Binder<AutonomiaBean> autonomiaBinder = new Binder<>();
    private final PaginatedGrid<AutonomiaBean> autonomiaGrid = new PaginatedGrid<>();
    private ArrayList<AutonomiaBean> autonomiaLista = new ArrayList<>();

    public FrmAutonomias() {
        super();
        this.estadoRadio = new ObjetosComunes().getEstadoRadio();
        this.autonomiaBean = new AutonomiaBean();
        estadoRadio.setItems(ObjetosComunes.SINO);
        /**
         * Estos métodos tienen que ser todos para que se monte y se construya
         * la pantalla. Deben formar parte de la clase master, pero no me
         * funciona bien.
         */
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        doControlBotones(null);
    }

    @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {
            codigo.setEnabled(true);
            codigo.focus();
        } else {

            codigo.setReadOnly(true);
            nombre.focus();
        }
    }

    @Override
    public void doGrabar() {
        if (autonomiaBinder.writeBeanIfValid(autonomiaBean)) {
            if (new AutonomiaDao().doGrabaDatos(autonomiaBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<AutonomiaBean> validate = autonomiaBinder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
            Notification.show(FrmMensajes.AVISODATOERRORVALIDANDO + errorText);
        }
    }

    @Override
    public void doCancelar() {
        this.removeAll();
        this.setVisible(false);
    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMensajes.AVISOCONFIRMACIONACCION,
                FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new AutonomiaDao().doBorraDatos(autonomiaBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
        autonomiaBean = new AutonomiaBean();
        autonomiaBinder.readBean(autonomiaBean);
        codigo.setReadOnly(false);
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        autonomiaGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        autonomiaGrid.setHeightByRows(true);
        autonomiaGrid.setPageSize(25);
        autonomiaGrid.setPaginatorSize(25);
        autonomiaGrid.addColumn(AutonomiaBean::getCodigo).setAutoWidth(true).setHeader(new Html("<b>Código</b>"));
        autonomiaGrid.addColumn(AutonomiaBean::getNombre).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));
        autonomiaGrid.addColumn(AutonomiaBean::getEstado).setKey("estado").setAutoWidth(true).setHeader(new Html("<b>Estado</b>"));
        autonomiaGrid.setClassName("error_row");
        autonomiaGrid.setClassNameGenerator(auto -> {
            if (auto.getEstado() == 0) {
                //    return "my-style-2";
                return "error_row";
            } else {
                return "my-style-1";
            }

        });
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        autonomiaLista = new AutonomiaDao().getLista(buscador.getValue());
        autonomiaGrid.setItems(autonomiaLista);
    }

    @Override
    public void doBinderPropiedades() {
        autonomiaBinder.forField(codigo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 2))
                .bind(AutonomiaBean::getCodigo, AutonomiaBean::setCodigo);

        autonomiaBinder.forField(nombre)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 55))
                .bind(AutonomiaBean::getNombre, AutonomiaBean::setNombre);

        autonomiaBinder.forField(estadoRadio)
                .bind(AutonomiaBean::getEstadoString, AutonomiaBean::setEstado);

    }

    @Override
    public void doComponenesAtributos() {
        this.titulo.setText("Autonomías");
    }

    @Override
    public void doComponentesOrganizacion() {
        /**
         * Rellena el contenedor del formulario con los campos del formulario
         */
        this.contenedorFormulario.add(codigo, nombre, estadoRadio);
        /**
         * Añade el buscador en el contenedor buscadores
         */
        this.contenedorBuscadores.add(buscador);
        this.contenedorDerecha.removeAll();
        /**
         * En el contenedor de la derecha añade el contenedor de buscadores y el
         * drid
         */
        this.contenedorDerecha.add(contenedorBuscadores, autonomiaGrid);
    }

    @Override
    public void doCompentesEventos() {
        /**
         * Cuando ambia un datos del campo buscador y actualiza a lista de
         * valores del grid
         */
        buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        /**
         * Cuando sale del campo buscador actualiza el grid
         */
        buscador.addBlurListener(event -> {
            doActualizaGrid();
        });

        /**
         * Cuando hace click en un valor del grid 1.- Carga el valor del bean
         * actual con el dato del click 2.- Hace read del binder de modo que los
         * datos del bean pase a los componentes de pantalla 3.- Actualiza la
         * visibilidad de los botoes
         */
        autonomiaGrid.addItemClickListener(event -> {
            autonomiaBean = event.getItem();
            autonomiaBinder.readBean(event.getItem());
            doControlBotones(autonomiaBean);
        }
        );

    }

}
