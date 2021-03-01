/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.NivelesAtencionBean;
import es.sacyl.gsa.inform.bean.NivelesAtentionTipoBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.dao.NivelesAtencionDao;
import es.sacyl.gsa.inform.dao.NivelesAtencionTipoDao;
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
 * @author JuanNieto
 */
public final class FrmNivelesAtencion extends FrmMasterPantalla {

    private final TextField id = new ObjetosComunes().getTextField("Id", "id", 10, "50px", "50px");
    private final TextField codigo = new ObjetosComunes().getTextField("Código");
    private final TextField descripcion = new ObjetosComunes().getTextField("Descripcion");
    // private TextField tipo = new ObjetosComunes().getTextField("Tipo", "id", 10, "50px");
    private final ComboBox<NivelesAtentionTipoBean> areaTipoCombo = new CombosUi().getNivelestTipoCombo(null);
    private final ComboBox<NivelesAtentionTipoBean> areaTipoComboBuscador = new CombosUi().getNivelestTipoCombo(null);
       private final RadioButtonGroup<String>  estadoRadio = new ObjetosComunes().getEstadoRadio();
     private final ComboBox<ProvinciaBean> provinciaComboBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
     private final ComboBox<ProvinciaBean> provinciaCombo = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);

    
    private NivelesAtencionBean nivelAtencionBean = null;
    private final Binder<NivelesAtencionBean> nivelAtencionBinder = new Binder<>();
    private final PaginatedGrid<NivelesAtencionBean> nivelAtencionGrid = new PaginatedGrid<>();
    private ArrayList<NivelesAtencionBean> nivelAtencionLista = new ArrayList<>();

    public FrmNivelesAtencion() {
        super();
        this.nivelAtencionBean = new NivelesAtencionBean();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        nivelAtencionBinder.readBean(nivelAtencionBean);
        if (provinciaCombo.getValue()==null){
            provinciaCombo.setValue(ProvinciaBean.PROVINCIA_DEFECTO);
        }
        
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
            descripcion.focus();
        }
    }

    @Override
    public void doGrabar() {
        if (nivelAtencionBinder.writeBeanIfValid(nivelAtencionBean)) {
            if (new NivelesAtencionDao().doGrabaDatos(nivelAtencionBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<NivelesAtencionBean> validate = nivelAtencionBinder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
            Notification.show(FrmMensajes.AVISODATOERRORVALIDANDO + errorText);
        }
    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMensajes.AVISOCONFIRMACIONACCION,
                FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new NivelesAtencionDao().doBorraDatos(nivelAtencionBean);
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
    public void doActualizaGrid() {

        nivelAtencionLista = new NivelesAtencionDao().getLista(buscador.getValue(), areaTipoComboBuscador.getValue(), provinciaComboBuscador.getValue(),null);
        nivelAtencionGrid.setItems(nivelAtencionLista);
    }

    @Override
    public void doBinderPropiedades() {
        nivelAtencionBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(NivelesAtencionBean::getId, NivelesAtencionBean::setId);

        nivelAtencionBinder.forField(codigo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(NivelesAtencionBean::getCodigo, NivelesAtencionBean::setCodigo);

        nivelAtencionBinder.forField(descripcion)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 200))
                .bind(NivelesAtencionBean::getDescripcion, NivelesAtencionBean::setDescripcion);

        nivelAtencionBinder.forField(areaTipoCombo)
                .withNullRepresentation(new NivelesAtentionTipoBean(null, ""))
                .bind(NivelesAtencionBean::getTipo, NivelesAtencionBean::setTipo);

            nivelAtencionBinder.forField(estadoRadio)
                .bind(NivelesAtencionBean::getEstadoString, NivelesAtencionBean::setEstado);

     nivelAtencionBinder.forField(provinciaCombo)
             .asRequired()
                .bind(NivelesAtencionBean::getProvincia, NivelesAtencionBean::setProvincia);
            
    }

    @Override
    public void doComponenesAtributos() {
        buscador.focus();
        buscador.setLabel("Contenido a buscar");
        buscador.setValueChangeMode(ValueChangeMode.EAGER);

        ArrayList<NivelesAtentionTipoBean> listaNivelTipo = new NivelesAtencionTipoDao().getLista(null);

        areaTipoComboBuscador.setItems(listaNivelTipo);

        id.setReadOnly(true);
    }

     @Override
    public void doComponentesOrganizacion() {
        this.titulo.setText(NivelesAtencionBean.getTitulo());

        this.contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("100px", 1),
                new FormLayout.ResponsiveStep("200px", 2));

        this.contenedorFormulario.add(id, codigo);
        this.contenedorFormulario.add(descripcion, 2);
        this.contenedorFormulario.add(areaTipoCombo, 2);
        this.contenedorFormulario.add(provinciaCombo, estadoRadio);
  
        this.contenedorBuscadores.add(buscador, this.areaTipoComboBuscador,provinciaComboBuscador);
        this.contenedorDerecha.removeAll();
        this.contenedorDerecha.add(contenedorBuscadores, nivelAtencionGrid);
    }

    @Override
    public void doGrid() {
        nivelAtencionGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        nivelAtencionGrid.setHeightByRows(true);
        nivelAtencionGrid.setPageSize(14);
        nivelAtencionGrid.addColumn(NivelesAtencionBean::getId).setAutoWidth(true).setHeader(new Html("<b>Id</b>"));
        nivelAtencionGrid.addColumn(NivelesAtencionBean::getCodigo).setAutoWidth(true).setHeader(new Html("<b>Código</b>"));
        nivelAtencionGrid.addColumn(NivelesAtencionBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Est</b>"));
        nivelAtencionGrid.addColumn(NivelesAtencionBean::getDescripcion).setAutoWidth(true).setHeader(new Html("<b>Descripción</b>"));
        nivelAtencionGrid.addColumn(NivelesAtencionBean::getTipoInteger).setAutoWidth(true).setHeader(new Html("<b>Tipo</b>"));

        doActualizaGrid();
    }

    @Override
    public void doLimpiar() {
        nivelAtencionBean = new NivelesAtencionBean();
        nivelAtencionBinder.readBean(nivelAtencionBean);
    }

    @Override
    public void doCancelar() {
        this.removeAll();
    }

    @Override
    public void doCompentesEventos() {
        buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

          areaTipoComboBuscador.addValueChangeListener(valueChangeEvent -> {
            if (valueChangeEvent.getValue() == null) {
                //    message.setText("No project selected");
            } else {
                //  doActualizaGrid(valueChangeEvent.getValue());
                doActualizaGrid();
            }
        });
          provinciaComboBuscador.addValueChangeListener(event->{
          doActualizaGrid();});
        nivelAtencionGrid.addItemClickListener(event -> {
            nivelAtencionBean = event.getItem();
            nivelAtencionBinder.readBean(event.getItem());
        }
        );

      

    }

    @Override
    public void doImprimir() {
    }
}
