package es.sacyl.gsa.inform.ui.usuarios;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.CategoriaBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
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
public final class FrmUsuarios extends FrmMasterPantalla {

    private final TextField id = new ObjetosComunes().getTextField("Id");
    private final TextField nombreUsuario = new ObjetosComunes().getTextField("Nombre", "teclea nombre", 25, "100px", "30px");
    private final TextField apellido1Usuario = new ObjetosComunes().getTextField("Apellido 1", "teclea primer apellido", 25, "100px", "30px");
    private final TextField apellido2Usuario = new ObjetosComunes().getTextField("Apellido 2", "teclea segundo apellido", 25, "100px", "30px");
    private final TextField nifUsuario = new ObjetosComunes().getDni();
    private final TextField correoUsuario = new ObjetosComunes().getMail("Correo Electrónico", "Correo del usuario");
    private final TextField telefonoUsuario = new ObjetosComunes().getTelefono();
    private final ComboBox<CategoriaBean> categoriaUsuario = new CombosUi().getCategoriaCombo(null, null);
    private final RadioButtonGroup<String> estadoRadio = new ObjetosComunes().getEstadoRadio();
    private final RadioButtonGroup<String> solicitaRadio = new ObjetosComunes().getEstadoRadio();
    private final TextField movilUsuario = new ObjetosComunes().getTelefono();
    private final TextField correoPrivadoUsuario = new ObjetosComunes().getMail("Correo particular", "Correo del particular");
    private final TextField telegram = new ObjetosComunes().getTextField("Telegram");

    private UsuarioBean usuarioBean = new UsuarioBean();
    private Binder<UsuarioBean> usuarioBinder = new Binder<>();
    private PaginatedGrid<UsuarioBean> usuarioGrid = new GridUi().getUsuarioGrid();
    private ArrayList<UsuarioBean> usuarioBeansArrayList = new ArrayList<>();

    public FrmUsuarios() {
        super();
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
        } else {
        }
    }

    @Override
    public void doGrabar() {
        if (usuarioBinder.writeBeanIfValid(usuarioBean)) {
            if (new UsuarioDao().doGrabaDatos(usuarioBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<UsuarioBean> validate = usuarioBinder.validate();
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
                    new UsuarioDao().doBorraDatos(usuarioBean);
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
        usuarioBean = new UsuarioBean();
        usuarioBinder.readBean(usuarioBean);
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        usuarioGrid.removeAllColumns();
        usuarioGrid.addColumn(UsuarioBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Es</b>"));
        usuarioGrid.addColumn(UsuarioBean::getDni).setAutoWidth(true).setHeader(new Html("<b>Dni</b>"));
        usuarioGrid.addColumn(UsuarioBean::getApellidosNombre).setAutoWidth(true).setHeader(new Html("<b>Dni</b>"));
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        usuarioBeansArrayList = new UsuarioDao().getLista(buscador.getValue());
        usuarioGrid.setItems(usuarioBeansArrayList);
    }

    @Override
    public void doBinderPropiedades() {
        usuarioBinder.forField(nombreUsuario)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 25))
                .bind(UsuarioBean::getNombre, UsuarioBean::setNombre);

        usuarioBinder.forField(apellido1Usuario)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 25))
                .bind(UsuarioBean::getApellido1, UsuarioBean::setApellido1);

        usuarioBinder.forField(apellido2Usuario)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 25))
                .bind(UsuarioBean::getApellido2, UsuarioBean::setApellido2);

        usuarioBinder.forField(nifUsuario)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 9))
                .bind(UsuarioBean::getDni, UsuarioBean::setDni);

        usuarioBinder.forField(correoUsuario)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 100))
                .bind(UsuarioBean::getMail, UsuarioBean::setMail);

        usuarioBinder.forField(telefonoUsuario)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 25))
                .bind(UsuarioBean::getTelefono, UsuarioBean::setTelefono);

        usuarioBinder.forField(categoriaUsuario)
                .asRequired()
                .bind(UsuarioBean::getCategoria, UsuarioBean::setCategoria);

        usuarioBinder.forField(estadoRadio)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 25))
                .bind(UsuarioBean::getEstadoString, UsuarioBean::setEstado);

        usuarioBinder.forField(solicitaRadio)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 25))
                .bind(UsuarioBean::getSolicita, UsuarioBean::setSolicita);

        usuarioBinder.forField(movilUsuario)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 20))
                .bind(UsuarioBean::getMovilUsuario, UsuarioBean::setMovilUsuario);

        usuarioBinder.forField(correoPrivadoUsuario)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 100))
                .bind(UsuarioBean::getCorreoPrivadoUsuario, UsuarioBean::setCorreoPrivadoUsuario);

        usuarioBinder.forField(telegram)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 20))
                .bind(UsuarioBean::getTelegram, UsuarioBean::setTelegram);
    }

    @Override
    public void doComponenesAtributos() {
        this.titulo.setText("Gestión de usuarios");
        solicitaRadio.setLabel("Pide usuario");
    }

    @Override
    public void doComponentesOrganizacion() {
        this.contenedorFormulario.add(nombreUsuario, apellido1Usuario, apellido2Usuario, nifUsuario, correoUsuario,
                telefonoUsuario, categoriaUsuario, estadoRadio, solicitaRadio, movilUsuario, correoPrivadoUsuario, telegram);

        this.contenedorBuscadores.add(buscador);
        this.contenedorDerecha.removeAll();
        /**
         * En el contenedor de la derecha añade el contenedor de buscadores y el
         * drid
         */
        this.contenedorDerecha.add(contenedorBuscadores, usuarioGrid);
    }

    @Override
    public void doCompentesEventos() {

        buscador.addBlurListener(event -> {
            doActualizaGrid();
        });
        usuarioGrid.addItemClickListener(event -> {
            usuarioBean = event.getItem();
            usuarioBinder.readBean(event.getItem());
            doControlBotones(usuarioBean);
        }
        );
    }

}
