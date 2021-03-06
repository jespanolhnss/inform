package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConfirmDialog extends Dialog {

    final VerticalLayout content = new VerticalLayout();
    final HorizontalLayout buttons = new HorizontalLayout();

    public ConfirmDialog(String caption, String text, String confirmButtonText,
            Runnable confirmListener) {

        content.setPadding(false);
        content.setWidth("400px");
        add(content);

        add(new H3(caption));
        add(new Span(text));

        buttons.setPadding(false);
        add(buttons);

        final Button confirm = new Button(confirmButtonText, e -> {
            confirmListener.run();
            this.close();
        });
        confirm.setWidth("100%");
        confirm.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);
        confirm.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttons.add(confirm);

        final Button cancel = new Button("Cancel", e -> close());
        buttons.add(cancel);

    }

}
