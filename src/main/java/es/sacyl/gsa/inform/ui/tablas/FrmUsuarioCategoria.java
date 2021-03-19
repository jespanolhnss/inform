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
import es.sacyl.gsa.inform.bean.CategoriaBean;
import es.sacyl.gsa.inform.dao.CategoriaDao;
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
public class FrmUsuarioCategoria extends FrmMasterPantalla {

    private final TextField id = new ObjetosComunes().getTextField("Id");
    private final TextField codigo = new ObjetosComunes().getTextField("Código");
    private final TextField nombre = new ObjetosComunes().getTextField("Nombre");
    private final RadioButtonGroup<String> estadoRadio = new ObjetosComunes().getEstadoRadio();

    private CategoriaBean categoriaBean;
    private final Binder<CategoriaBean> usuarioCategoriaBinder = new Binder<>();
    private final PaginatedGrid<CategoriaBean> usuarioCategoriaGrid = new PaginatedGrid<>();
    private ArrayList<CategoriaBean> usuarioCategoriaArrayList = new ArrayList<>();

    public FrmUsuarioCategoria() {
        super();
        this.categoriaBean = new CategoriaBean();
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
    }

    @Override
    public void doGrabar() {
        if (usuarioCategoriaBinder.writeBeanIfValid(categoriaBean)) {
            if (new CategoriaDao().doGrabaDatos(categoriaBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<CategoriaBean> validate = usuarioCategoriaBinder.validate();
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
                    new CategoriaDao().doBorraDatos(categoriaBean);
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
        categoriaBean = new CategoriaBean();
        usuarioCategoriaBinder.readBean(categoriaBean);
        codigo.setReadOnly(false);
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        usuarioCategoriaGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        usuarioCategoriaGrid.setHeightByRows(true);
        usuarioCategoriaGrid.setPageSize(14);
        usuarioCategoriaGrid.addColumn(CategoriaBean::getId).setAutoWidth(true).setHeader(new Html("<b>Id</b>"));
        usuarioCategoriaGrid.addColumn(CategoriaBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Estado</b>"));
        usuarioCategoriaGrid.addColumn(CategoriaBean::getCodigopersigo).setAutoWidth(true).setHeader(new Html("<b>Código</b>"));
        usuarioCategoriaGrid.addColumn(CategoriaBean::getNombre).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        usuarioCategoriaArrayList = new CategoriaDao().getLista(buscador.getValue());
        usuarioCategoriaGrid.setItems(usuarioCategoriaArrayList);
    }

    @Override
    public void doBinderPropiedades() {
        usuarioCategoriaBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(CategoriaBean::getId, null);
        usuarioCategoriaBinder.forField(codigo)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 25))
                .bind(CategoriaBean::getCodigopersigo, CategoriaBean::setCodigopersigo);

        usuarioCategoriaBinder.forField(nombre)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 25))
                .bind(CategoriaBean::getNombre, CategoriaBean::setNombre);

        usuarioCategoriaBinder.forField(estadoRadio)
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 15))
                .bind(CategoriaBean::getEstadoString, CategoriaBean::setEstado);

    }

    @Override
    public void doComponenesAtributos() {
        codigo.setMaxLength(25);
        nombre.setMaxLength(25);
    }

    @Override
    public void doComponentesOrganizacion() {
        this.titulo.setText("Categorías de los usuarios");

        this.contenedorFormulario.add(id, codigo, nombre, estadoRadio);

        this.contenedorBuscadores.add(buscador);
        this.contenedorDerecha.removeAll();
        this.contenedorDerecha.add(contenedorBuscadores, usuarioCategoriaGrid);
    }

    @Override
    public void doCompentesEventos() {
        buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });
        usuarioCategoriaGrid.addItemClickListener(event -> {
            categoriaBean = event.getItem();
            usuarioCategoriaBinder.readBean(event.getItem());
            doControlBotones(categoriaBean);
        }
        );
    }

    /*

    WebApplicationContext appCtx = ((WebApplicationContext) this.getContext());
    ServletContext servletCtx = appCtx.getHttpSession().getServletContext();

     */
}
