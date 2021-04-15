package es.sacyl.gsa.inform.ui.usuarios;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.AplicacionPerfilBean;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CategoriaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.bean.UsuarioPeticionAppBean;
import es.sacyl.gsa.inform.bean.UsuarioPeticionBean;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterConstantes;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import es.sacyl.gsa.inform.util.Constantes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

public final class FrmUsuariosPedir extends FrmMasterPantalla {

    private static final long serialVersionUID = 1L;

    /* Campos del formulario */
    TextField idSolicitante = new TextField();
    TextField nifSolicitante = new ObjetosComunes().getDni();
    TextField nombreSolicitante = new ObjetosComunes().getTextField("Nombre", "teclea nombre", 25, "100px", "30px");
    TextField apellido1Solicitante = new ObjetosComunes().getTextField("Apellido 1", "teclea primer apellido", 25, "100px", "30px");
    TextField apellido2Solicitante = new ObjetosComunes().getTextField("Apellido 2", "teclea segundo apellido", 25, "100px", "30px");
    TextField correoSolicitante = new ObjetosComunes().getMail("Correo Electrónico", "Correo del solicitante");
    TextField movilSolicitante = new ObjetosComunes().getMovil();
    TextField telefonoSolicitante = new ObjetosComunes().getTelefono();
    DatePicker fechaSolicitud = new ObjetosComunes().getDatePicker("Fecha Solicitud", "Fecha solicitud", LocalDate.now()); 
    TextField idUsuario = new TextField();
    TextField nombreUsuario = new ObjetosComunes().getTextField("Nombre", "teclea nombre", 25, "100px", "30px");
    TextField apellido1Usuario = new ObjetosComunes().getTextField("Apellido 1", "teclea primer apellido", 25, "100px", "30px");
    TextField apellido2Usuario = new ObjetosComunes().getTextField("Apellido 2", "teclea segundo apellido", 25, "100px", "30px");
    TextField nifUsuario = new ObjetosComunes().getDni();
    TextField correoUsuario = new ObjetosComunes().getMail("Correo Electrónico", "Correo del usuario");
    TextField telefonoUsuario = new ObjetosComunes().getTelefono();
    ComboBox<CategoriaBean> categoriaUsuario = new CombosUi().getCategoriasUsuarios(null);
    ComboBox<GfhBean> gfhUsuario = new CombosUi().getGfhPorCodigoUsuarios(null);
    ComboBox<String> tipo = new ComboBox();
    ComboBox<ProvinciaBean> provinciasCombo = new CombosUi().getProvinciaCombo(ProvinciaBean.PROVINCIA_DEFECTO, null,
            AutonomiaBean.AUTONOMIADEFECTO);
    CheckboxGroup<CentroTipoBean> tiposCentro = new ObjetosComunes().getTipoCentroCecheckboxGroup();
    CheckboxGroup<CentroBean> centro = new ObjetosComunes().getCentrosCheckboxGroup();
    Long idGaleno = new Long(9);
    RadioButtonGroup<AplicacionPerfilBean> perfilesGaleno
                = new ObjetosComunes().getAplicacionesPerfilesPorIdRadioButtonGroup(idGaleno);
    Long idJimena = new Long(8);
    RadioButtonGroup<AplicacionPerfilBean> perfilesJimena
                = new ObjetosComunes().getAplicacionesPerfilesPorIdRadioButtonGroup(idJimena);
    TextField comentario = new TextField();
    Accordion aplicacionesAccordion = new Accordion();

    /* Campos para el Grid */
    ComboBox<String> camposFiltro = new CombosUi().getStringCombo("Buscar por campo: ", null, ObjetosComunes.FiltroBusquedaUsuarios, "150px");

    /* Componentes */
    Grid<UsuarioBean> usuariosGrid = new Grid<>();
    UsuarioBean usuarioBean = new UsuarioBean();
    UsuarioBean solicitanteBean = new UsuarioBean();
    UsuarioPeticionBean peticionBean = new UsuarioPeticionBean();
    //UsuarioPeticionAppBean peticionAppBean = new UsuarioPeticionAppBean();
    Binder<UsuarioBean> usuarioBinder = new Binder<>();
    Binder<UsuarioBean> solicitanteBinder = new Binder<>();
    ArrayList<UsuarioBean> arrayListUsuarios = new ArrayList<>();
    ArrayList<UsuarioPeticionAppBean> aplicacionesArrayList = new ArrayList<>();
    
    public FrmUsuariosPedir() {
        super();
        setSizeFull();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades();
        construirAccordion();
        recuperaSolicitante();
    }

    
    @Override
    public void doGrabar() {            
            usuarioBinder.writeBeanIfValid(usuarioBean); 
            String centrosString = "";
            for (CentroBean cadena : centro.getValue()) {
                if (centrosString.length() > 1) {
                    centrosString = centrosString.concat(",");
                }
                centrosString = centrosString.concat(cadena.getId().toString());
            }            
            peticionBean.setCentros(centrosString);
            peticionBean.setComentario(comentario.getValue());
            if ((new UsuarioDao().doGrabaDatos(usuarioBean) == true) && 
                    (new UsuarioDao().doGrabaPeticion(usuarioBean, peticionBean) == true) && 
                    (new UsuarioDao().doGrabaPeticionApp(peticionBean, aplicacionesArrayList))) {
                (new Notification(FrmMasterConstantes.AVISODATOALMACENADO, 4000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMasterConstantes.AVISODATOERRORBBDD, 4000, Notification.Position.MIDDLE)).open();
            }           
    }
    
    @Override
    public void doCancelar() {
        this.removeAll();

    }

    @Override
    public void doBorrar() { 
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMasterConstantes.AVISOCONFIRMACIONACCION,
                FrmMasterConstantes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMasterConstantes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new UsuarioDao().doBorraDatos(usuarioBean);
                    Notification.show(FrmMasterConstantes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();
        dialog.addDialogCloseActionListener(e -> {
        });
    }

    @Override
    public void doAyuda() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doLimpiar() {
        usuarioBinder.readBean(null); 
        perfilesGaleno.removeAll();
    }

    @Override
    public void doGrid() {
        usuariosGrid.addColumn(UsuarioBean::getId).setHeader("ID");
        usuariosGrid.addColumn(UsuarioBean::getNombre).setHeader("Nombre");
        usuariosGrid.addColumn(UsuarioBean::getApellido1).setHeader("Apellido 1");
        usuariosGrid.addColumn(UsuarioBean::getApellido2).setHeader("Apellido 2");
        usuariosGrid.addColumn(UsuarioBean::getDni).setHeader("NIF");
        usuariosGrid.addColumn(UsuarioBean::getMail).setHeader("Correo electrónico");
        usuariosGrid.addColumn(UsuarioBean::getTelefono).setHeader("Teléfono");
        usuariosGrid.addColumn(UsuarioBean::getNombreCategoria).setHeader("Categoria");
        usuariosGrid.addColumn(UsuarioBean::getNombreGfh).setHeader("GFH");
        
        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        arrayListUsuarios = new UsuarioDao().getLista(null);
        usuariosGrid.setItems(arrayListUsuarios);
    }

    @Override
    public void doBinderPropiedades() {
        usuarioBinder.forField(idUsuario)
                .withConverter(new StringToLongConverter("Introducir un Long"))
                .bind(UsuarioBean::getId, UsuarioBean::setId);
        usuarioBinder.forField(nombreUsuario).asRequired("El nombre es obligatorio").bind(UsuarioBean::getNombre, UsuarioBean::setNombre);
        usuarioBinder.forField(apellido1Usuario).bind(UsuarioBean::getApellido1, UsuarioBean::setApellido1);
        usuarioBinder.forField(apellido2Usuario).bind(UsuarioBean::getApellido2, UsuarioBean::setApellido2);
        usuarioBinder.forField(nifUsuario)
                .asRequired("El NIF es obligatorio")
                .bind(UsuarioBean::getDni, UsuarioBean::setDni);
        usuarioBinder.forField(correoUsuario).bind(UsuarioBean::getMail, UsuarioBean::setMail);
        usuarioBinder.forField(telefonoUsuario).bind(UsuarioBean::getTelefono, UsuarioBean::setTelefono);
        usuarioBinder.forField(categoriaUsuario).bind(UsuarioBean::getCategoria, UsuarioBean::setCategoria);
        usuarioBinder.forField(gfhUsuario).bind(UsuarioBean::getGfh, UsuarioBean::setGfh);
        usuarioBinder.forField(tipo).bind(UsuarioBean::getTipo, UsuarioBean::setTipo);
        usuarioBinder.forField(comentario).bind(UsuarioBean::getComentario, UsuarioBean::setComentario);
        
        solicitanteBinder.forField(idSolicitante)
                .withConverter(new StringToLongConverter("Introducir un Long"))
                .bind(UsuarioBean::getId, UsuarioBean::setId);
        solicitanteBinder.forField(nombreSolicitante).asRequired("El nombre es obligatorio").bind(UsuarioBean::getNombre, UsuarioBean::setNombre);
        solicitanteBinder.forField(apellido1Solicitante).bind(UsuarioBean::getApellido1, UsuarioBean::setApellido1);
        solicitanteBinder.forField(apellido2Solicitante).bind(UsuarioBean::getApellido2, UsuarioBean::setApellido2);
        solicitanteBinder.forField(nifSolicitante)
                .asRequired("El NIF es obligatorio")
                .bind(UsuarioBean::getDni, UsuarioBean::setDni);
        solicitanteBinder.forField(correoSolicitante).bind(UsuarioBean::getMail, UsuarioBean::setMail);
        solicitanteBinder.forField(telefonoSolicitante).bind(UsuarioBean::getTelefono, UsuarioBean::setTelefono);
    }

    @Override
    public void doComponenesAtributos() {
        buscador.focus();
        buscador.setLabel("Texto de la búsqueda:");
        aplicacionesAccordion.setSizeFull();
        aplicacionesAccordion.close();
        idUsuario.setValue("0");
        centro.setVisible(false);  
        tipo.setItems("Alta", "Baja");
        tipo.setLabel("Tipo");
        tipo.setPlaceholder("Alta o Baja");
        comentario.setWidthFull();
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(aplicacionesAccordion);
        contenedorFormulario.setSizeFull();
        contenedorBuscadores.add(camposFiltro, buscador);
        contenedorDerecha.setSizeFull();
        contenedorDerecha.add(usuariosGrid);
    }

    @Override
    public void doCompentesEventos() {
        usuariosGrid.addItemClickListener(event -> {
            usuarioBean = event.getItem();
            usuarioBinder.readBean(usuarioBean);
        });

        nifUsuario.addBlurListener(event -> {
            if (!nifUsuario.getValue().isEmpty() && nifUsuario.getValue() != null) {
                usuarioBean = new UsuarioDao().getUsuarioPersigo(nifUsuario.getValue());
                if (usuarioBean.getDni() != null) {
                    usuarioBinder.readBean(usuarioBean); 
                    categoriaUsuario.setValue(usuarioBean.getCategoria());
                }
            } 
        });

        tiposCentro.addValueChangeListener(event -> {            
            doCargaCentros(tiposCentro.getSelectedItems());
        });
        
        perfilesGaleno.addValueChangeListener(event -> {
            UsuarioPeticionAppBean perfil = new UsuarioPeticionAppBean();
            perfil.setIdAplicacion(idGaleno);
            perfil.setIdPerfil(event.getValue().getId());
            aplicacionesArrayList.add(perfil);
        });
        
        perfilesJimena.addValueChangeListener(event -> {
            UsuarioPeticionAppBean perfil = new UsuarioPeticionAppBean();
            perfil.setIdAplicacion(idJimena);
            perfil.setIdPerfil(event.getValue().getId());
            aplicacionesArrayList.add(perfil);
        });
        
//        buscador.addBlurListener(event -> {
//            if (buscador.getValue().isEmpty() && camposFiltro.getValue() == null) {
//                arrayListUsuarios = new UsuarioDao().getLista(null);
//            } else if (!buscador.getValue().isEmpty() && camposFiltro.getValue() != null) {
//                arrayListUsuarios = new UsuarioDao().getUsuariosFiltro(buscador.getValue().trim(), camposFiltro.getValue());
//            } else if (buscador.getValue().isEmpty() && camposFiltro.getValue() != null) {
//                arrayListUsuarios = new UsuarioDao().getUsuariosFiltro(null, camposFiltro.getValue());
//            } else if (!buscador.getValue().isEmpty() && camposFiltro.getValue() == null) {
//                arrayListUsuarios = new UsuarioDao().getUsuariosFiltro(buscador.getValue().trim(), null);
//            }
//            usuariosGrid.setItems(arrayListUsuarios);
//        });
    }

    @Override
    public void doImprimir() {

    }

    public void construirAccordion() {
        FormLayout solicitanteLayout = new FormLayout();
        solicitanteLayout.setResponsiveSteps(
            new ResponsiveStep("25em", 1), 
            new ResponsiveStep("32em", 2),
            new ResponsiveStep("40em", 3));        
        solicitanteLayout.add(nifSolicitante, nombreSolicitante, apellido1Solicitante, apellido2Solicitante, 
                movilSolicitante, telefonoSolicitante, fechaSolicitud);
        aplicacionesAccordion.add("Datos del Solicitante", solicitanteLayout);
        
        FormLayout usuarioLayout = new FormLayout();
        
        usuarioLayout.add(nifUsuario, nombreUsuario, apellido1Usuario, apellido2Usuario, correoUsuario, 
                telefonoUsuario, categoriaUsuario, gfhUsuario, tipo);
        aplicacionesAccordion.add("Datos del Usuario", usuarioLayout);
        
        VerticalLayout tipoCentroLayout = new VerticalLayout();
        tipoCentroLayout.add(tiposCentro);
        aplicacionesAccordion.add("Tipo Centro", tipoCentroLayout);

        VerticalLayout centrosLayout = new VerticalLayout();
        centrosLayout.add(centro);
        aplicacionesAccordion.add("Centros", centrosLayout);

        VerticalLayout galenoLayout = new VerticalLayout();
        galenoLayout.add(perfilesGaleno);
        aplicacionesAccordion.add("Galeno", galenoLayout);

        VerticalLayout jimenaLayout = new VerticalLayout();
        jimenaLayout.add(perfilesJimena);
        aplicacionesAccordion.add("Jimena", jimenaLayout);
        
        VerticalLayout comentarioLayout = new VerticalLayout();
        comentarioLayout.add(comentario);
        aplicacionesAccordion.add("Comentario", comentarioLayout);
    }

    private void doCargaCentros(Set<CentroTipoBean> c) {
        ArrayList<CentroBean> alc = new ArrayList();
        for (CentroTipoBean ct : c) {
            alc.addAll(
                    new CentroDao().getLista(null, AutonomiaBean.AUTONOMIADEFECTO, ProvinciaBean.PROVINCIA_DEFECTO,
                            null, null, ct, null, ConexionDao.BBDD_ACTIVOSI));
        }
        
        centro.setVisible(true);
        centro.setItems(alc);
    }

    private void recuperaSolicitante() {
        solicitanteBean = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
        nifSolicitante.setValue(solicitanteBean.getDni());
        nombreSolicitante.setValue(solicitanteBean.getNombre());
        apellido1Solicitante.setValue(solicitanteBean.getApellido1());
        apellido2Solicitante.setValue(solicitanteBean.getApellido2());
        correoSolicitante.setValue(solicitanteBean.getMail());
        telefonoSolicitante.setValue(solicitanteBean.getTelefono());
        
        peticionBean.setIdpeticionario(solicitanteBean.getId());        
    }
}
