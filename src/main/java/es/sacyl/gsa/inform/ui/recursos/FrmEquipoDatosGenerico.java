package es.sacyl.gsa.inform.ui.recursos;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
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
public final class FrmEquipoDatosGenerico extends FrmMasterVentana {

    private final Button datosGenericosButton = new ObjetosComunes().getBoton("Dat", null, VaadinIcon.TABLE.create());

    private final ArrayList<TextField> listaCamposTextFields = new ArrayList<>();

    private final Details equipoDetalle = new Details();

    private EquipoBean equipoBean = new EquipoBean();

    /**
     *
     * @param ancho
     * @param equipoBean
     */
    public FrmEquipoDatosGenerico(String ancho, EquipoBean equipoBean) {
        super(ancho);
        this.equipoBean = equipoBean;
        if (equipoBean == null) {
            com.vaadin.flow.component.notification.Notification.show(" Equipo nulo ", 500, Notification.Position.TOP_START);
            this.close();
        }
        setDetalleEquipo(equipoBean);
        equipoBean.getDatosGenericoBeans().forEach(dato -> {
            TextField textField = new ObjetosComunes().getTextField(dato.getTipoDato());
            if (dato.getValor() != null) {
                textField.setValue(dato.getValor());
            }
            listaCamposTextFields.add(textField);
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
        // Borra los datos genéricos actuales de bean y luego añade los nuevos
        ArrayList<DatoGenericoBean> lista = new ArrayList<>();
        for (TextField textField : listaCamposTextFields) {
            DatoGenericoBean dato = new DatoGenericoBean();
            dato.setTipoDato(textField.getLabel());
            dato.setIdDatoEqipo(equipoBean.getId());
            dato.setValoresAut();
            lista.add(dato);

            if (!textField.getValue().isEmpty()) {
                dato.setValor(textField.getValue());
            } else {
                dato.setValor("");
            }
            new EquipoDao().grabatValorDatoGenerico(dato);
        }
        equipoBean.setDatosGenericoBeans(lista);
        this.close();
    }

    public ArrayList<DatoGenericoBean> getDatosGenericoBeans() {
        return equipoBean.getDatosGenericoBeans();
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
        contenedorBotones.add(datosGenericosButton);

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
        contenedorBotones.addClickListener(event -> {
            for (TextField textField : listaCamposTextFields) {
                String valor = new EquipoDao().getValorDatoGenericoDemModelo(textField.getLabel(), equipoBean);
                if (valor != null) {
                    textField.setValue(valor);
                }
            }
        });
    }

    public void setDetalleEquipo(EquipoBean equipoBean) {
        equipoDetalle.setContent(new Html(equipoBean.toHtml()));
        equipoDetalle.setSummaryText(equipoBean.getTipo());
        equipoDetalle.setEnabled(true);
        equipoDetalle.setOpened(true);
    }
}
