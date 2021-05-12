package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.bean.UbicacionBean;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.ProvinciaDao;
import es.sacyl.gsa.inform.dao.UbicacionDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author juannietopajares
 */
public final class FrmUbicacion extends FrmMasterPantalla {

    private final ComboBox<AutonomiaBean> autonomiaComboBuscador = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> provinciaComboBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    private final ComboBox<CentroTipoBean> centroTipoComboBuscador = new CombosUi().getCentroTipoCombo(CentroTipoBean.CENTROTIPODEFECTO);
    private ComboBox<UbicacionBean> ubicacionComboBuscador = null;

    private final ComboBox<CentroBean> centroCombo = new CombosUi().getCentroCombo(AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, null, null, CentroTipoBean.CENTROTIPODEFECTO, null, null);
    private final ComboBox<UbicacionBean> ubicacionCombo;

    private final TextField id = new ObjetosComunes().getTextField("Id");
    private final TextField descripcion = new ObjetosComunes().getTextField("Descripción");
    private final TextField descripcionfull = new ObjetosComunes().getTextField("Descripción Completa");
    private final TextField nivel = new ObjetosComunes().getTextField("Nivel");

    private UbicacionBean ubicacionBean = new UbicacionBean();

    private final Binder<UbicacionBean> ubicacionBinder = new Binder<>();
    //   private final PaginatedGrid<UbicacionBean> ubicacionGrid = new PaginatedGrid<>();
    private ArrayList<UbicacionBean> ubicacionArrayList = new ArrayList<>();
    //  private TreeGrid<UbicacionBean> ubicacionGrid = new TreeGrid<>();
    // Tree<UbicacionBean> tree = new Tree<>();

    private TreeGrid<UbicacionBean> grid = new TreeGrid<>();

    public FrmUbicacion(UbicacionBean ubicacionBeanParam) {
        super();
        this.ubicacionBean = ubicacionBeanParam;
        ubicacionCombo = new CombosUi().getUbicacionCombo(null,
                centroCombo.getValue(), null, null);
        ubicacionComboBuscador = new CombosUi().getUbicacionCombo(null,
                centroCombo.getValue(), null, null);

        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        doActualizaComboCentro();
        doComponentesOrganizacion();
        ubicacionBinder.readBean(ubicacionBean);
        if (centroCombo.getValue()==null){
            centroCombo.setOpened(true);
        }
    }

    @Override
    public void doGrabar() {
        if (ubicacionBinder.writeBeanIfValid(ubicacionBean)) {
            if (new UbicacionDao().doGrabaDatos(ubicacionBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                // doActualizaComboPadre();
                doLimpiar();
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
            // this.close();
        } else {
            BinderValidationStatus<UbicacionBean> validate = ubicacionBinder.validate();
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
                    new UbicacionDao().doBorraDatos(ubicacionBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    doActualizaGrid();
                    //    doActualizaComboPadre();
                    doLimpiar();
                });
        dialog.open();
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
        CentroBean centroActual = centroCombo.getValue();
        UbicacionBean ubicacionPadre = ubicacionBean.getPadre();
        ubicacionBean = new UbicacionBean();
        ubicacionBinder.readBean(ubicacionBean);
        centroCombo.setValue(centroActual);
        ubicacionCombo.setValue(ubicacionPadre);
        descripcion.clear();
        nivel.setValue("0");
        descripcionfull.clear();
        //  doActualizaGrid();
        // doActualizaComboPadre();
    }

    @Override
    public void doImprimir() {
    }

    @Override
    public void doGrid() {
//        ubicacionGrid.addHierarchyColumn(UbicacionBean::getPadreString)                 .setHeader("Padre");
/*
        ubicacionGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        ubicacionGrid.setHeightByRows(true);
        ubicacionGrid.setPageSize(14);
        ubicacionGrid.addColumn(UbicacionBean::getDescripcion).setAutoWidth(true).setHeader(new Html("<b>Código</b>"));
        ubicacionGrid.addColumn(UbicacionBean::getId).setAutoWidth(true).setHeader(new Html("<b>Nombre</b>"));
        doActualizaGrid();
         */

        grid.setItems(new UbicacionDao().getListaPadresCentro(centroCombo.getValue()),
                UbicacionBean::getHijos);
        grid.addHierarchyColumn(UbicacionBean::getPadreString)
                .setHeader("Ubicación Padre");
        grid.addColumn(UbicacionBean::getDescripcion).setHeader("Descripción");
        grid.addColumn(UbicacionBean::getNivel).setHeader("Nivel");
        grid.setHeightByRows(true);
        grid.setPageSize(35);

    }

    /**
     * Para optimiar sql el grid sólo lo actualiza cuando se cambia el
     * comboubicacionBuscador padre
     *
     */
    @Override
    public void doActualizaGrid() {

        ubicacionArrayList = new ArrayList<>();
        ubicacionArrayList = new UbicacionDao().getListaHijos(ubicacionComboBuscador.getValue());
        grid.setItems(ubicacionArrayList,
                UbicacionBean::getHijos);
        grid.expand(ubicacionArrayList);
        grid.expandRecursively(ubicacionArrayList, 5);
        doActualizaComboPadre();

    }

    public void doActualizaComboPadre() {
        ArrayList<UbicacionBean> lista = new UbicacionDao().getListaHijos(ubicacionComboBuscador.getValue());
        ubicacionCombo.setItems(lista);
    }

    @Override
    public void doBinderPropiedades() {

        ubicacionBinder.forField(id)
                .withNullRepresentation("")
                .asRequired()
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(UbicacionBean::getId, null);

        ubicacionBinder.forField(centroCombo)
                .asRequired()
                .bind(UbicacionBean::getCentro, UbicacionBean::setCentro);

        ubicacionBinder.forField(descripcion)
                .withNullRepresentation("")
                .asRequired()
                .bind(UbicacionBean::getDescripcion, UbicacionBean::setDescripcion);

        ubicacionBinder.forField(nivel)
                .withNullRepresentation("")
                .asRequired()
                .withConverter(new StringToIntegerConverter(FrmMensajes.AVISONUMERO))
                .bind(UbicacionBean::getNivel, UbicacionBean::setNivel);

        ubicacionBinder.forField(ubicacionCombo)
                .bind(UbicacionBean::getPadre, UbicacionBean::setPadre);

        ubicacionBinder.forField(descripcionfull)
                .bind(UbicacionBean::getDescripcionFull, null);
    }

    @Override
    public void doComponenesAtributos() {
        autonomiaComboBuscador.setVisible(false);
    }

    @Override
    public void doComponentesOrganizacion() {

        /*
        grid.addExpandListener(event -> message.setValue(
                String.format("Expanded %s item(s)", event.getItems().size())
                + "\n" + message.getValue()));
        grid.addCollapseListener(event -> message.setValue(
                String.format("Collapsed %s item(s)", event.getItems().size())
                + "\n" + message.getValue()));
         */
        contenedorBuscadores.add(autonomiaComboBuscador, provinciaComboBuscador, centroTipoComboBuscador, ubicacionComboBuscador);
        contenedorFormulario.add(centroCombo, id, descripcion, ubicacionCombo, nivel, descripcionfull);
        contenedorDerecha.add(grid);
    }

    @Override
    public void doCompentesEventos() {
        autonomiaComboBuscador.addValueChangeListener(event -> {
            provinciaComboBuscador.setItems(new ProvinciaDao().getLista(null, event.getValue()));
        });
        centroTipoComboBuscador.addValueChangeListener(event -> {
            doActualizaComboCentro();
        });

        /**
         * Cuando elige centro actualiza el combo buscador padre
         */
        centroCombo.addValueChangeListener(event -> {
            //     doActualizaGrid();
            //  doActualizaComboPadre();

            if (centroCombo.getValue() != null) {
                ubicacionComboBuscador.setItems(new UbicacionDao().getListaPadresCentro(centroCombo.getValue()));
                ubicacionComboBuscador.focus();
                ubicacionComboBuscador.setOpened(true);
              
            } else {
                centroCombo.focus();
            }

        });
        ubicacionComboBuscador.addValueChangeListener(event -> {
            if (ubicacionComboBuscador.getValue()!=null) {
            doActualizaGrid();
              descripcion.focus();
            } else {
                ubicacionComboBuscador.setOpened(true);
            }
            //   doActualizaComboPadre();
        });

        ubicacionCombo.addValueChangeListener(event -> {
            if (event != null && event.getValue() != null && event.getValue().getNivel() != null) {
                nivel.setValue(Integer.toString(event.getValue().getNivel() + 1));
            } else {
                nivel.setValue("0");
            }
        });

        grid.addItemClickListener(event -> {
            ubicacionBean = event.getItem();
            ubicacionBinder.readBean(ubicacionBean);
        }
        );
    }

    public void doActualizaComboCentro() {
        centroCombo.setItems(new CentroDao().getLista(null, autonomiaComboBuscador.getValue(),
                provinciaComboBuscador.getValue(), null, null, centroTipoComboBuscador.getValue(), null, ConexionDao.BBDD_ACTIVOSI));
    }

}
