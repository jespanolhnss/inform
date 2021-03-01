package es.sacyl.gsa.inform.ui.lopd;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.LopdSujetoBean;
import es.sacyl.gsa.inform.bean.LopdTipoBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.LopdTipoDao;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Constantes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author juannietopajares
 */
public final class FrmLopdTipos extends FrmMasterPantalla {

    private final TextField id = new ObjetosComunes().getTextField("Id", null, 4, "30px", "30px");

    private final TextField descripcion = new ObjetosComunes().getTextField("Descripción", null, 50, "100px", "50px");

    private final ComboBox<LopdSujetoBean> sujetoCombo = new CombosUi().getLopdSujetoCombo(null);

    private final Checkbox mailReponsable = new Checkbox("Enviar mail reponsable LOPD");

    private final Checkbox estado = new Checkbox("Activo");

    private final DatePicker fechaCambio = new ObjetosComunes().getDatePicker("Fecha cambio", null, null);

    private final TextField usuCambio = new ObjetosComunes().getTextField("Dni ", null, 50, "100px", "50px");

    private final TextField usuCambioNombre = new ObjetosComunes().getTextField("Nombre", null, 200, "100px", "50px");

    private final ComboBox<LopdSujetoBean> sujetoComboBuscador = new CombosUi().getLopdSujetoCombo(null);

    private LopdTipoBean tipoBean = new LopdTipoBean();

    private UsuarioBean usuario = new UsuarioBean();

    private final Binder<LopdTipoBean> lopdTipoBinder = new Binder<>();
    private final Binder<UsuarioBean> usuarioBinder = new Binder<>();

    private final Grid<LopdTipoBean> grid = new Grid<>();

    public FrmLopdTipos(LopdTipoBean tipoParam) {
        super();
        this.tipoBean = tipoParam;
        doComponentesOrganizacion();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        doGrid();
        doControlBotones(tipoBean);
        tipoBean.setFechaCambio(LocalDate.now());
        lopdTipoBinder.readBean(tipoBean);
        usuario = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
        usuarioBinder.readBean(usuario);
    }

    @Override
    public void doGrabar() {
        if (lopdTipoBinder.writeBeanIfValid(tipoBean)) {
            tipoBean.setUsucambio(usuario);
            tipoBean.setFechaCambio(LocalDate.now());
            if (new LopdTipoDao().grabaDatos(tipoBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<LopdTipoBean> validate = lopdTipoBinder.validate();
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
    }

    @Override
    public void doAyuda() {

    }

    @Override
    public void doLimpiar() {
        tipoBean = new LopdTipoBean();
        lopdTipoBinder.readBean(tipoBean);
    }

    @Override
    public void doGrid() {
        grid.setHeightByRows(true);
        grid.setPageSize(14);
        grid.addColumn(LopdTipoBean::getId).setSortable(true).setAutoWidth(true).setHeader(new Html("<b>Id</b>"));
        grid.addColumn(LopdTipoBean::getSujetoId).setSortable(true).setAutoWidth(true).setHeader(new Html("<b>Sujeto</b>"));
        grid.addColumn(LopdTipoBean::getEstadoInt).setSortable(true).setAutoWidth(true).setHeader(new Html("<b>Estado</b>"));
        grid.addColumn(LopdTipoBean::getDescripcion).setSortable(true).setAutoWidth(true).setHeader(new Html("<b>Descripción</b>"));
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        //  grid.setClassNameGenerator(tipo -> tipo.getEstadoInt() == 0 ? "warn" : null);

        grid.setClassNameGenerator(tipo -> {
            if (!tipo.isEstado()) {
                return "error";
            } else {
                return "";
            }
        });

        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        UsuarioBean usuario = null;
        LopdSujetoBean sujeto = null;
        String cadena = null;
        if (sujetoComboBuscador.getValue() != null) {
            sujeto = sujetoComboBuscador.getValue();
        }
        if (!buscador.getValue().isEmpty()) {
            cadena = buscador.getValue();
        }
        ArrayList<LopdTipoBean> listaTipos = new LopdTipoDao().getListaIncidenciaTipos(sujeto, null, cadena, null);

        if (listaTipos.size() > 14) {
            grid.setPageSize(14);
        } else if (listaTipos.size() > 0) {
            grid.setPageSize(listaTipos.size());
        }
        grid.setHeightByRows(true);
        grid.setItems(listaTipos);

    }

    @Override
    public void doBinderPropiedades() {
        lopdTipoBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(LopdTipoBean::getId, null);

        lopdTipoBinder.forField(sujetoCombo)
                .asRequired()
                .bind(LopdTipoBean::getSujeto, LopdTipoBean::setSujeto);

        lopdTipoBinder.forField(descripcion)
                .withNullRepresentation("")
                .asRequired()
                .bind(LopdTipoBean::getDescripcion, LopdTipoBean::setDescripcion);

        lopdTipoBinder.forField(mailReponsable)
                .bind(LopdTipoBean::getMailReponsable, LopdTipoBean::setMailReponsable);

        lopdTipoBinder.forField(mailReponsable)
                .bind(LopdTipoBean::getMailReponsable, LopdTipoBean::setMailReponsable);

        lopdTipoBinder.forField(estado)
                .bind(LopdTipoBean::isEstado, LopdTipoBean::setEstado);

        lopdTipoBinder.forField(fechaCambio)
                .bind(LopdTipoBean::getFechaCambio, null);

        usuarioBinder.forField(usuCambio)
                .bind(UsuarioBean::getDni, null);
        usuarioBinder.forField(usuCambioNombre)
                .bind(UsuarioBean::getApellidosNombre, null);

    }

    @Override
    public void doComponenesAtributos() {
        id.setReadOnly(true);

        estado.setValue(Boolean.TRUE);

        buscador.setLabel("Buscar");

    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("60px", 1));
        contenedorIzquierda.removeAll();
        contenedorIzquierda.add(contenedorBotones, contenedorFormulario);
        contenedorFormulario.add(id, descripcion, sujetoCombo, mailReponsable, estado, fechaCambio, usuCambio, usuCambioNombre);

        contenedorBuscadores.add(buscador, sujetoComboBuscador);
        contenedorDerecha.add(grid);
    }

    @Override
    public void doCompentesEventos() {
        /**
         * Cuando salta del dni del usuario recupera los datos
         */
        usuCambio.addBlurListener(event -> {
            usuario = new UsuarioDao().getUsuarioDni(usuCambio.getValue(), Boolean.FALSE);
            if (usuario == null) {
                Notification.show(FrmMensajes.USUARIONOEXISTE + usuCambio.getValue());
                usuCambio.focus();
            } else {
                usuarioBinder.readBean(usuario);
            }
        });

        /*+
        * Selecciona un tipo en el grid
         */
        grid.addItemClickListener(event -> {
            tipoBean = event.getItem();
            usuario = tipoBean.getUsucambio();
            lopdTipoBinder.readBean(tipoBean);
            usuarioBinder.readBean(usuario);
            doControlBotones(tipoBean);
        });

        /**
         * Cambia el combo sujeto se recarga la lista de datos del grid
         */
        sujetoComboBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        /**
         * Cada vez que escribe en el buscador se recarga la lista de tipos del
         * grid
         */
        buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });
    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMensajes.AVISOCONFIRMACIONACCION,
                FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    tipoBean.setUsucambio(usuario);
                    tipoBean.setFechaCambio(LocalDate.now());
                    new LopdTipoDao().borraDatos(tipoBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                });
        dialog.open();
        doActualizaGrid();
    }

    @Override
    public void doImprimir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
