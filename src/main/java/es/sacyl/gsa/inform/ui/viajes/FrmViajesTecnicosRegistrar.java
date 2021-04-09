package es.sacyl.gsa.inform.ui.viajes;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.bean.ViajeBean;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.dao.ViajesDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterConstantes;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;

/**
 *
 * @author 06532775Q
 */
public final class FrmViajesTecnicosRegistrar extends FrmMasterVentana {

    /* Campos del formulario */
    TextField idViaje = new ObjetosComunes().getTextField("idViaje");
    ComboBox<UsuarioBean> tecnicosComboBox = new CombosUi().getInformaticosCombo(null);

    Grid<UsuarioBean> usuGrid = new Grid<>();

    Icon icon = new Icon(VaadinIcon.BUTTON);

    /* Componentes */
    ViajeBean viajeBean = new ViajeBean();

    public FrmViajesTecnicosRegistrar(ViajeBean viajeBean) {
        super("640px");
        this.viajeBean = viajeBean;
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doCompentesEventos();
        //   doBinderPropiedades();
    }

    @Override
    public void doGrabar() {
        for (UsuarioBean usuMarcado : usuGrid.getSelectedItems()) {
            if (new ViajesDao().doInsertaUnTecnico(viajeBean, usuMarcado) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doLimpiar();
                this.close();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
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

        usuGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        usuGrid.setHeightByRows(true);
        usuGrid.setPageSize(15);
        usuGrid.addColumn(UsuarioBean::getApellidosNombre).setAutoWidth(true).setHeader(new Html("<b>Usuario</b>"));
        usuGrid.setWidth("500px");
        ArrayList<UsuarioBean> lista = new UsuarioDao().getInformaticos();
        usuGrid.setItems(lista);
        if (lista.size() > 0) {
            usuGrid.setHeightByRows(true);
            usuGrid.setPageSize(lista.size());
        }
        usuGrid.setSelectionMode(Grid.SelectionMode.MULTI);

    }

    @Override
    public void doActualizaGrid() {
        usuGrid.setItems(new UsuarioDao().getInformaticos());
    }

    @Override
    public void doBinderPropiedades() {

    }

    @Override
    public void doComponenesAtributos() {
        contenedorFormulario.setWidth("640px");
        idViaje.setValue(viajeBean.getId().toString());
        idViaje.setEnabled(false);

        botonCancelar.setText("Salir");
        botonAyuda.setVisible(false);
        botonLimpiar.setVisible(false);
        botonBorrar.setVisible(false);

    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(idViaje, usuGrid);
    }

    @Override
    public void doCompentesEventos() {

    }
}
