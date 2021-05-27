package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.AplicacionPerfilBean;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.GruposPaginasGalenoBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.dao.AplicacionDao;
import es.sacyl.gsa.inform.dao.AplicacionPerfilDao;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.CentroTipoDao;
import es.sacyl.gsa.inform.dao.ComboDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.GfhDao;
import es.sacyl.gsa.inform.dao.JimenaDao;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *
 * @author 06551256M
 */
public class ObjetosComunes {

    public static ArrayList<String> SINO = new ArrayList<String>() {
        {
            add("S");
            add("N");
        }
    };

    public static ArrayList<String> SINOB = new ArrayList<String>() {
        {
            add("");
            add("S");
            add("N");
        }
    };

    public static ArrayList<String> Categorias = new ArrayList<String>() {

        private static final long serialVersionUID = 1L;

        {
            add("");
            add("Administrativo");
            add("Auxiliar Clínica");
            add("D.U.E.");
            add("Médico Especialista");
            add("Médico Primaria");
        }
    };

    public static ArrayList<String> FiltroBusquedaUsuarios = new ArrayList<String>() {

        private static final long serialVersionUID = 1L;

        {
            add("");
            add("Nombre");
            add("Apellido 1");
            add("NIF");
        }
    };

    public ObjetosComunes() {

    }

    /**
     *
     * @return
     */
    public RadioButtonGroup<String> getEstadoRadio() {
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("Activo");
        radioGroup.setItems("S", "N");
        radioGroup.setValue("S");
        return radioGroup;
    }

    public RadioButtonGroup<String> getTiposIndicadores() {
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("Tipos de indicadore ");
        radioGroup.setItems(new ComboDao().getListaGruposRama(ComboBean.DWTIPOINDICADOR, 25));

        return radioGroup;
    }

    public RadioButtonGroup<String> getAreasIndicadores() {
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("Área de actvidad ");
        radioGroup.setItems(new ComboDao().getListaGruposRama(ComboBean.DWAREASINDICADORES, 25));

        return radioGroup;
    }

    /**
     *
     * @param label
     * @return
     */
    public RadioButtonGroup<String> getSNRadio(String label) {
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        if (label != null) {
            radioGroup.setLabel(label);
        }
        radioGroup.setItems(SINO);
        return radioGroup;
    }

    /**
     *
     * @return
     */
    public Details getDetails() {
        Details details = new Details();
        details.setEnabled(true);
        details.setOpened(true);
        return details;
    }

    /**
     *
     * @param valor
     * @return
     */
    public CheckboxGroup<CentroBean> getCentrosPrimariaCheckboxGroup(ArrayList<CentroBean> valor) {
        CheckboxGroup<CentroBean> checkboxGroup;
        checkboxGroup = new CheckboxGroup<CentroBean>();
        checkboxGroup.setItems(new JimenaDao().getListaCentrosPrimaria());
        checkboxGroup.setItemLabelGenerator(CentroBean::getNomcen);
        return checkboxGroup;
    }

    /**
     *
     * @return
     */
    public CheckboxGroup<AplicacionBean> getAplicacionesCheckboxGroup() {
        CheckboxGroup<AplicacionBean> checkboxGroup;
        checkboxGroup = new CheckboxGroup<AplicacionBean>();
        checkboxGroup.setLabel("Aplicaciones");
        checkboxGroup.setItems(new AplicacionDao().getLista(null));
        checkboxGroup.setItemLabelGenerator(AplicacionBean::getNombre);

        return checkboxGroup;
    }
    
    /**
     *
     * @return
     */
    public CheckboxGroup<GruposPaginasGalenoBean> getGruposPaginasGalenoCheckboxGroup() {
        CheckboxGroup<GruposPaginasGalenoBean> checkboxGroup;
        checkboxGroup = new CheckboxGroup<GruposPaginasGalenoBean>();
        checkboxGroup.setLabel("Grupos de Páginas");
        checkboxGroup.setItems(new UsuarioDao().getGruposPaginasGaleno());
        checkboxGroup.setItemLabelGenerator(GruposPaginasGalenoBean::getGrupo);

        return checkboxGroup;
    }

    /**
     *
     * @param id
     * @return
     */
    public RadioButtonGroup<AplicacionPerfilBean> getAplicacionesPerfilesPorIdRadioButtonGroup(Long id) {
        AplicacionBean aplicacion = new AplicacionBean();
        aplicacion.setId(id);
        RadioButtonGroup<AplicacionPerfilBean> radioButtonGroup;
        radioButtonGroup = new RadioButtonGroup<>();
        radioButtonGroup.setItems(new AplicacionPerfilDao().getLista(null, aplicacion));
        radioButtonGroup.setRenderer(new ComponentRenderer<>(aplicacionPerfilBean -> {
            Div title = new Div();
            title.setText(aplicacionPerfilBean.getNombre());
            return title;
        }));

        return radioButtonGroup;
    }

    /**
     * Devuelve un checkboxGroup con los tipos de centro
     *
     * @return
     */
    public CheckboxGroup<CentroTipoBean> getTipoCentroCecheckboxGroup() {
        CheckboxGroup<CentroTipoBean> tipoCentro = new CheckboxGroup<>();
        ArrayList<CentroTipoBean> tiposArrayList = new CentroTipoDao().getLista(null, ConexionDao.BBDD_ACTIVOSI);
        tipoCentro.setItems(tiposArrayList);
        tipoCentro.setItemLabelGenerator(CentroTipoBean::getDescripcion);
        return tipoCentro;
    }

    /**
     * Devuelve un checkboxGroup con los centros de Ávila
     *
     * @return
     */
    public CheckboxGroup<CentroBean> getCentrosCheckboxGroup() {
        CheckboxGroup<CentroBean> centros = new CheckboxGroup<>();
        ArrayList<CentroBean> centrosArrayList = new CentroDao().getLista(null, AutonomiaBean.AUTONOMIADEFECTO,
                ProvinciaBean.PROVINCIA_DEFECTO, null, null, null, null, ConexionDao.BBDD_ACTIVOSI);
        centros.setItems(centrosArrayList);
        centros.setItemLabelGenerator(CentroBean::getNomcorto);
        return centros;
    }

    /**
     *
     * @param valor el valor que esté seleccionado del combo
     * @param codigosolo si se muestra el código en el combo o si no se muestra
     * la descripción
     * @return
     */
    public ComboBox<GfhBean> getServicioCombo(GfhBean valor, Boolean codigosolo) {
        ComboBox<GfhBean> combo;
        combo = new ComboBox<>("Servicos");
        combo.setItems(new GfhDao().getLista(null, ConexionDao.BBDD_ACTIVOSI));
        if (codigosolo == true) {
            combo.setItemLabelGenerator(GfhBean::getCodigo);
            combo.setWidth("50px");
            combo.setMaxWidth("50px");
            combo.setMinWidth("50px");
        } else {
            combo.setItemLabelGenerator(GfhBean::getDescripcion);
            combo.setWidth("110px");
            combo.setMaxWidth("250px");
            combo.setMinWidth("130px");
        }
        if (valor != null) {
            combo.setValue(valor);
        }
        return combo;
    }

    /**
     *
     * @param text
     * @param buttonVariant
     * @param icon
     * @return
     */
    public Button getBoton(String text, ButtonVariant buttonVariant, Icon icon) {
        Button boton = new Button();
        if (text != null) {
            boton.setText(text);
        }
        if (buttonVariant != null) {
            boton.addThemeVariants(buttonVariant);
        } else {
            boton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }
        if (icon != null) {
            boton.setIcon(icon);
        }

        boton.setHeight("35px");
        return boton;
    }

    public Button getExcelBoton() {
        return getBoton("Excel", ButtonVariant.LUMO_LARGE, VaadinIcon.GRID_BEVEL.create());
    }

    /**
     *
     * @return
     */
    public Button getBotonMini() {
        return getBotonMini(VaadinIcon.QUESTION_CIRCLE.create());
    }

    public Button getBotonMini(Icon icono) {
        Button boton = getBoton(null, null, icono);
        boton.setWidth("30px");
        boton.setMaxWidth("30px");
        boton.setMinWidth("30px");
        return boton;
    }

    /**
     *
     * @param text
     * @param Placeholder
     * @param maxLength
     * @param width
     * @return
     */
    public TextField getTextField(String text, String Placeholder, Integer maxLength, String Maxwidth, String minWidth) {
        TextField textField = new TextField();
        if (text != null) {
            textField.setLabel(text);
        }
        if (Placeholder != null) {
            textField.setPlaceholder(Placeholder);
        }
        if (maxLength != null) {
            textField.setMaxLength(maxLength);
        }

        if (minWidth != null) {
            textField.setMinWidth(minWidth);
        }
        if (Maxwidth != null) {
            textField.setMinWidth(Maxwidth);
        }
        textField.getEmptyValue();

        textField.setClearButtonVisible(true);
        return textField;
    }

    /**
     *
     * @param text
     * @return
     */
    public TextField getTextField(String text) {
        TextField textField = new TextField();
        if (text != null) {
            textField.setLabel(text);
        }
        textField.setClearButtonVisible(true);
        return textField;
    }

    /**
     *
     * @param text
     * @param Placeholder
     * @param maxLength
     * @param width
     * @param height
     * @param maxHeight
     * @param minHeight
     * @return
     */
    public TextArea getTextArea(String text, String Placeholder, Integer maxLength, String width, String height, String maxHeight, String minHeight) {
        TextArea textArea = getTextArea(text);
        if (text != null) {
            textArea.setLabel(text);
        }
        if (Placeholder != null) {
            textArea.setPlaceholder(Placeholder);
        }
        if (maxLength != null) {
            textArea.setMaxLength(maxLength);
        }
        if (width != null) {
            textArea.setWidth(width);
        }
        if (height != null) {
            textArea.setHeight(height);
        }
        if (maxHeight != null) {
            textArea.getStyle().set("maxHeight", maxHeight);
        }
        if (minHeight != null) {
            textArea.getStyle().set("minHeight", minHeight);
        }
        textArea.setClearButtonVisible(true);
        return textArea;
    }

    /**
     *
     * @param text
     * @return
     */
    public TextArea getTextArea(String text) {
        TextArea textArea = new TextArea();
        if (text != null) {
            textArea.setLabel(text);
        }
        textArea.setClearButtonVisible(true);
        return textArea;
    }

    /**
     *
     * @param label
     * @param placeholder
     * @param date
     * @return
     */
    public DatePicker getDatePicker(String label, String placeholder, LocalDate date) {

        DatePicker datePicker = new DatePicker();
        if (label != null) {
            datePicker.setLabel(label);
        }

        if (placeholder != null) {
            datePicker.setPlaceholder(placeholder);
        }

        if (date != null) {
            datePicker.setValue(date);
        }
        datePicker.setClearButtonVisible(true);
        datePicker.setWidth("150px");
        datePicker.setClearButtonVisible(true);
        return datePicker;
    }

    /**
     *
     * @param label
     * @param placeholder
     * @param date
     * @return
     */
    public DateTimePicker getDateTimePicker(String label, String placeholder, LocalDateTime date) {
        DateTimePicker dateTimePicker = new DateTimePicker();
        if (label != null) {
            dateTimePicker.setLabel(label);
        }
        if (placeholder != null) {
            // dateTimePicker.setPlaceholder(placeholder);
        }
        if (date != null) {
            dateTimePicker.setValue(date);
        }
        dateTimePicker.setMinWidth("240px");
        dateTimePicker.setMaxWidth("240px");
        return dateTimePicker;
    }

    public TextField getId() {
        TextField campo = getTextField("Id");
        campo.setMaxWidth("100px");
        campo.setMinWidth("100px");
        campo.setWidth("100px");
        return campo;
    }

    /**
     *
     * @param label
     * @return
     */
    public IntegerField getIntegerField(String label) {
        IntegerField campo = new IntegerField();
        if (label != null) {
            campo.setLabel(label);
        }
        campo.setClearButtonVisible(true);
        return campo;
    }

    /**
     *
     * @return
     */
    public IntegerField getMes() {
        IntegerField campo = getIntegerField(null);
        campo.setLabel("Mes");
        campo.setClearButtonVisible(true);
        campo.setWidth("50px");
        campo.setMaxWidth("75px");
        campo.setMinWidth("75px");
        campo.setMin(1);
        campo.setMax(12);
        return campo;
    }

    public IntegerField getAno() {
        IntegerField campo = getIntegerField(null);
        campo.setLabel("Año");
        campo.setClearButtonVisible(true);
        campo.setWidth("100px");
        campo.setMaxWidth("100px");
        campo.setMinWidth("100px");
        campo.setMin(2000);
        campo.setMax(LocalDate.now().getYear() + 2);
        return campo;
    }

    /**
     *
     * @param label
     * @return
     */
    public BigDecimalField getBigDecimalField(String label) {
        BigDecimalField campo = new BigDecimalField();
        if (label != null) {
            campo.setLabel(label);
        }
        campo.setClearButtonVisible(true);
        return campo;
    }

    /**
     *
     * @return
     */
    public TextField getDni() {
        TextField campo = getTextField("DNI", "dni", 9, "95px", "95px");
        campo.setMaxWidth("125px");
        campo.setMinWidth("125px");
        campo.setWidth("125px");
        return campo;
    }

    public TextField getMacAdress() {
        TextField campo = getTextField("Mac", null, 17, "95px", "95px");
        campo.setMaxWidth("170px");
        campo.setMinWidth("170px");
        campo.setWidth("170px");
        return campo;
    }

    /**
     *
     * @return
     */
    public TextField getNumeroHc() {
        TextField campo = getTextField("Nº Historia", null, 6, "105px", "95px");

        return campo;
    }

    /**
     *
     * @return
     */
    public TextField getTelefono() {
        TextField campo = getTextField("Teléfono", "teléfono", 9, "30px", "30px");
        return campo;
    }

    /**
     *
     * @return
     */
    public TextField getMovil() {
        TextField campo = getTextField("Teléfono Móvil", "número de móvil", 9, "30px", "30px");
        return campo;
    }

    /**
     *
     * @return
     */
    public TextField getMail() {
        TextField campo = getTextField("Mail", "mail", 100, "40px", "100px");
        return campo;
    }

    /**
     *
     * @param label
     * @param place
     * @return
     */
    public TextField getMail(String label, String place) {
        TextField campo = getTextField(null, null, null, "40px", "100px");
        campo.setLabel(label);
        campo.setPlaceholder(place);
        return campo;
    }

    /**
     *
     * @param label
     * @param place
     * @return
     */
    public TextField getApeNoml(String label, String place) {
        TextField campo = getTextField(null, null, null, "60px", "110px");
        if (label != null) {
            campo.setLabel(label);
        }
        if (place != null) {
            campo.setPlaceholder(place);
        }
        return campo;
    }

    public Component getCaja() {
        FlexLayout caja = new FlexLayout();
        caja.addClassName("card");
        caja.setAlignItems(FlexComponent.Alignment.CENTER);
        caja.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        caja.setWidth("50%");
        caja.setHeight("400px");
        return caja;
    }

    public Component getCaja(String titulo) {
        Span card1Label = new Span(titulo);
        FlexLayout card = new FlexLayout(card1Label);
        card.addClassName("card");
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        card.setWidth("100%");
        card.setHeight("200px");
        return card;

    }
}
