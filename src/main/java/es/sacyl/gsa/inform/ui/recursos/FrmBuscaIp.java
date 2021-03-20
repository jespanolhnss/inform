package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.IpBean;
import es.sacyl.gsa.inform.bean.VlanBean;
import es.sacyl.gsa.inform.dao.IpDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.GridUi;
import java.util.ArrayList;
import java.util.Set;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 *
 * Muestras las ip asigndas al equipo y las libres de la vlan
 *
 */
public final class FrmBuscaIp extends FrmMasterVentana {

    private final PaginatedGrid<IpBean> ipGrid = new GridUi().getIpGrid();
    private final ComboBox<VlanBean> vlanCombo = new CombosUi().getVlanCombo(null, null);
//    private final RadioButtonGroup<String> ipLibre = new ObjetosComunes().getSNRadio("Ip Libre");
    private ArrayList<IpBean> ipArrayList = new ArrayList<>();
    private EquipoBean equipoBean = null;

    public FrmBuscaIp(EquipoBean equipoBean) {
        // super("900px");
        // this.setHeight("600px");
        if (equipoBean == null || equipoBean.getId() == null) {
            Notification.show("Sin equipo en la llamada ");
            this.close();
        } else {
            this.equipoBean = equipoBean;
        }

        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
        doControlBotones(null);

        /**
         * Si el equipo tiene ip, selecciona la vlan en el comobo
         */
        if (equipoBean.getListaIps().size() > 0) {
            vlanCombo.setValue(equipoBean.getListaIps().get(0).getVlan());
            doActualizaGrid(equipoBean);
        }

    }

    /**
     *
     * @param equipoBean La lista de ips la forma con las ips del equipo acutal
     * mas las libres y/o ocupadas de la vlan elegida según la selección
     */
    public void doActualizaGrid(EquipoBean equipoBean) {
        ArrayList<IpBean> lista = new ArrayList<>();
        lista.addAll(equipoBean.getListaIps());
        lista.addAll(new IpDao().getLista(null, vlanCombo.getValue(), null, null, IpBean.IPLIBRESI));
        ipGrid.setItems(lista);
    }

    /**
     *
     */
    @Override
    public void doCompentesEventos() {
        vlanCombo.addValueChangeListener(evento -> {
            if (vlanCombo.getValue() != null) {
                // primero las ip asignadas al  equipo
                doActualizaGrid(equipoBean);
            }
        });
    }

    /**
     *
     */
    @Override
    public void doActualizaGrid() {
        doActualizaGrid(equipoBean);

    }

    public Details getDetalleEquipo(EquipoBean equipoBean) {
        Details component = new Details(new Span("Equipo:" + equipoBean.getTipo()),
                new Html(equipoBean.toHtml()));
        component.setEnabled(false);
        component.setOpened(true);
        add(component);
        return component;
    }

    public Set<IpBean> getIps() {
        return ipGrid.getSelectedItems();
    }

    @Override
    public void doGrabar() {
        for (IpBean ipBean : ipGrid.getSelectedItems()) {
            ipBean.setValoresAut();
            if (ipBean.getEquipo() == null) {
                ipBean.setEquipo(equipoBean);
                new IpDao().doActualizaEquipo(ipBean);
                equipoBean.getListaIps().add(ipBean);
            } else {
                //libera la ip del equipo si la tiene
                ipBean.setEquipo(null);
                new IpDao().doActualizaEquipo(ipBean);
                equipoBean.getListaIps().remove(ipBean);
            }
        }
        this.close();

    }

    @Override
    public void doBorrar() {
    }

    @Override
    public void doCancelar() {
        // desmarca todas las ip seleccionadas
        ipGrid.deselectAll();
        this.close();
    }

    @Override
    public void doCerrar() {
        this.close();
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
    }

    @Override
    public void doGrid() {
        ipGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        ipGrid.setPageSize(10);
        ipGrid.setPageSize(12);
    }

    @Override
    public void doBinderPropiedades() {
    }

    @Override
    public void doComponenesAtributos() {
        botonAyuda.setVisible(false);
        botonImprimir.setVisible(false);
        botonLimpiar.setVisible(false);
        botonBorrar.setVisible(false);

        ipGrid.setWidth("600px");
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.removeAll();
        contenedorIzquierda.add(getDetalleEquipo(equipoBean));
        contenedorFiltros.add(vlanCombo);
        contenedorDerecha.add(ipGrid);

    }

}
