/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.BlurNotifier;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.dao.ProvinciaDao;
import es.sacyl.gsa.inform.ui.CombosUi;
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
public final class FrmProvincia extends FrmMasterPantalla {

    private final TextField codigo = new ObjetosComunes().getTextField("Código", "codigo", 10, "150px", "50px");
    private final TextField nombre = new ObjetosComunes().getTextField("Nombre", "nombre", 10, "300px", "50px");
    private final ComboBox<AutonomiaBean> autonomiaCombo = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<AutonomiaBean> autonomiaComboBuscado = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);

    private ProvinciaBean provinciaBean = null;
    private final Binder<ProvinciaBean> provinciaBinder = new Binder<>();
    private final PaginatedGrid<ProvinciaBean> provinciaGrid = new PaginatedGrid<>();
    private ArrayList<ProvinciaBean> provinciaArrayList = new ArrayList<>();

    public FrmProvincia() {
        super();
        this.provinciaBean = new ProvinciaBean();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

     @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {
            codigo.setEnabled(true);
            codigo.focus();
        } else {
            // codigo.setEnabled(false);
            codigo.setReadOnly(true);
            nombre.focus();
        }
    }
    @Override
    public void doGrabar() {
        if (provinciaBinder.writeBeanIfValid(provinciaBean)) {
            if (new ProvinciaDao().doGrabaDatos(provinciaBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<ProvinciaBean> validate = provinciaBinder.validate();
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
                    new ProvinciaDao().doBorraDatos(provinciaBean);
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
        provinciaBean = new ProvinciaBean();
        provinciaBinder.readBean(provinciaBean);
        codigo.setReadOnly(false);
        autonomiaCombo.setValue(AutonomiaBean.AUTONOMIADEFECTO);
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        provinciaGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        provinciaGrid.setHeightByRows(true);
        provinciaGrid.setPageSize(14);
        provinciaGrid.addColumn(ProvinciaBean::getCodigo).setAutoWidth(true).setHeader(new Html("<b>Código</b>"));
        provinciaGrid.addColumn(ProvinciaBean::getNombre).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));

        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        provinciaArrayList = new ProvinciaDao().getLista(buscador.getValue(), autonomiaComboBuscado.getValue());
        provinciaGrid.setItems(provinciaArrayList);
    }

    @Override
    public void doBinderPropiedades() {
        provinciaBinder.forField(codigo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(ProvinciaBean::getCodigo, ProvinciaBean::setCodigo);
        provinciaBinder.forField(nombre)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(ProvinciaBean::getNombre, ProvinciaBean::setNombre);
        provinciaBinder.forField(autonomiaCombo)
                .withNullRepresentation(new AutonomiaBean())
                .asRequired()
                .bind(ProvinciaBean::getAutonomia, ProvinciaBean::setAutonomia);

    }

    @Override
    public void doComponenesAtributos() {
        buscador.setTitle("Texto a buscar");
        buscador.setTitle(" Valores a buscar ");
    }

    @Override
    public void doComponentesOrganizacion() {
        this.titulo.setText("Provincia");
        this.contenedorFormulario.add(autonomiaCombo, codigo, nombre);
        this.contenedorBuscadores.add(buscador, autonomiaComboBuscado);
        this.contenedorDerecha.removeAll();
        this.contenedorDerecha.add(contenedorBuscadores, provinciaGrid);
    }

    @Override
    public void doCompentesEventos() {
        buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });
        buscador.addKeyPressListener(event -> {
            doActualizaGrid();
        });

        codigo.addBlurListener(new ComponentEventListener<BlurNotifier.BlurEvent<TextField>>() {
            @Override
            public void onComponentEvent(BlurNotifier.BlurEvent<TextField> event) {
                ProvinciaBean pr=new ProvinciaDao().getPorCodigo(codigo.getValue());
                if (pr!=null){
                    Notification.show("Código ya existe");
                    codigo.focus();
                }
            }
        });
        autonomiaComboBuscado.addValueChangeListener(event -> {
            doActualizaGrid();;
        });

        provinciaGrid.addItemClickListener(event -> {
            provinciaBean = event.getItem();
            provinciaBinder.readBean(event.getItem());
            doControlBotones(provinciaBean);
        }
        );
    }

}
