/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui.usuarios;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToLongConverter;
import es.sacyl.gsa.inform.bean.GruposPaginasGalenoBean;
import es.sacyl.gsa.inform.bean.UsuarioGalenoBean;
import es.sacyl.gsa.inform.bean.UsuarioPeticionAppBean;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.ObjetosComunes;

/**
 *
 * @author 06532775Q
 */
public final class FrmGrabarAplicacion extends FrmMasterVentana{
    
    UsuarioPeticionAppBean peticion;
    UsuarioGalenoBean usuarioGaleno = new UsuarioGalenoBean();
    //Binder<UsuarioGalenoBean> grabarAplicacionBinder = new Binder();
    Binder<UsuarioPeticionAppBean> grabarAplicacionBinder = new Binder();
    
    TextField nombreUsuario = new ObjetosComunes().getTextField("Nombre");
    TextField apellido1Usuario = new ObjetosComunes().getTextField("Apellido1");
    TextField apellido2Usuario = new ObjetosComunes().getTextField("Apellido2");
    TextField dniUsuario = new ObjetosComunes().getTextField("NIF");
    TextField perfilUsuario = new ObjetosComunes().getTextField("Perfil");
    
    CheckboxGroup<GruposPaginasGalenoBean> gruposPaginasGaleno = new ObjetosComunes().
            getGruposPaginasGalenoCheckboxGroup();
    
    public FrmGrabarAplicacion() {
        super();
        setSizeFull();
        doComponentesOrganizacion();
        doGrid();
        doActualizaGrid();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades();
    }

    /**
     *
     * @param peticion
     */
    public FrmGrabarAplicacion(UsuarioPeticionAppBean peticion) {
        super();
        this.peticion = peticion;
        setSizeFull(); 
        contenedorVentana.remove(contenedorDerecha);
        doComponentesOrganizacion();
        doGrid();
        doActualizaGrid();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades(); 
        //grabarAplicacionBinder.readBean(peticion);
    }

    @Override
    public void doGrabar() {
        doCompletaDatos();
        
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
        this.removeAll();        
    }

    @Override
    public void doGrid() {
        
    }

    @Override
    public void doActualizaGrid() {
        
    }

    @Override
    public void doBinderPropiedades() {
        grabarAplicacionBinder.readBean(peticion);
        /*
        grabarAplicacionBinder.forField(nombreUsuario)
                .bind(UsuarioGalenoBean::getNombre,UsuarioGalenoBean::setNombre);
        grabarAplicacionBinder.forField(apellido1Usuario)
                .bind(UsuarioGalenoBean::getApellido1,UsuarioGalenoBean::setApellido1);        
        grabarAplicacionBinder.forField(apellido2Usuario)
                .bind(UsuarioGalenoBean::getApellido2,UsuarioGalenoBean::setApellido2);     
        grabarAplicacionBinder.forField(dniUsuario)
                .bind(UsuarioGalenoBean::getDni,UsuarioGalenoBean::setDni);     
        grabarAplicacionBinder.forField(perfilUsuario)
                .bind(UsuarioGalenoBean::getGrupousu,UsuarioGalenoBean::setGrupousu);   
        */
        grabarAplicacionBinder.forField(perfilUsuario)
                .withConverter(new StringToLongConverter("Introducir un Long"))
                .bind(UsuarioPeticionAppBean::getIdPerfil,UsuarioPeticionAppBean::setIdPerfil);  
               
    }

    @Override
    public void doComponenesAtributos() {
            
    }

    @Override
    public void doComponentesOrganizacion() {
        doCompletaDatos();
        contenedorFormulario.add(nombreUsuario,apellido1Usuario,apellido2Usuario);
        contenedorFormulario.add(dniUsuario,perfilUsuario);    
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3));
        
        if (peticion.getAplicacion().getNombre().equals("Galeno")) {
            contenedorFormulario.add(gruposPaginasGaleno,3);
        }
        
        //perfilUsuario.setValue(peticion.getPerfil().getNombre());
        
           
    }
    
    @Override
    public void doCompentesEventos() {
        
    }
    
    public void doCompletaDatos() {
        usuarioGaleno.setNombre(peticion.getPeticion().getUsuario().getNombre());
        usuarioGaleno.setApellido1(peticion.getPeticion().getUsuario().getApellido1());
        usuarioGaleno.setApellido2(peticion.getPeticion().getUsuario().getApellido2());
        usuarioGaleno.setDni(peticion.getPeticion().getUsuario().getDni());
        usuarioGaleno.setMovilUsuario(peticion.getPeticion().getUsuario().getMovilUsuario());
        usuarioGaleno.setMail(peticion.getPeticion().getUsuario().getMail());
        usuarioGaleno.setGrupousu(peticion.getPerfil().getId().toString());
        usuarioGaleno.setCentrohis(peticion.getPeticion().getCentros());
        

    }    
}
