/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui.usuarios;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.bean.UsuarioPeticionBean;
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

    Label peticionarioLbl = new Label();
    /* Componentes */
    UsuarioPeticionBean peticionBean = new UsuarioPeticionBean();
    UsuarioBean peticionario = new UsuarioBean();
    UsuarioBean usuario = new UsuarioBean();

    private final Details peticionarioDetalle = new ObjetosComunes().getDetails();
    private final Details usuarioDetalle = new ObjetosComunes().getDetails();
    // UsuarioGalenoBean pendientesBean = new UsuarioGalenoBean();
    // Binder<UsuarioGalenoBean> peticionBinder;
    ArrayList<UsuarioPeticionBean> peticionArrayList = new ArrayList<>();
    PaginatedGrid<UsuarioPeticionBean> gridPeticiones = new PaginatedGrid<>();
    PaginatedGrid<AplicacionBean> gridAplicaciones = new PaginatedGrid<>();

    public FrmPeticiones() {
        super();
        setSizeFull();
        doComponentesOrganizacion();
        doGrid();
        doActualizaGrid();
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

        gridPeticiones.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        gridPeticiones.setHeightByRows(true);
        gridPeticiones.setPageSize(25);
        gridPeticiones.addColumn(UsuarioPeticionBean::getUsuarioNombre).setAutoWidth(true).setHeader(new Html("<b>Usu</b>"));
        gridPeticiones.addColumn(UsuarioPeticionBean::getFechaSolicitud).setAutoWidth(true).setHeader(new Html("<b>Fecha</b>"));

    }

    @Override
    public void doActualizaGrid() {
        peticionArrayList = new UsuarioDao().getPeticionesPendientes();
        gridPeticiones.setItems(peticionArrayList);

    }

    @Override
    public void doBinderPropiedades() {
        /*
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
         */
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
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("50px", 2),
                new FormLayout.ResponsiveStep("50px", 3));
        contenedorFormulario.add(peticionarioDetalle, 3);
        contenedorFormulario.add(usuarioDetalle, 3);
        contenedorFormulario.add(usuarioDetalle, idUsuario, nombreUsuario, tipo, fechaSolicitud, idAplicacion, perfil);
        contenedorDerecha.add(gridPeticiones);

    }

    @Override
    public void doCompentesEventos() {
        gridPeticiones.addItemClickListener(event -> {
            Notification.show("Hola caracola", 14, Notification.Position.MIDDLE);
            peticionBean = event.getItem();
            usuario = peticionBean.getUsuario();
            peticionario = peticionBean.getPeticionario();
            peticionarioDetalle.setContent(new Html(peticionario.toHtml(".")));
            peticionarioDetalle.setSummary(new Label("Peticionario" + peticionario.getApellidosNombre()));
            peticionarioDetalle.setOpened(false);
            peticionarioLbl.setText(peticionario.toHtml("<br>"));
            usuarioDetalle.setContent(new Html(usuario.toHtml("<br>")));
            usuarioDetalle.setSummary(new Label("Usuario:" + usuario.getApellidosNombre()));
            usuarioDetalle.setOpened(false);
        });

    }

}
