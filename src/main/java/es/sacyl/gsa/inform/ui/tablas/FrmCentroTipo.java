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
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.dao.AutonomiaDao;
import es.sacyl.gsa.inform.dao.CentroTipoDao;
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
public final class FrmCentroTipo extends FrmMasterPantalla {
 private final TextField id = new ObjetosComunes().getTextField("Id", null, 10, "150px", "50px");
    private final TextField nombre = new ObjetosComunes().getTextField("Descripcioón", null, 10, "300px", "50px");
     private final RadioButtonGroup<String>  estadoRadio = new ObjetosComunes().getEstadoRadio();
 
    private CentroTipoBean centroTipoBean = null;
    private final Binder<CentroTipoBean> centroTipoBinder = new Binder<>();
    private final PaginatedGrid<CentroTipoBean> centroTipoGrid = new PaginatedGrid<>();
    private ArrayList<CentroTipoBean> centroTipoArrayList = new ArrayList<>();

     public FrmCentroTipo() {
        super();
        this.centroTipoBean = new CentroTipoBean();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        centroTipoBinder.readBean(centroTipoBean);
    }
     
      @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {
            id.setEnabled(true);
            id.focus();
        } else {
            // codigo.setEnabled(false);
            id.setReadOnly(true);
            nombre.focus();
        }
    }
    
    @Override
    public void doGrabar() {
        if (centroTipoBinder.writeBeanIfValid(centroTipoBean)) {
            if (new CentroTipoDao().doGrabaDatos(centroTipoBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<CentroTipoBean> validate = centroTipoBinder.validate();
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
                    new CentroTipoDao().doBorraDatos(centroTipoBean);
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
        centroTipoBean = new CentroTipoBean();
        centroTipoBinder.readBean(centroTipoBean);
        id.setReadOnly(false);
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

   @Override
    public void doGrid() {
        centroTipoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        centroTipoGrid.setHeightByRows(true);
        centroTipoGrid.setPageSize(14);
        centroTipoGrid.addColumn(CentroTipoBean::getId).setAutoWidth(true).setHeader(new Html("<b>Código</b>"));
        centroTipoGrid.addColumn(CentroTipoBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Estado</b>"));
        centroTipoGrid.addColumn(CentroTipoBean::getDescripcion).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));
        doActualizaGrid();
    }
   @Override
    public void doActualizaGrid() {
        centroTipoArrayList = new CentroTipoDao().getLista(buscador.getValue());
        centroTipoGrid.setItems(centroTipoArrayList);
    }
     @Override
    public void doBinderPropiedades() {
        centroTipoBinder.forField(id)
                .withNullRepresentation("")
                .asRequired()
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(CentroTipoBean::getId, CentroTipoBean::setId);
        
        centroTipoBinder.forField(nombre)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 250))
                .bind(CentroTipoBean::getDescripcion, CentroTipoBean::setDescripcion);

          centroTipoBinder.forField(estadoRadio)
                .bind(CentroTipoBean::getEstadoString, CentroTipoBean::setEstado);
    }

    @Override
    public void doComponenesAtributos() {
    }

      @Override
    public void doComponentesOrganizacion() {
        this.titulo.setText("Centros Tipos");
        this.contenedorFormulario.add(id, nombre,estadoRadio);
        this.contenedorBuscadores.add(buscador);
        this.contenedorDerecha.removeAll();
        this.contenedorDerecha.add(contenedorBuscadores, centroTipoGrid);
    }

    @Override
    public void doCompentesEventos() {
          buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        buscador.addBlurListener(event -> {
            doActualizaGrid();
        });

        centroTipoGrid.addItemClickListener(event -> {
            centroTipoBean = event.getItem();
            centroTipoBinder.readBean(event.getItem());
            doControlBotones(centroTipoBean);
        }
        );
    }
    
    
}
