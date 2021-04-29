package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.AplicacionPerfilBean;
import es.sacyl.gsa.inform.bean.CategoriaBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.AplicacionPerfilDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Constantes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public final class FrmAplicacionPerfiles extends FrmMasterVentana {

    private ComboBox<AplicacionBean> aplicacionCombo = new CombosUi().getAplicacionCombo(null, null, null, null);
    private final TextField id = new ObjetosComunes().getTextField("Id");
    private final TextField nombre = new ObjetosComunes().getTextField("Nombre");
    private ComboBox<CategoriaBean> usuarioCategoriaCombo = new CombosUi().getCategoriaCombo(null, null);

    private final TextField codigo = new ObjetosComunes().getTextField("Codigo APP");
    private final TextField comentario = new ObjetosComunes().getTextField("Comentario");

    private AplicacionPerfilBean aplicacionPerfilBean = null;
    private final Binder<AplicacionPerfilBean> aplicacionPerfilBinder = new Binder<>();
    private final PaginatedGrid<AplicacionPerfilBean> aplicacionPerfilGrid = new PaginatedGrid<>();
    private ArrayList<AplicacionPerfilBean> aplicacionPerfilLista = new ArrayList<>();

    public FrmAplicacionPerfiles(String ancho, AplicacionPerfilBean aplicacionPerfilBean) {
        super(ancho);
        this.aplicacionPerfilBean = aplicacionPerfilBean;
        aplicacionCombo.setValue(aplicacionPerfilBean.getAplicacion());
        aplicacionCombo.setEnabled(false);
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        aplicacionPerfilBinder.readBean(aplicacionPerfilBean);
        doCompentesEventos();
    }

    @Override
    public void doControlBotones(Object obj) {
        super.doControlBotones(obj);
        if (obj == null) {
        } else {
            nombre.focus();
        }
    }

    @Override
    public void doGrabar() {
        if (aplicacionPerfilBinder.writeBeanIfValid(aplicacionPerfilBean)) {
            if (new AplicacionPerfilDao().doGrabaDatos(aplicacionPerfilBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                this.close();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<AplicacionPerfilBean> validate = aplicacionPerfilBinder.validate();
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
                    aplicacionPerfilBean.setEstado(ConexionDao.BBDD_ACTIVONO);
                    aplicacionPerfilBean.setUsucambio(((UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME)));
                    aplicacionPerfilBean.setFechacambio(LocalDate.now());
                    new AplicacionPerfilDao().doBorraDatos(aplicacionPerfilBean);
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
        aplicacionPerfilBean = new AplicacionPerfilBean();
        aplicacionPerfilBinder.readBean(aplicacionPerfilBean);
        doControlBotones(null);
    }

    @Override
    public void doGrid() {
    }

    @Override
    public void doActualizaGrid() {
    }

    @Override
    public void doBinderPropiedades() {
        aplicacionPerfilBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(AplicacionPerfilBean::getId, null);

        aplicacionPerfilBinder.forField(nombre)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 100))
                .bind(AplicacionPerfilBean::getNombre, AplicacionPerfilBean::setNombre);

        /*

        aplicacionPerfilBinder.forField(aplicacionCombo)
                .asRequired()
                .bind(AplicacionPerfilBean::getAplicacion, AplicacionPerfilBean::setAplicacion);
         */
        aplicacionPerfilBinder.forField(codigo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 50))
                .bind(AplicacionPerfilBean::getCodigo, AplicacionPerfilBean::setCodigo);

    }

    @Override
    public void doComponenesAtributos() {
        titulo.setText(" Perfiles de la aplicación: " + aplicacionPerfilBean.getAplicacion().getNombre());
        botonBorrar.setVisible(false);
        botonImprimir.setVisible(false);
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(aplicacionCombo, id, nombre, codigo);

    }

    @Override
    public void doCompentesEventos() {
    }

}
