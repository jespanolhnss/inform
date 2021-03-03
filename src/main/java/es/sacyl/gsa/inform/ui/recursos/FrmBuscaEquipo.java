/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.EquipoDao;
import es.sacyl.gsa.inform.dao.ProvinciaDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.GridUi;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public class FrmBuscaEquipo extends Dialog {

    private final ComboBox<String> equipoTipoComboBuscador = new CombosUi().getEquipoTipoCombo(null, 50);

    private final ComboBox<AutonomiaBean> autonomiaComboBuscador = new CombosUi().getAutonomiaCombo(AutonomiaBean.AUTONOMIADEFECTO, null);
    private final ComboBox<ProvinciaBean> provinciaComboBuscador = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null, AutonomiaBean.AUTONOMIADEFECTO);
    private final ComboBox<CentroTipoBean> centroTipoComboBuscador = new CombosUi().getCentroTipoCombo(null);
    private final ComboBox<CentroBean> centroComboBuscador = new CombosUi().getCentroCombo(AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO, null, null, CentroTipoBean.CENTROTIPOCENTROSALUD, null, null);
    private final PaginatedGrid<EquipoBean> equipoGrid = new GridUi().getEquipoGrid();

    private final Button botonCancelar = new ObjetosComunes().getBoton("Cancelar", null, VaadinIcon.CLOSE_CIRCLE.create());

    private HorizontalLayout filabuscadores = new HorizontalLayout();

    private EquipoBean equipoBean = new EquipoBean();

    public FrmBuscaEquipo() {
        this.add(filabuscadores);
        filabuscadores.add(provinciaComboBuscador, centroTipoComboBuscador, centroComboBuscador);
        this.add(equipoGrid);
        doActualizaGrid();
        autonomiaComboBuscador.addValueChangeListener(event -> {
            AutonomiaBean autonomiaBean = event.getValue();
            doActualizaComboProvinicas(provinciaComboBuscador, autonomiaBean);
        });
        equipoTipoComboBuscador.addValueChangeListener(evetn -> {
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

        equipoGrid.addItemClickListener(event -> {
            equipoBean = event.getItem();
            this.close();
        });

    }

    public EquipoBean getEquipoBean() {
        return equipoBean;
    }

    public void setEquipoBean(EquipoBean equipoBean) {
        this.equipoBean = equipoBean;
    }

    public void doActualizaComboProvinicas(ComboBox<ProvinciaBean> combo, AutonomiaBean autonomia) {
        ArrayList<ProvinciaBean> privinciaArrayList = new ProvinciaDao().getLista(null, autonomia);
        combo.setItems(privinciaArrayList);
    }

    public void doActualizaGrid() {
        ArrayList<EquipoBean> equipoArrayList = new EquipoDao().getLista(null, equipoTipoComboBuscador.getValue(),
                centroComboBuscador.getValue(), null, null);
        equipoGrid.setItems(equipoArrayList);
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
