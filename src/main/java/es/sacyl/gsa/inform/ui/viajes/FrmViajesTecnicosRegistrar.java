package es.sacyl.gsa.inform.ui.viajes;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.bean.ViajeBean;
import es.sacyl.gsa.inform.bean.ViajeTecnicoBean;
import es.sacyl.gsa.inform.dao.ViajesDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterConstantes;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author 06532775Q
 */
public class FrmViajesTecnicosRegistrar extends FrmMasterVentana {

    /* Campos del formulario */
    TextField id = new ObjetosComunes().getTextField("id");
    TextField idViaje = new ObjetosComunes().getTextField("idViaje");
    ComboBox<UsuarioBean> tecnicosComboBox = new CombosUi().getInformaticosCombo(null);

    Icon icon = new Icon(VaadinIcon.BUTTON);
    Button modificarButton = new ObjetosComunes().getBoton("Modificar", ButtonVariant.LUMO_PRIMARY, icon);

    /* Componentes */
    ViajeBean viajeBean = new ViajeBean();
    ViajeTecnicoBean viajeTecnicoBean = new ViajeTecnicoBean();
    Binder<ViajeTecnicoBean> viajeTecnicoBinder = new Binder<>();
    ArrayList<ViajeTecnicoBean> arrayListTecnicoViaje = new ArrayList<>();
    UsuarioBean informaticos = new UsuarioBean();

    public FrmViajesTecnicosRegistrar(String ancho, ViajeBean viajeBean) {
        super(ancho);
        this.viajeBean = viajeBean;
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades();
    }

    @Override
    public void doGrabar() {
        if (viajeTecnicoBinder.writeBeanIfValid(viajeTecnicoBean)) {
            viajeTecnicoBean.setViaje(viajeBean);
            informaticos.setId(viajeTecnicoBean.getTecnico().getId());
            if (new ViajesDao().doInsertaUnTecnico(viajeBean, informaticos) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<ViajeTecnicoBean> validate = viajeTecnicoBinder.validate();
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
                FrmMasterConstantes.AVISOCONFIRMACIONACCION,
                FrmMasterConstantes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMasterConstantes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new ViajesDao().doBorraUnTecnico(viajeTecnicoBean);
                    Notification.show(FrmMasterConstantes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();
        dialog.addDialogCloseActionListener(e -> {
        });
    }

    @Override
    public void doCancelar() {
        this.close();
    }

    @Override
    public void doCerrar() {

    }

    @Override
    public void doAyuda() {

    }

    @Override
    public void doLimpiar() {
        viajeTecnicoBinder.readBean(null);
    }

    @Override
    public void doGrid() {

    }

    @Override
    public void doActualizaGrid() {

    }

    @Override
    public void doBinderPropiedades() {
        viajeTecnicoBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(ViajeTecnicoBean::getId, null);

        /*
        viajeTecnicoBinder.forField(idViaje)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(ViajeTecnicoBean::getIdViaje, null);
         */
        viajeTecnicoBinder.forField(tecnicosComboBox).bind(ViajeTecnicoBean::getTecnico, ViajeTecnicoBean::setTecnico);
    }

    @Override
    public void doComponenesAtributos() {
        idViaje.setValue(viajeBean.getId().toString());
        botonCancelar.setText("Salir");
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorBotones.add(modificarButton);
        contenedorFormulario.add(id, idViaje, tecnicosComboBox);
    }

    @Override
    public void doCompentesEventos() {

    }
}
