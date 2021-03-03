package es.sacyl.gsa.inform.ui.usuarios;

import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterConstantes;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FrmUsuariosPedir extends FrmMasterPantalla {

    private static final long serialVersionUID = 1L;

    /* Campos del formulario */
    TextField nombreUsuario = new ObjetosComunes().getTextField("Nombre", "teclea nombre", 25, "100px", "30px");
    TextField apellido1Usuario = new ObjetosComunes().getTextField("Apellido 1", "teclea primer apellido", 25, "100px", "30px");
    TextField apellido2Usuario = new ObjetosComunes().getTextField("Apellido 2", "teclea segundo apellido", 25, "100px", "30px");
    TextField nifUsuario = new ObjetosComunes().getTextField("NIF", "teclea el NIF", 25, "50px", "30px");
    TextField correoUsuario = new ObjetosComunes().getTextField("Correo Electrónico", "teclea el correo electrónico", 25, "100px", "30px");
    ComboBox<String> categoriaUsuario = new CombosUi().getStringCombo("Categoria", null, ObjetosComunes.Categorias, "50px");
    CheckboxGroup<String> aplicacionesUsuario = new CheckboxGroup<String>();

    /* Campos para el Grid */
    ComboBox<String> camposFiltro = new CombosUi().getStringCombo("Buscar por campo: ", null, ObjetosComunes.FiltroBusquedaUsuarios, "150px");

    /* Componentes */
    Grid<UsuarioBean> usuariosGrid = new Grid<>();
    UsuarioBean usuarioBean = new UsuarioBean();
    Binder<UsuarioBean> usuarioBinder = new Binder<>();
    ArrayList<UsuarioBean> arrayListUsuarios = new ArrayList<>();

    public FrmUsuariosPedir() {
        super();
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doCompentesEventos();
        doBinderPropiedades();
    }

    @Override
    public void doGrabar() {
        try {
            usuarioBinder.writeBean(usuarioBean);
            if (new UsuarioDao().doGrabaDatos(usuarioBean) == true) {
                (new Notification(FrmMasterConstantes.AVISODATOALMACENADO, 4000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                doLimpiar();
            } else {
                (new Notification(FrmMasterConstantes.AVISODATOERRORBBDD, 4000, Notification.Position.MIDDLE)).open();
            }
        } catch (ValidationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    }

    @Override
    public void doGrid() {
        usuariosGrid.addColumn(UsuarioBean::getNombre).setHeader("Nombre");
        usuariosGrid.addColumn(UsuarioBean::getApellido1).setHeader("Apellido 1");
        usuariosGrid.addColumn(UsuarioBean::getApellido2).setHeader("Apellido 2");
        usuariosGrid.addColumn(UsuarioBean::getDni).setHeader("NIF");
        usuariosGrid.addColumn(UsuarioBean::getMail).setHeader("Correo electrónico");

        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
        arrayListUsuarios = new UsuarioDao().getLista(null);
        usuariosGrid.setItems(arrayListUsuarios);
    }

    @Override
    public void doBinderPropiedades() {
        usuarioBinder.forField(nombreUsuario).asRequired("El nombre es obligatorio").bind(UsuarioBean::getNombre, UsuarioBean::setNombre);
        usuarioBinder.forField(apellido1Usuario).bind(UsuarioBean::getApellido1, UsuarioBean::setApellido1);
        usuarioBinder.forField(apellido2Usuario).bind(UsuarioBean::getApellido2, UsuarioBean::setApellido2);
        usuarioBinder.forField(nifUsuario)
                .withValidator(
                        name -> Pattern.matches("(\\d{1,8})([TRWAGMYFPDXBNJZSQVHLCKEtrwagmyfpdxbnjzsqvhlcke])", name),
                        "Name must contain at least three characters")
                .bind(UsuarioBean::getDni, UsuarioBean::setDni);
        usuarioBinder.forField(correoUsuario).bind(UsuarioBean::getMail, UsuarioBean::setMail);
    }

    @Override
    public void doComponenesAtributos() {
        aplicacionesUsuario.setLabel("Aplicaciones");
        aplicacionesUsuario.setItems("Galeno", "Jimena", "Gacela", "HP-HIS");

        buscador.focus();
        buscador.setLabel("Texto de la búsqueda:");
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(nombreUsuario);
        contenedorFormulario.add(apellido1Usuario);
        contenedorFormulario.add(apellido2Usuario);
        contenedorFormulario.add(nifUsuario);
        contenedorFormulario.add(correoUsuario);
        contenedorFormulario.add(categoriaUsuario);
        contenedorFormulario.add(aplicacionesUsuario);

        contenedorBuscadores.add(camposFiltro, buscador);
        contenedorDerecha.add(usuariosGrid);
    }

    @Override
    public void doCompentesEventos() {
        usuariosGrid.addItemClickListener(event -> {
            usuarioBean = event.getItem();
            usuarioBinder.readBean(usuarioBean);
        });

        buscador.addBlurListener(event -> {
            /*
            if (buscador.getValue().isEmpty() && camposFiltro.getValue() == null) {
                arrayListUsuarios = new UsuarioDao().getLista(null);
            } else if (!buscador.getValue().isEmpty() && camposFiltro.getValue() != null) {
                arrayListUsuarios = new UsuarioDao().getUsuariosFiltro(buscador.getValue().trim(), camposFiltro.getValue());
            } else if (buscador.getValue().isEmpty() && camposFiltro.getValue() != null) {
                arrayListUsuarios = new UsuarioDao().getUsuariosFiltro(null, camposFiltro.getValue());
            } else if (!buscador.getValue().isEmpty() && camposFiltro.getValue() == null) {
                arrayListUsuarios = new UsuarioDao().getUsuariosFiltro(buscador.getValue().trim(), null);
            }
             */
            usuariosGrid.setItems(arrayListUsuarios);
        });
    }

    @Override
    public void doImprimir() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
