/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Image;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.bean.IpBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public class GridUi {

    public PaginatedGrid<IpBean> getIpGrid() {
        PaginatedGrid<IpBean> ipGrid = new PaginatedGrid<>();
        ipGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        ipGrid.setHeightByRows(true);
        ipGrid.setPageSize(14);
        ipGrid.addColumn(IpBean::getIp).setAutoWidth(true).setHeader(new Html("<b>Ip</b>")).setWidth("70px");
        ipGrid.addColumn(IpBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Est</b>")).setWidth("20px");
        ipGrid.addColumn(IpBean::getEquipoString).setAutoWidth(true).setHeader(new Html("<b>Equipo</b>"));

        ipGrid.addComponentColumn(e -> {
            if (e.getEquipo() == null) {
                Image im = new Image("icons/puntoverde.png", "Libre");
                im.setWidth("18px");
                im.setHeight("18px");
                return im;
            } else {
                Image im = new Image("icons/puntorojo.png", "Ocupads");
                im.setWidth("18px");
                im.setHeight("18ox");
                return im;
            }
        }).setHeader("Libre");

        return ipGrid;
    }

    public PaginatedGrid<UsuarioBean> getUsuarioGrid() {
        PaginatedGrid<UsuarioBean> usuGrid = new PaginatedGrid<>();
        usuGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        usuGrid.setHeightByRows(true);
        usuGrid.setPageSize(14);

        usuGrid.addColumn(UsuarioBean::getDni).setAutoWidth(true).setHeader(new Html("<b>Dni</b>"));
        usuGrid.addColumn(UsuarioBean::getApellidosNombre).setAutoWidth(true).setHeader(new Html("<b>Usuario</b>"));

        return usuGrid;
    }

    public PaginatedGrid<EquipoBean> getEquipoGrid() {

        PaginatedGrid<EquipoBean> equipoGrid = new PaginatedGrid<>();
        equipoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        equipoGrid.setHeightByRows(true);
        equipoGrid.setPageSize(14);
        equipoGrid.addColumn(EquipoBean::getTipo).setAutoWidth(true).setHeader(new Html("<b>Tipo</b>")).setWidth("70px");
        equipoGrid.addColumn(EquipoBean::getEstado).setAutoWidth(true).setHeader(new Html("<b>Est</b>")).setWidth("20px");
        equipoGrid.addColumn(EquipoBean::getInventario).setAutoWidth(true).setHeader(new Html("<b>Invent</b>")).setWidth("70px");
        equipoGrid.addColumn(EquipoBean::getMarca).setAutoWidth(true).setHeader(new Html("<b>Marca</b>"));
        equipoGrid.addColumn(EquipoBean::getModelo).setAutoWidth(true).setHeader(new Html("<b>Modelo</b>"));
        return equipoGrid;
    }
}
