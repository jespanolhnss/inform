package es.sacyl.gsa.inform.ui.indicadores;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import es.sacyl.gsa.inform.bean.DWIndicador;
import es.sacyl.gsa.inform.ctrl.IndicadoresEtlCrl;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.time.LocalDate;

/**
 *
 * @author 06551256M
 */
public class FrmIndicadores extends FrmMasterPantalla {

    private DatePicker desde = new ObjetosComunes().getDatePicker("Desde", null, LocalDate.now());
    private DatePicker hasta = new ObjetosComunes().getDatePicker("Desde", null, LocalDate.now());

    RadioButtonGroup<String> areaCalucloradioGroup = new RadioButtonGroup<>();

    public FrmIndicadores() {
        doComponentesOrganizacion();
        doGrid();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doGrabar() {
        // calcular
        if (desde.getValue().isBefore(hasta.getValue())) {
            IndicadoresEtlCrl indicadoresEtlCrl = new IndicadoresEtlCrl(desde.getValue(), hasta.getValue(), areaCalucloradioGroup.getValue());
            indicadoresEtlCrl.doProcesa();
        } else {
            Notification.show(" Desde debe ser antes que hasta");

        }
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
    }

    @Override
    public void doActualizaGrid() {
    }

    @Override
    public void doBinderPropiedades() {
    }

    @Override
    public void doComponenesAtributos() {
        contenedorIzquierda.setWidth("640px");
        contenedorFormulario.setWidth("640px");
        areaCalucloradioGroup.setLabel("Área");
        areaCalucloradioGroup.setItems(DWIndicador.AREASCALCULO);
        areaCalucloradioGroup.setValue("HOS");
        botonGrabar.setText("Calcular");
    }

    @Override
    public void doComponentesOrganizacion() {
        contenedorFormulario.removeAll();
        contenedorFormulario.add(desde, hasta, areaCalucloradioGroup);
    }

    @Override
    public void doCompentesEventos() {
    }

}
