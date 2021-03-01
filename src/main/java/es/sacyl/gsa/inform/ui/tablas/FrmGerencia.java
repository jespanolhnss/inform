package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.GerenciaBean;
import es.sacyl.gsa.inform.bean.LocalidadBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.dao.GerenciaDao;
import es.sacyl.gsa.inform.dao.LocalidadDao;
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
public final class FrmGerencia extends FrmMasterPantalla {

    /*

       codauto          varchar2(2 char)
codigo              varchar2(2 char)
nombre             varchar2(65 char)
tipovia              varchar2(5 char)
callesec            varchar2(4000 char)
numcalsec     number(38,0)
otrdomger    varchar2(40 char)
cpger  varchar2(5 char)
          varchar2(4000 char)

           codauto ,codigo, nombre , tipovia, callesec ,numcalsec, otrdomger, cpger , localger
     */
    //  private final TextField codauto = new ObjetosComunes().getTextField("codauto", "codigo", 2, "150px", "50px");
    private final ComboBox<AutonomiaBean> autonomiaComboBuscador = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> provinciaComboBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    
    private final ComboBox<AutonomiaBean> autonomiaCombo = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> provinciaCombo = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
  private final ComboBox<LocalidadBean> localidadCombo = new CombosUi().getLocalidadCombo(null,AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, null);
  
    private final TextField codigo = new ObjetosComunes().getTextField("Código", null, 2, "150px", "50px");
    private final TextField nombre = new ObjetosComunes().getTextField("Nombre", null, 65, "300px", "50px");
    private final TextField tipovia = new ObjetosComunes().getTextField("tipovia", null, 5, "150px", "50px");
    private final TextField callesec = new ObjetosComunes().getTextField("callesec", null, 65, "150px", "50px");
    private final IntegerField numcalsec = new ObjetosComunes().getIntegerField("callesec");
    private final TextField otrdomger = new ObjetosComunes().getTextField("otrdomger", null, 65, "150px", "50px");
    private final TextField cpger = new ObjetosComunes().getTextField("cpger", null, 5, "150px", "50px");
    private final TextField localger = new ObjetosComunes().getTextField("localger", null, 65, "150px", "50px");
    private final RadioButtonGroup<String>  estadoRadio = new ObjetosComunes().getEstadoRadio();
 
    private GerenciaBean gerenciaBean;
    private final Binder<GerenciaBean> gerenciaBinder = new Binder<>();
    private final PaginatedGrid<GerenciaBean> gerenciaGrid = new PaginatedGrid<>();
    private ArrayList<GerenciaBean> gerenciaArrayList = new ArrayList<>();

    public FrmGerencia() {
        super();
        this.gerenciaBean = new GerenciaBean();
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
        botonBorrar.setEnabled(false);
    }

    @Override
    public void doGrabar() {
        if (gerenciaBinder.writeBeanIfValid(gerenciaBean)) {
            if (new GerenciaDao().doGrabaDatos(gerenciaBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<GerenciaBean> validate = gerenciaBinder.validate();
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
                    new GerenciaDao().doBorraDatos(gerenciaBean);
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
        gerenciaBean = new GerenciaBean();
        gerenciaBinder.readBean(gerenciaBean);
        autonomiaCombo.setReadOnly(false);
        autonomiaCombo.setValue(AutonomiaBean.AUTONOMIADEFECTO);
        provinciaCombo.setValue(ProvinciaBean.PROVINCIA_DEFECTO);
        codigo.setReadOnly(false);
        
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        gerenciaGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        gerenciaGrid.setHeightByRows(true);
        gerenciaGrid.setPageSize(14);
      //  gerenciaGrid.addColumn(GerenciaBean::getCodauto).setAutoWidth(true).setHeader(new Html("<b>CódAut</b>"));
        gerenciaGrid.addColumn(GerenciaBean::getCodigo).setAutoWidth(true).setHeader(new Html("<b>Código</b>"));
        gerenciaGrid.addColumn(GerenciaBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Esta</b>"));
        gerenciaGrid.addColumn(GerenciaBean::getNombre).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));

        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        gerenciaArrayList = new GerenciaDao().getLista(buscador.getValue(), autonomiaComboBuscador.getValue(),provinciaComboBuscador.getValue(), null);
        gerenciaGrid.setItems(gerenciaArrayList);
    }

    @Override
    public void doBinderPropiedades() {
        
         gerenciaBinder.forField(autonomiaCombo)
                .asRequired()
                .bind(GerenciaBean::getAutonomia, GerenciaBean::setAutonomia);
         
   gerenciaBinder.forField(provinciaCombo)
                .asRequired()
                .bind(GerenciaBean::getProvincia, GerenciaBean::setProvincia);

 gerenciaBinder.forField(localidadCombo)
                .asRequired()
                .bind(GerenciaBean::getLocalidad, GerenciaBean::setLocalidad);

 
        gerenciaBinder.forField(codigo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 2))
                .bind(GerenciaBean::getCodigo, GerenciaBean::setCodigo);

        gerenciaBinder.forField(nombre)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 65))
                .bind(GerenciaBean::getNombre, GerenciaBean::setNombre);

        gerenciaBinder.forField(tipovia)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 5))
                .bind(GerenciaBean::getTipovia, GerenciaBean::setTipovia);

        gerenciaBinder.forField(callesec)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 65))
                .bind(GerenciaBean::getCallesec, GerenciaBean::setCallesec);

        gerenciaBinder.forField(numcalsec)
                .asRequired()
                .bind(GerenciaBean::getNumcalsec, GerenciaBean::setNumcalsec);

        gerenciaBinder.forField(otrdomger)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 65))
                .bind(GerenciaBean::getOtrdomger, GerenciaBean::setOtrdomger);

        gerenciaBinder.forField(cpger)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 65))
                .bind(GerenciaBean::getCpger, GerenciaBean::setCpger);

     gerenciaBinder.forField(estadoRadio)
                .asRequired()
                .bind(GerenciaBean::getEstadoString, GerenciaBean::setEstado);

        //   codauto ,codigo, nombre , tipovia, callesec ,numcalsec, otrdomger, cpger , localger
    }

    @Override
    public void doComponenesAtributos() {
        botonBorrar.setEnabled(false);

        buscador.setLabel("Valores a buscar");
    }

    @Override
    public void doComponentesOrganizacion() {
        this.titulo.setText("Gerencias");
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3));
        // codauto ,codigo, nombre , tipovia, callesec ,numcalsec, otrdomger, cpger , localger
        this.contenedorFormulario.add(autonomiaCombo, provinciaCombo,localidadCombo);
        
        this.contenedorFormulario.add(codigo);
            this.contenedorFormulario.add( nombre,2);
        this.contenedorFormulario.add(tipovia, callesec,numcalsec);
        this.contenedorFormulario.add(otrdomger, cpger,estadoRadio);
 
        this.contenedorBuscadores.add(buscador,autonomiaComboBuscador,provinciaComboBuscador);
        this.contenedorDerecha.removeAll();
        this.contenedorDerecha.add(contenedorBuscadores, gerenciaGrid);
    }

    @Override
    public void doCompentesEventos() {
        buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        buscador.addBlurListener(event -> {
            doActualizaGrid();
        });
        buscador.addKeyPressListener(event -> {
            doActualizaGrid();
        });

        autonomiaComboBuscador.addValueChangeListener(evvent->{
        provinciaComboBuscador.setItems(new ProvinciaDao().getLista(null, autonomiaComboBuscador.getValue()));
        doActualizaGrid();
        });
        
        provinciaComboBuscador.addValueChangeListener(event->{
        doActualizaGrid();
        });
        
         autonomiaCombo.addValueChangeListener(evvent->{
        provinciaCombo.setItems(new ProvinciaDao().getLista(null, autonomiaCombo.getValue()));
        });
        
        provinciaCombo.addValueChangeListener(event->{
        localidadCombo.setItems(new LocalidadDao().getLista(null,autonomiaCombo.getValue(), provinciaCombo.getValue()));
        });
        gerenciaGrid.addItemClickListener(event -> {
            gerenciaBean = event.getItem();
            gerenciaBinder.readBean(event.getItem());
            doControlBotones(gerenciaBean);
        }
        );
    }

}
