package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.LopdSujetoBean;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.GfhDao;
import es.sacyl.gsa.inform.dao.JimenaDao;
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

    public RadioButtonGroup<String> getEstadoRadio() {
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("Activo");
        radioGroup.setItems("S", "N");
        return radioGroup;
    }

    public RadioButtonGroup<String> getSNRadio(String label) {
        RadioButtonGroup<String> radioGroup = new RadioButtonGroup<>();
        if (label != null) {
            radioGroup.setLabel(label);
        }
        radioGroup.setItems("S", "N");
        return radioGroup;
    }

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

//      if (valor != null) {
//    	  checkboxGroup.setValue(valor);
//      }
        //checkboxGroup.setWidth("150px");
        return checkboxGroup;
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
     * @param valor
     * @return
     */
    public ComboBox<LopdSujetoBean> getLopdSujetoCombo(LopdSujetoBean valor) {
        ComboBox<LopdSujetoBean> combo;
        combo = new ComboBox<>("Sujeto");
        combo.setItems(LopdSujetoBean.LISTASUJETOS_COMPLETA);
        combo.setItemLabelGenerator(lopdSujeto -> {
            return lopdSujeto.getDescripcion();
        });
        if (valor != null) {
            combo.setValue(valor);
        } else {
            combo.setValue(LopdSujetoBean.SUJETO_PACIENTE);
        }
        combo.setWidth("200px");
        combo.setClearButtonVisible(true);
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

    /**
     *
     * @return
     */
    public Button getBotonMini() {
        Button boton = getBoton(null, null, VaadinIcon.QUESTION_CIRCLE.create());
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
        dateTimePicker.setMinWidth("190px");
        dateTimePicker.setMaxWidth("220px");
        return dateTimePicker;
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
        campo.setMaxWidth("100px");
        campo.setMinWidth("100px");
        campo.setWidth("100px");
        return campo;
    }

    public TextField getNumeroHc() {
        TextField campo = getTextField("Nº Historia", null, 6, "105px", "95px");

        return campo;
    }

    /**
     *
     * @return
     */
    public TextField getTelefono() {
        TextField campo = getTextField("Teléfono", "dni", 9, "30px", "30px");
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

}
