/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui.usuarios;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToLongConverter;
import es.sacyl.gsa.inform.bean.UsuarioPeticionBean;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import java.util.ArrayList;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06532775Q
 */
public final class FrmPeticiones extends FrmMasterPantalla {
    
    private static final long serialVersionUID = 1L;
    
    TextField idUsuario = new TextField();
    TextField idPeticionario = new TextField();
    IntegerField estado = new IntegerField();
    TextField centro = new TextField();
    TextField comentario = new TextField();
    TextField tipo = new TextField();
    
    /* Componentes */
    UsuarioPeticionBean peticionBean = new UsuarioPeticionBean();
    Binder<UsuarioPeticionBean> peticionBinder;
    ArrayList<UsuarioPeticionBean> peticionArrayList;
    PaginatedGrid<UsuarioPeticionBean> gridPeticiones;
    
    public FrmPeticiones() {
        super();
        this.gridPeticiones = new PaginatedGrid<>();
        this.peticionArrayList = new ArrayList<>();
        this.peticionBinder = new Binder<>();        
        setSizeFull();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades();
    }

    @Override
    public void doGrabar() {
        
    }

    @Override
    public void doBorrar() {
        
    }

    @Override
    public void doAyuda() {
        
    }

    @Override
    public void doLimpiar() {
        
    }

    @Override
    public void doImprimir() {
        
    }

    @Override
    public void doGrid() {
        gridPeticiones.addColumn(UsuarioPeticionBean::getFechaSolicitud).setHeader("Fecha");
        gridPeticiones.addColumn(UsuarioPeticionBean::getIdusuario).setHeader("Usuario");
        gridPeticiones.addColumn(UsuarioPeticionBean::getTipo).setHeader("Tipo");
        gridPeticiones.addColumn(UsuarioPeticionBean::getComentario).setHeader("Comentario");
        doActualizaGrid();        
    }

    @Override
    public void doActualizaGrid() {
        
    }

    @Override
    public void doBinderPropiedades() {
        peticionBinder.forField(idUsuario)
                .withConverter(new StringToLongConverter("Introducir un Long"))
                .bind(UsuarioPeticionBean::getIdusuario, UsuarioPeticionBean::setIdusuario);
        peticionBinder.forField(idPeticionario)
                .withConverter(new StringToLongConverter("Introducir un Long"))
                .bind(UsuarioPeticionBean::getIdpeticionario, UsuarioPeticionBean::setIdpeticionario);
        peticionBinder.forField(estado).bind(UsuarioPeticionBean::getEstado, UsuarioPeticionBean::setEstado);
        peticionBinder.forField(tipo).bind(UsuarioPeticionBean::getTipo, UsuarioPeticionBean::setTipo);
    }

    @Override
    public void doComponenesAtributos() {
        
        
    }

    @Override
    public void doComponentesOrganizacion() {
        
    }

    @Override
    public void doCompentesEventos() {
        
    }
    
}