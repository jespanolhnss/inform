/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.NivelesAtentionTipoBean;
import es.sacyl.gsa.inform.dao.CentroTipoDao;
import es.sacyl.gsa.inform.dao.NivelesAtencionTipoDao;
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
 * @author 06551256M
 */
public final class FrmNivelesAtencionTipo extends FrmMasterPantalla {
     private final TextField id = new ObjetosComunes().getTextField("Id", null, 10, "150px", "50px");
    private final TextField descripcion = new ObjetosComunes().getTextField("Descripcioón", null, 50, "300px", "50px");
       private final RadioButtonGroup<String>  estadoRadio = new ObjetosComunes().getEstadoRadio();
 
    private NivelesAtentionTipoBean nivelesAtentionTipoBean = null;
    private final Binder<NivelesAtentionTipoBean> nivelesAtentionTipoBinder = new Binder<>();
    private final PaginatedGrid<NivelesAtentionTipoBean> nivelesAtentionTipoGrid = new PaginatedGrid<>();
    private ArrayList<NivelesAtentionTipoBean> nivelesAtentionTipoArrayList = new ArrayList<>();

     public FrmNivelesAtencionTipo() {
        super();
        this.nivelesAtentionTipoBean = new NivelesAtentionTipoBean();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        nivelesAtentionTipoBinder.readBean(nivelesAtentionTipoBean);
    }

         @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {
            id.setEnabled(true);
            id.focus();
        } else {
            id.setReadOnly(true);
            descripcion.focus();
        }
    }
    
  @Override
    public void doGrabar() {
        if (nivelesAtentionTipoBinder.writeBeanIfValid(nivelesAtentionTipoBean)) {
            if (new NivelesAtencionTipoDao().doGrabaDatos(nivelesAtentionTipoBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<NivelesAtentionTipoBean> validate = nivelesAtentionTipoBinder.validate();
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
                    new NivelesAtencionTipoDao().doBorraDatos(nivelesAtentionTipoBean);
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
        nivelesAtentionTipoBean = new NivelesAtentionTipoBean();
        nivelesAtentionTipoBinder.readBean(nivelesAtentionTipoBean);
        id.setReadOnly(false);
        doControlBotones(null);
    }

  @Override
    public void doGrid() {
        nivelesAtentionTipoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        nivelesAtentionTipoGrid.setHeightByRows(true);
        nivelesAtentionTipoGrid.setPageSize(14);
        nivelesAtentionTipoGrid.addColumn(NivelesAtentionTipoBean::getId).setAutoWidth(true).setHeader(new Html("<b>Código</b>"));
        nivelesAtentionTipoGrid.addColumn(NivelesAtentionTipoBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Estado</b>"));
        nivelesAtentionTipoGrid.addColumn(NivelesAtentionTipoBean::getDescripcion).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));

        doActualizaGrid();
    }

 @Override
    public void doActualizaGrid() {
        nivelesAtentionTipoArrayList = new NivelesAtencionTipoDao().getLista(buscador.getValue());
        nivelesAtentionTipoGrid.setItems(nivelesAtentionTipoArrayList);
    }

    @Override
    public void doBinderPropiedades() {
        nivelesAtentionTipoBinder.forField(id)
                .withNullRepresentation("")
                .asRequired()
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(NivelesAtentionTipoBean::getId, NivelesAtentionTipoBean::setId);
        
        nivelesAtentionTipoBinder.forField(descripcion)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 250))
                .bind(NivelesAtentionTipoBean::getDescripcion, NivelesAtentionTipoBean::setDescripcion);
        
         nivelesAtentionTipoBinder.forField(estadoRadio)
                .bind(NivelesAtentionTipoBean::getEstadoString, NivelesAtentionTipoBean::setEstado);

    }

    @Override
    public void doComponenesAtributos() {
    }

      @Override
    public void doComponentesOrganizacion() {
        this.titulo.setText("Niveles atención Tipos");
        this.contenedorFormulario.add(id, descripcion,estadoRadio);
        
        this.contenedorBuscadores.add(buscador);
        this.contenedorDerecha.removeAll();
        this.contenedorDerecha.add(contenedorBuscadores, nivelesAtentionTipoGrid);
    }

     @Override
    public void doCompentesEventos() {
          buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });
        buscador.addBlurListener(event -> {
            doActualizaGrid();
        });
        nivelesAtentionTipoGrid.addItemClickListener(event -> {
            nivelesAtentionTipoBean = event.getItem();
            nivelesAtentionTipoBinder.readBean(event.getItem());
            doControlBotones(nivelesAtentionTipoBean);
        }
        );
    }

    @Override
    public void doImprimir() {
    }
    
}
