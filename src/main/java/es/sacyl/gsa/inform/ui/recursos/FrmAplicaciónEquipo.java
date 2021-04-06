package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToLongConverter;
import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.EquipoAplicacionBean;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ComboDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.EquipoAplicacionDao;
import es.sacyl.gsa.inform.dao.EquipoDao;
import es.sacyl.gsa.inform.dao.ProvinciaDao;
import es.sacyl.gsa.inform.ui.CombosUi;
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
public final class FrmAplicaciónEquipo extends FrmMasterVentana {

    private final ComboBox<String> equipoTipoComboBuscador = new CombosUi().getEquipoTipoCombo(null, 50);

    private final ComboBox<AutonomiaBean> autonomiaComboBuscador = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> provinciaComboBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    private final ComboBox<CentroTipoBean> centroTipoComboBuscador = new CombosUi().getCentroTipoCombo(null);
    private final ComboBox<String> equipoMarcaComboBuscador = new CombosUi().getGrupoRamaComboValor(ComboBean.TIPOEQUIPOMARCA, equipoTipoComboBuscador.getValue(), null, "Marca");

    private final ComboBox<CentroBean> centroComboBuscador = new CombosUi().getCentroCombo(AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, null, null, CentroTipoBean.CENTROTIPOCENTROSALUD, null, null);
    private final PaginatedGrid<EquipoBean> equipoGrid = new GridUi().getEquipoGridPaginado();

    private final TextField id = new ObjetosComunes().getTextField("Id");
    private final DatePicker fecha = new ObjetosComunes().getDatePicker("Fecha instalación", null, null);
    private final TextArea comentario = new TextArea();

    private final TextField buscador = new ObjetosComunes().getTextField("Valor a buscar");

    private final Details equipoDetalle = new ObjetosComunes().getDetails();
    private AplicacionBean aplicacionBean = new AplicacionBean();
    private EquipoBean equipoBean = new EquipoBean();
    private EquipoAplicacionBean equipoAplicacionBean = new EquipoAplicacionBean();
    private final Binder<EquipoAplicacionBean> equipoAplicacionBinder = new Binder<>();
    private final ArrayList<EquipoAplicacionBean> equipoAplicacionArrayList = new ArrayList<>();

    public FrmAplicaciónEquipo(AplicacionBean aplicacion) {
        this.setAriaLabel("afafadadf");
        titulo.setText(aplicacion.getNombre());
        this.aplicacionBean = aplicacion;
        equipoAplicacionBean.setAplicacion(aplicacion);
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        doActualizaComboCentro();
        equipoAplicacionBinder.readBean(equipoAplicacionBean);
    }

    @Override
    public void doGrabar() {
        if (equipoAplicacionBinder.writeBeanIfValid(equipoAplicacionBean) && equipoBean != null && equipoAplicacionBean.getId() != null) {
            equipoAplicacionBean.setEquipo(equipoBean);
            if (new EquipoAplicacionDao().doGrabaDatos(equipoAplicacionBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doLimpiar();
                this.close();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<EquipoAplicacionBean> validate = equipoAplicacionBinder.validate();
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
        equipoAplicacionBean = new EquipoAplicacionBean();
        equipoAplicacionBean.setAplicacion(aplicacionBean);
        equipoAplicacionBinder.readBean(equipoAplicacionBean);
        equipoDetalle.setContent(new Html(""));
    }

    @Override
    public void doGrid() {
    }

    @Override
    public void doActualizaGrid() {
        ArrayList<EquipoBean> equipoArrayList = new EquipoDao().getLista(buscador.getValue(), equipoTipoComboBuscador.getValue(),
                equipoMarcaComboBuscador.getValue(), centroComboBuscador.getValue(), null, null, aplicacionBean);
        equipoGrid.setItems(equipoArrayList);
    }

    @Override
    public void doBinderPropiedades() {

        equipoAplicacionBinder.forField(id)
                .withNullRepresentation("")
                .asRequired()
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(EquipoAplicacionBean::getId, null);

        equipoAplicacionBinder.forField(fecha)
                .asRequired()
                .bind(EquipoAplicacionBean::getFecha, EquipoAplicacionBean::setFecha);

        equipoAplicacionBinder.forField(comentario)
                .bind(EquipoAplicacionBean::getComentario, EquipoAplicacionBean::setComentario);

    }

    @Override
    public void doComponenesAtributos() {
        buscador.setTitle("Valor a buscar");
    }

    @Override
    public void doComponentesOrganizacion() {

        contenedorFiltros.add(provinciaComboBuscador, centroTipoComboBuscador, equipoMarcaComboBuscador, centroComboBuscador, buscador);
        contenedorDerecha.add(equipoGrid);

        contenedorFormulario.add(id, fecha, comentario, equipoDetalle);
    }

    @Override
    public void doCompentesEventos() {
        autonomiaComboBuscador.addValueChangeListener(event -> {
            AutonomiaBean autonomiaBean = event.getValue();
            doActualizaComboProvinicas(provinciaComboBuscador, autonomiaBean);
        });
        equipoTipoComboBuscador.addValueChangeListener(evetn -> {
            doActualizaGrid();
        });
        equipoTipoComboBuscador.addValueChangeListener(event -> {
            equipoMarcaComboBuscador.setItems(new ComboDao().getListaGruposRamaValor(ComboBean.TIPOEQUIPOMARCA, event.getValue(), 100));
            doActualizaGrid();
        });
        provinciaComboBuscador.addValueChangeListener(event -> {
            doActualizaComboCentro();
        });
        centroTipoComboBuscador.addValueChangeListener(event -> {
            doActualizaComboCentro();
        });
        centroComboBuscador.addValueChangeListener(event -> {
            doActualizaGrid();
        });

        buscador.addBlurListener(evento -> {
            doActualizaGrid();
        });
        equipoGrid.addItemClickListener(event -> {
            equipoBean = event.getItem();
            equipoDetalle.setContent(new Html(equipoBean.toHtml()));

        });
    }

    public void doActualizaComboProvinicas(ComboBox<ProvinciaBean> combo, AutonomiaBean autonomia) {
        ArrayList<ProvinciaBean> privinciaArrayList = new ProvinciaDao().getLista(null, autonomia);
        combo.setItems(privinciaArrayList);
    }

    public void doActualizaComboCentro() {
        ArrayList<CentroBean> centroArrayList = new CentroDao().getLista(null, autonomiaComboBuscador.getValue(),
                provinciaComboBuscador.getValue(), null, null, centroTipoComboBuscador.getValue(), null, ConexionDao.BBDD_ACTIVOSI);

        centroComboBuscador.setItems(centroArrayList);
        if (centroArrayList.size() > 0) {
            centroComboBuscador.setValue(centroArrayList.get(0));
        }
    }
}
