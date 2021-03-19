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
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.dao.GfhDao;
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
public final class FrmGfh extends FrmMasterPantalla {

    private final TextField id = new ObjetosComunes().getTextField("Id");
    private final TextField idjimena = new ObjetosComunes().getTextField("IdJimena");
    private final TextField codigo = new ObjetosComunes().getTextField("Código", "codigo", 10, "150px", "50px");
    private final TextField descripcion = new ObjetosComunes().getTextField("Nombre", "nombre", 10, "300px", "50px");
    private final RadioButtonGroup<String> estadoRadio = new ObjetosComunes().getEstadoRadio();
    private final RadioButtonGroup<String> asistencialRadio = new ObjetosComunes().getSNRadio("Asistencial");

    private GfhBean servicioBean = null;
    private final Binder<GfhBean> servicioBinder = new Binder<>();
    private final PaginatedGrid<GfhBean> servicioGrid = new PaginatedGrid<>();
    private ArrayList<GfhBean> servicioLista = new ArrayList<>();

    public FrmGfh() {
        super();
        this.servicioBean = new GfhBean();
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
            descripcion.focus();
        }
    }

    @Override
    public void doGrabar() {
        if (servicioBinder.writeBeanIfValid(servicioBean)) {
            if (servicioBean.getIdjimena() == null) {
                servicioBean.setIdjimena(new Long(0));
            }
            if (new GfhDao().doGrabaDatos(servicioBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<GfhBean> validate = servicioBinder.validate();
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
                    new GfhDao().doBorraDatos(servicioBean);
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
        servicioBean = new GfhBean();
        servicioBinder.readBean(servicioBean);
        codigo.setReadOnly(false);
        doControlBotones(null);
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
        servicioGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        servicioGrid.setHeightByRows(true);
        servicioGrid.setPageSize(14);
        servicioGrid.addColumn(GfhBean::getId).setAutoWidth(true).setHeader(new Html("<b>Id</b>"));
        servicioGrid.addColumn(GfhBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Estado</b>"));
        servicioGrid.addColumn(GfhBean::getCodigo).setAutoWidth(true).setHeader(new Html("<b>Cod</b>"));
        servicioGrid.addColumn(GfhBean::getDescripcion).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));

        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        servicioLista = new GfhDao().getLista(buscador.getValue(), null);
        servicioGrid.setItems(servicioLista);
    }

    @Override
    public void doBinderPropiedades() {
        servicioBinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(GfhBean::getId, null);
        servicioBinder.forField(codigo)
                .asRequired()
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 3))
                .bind(GfhBean::getCodigo, GfhBean::setCodigo);
        servicioBinder.forField(descripcion)
                .asRequired()
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 50))
                .bind(GfhBean::getDescripcion, GfhBean::setDescripcion);

        servicioBinder.forField(estadoRadio)
                .asRequired()
                .bind(GfhBean::getEstadoString, GfhBean::setEstado);

        servicioBinder.forField(asistencialRadio)
                .asRequired()
                .bind(GfhBean::getAsistencialString, GfhBean::setAsistencia);

        servicioBinder.forField(idjimena)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(GfhBean::getIdjimena, GfhBean::setIdjimena);
    }

    @Override
    public void doComponenesAtributos() {
        titulo.setText("GFH ");
        buscador.setTitle("Contenido a buscar");
    }

    @Override
    public void doComponentesOrganizacion() {
        this.contenedorFormulario.add(id, codigo, descripcion, estadoRadio, idjimena, asistencialRadio);
        this.contenedorBuscadores.add(buscador);
        this.contenedorDerecha.removeAll();
        this.contenedorDerecha.add(contenedorBuscadores, servicioGrid);
    }

    @Override
    public void doCompentesEventos() {
        buscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        buscador.addBlurListener(event -> {
            doActualizaGrid();
        });

        servicioGrid.addItemClickListener(event -> {
            servicioBean = event.getItem();
            servicioBinder.readBean(event.getItem());
            doControlBotones(servicioBean);
        }
        );
    }

}
