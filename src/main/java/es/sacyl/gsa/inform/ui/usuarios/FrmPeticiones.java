/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui.usuarios;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import es.sacyl.gsa.inform.bean.PeticionesPendientesBean;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.time.LocalDate;
import java.util.ArrayList;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06532775Q
 */
public final class FrmPeticiones extends FrmMasterPantalla {
    
    private static final long serialVersionUID = 1L;
    
    TextField idUsuario = new TextField("Id Usuario");
    TextField nombreUsuario = new TextField("Nombre Usuario");
    TextField idPeticionario = new TextField("Peticionario");
    IntegerField estado = new IntegerField("Estado");
    TextField centro = new TextField("Centro");
    TextField comentario = new TextField("Comentario");
    TextField tipo = new TextField("Tipo");
    DatePicker fechaSolicitud = new ObjetosComunes()
            .getDatePicker("Fecha Solicitud", "Fecha solicitud", LocalDate.now());
    TextField idAplicacion = new TextField("Aplicación");
    TextField perfil = new TextField("Perfil");
    
    /* Componentes */
    //UsuarioPeticionBean peticionBean = new UsuarioPeticionBean();
    PeticionesPendientesBean pendientesBean = new PeticionesPendientesBean();
    Binder<PeticionesPendientesBean> peticionBinder;
    //ArrayList<UsuarioPeticionBean> peticionArrayList;
    PaginatedGrid<PeticionesPendientesBean> gridPeticiones;
    
    public FrmPeticiones() {
        super();
        this.gridPeticiones = new PaginatedGrid<>();
        //this.peticionArrayList = new ArrayList<>();
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
        //gridPeticiones.addColumn(PeticionesPendientesBean::getFechaSolicitud).setHeader("Fecha");
        gridPeticiones.addColumn(new LocalDateRenderer<>(PeticionesPendientesBean::getFechaSolicitud,"dd/MM/YYYY")).setHeader("Fecha");
        gridPeticiones.addColumn(PeticionesPendientesBean::getNombreUsuario).setHeader("Usuario");
        gridPeticiones.addColumn(PeticionesPendientesBean::getNombreAplicacion).setHeader("Aplicacion");       
        gridPeticiones.addColumn(PeticionesPendientesBean::getTipo).setHeader("Tipo"); 
        
        doActualizaGrid();        
    }

    @Override
    public void doActualizaGrid() {
        ArrayList<PeticionesPendientesBean> peticionesPendientes;
        peticionesPendientes = new UsuarioDao().getPeticionesPendientes();
        gridPeticiones.setItems(peticionesPendientes);
        
    }

    @Override
    public void doBinderPropiedades() {
        peticionBinder.forField(idUsuario)
                .withConverter(new StringToLongConverter("Introducir un Long"))
                .bind(PeticionesPendientesBean::getIdUsuario, PeticionesPendientesBean::setIdUsuario);
        peticionBinder.forField(estado)
                .bind(PeticionesPendientesBean::getEstado, PeticionesPendientesBean::setEstado);
        peticionBinder.forField(tipo).bind(PeticionesPendientesBean::getTipo, PeticionesPendientesBean::setTipo);
        peticionBinder.forField(fechaSolicitud)
                .bind(PeticionesPendientesBean::getFechaSolicitud, PeticionesPendientesBean::setFechaSolicitud);
        peticionBinder.forField(idAplicacion)
                .withConverter(new StringToLongConverter("Introducir un Long"))
                .bind(PeticionesPendientesBean::getIdAplicacion, PeticionesPendientesBean::setIdAplicacion);
        peticionBinder.forField(perfil)
                .withConverter(new StringToIntegerConverter("Solo números"))
                .bind(PeticionesPendientesBean::getIdPerfil, PeticionesPendientesBean::setIdPerfil);        
    }

    @Override
    public void doComponenesAtributos() {
        contenedorDerecha.setSpacing(true);
        contenedorDerecha.setMargin(true); 
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3));
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(idUsuario,nombreUsuario,tipo,fechaSolicitud,idAplicacion,perfil);
        contenedorDerecha.add(gridPeticiones);
        
    }

    @Override
    public void doCompentesEventos() {
        gridPeticiones.addItemClickListener(event -> { 
            Notification.show("Hola caracola", 14, Notification.Position.MIDDLE);
            pendientesBean = event.getItem();
            nombreUsuario.setValue(pendientesBean.getNombreUsuario());
            peticionBinder.readBean(event.getItem());
        });
        
    }
    
}