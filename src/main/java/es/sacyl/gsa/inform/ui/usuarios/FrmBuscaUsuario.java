package es.sacyl.gsa.inform.ui.usuarios;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import es.sacyl.gsa.inform.bean.CategoriaBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.GridUi;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author 06551256M
 */
public final class FrmBuscaUsuario extends FrmMasterVentana {

    public final TextField dni = new ObjetosComunes().getDni();
    public final TextField nombre = new ObjetosComunes().getApeNoml("Nombre", null);
    public final TextField apellido2 = new ObjetosComunes().getApeNoml("Apellido2", null);
    public final TextField apellido1 = new ObjetosComunes().getApeNoml("Apellido1", null);
    private final PaginatedGrid<UsuarioBean> usuarioGrid = new GridUi().getUsuarioGrid();
    private final ComboBox<CategoriaBean> categoriaCombo = new CombosUi().getCategoriasUsuarios(null);
    private ArrayList<UsuarioBean> usuarioArrayList = new ArrayList<>();
    private UsuarioBean usuarioBean = null;

    public FrmBuscaUsuario() {
        super();
        doComponentesOrganizacion();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doGrabar() {
    }

    @Override
    public void doBorrar() {
    }

    @Override
    public void doCancelar() {
        usuarioBean = null;
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
        dni.clear();
        apellido1.clear();
        apellido2.clear();
        nombre.clear();
        usuarioGrid.setItems(new ArrayList<>());
    }

    @Override
    public void doGrid() {
    }

    @Override
    public void doActualizaGrid() {
        usuarioArrayList = new UsuarioDao().getLista(dni.getValue(), apellido1.getValue(), apellido2.getValue(),
                nombre.getValue(), categoriaCombo.getValue(), 10);
        usuarioGrid.setItems(usuarioArrayList);
    }

    @Override
    public void doBinderPropiedades() {
    }

    @Override
    public void doComponenesAtributos() {
        botonGrabar.setVisible(false);
        botonLimpiar.setVisible(true);
        botonBorrar.setVisible(false);
        botonImprimir.setVisible(false);
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.add(categoriaCombo, dni, nombre, apellido1, apellido2);
        contenedorDerecha.add(usuarioGrid);
    }

    @Override
    public void doCompentesEventos() {
        apellido1.addValueChangeListener(event -> {
            if (!apellido1.getValue().isEmpty()) {
                doActualizaGrid();;
            }
        });

        apellido1.addKeyPressListener(event -> {
            if (!apellido1.getValue().isEmpty()) {
                doActualizaGrid();;
            }
        });
        apellido2.addValueChangeListener(event -> {
            if (!apellido2.getValue().isEmpty()) {
                doActualizaGrid();;
            }
        });
        apellido2.addKeyPressListener(event -> {
            if (!apellido2.getValue().isEmpty()) {
                doActualizaGrid();;
            }
        });
        nombre.addValueChangeListener(event -> {
            if (!nombre.getValue().isEmpty()) {
                doActualizaGrid();;
            }
        });
        nombre.addKeyPressListener(event -> {
            if (!nombre.getValue().isEmpty()) {
                doActualizaGrid();;
            }
        });
        dni.addValueChangeListener(event -> {
            if (!dni.getValue().isEmpty()) {
                doActualizaGrid();;
            }
        });
        dni.addKeyPressListener(event -> {
            if (!dni.getValue().isEmpty()) {
                doActualizaGrid();;
            }
        });
        categoriaCombo.addValueChangeListener(evetn -> {
            doActualizaGrid();
        });

        usuarioGrid.addItemClickListener(event -> {
            usuarioBean = event.getItem();
            this.close();
        });

    }

    public ArrayList<UsuarioBean> getUsuarioArrayList() {
        return usuarioArrayList;
    }

    public void setUsuarioArrayList(ArrayList<UsuarioBean> usuarioArrayList) {
        this.usuarioArrayList = usuarioArrayList;
    }

    public UsuarioBean getUsuarioBean() {
        return usuarioBean;
    }

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

}
