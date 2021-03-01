package es.sacyl.gsa.inform.ui.tablas;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.StringLengthValidator;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.dao.ComboDao;
import es.sacyl.gsa.inform.ui.CombosUi;
import es.sacyl.gsa.inform.ui.ConfirmDialog;
import es.sacyl.gsa.inform.ui.FrmMasterPantalla;
import es.sacyl.gsa.inform.ui.FrmMensajes;
import es.sacyl.gsa.inform.ui.ObjetosComunes;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.vaadin.klaudeta.PaginatedGrid;

/**
 *
 * @author JuanNieto
 */
public final class FrmCombos extends FrmMasterPantalla {

    private final TextField id = new ObjetosComunes().getTextField("Id", "id", 10, "50px", "50px");
    private final TextField grupo = new ObjetosComunes().getTextField("Grupo", "grupo", 50, "50px", "50px");
    private final TextField descripcion = new ObjetosComunes().getTextField("Descripción", "grupo", 250, "300px", "50px");
    private final TextField orden = new ObjetosComunes().getTextField("Orden", "orden", 10, "50px", "50px");
    private final TextField pertenece = new ObjetosComunes().getTextField("Pertenece", "id", 10, "50px", "50px");
    private final TextField rama = new ObjetosComunes().getTextField("Rama", "rama", 100, "200px", "50px");
    private final TextField valor = new ObjetosComunes().getTextField("Valor", "valor", 100, "200px", "50px");
    private ComboBox<String> grupoCombo = new ComboBox<>();

      private ComboBox<String> ramaCombo =  new ComboBox<>();

    private ArrayList<ComboBean> listaCombos = new ArrayList<>();
    private final Binder<ComboBean> combobinder = new Binder<>();
    private ComboBean comboBean = new ComboBean();
    private final PaginatedGrid<ComboBean> grid = new PaginatedGrid<>();

    public FrmCombos() {
        super();
        ArrayList<String> lista = new ComboDao().getListaGruposCombos();
        grupoCombo = new CombosUi().getStringCombo("Grupos", null, lista, "250px");
         ramaCombo = new CombosUi().getGrupoRamaCombo(grupoCombo.getValue(),null,"Rama");
        doGrid();
        doComponentesOrganizacion();
        doComponenesAtributos();
        doBinderPropiedades();
        doCompentesEventos();
    }

    @Override
    public void doGrabar() {
        if (combobinder.writeBeanIfValid(comboBean)) {
            if (new ComboDao().doGrabaDatos(comboBean) == true) {
                (new Notification(FrmMensajes.AVISODATOALMACENADO, 1000, Notification.Position.MIDDLE)).open();
                doActualizaGrid();
                id.clear();
                valor.clear();
                comboBean=new ComboBean();
                    
            } else {
                (new Notification(FrmMensajes.AVISODATOERRORBBDD, 1000, Notification.Position.MIDDLE)).open();
            }
        } else {
            BinderValidationStatus<ComboBean> validate = combobinder.validate();
            String errorText = validate.getFieldValidationStatuses()
                    .stream().filter(BindingValidationStatus::isError)
                    .map(BindingValidationStatus::getMessage)
                    .map(Optional::get).distinct()
                    .collect(Collectors.joining(", "));
            Notification.show(FrmMensajes.AVISODATOERRORVALIDANDO + errorText);
        }
    }

    @Override
    public void doBorrar() {
        final ConfirmDialog dialog = new ConfirmDialog(
                FrmMensajes.AVISOCONFIRMACIONACCION,
                FrmMensajes.AVISOCONFIRMACIONACCIONSEGURO,
                FrmMensajes.AVISOCONFIRMACIONACCIONBORRAR, () -> {
                    new ComboDao().doBorraDatos(comboBean);
                    Notification.show(FrmMensajes.AVISODATOBORRADO);
                    doActualizaGrid();
                    doLimpiar();
                });
        dialog.open();

    }

    @Override
    public void doAyuda() {
    }

    @Override
    public void doGrid() {

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.setHeightByRows(true);
        grid.setPageSize(14);
        grid.addColumn(ComboBean::getGrupo).setHeader(new Html("<b>Grupo</b>")).setAutoWidth(true);
        grid.addColumn(ComboBean::getRama).setHeader(new Html("<b>Rama</b>")).setAutoWidth(true);
        grid.addColumn(ComboBean::getValor).setHeader(new Html("<b>Valor</b>")).setAutoWidth(true);

        doActualizaGrid();
    }

    @Override
    public void doActualizaGrid() {
            listaCombos = new ComboDao().getLista(buscador.getValue().toString(), grupoCombo.getValue().toString(),ramaCombo.getValue());
        grid.setItems(listaCombos);
    }

    @Override
    public void doBinderPropiedades() {

        combobinder.forField(id)
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(ComboBean::getId, ComboBean::setId);

        combobinder.forField(grupo)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 50))
                .bind(ComboBean::getGrupo, ComboBean::setGrupo);

        combobinder.forField(descripcion)
                .withNullRepresentation("")
                .asRequired()
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 250))
                .bind(ComboBean::getDescripcion, ComboBean::setDescripcion);

        combobinder.forField(orden)
                .withNullRepresentation("")
                .asRequired()
                .withConverter(new StringToIntegerConverter(FrmMensajes.AVISONUMERO))
                .bind(ComboBean::getOrden, ComboBean::setOrden);

        combobinder.forField(pertenece)
                .withNullRepresentation("")
                .withNullRepresentation("")
                .withConverter(new StringToLongConverter(FrmMensajes.AVISONUMERO))
                .bind(ComboBean::getPertenece, ComboBean::setPertenece);

        combobinder.forField(rama)
                .asRequired()
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 100))
                .bind(ComboBean::getRama, ComboBean::setRama);

        combobinder.forField(valor)
                .asRequired()
                .withNullRepresentation("")
                .withValidator(new StringLengthValidator(
                        FrmMensajes.AVISODATOABLIGATORIO, 1, 100))
                .bind(ComboBean::getValor, ComboBean::setValor);
    }

    @Override
    public void doComponenesAtributos() {

        buscador.setTitle("Valor a buscar");
        grupoCombo.addValueChangeListener(e -> doActualizaGrid());

    }

    @Override
    public void doComponentesOrganizacion() {
        titulo.setText(ComboBean.getTitulo());

        contenedorFormulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("50px", 1),
                new FormLayout.ResponsiveStep("250px", 2));
        contenedorFormulario.add(id, grupo);
        contenedorFormulario.add(descripcion, 2);
        contenedorFormulario.add(orden, pertenece);
        contenedorFormulario.add(rama, 2);
        contenedorFormulario.add(valor, 2);

        contenedorBuscadores.add(buscador, grupoCombo,ramaCombo);
        contenedorDerecha.add(grid);
    }

    @Override
    public void doLimpiar() {
        comboBean = new ComboBean();
        combobinder.readBean(comboBean);
    }

    @Override
    public void doCancelar() {
        this.removeAll();
    }

    @Override
    public void doCompentesEventos() {
        
        buscador.addValueChangeListener(evegtn->{
            doActualizaGrid();
        });
        /**
         * Cuando clic en el grid de la lista de combos
         */
        grid.addItemClickListener(event -> {
            comboBean = event.getItem();
            try {
                combobinder.readBean(comboBean);
            } catch (Exception e) {
                Notification.show(FrmMensajes.AVISODATOERRORRECUPERANDO + comboBean.toString());
            }
        });
        grupoCombo.addValueChangeListener(event->{
          ramaCombo.setItems(new ComboDao().getListaGruposRama(event.getValue(),100));
        });
        
        ramaCombo.addValueChangeListener(evetn->{
        doActualizaGrid();
        });
        
    }

    @Override
    public void doImprimir() {
    }

}
