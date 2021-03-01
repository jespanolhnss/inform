package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.IpBean;
import es.sacyl.gsa.inform.bean.VlanBean;
import es.sacyl.gsa.inform.dao.IpDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.GridUi;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
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
public final class FrmBuscaIp extends Dialog {

    private final PaginatedGrid<IpBean> ipGrid = new GridUi().getIpGrid();

    private final ComboBox<VlanBean> vlanCombo = new CombosUi().getVlanCombo(null, null);
//    private final RadioButtonGroup<String> ipLibre = new ObjetosComunes().getSNRadio("Ip Libre");

    private final Button botonCancelar = new ObjetosComunes().getBoton("Cancelar", null, VaadinIcon.CLOSE_CIRCLE.create());
    private final Button botonGrabar = new ObjetosComunes().getBoton("Graba", null, VaadinIcon.CHECK_CIRCLE.create());

    public FrmBuscaIp(EquipoBean equipoBean) {
        if (equipoBean == null || equipoBean.getId() == null) {
            Notification.show("Sin equipo en la llamada ");
            this.close();
        }
        this.setWidth("900px");
        this.setHeight("600px");
        doCompentesEventos();

        this.add(vlanCombo, botonGrabar, botonCancelar, getDetalleEquipo(equipoBean), ipGrid);

        if (equipoBean.getListaIps().size() > 0) {
            vlanCombo.setValue(equipoBean.getListaIps().get(0).getVlan());
            doActualizaGrid(equipoBean);
        }
        ipGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        ipGrid.setPageSize(10);
        vlanCombo.addValueChangeListener(evento -> {
            if (vlanCombo.getValue() != null) {
                // primero las ip asignadas al  equipo
                doActualizaGrid(equipoBean);

            }
        });

        botonGrabar.addClickListener(event -> {
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
        });
    }

    public void doActualizaGrid(EquipoBean equipoBean) {
        ArrayList<IpBean> lista = new ArrayList<>();
        lista.addAll(equipoBean.getListaIps());
        lista.addAll(new IpDao().getLista(null, vlanCombo.getValue(), null, null, IpBean.IPLIBRESI));
        ipGrid.setItems(lista);

    }

    public void doCompentesEventos() {
        botonCancelar.addClickListener(even -> {
            ipGrid.deselectAll();
            this.close();
        });

    }

    public Details getDetalleEquipo(EquipoBean equipoBean) {
        String datos = "";
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

}
