package es.sacyl.gsa.inform.ui.viajes;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.bean.ViajeBean;
import es.sacyl.gsa.inform.dao.ViajesDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterConstantes;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;

/**
 *
 * @author 06532775Q
 */
public final class FrmViajesTecnicosRegistrar extends FrmMasterVentana {

    /* Campos del formulario */
    TextField idViaje = new ObjetosComunes().getTextField("idViaje");
    ComboBox<UsuarioBean> tecnicosComboBox = new CombosUi().getInformaticosCombo(null);

    Icon icon = new Icon(VaadinIcon.BUTTON);

    /* Componentes */
    ViajeBean viajeBean = new ViajeBean();

    public FrmViajesTecnicosRegistrar(String ancho, ViajeBean viajeBean) {
        super(ancho);
        this.viajeBean = viajeBean;
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doCompentesEventos();
        //   doBinderPropiedades();
    }

    @Override
    public void doGrabar() {
        if (tecnicosComboBox.getValue() != null) {
            if (new ViajesDao().doInsertaUnTecnico(viajeBean, tecnicosComboBox.getValue()) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doLimpiar();
                this.close();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {

            Notification.show(FrmMensajes.AVISODATOERRORVALIDANDO);
        }
    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMasterConstantes.AVISOCONFIRMACIONACCION,
                FrmMasterConstantes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMasterConstantes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new ViajesDao().doBorraUnTecnico(viajeBean, tecnicosComboBox.getValue());
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

    }

    @Override
    public void doGrid() {

    }

    @Override
    public void doActualizaGrid() {

    }

    @Override
    public void doBinderPropiedades() {
        /*
        viajeTecnicoBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(ViajeTecnicoBean::getId, null);


        viajeTecnicoBinder.forField(idViaje)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(ViajeTecnicoBean::getIdViaje, null);

        viajeTecnicoBinder.forField(tecnicosComboBox).bind(ViajeTecnicoBean::getTecnico, ViajeTecnicoBean::setTecnico);
         */
    }

    @Override
    public void doComponenesAtributos() {
        idViaje.setValue(viajeBean.getId().toString());
        idViaje.setEnabled(false);

        botonCancelar.setText("Salir");
        botonAyuda.setVisible(false);
        botonLimpiar.setVisible(false);
        botonBorrar.setVisible(false);
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(idViaje, tecnicosComboBox);
    }

    @Override
    public void doCompentesEventos() {

    }
}
