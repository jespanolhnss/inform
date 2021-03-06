package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroUsuarioBean;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.CentroUsuarioDao;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.GridUi;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public final class FrmCentroUsuario extends FrmMasterVentana {

    private final TextField buscador = new ObjetosComunes().getTextField(null);

    private final TextField dni = new ObjetosComunes().getDni();

    private final TextField nombre = new ObjetosComunes().getTextField("Nombre");

    private final ComboBox<String> cargoCombo;

    private final TextField comentario = new ObjetosComunes().getTextField("Comentario");

    private CentroBean centroBean = new CentroBean();
    private CentroUsuarioBean centroUsuarioBean = new CentroUsuarioBean();

    private final Binder<CentroUsuarioBean> centroUsuarioBinder = new Binder<>();
    private final PaginatedGrid<UsuarioBean> usuarioGrid = new GridUi().getUsuarioGrid();
    private ArrayList<CentroUsuarioBean> centroUsuarioLista = new ArrayList<>();
    private final PaginatedGrid<CentroUsuarioBean> centroUsuarioGrid = new GridUi().getCentroUsuarioPaginateGrid();

    public FrmCentroUsuario(CentroBean centroBean) {
        super("500px");
        this.centroBean = centroBean;
        this.centroUsuarioBean.setCentro(centroBean);
        cargoCombo = new CombosUi().getGrupoRamaComboValor(ComboBean.CARGOSCENTROS, centroBean.getTipocentro().getId().toString(), null, null);
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();

        doActualizaGrid();

        doActualizaGridSeleccionados();
    }

    @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {
        } else {
        }
    }

    @Override
    public void doGrabar() {
        if (centroUsuarioBinder.writeBeanIfValid(centroUsuarioBean)) {
            if (new CentroUsuarioDao().doGrabaDatos(centroUsuarioBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGridSeleccionados();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<CentroUsuarioBean> validate = centroUsuarioBinder.validate();
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
                    new CentroUsuarioDao().doBorraDatos(centroUsuarioBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();
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
        centroUsuarioBean = new CentroUsuarioBean();
        centroUsuarioBinder.readBean(centroUsuarioBean);
        doControlBotones(null);
    }

    @Override
    public void doGrid() {
        centroUsuarioGrid.setPageSize(25);
        centroUsuarioGrid.setPaginatorSize(25);
    }

    @Override
    public void doActualizaGrid() {
        ArrayList<UsuarioBean> listaUsuario = new UsuarioDao().getLista(dni.getValue());
        // quitamos los usuarios elegidos
        for (CentroUsuarioBean centrousuario : centroUsuarioGrid.getSelectedItems()) {
            listaUsuario.remove(centrousuario.getUsuario());
        }
        usuarioGrid.setItems(listaUsuario);
    }

    public void doActualizaGridSeleccionados() {
        ArrayList<CentroUsuarioBean> lista = new CentroUsuarioDao().getLista(null, centroBean);
        centroUsuarioGrid.setItems(lista);
    }

    @Override
    public void doBinderPropiedades() {

        centroUsuarioBinder.forField(dni)
                .bind(CentroUsuarioBean::getUsuarioDni, null);

        centroUsuarioBinder.forField(nombre)
                .bind(CentroUsuarioBean::getUsuarioNombre, null);

        centroUsuarioBinder.forField(cargoCombo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 50))
                .bind(CentroUsuarioBean::getCargo, CentroUsuarioBean::setCargo);

        centroUsuarioBinder.forField(comentario)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 500))
                .bind(CentroUsuarioBean::getComentario, CentroUsuarioBean::setComentario);

    }

    @Override
    public void doComponenesAtributos() {
        buscador.setWidth("300px");

    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("150px", 1),
                new FormLayout.ResponsiveStep("150px", 2));

        contenedorFormulario.add(dni, nombre, cargoCombo, comentario);
        contenedorIzquierda.add(centroUsuarioGrid);
        contenedorFiltros.add(buscador);
        contenedorDerecha.add(usuarioGrid);
    }

    /**
     *
     */
    @Override
    public void doCompentesEventos() {
        usuarioGrid.addItemClickListener(event -> {
            centroUsuarioBean = new CentroUsuarioBean();
            centroUsuarioBean.setCentro(centroBean);
            UsuarioBean usuario = event.getItem();
            centroUsuarioBean.setUsuario(usuario);
            centroUsuarioBinder.readBean(centroUsuarioBean);
        });

        centroUsuarioGrid.addItemClickListener(event1 -> {
            centroUsuarioBean = event1.getItem();
            centroUsuarioBinder.readBean(centroUsuarioBean);
            doControlBotones(centroUsuarioBean);
        });
    }

}
