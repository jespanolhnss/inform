package es.sacyl.gsa.inform.ui.indicadores;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.progressbar.ProgressBar;
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
public final class FrmIndicadoresCalcular extends FrmMasterPantalla {

    private final DatePicker desde = new ObjetosComunes().getDatePicker("Desde", null, LocalDate.now());
    private final DatePicker hasta = new ObjetosComunes().getDatePicker("Desde", null, LocalDate.now());

    private final RadioButtonGroup<String> areaCalucloradioGroup = new RadioButtonGroup<>();

    private final ProgressBar progressBar = new ProgressBar();

    public FrmIndicadoresCalcular() {
        super();
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
            progressBar.setVisible(true);
            IndicadoresEtlCrl indicadoresEtlCrl = new IndicadoresEtlCrl(desde.getValue(), hasta.getValue(), areaCalucloradioGroup.getValue());
            indicadoresEtlCrl.doProcesa();
            progressBar.setVisible(false);
        } else {
            Notification.show(" Desde debe ser UNA  que hasta");

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
        //  contenedorIzquierda.setWidth("640px");
        // contenedorFormulario.setWidth("640px");
        areaCalucloradioGroup.setLabel("Área");
        areaCalucloradioGroup.setItems(DWIndicador.AREASCALCULO);
        areaCalucloradioGroup.setValue("HOS");
        botonGrabar.setText("Calcular");

        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);

    }

    @Override
    public void doComponentesOrganizacion() {
        titulo.setText("Carga de indicadores de HP-HIS  a DW ");
        contenedorFormulario.removeAll();
        contenedorFormulario.add(desde, hasta, areaCalucloradioGroup);

        contenedorDerecha.removeAll();
        contenedorDerecha.add(progressBar);

    }

    @Override
    public void doCompentesEventos() {
    }

}
