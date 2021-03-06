package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.bean.EquipoBean;
import es.sacyl.gsa.inform.dao.EquipoDao;
import es.sacyl.gsa.inform.ui.FrmMasterVentana;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;

/**
 *
 * @author 06551256M
 */
public final class FrmDatosGenerico extends FrmMasterVentana {

    private final ArrayList<TextField> listaCamposTextFields = new ArrayList<>();

    private final Details equipoDetalle = new Details();

    private EquipoBean equipoBean = new EquipoBean();

    /**
     *
     * @param ancho
     * @param equipoBean
     */
    public FrmDatosGenerico(String ancho, EquipoBean equipoBean) {
        super(ancho);
        this.equipoBean = equipoBean;
        setDetalleEquipo(equipoBean);
        equipoBean.getDatosGenericoBeans().forEach(dato -> {
            listaCamposTextFields.add(new ObjetosComunes().getTextField(dato.getTipoDato()));
        });
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    /**
     *
     */
    @Override
    public void doGrabar() {
        equipoBean.getDatosGenericoBeans().removeAll(equipoBean.getDatosGenericoBeans());
        for (TextField textField : listaCamposTextFields) {
            DatoGenericoBean dato = new DatoGenericoBean();
            dato.setTipoDato(textField.getLabel());
            dato.setIdDatoEqipo(equipoBean.getId());
            if (!textField.getValue().isEmpty()) {
                dato.setValor(textField.getValue());
                new EquipoDao().grabatValorDatoGenerico(dato);
            } else {
                dato.setValor("");
                new EquipoDao().grabatValorDatoGenerico(dato);
            }
            equipoBean.getDatosGenericoBeans().add(dato);
        }
    }

    /**
     *
     */
    @Override
    public void doBorrar() {
        for (TextField textField : listaCamposTextFields) {
            DatoGenericoBean dato = new DatoGenericoBean();
            textField.clear();
        }
    }

    @Override
    public void doCancelar() {
        this.close();
    }

    @Override
    public void doCerrar() {
        this.close();
    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doLimpiar() {
        for (TextField textField : listaCamposTextFields) {
            textField.clear();
        }
    }

    @Override
    public void doGrid() {
    }

    @Override
    public void doActualizaGrid() {
    }

    @Override
    public void doBinderPropiedades() {
    }

    @Override
    public void doComponenesAtributos() {
        titulo.setText("Datos del equipo:" + equipoBean.getId());
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("150px", 1),
                new FormLayout.ResponsiveStep("150px", 2));
        for (TextField textField : listaCamposTextFields) {
            contenedorFormulario.add(textField);
        }
        contenedorDerecha.removeAll();
        contenedorDerecha.add(equipoDetalle);
    }

    @Override
    public void doCompentesEventos() {
    }

    public void setDetalleEquipo(EquipoBean equipoBean) {

        equipoDetalle.setContent(new Html(equipoBean.toHtml()));
        equipoDetalle.setSummaryText(equipoBean.getTipo());
        equipoDetalle.setEnabled(true);
        equipoDetalle.setOpened(true);

    }
}
