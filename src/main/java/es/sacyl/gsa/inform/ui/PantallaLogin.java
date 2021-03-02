/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.ctrl.SesionCtrl;
import es.sacyl.gsa.inform.dao.UsuarioDao;

/**
 *
 * @author 06551256M
 */
public class PantallaLogin extends Dialog {

    private Boolean conectado = false;

    public PantallaLogin() {
        LoginForm componentLogin = new LoginForm();
        componentLogin.setForgotPasswordButtonVisible(false);
        componentLogin.setI18n(createEspanolI18n());
        componentLogin.addLoginListener(e -> {
            boolean isAuthenticated = authenticate(e.getUsername(), e.getPassword());
            if (isAuthenticated) {
                conectado = true;
                this.close();
            } else {
                componentLogin.setError(true);
            }
        });
        this.add(componentLogin);
    }

    public Boolean getConectado() {
        return conectado;
    }

    public void setConectado(Boolean conectado) {
        this.conectado = conectado;
    }

    public boolean authenticate(String user, String passe3) {
        UsuarioBean usuario = new UsuarioDao().getUsuarioDni(user, Boolean.FALSE);
        if (usuario != null) {
            SesionCtrl.doCreaSesionUsuario(usuario);
        } else {
            SesionCtrl.doCreaSesionUsuario(usuario);
        }
        return true;
    }

    private LoginI18n createEspanolI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("Informática");
        i18n.getHeader().setDescription("Aplicaciones internas del servicio ");
        i18n.getForm().setUsername("Usuario");
        i18n.getForm().setTitle("Informática GSA");
        i18n.getForm().setSubmit("Conectar");
        i18n.getForm().setPassword("Clave");
        i18n.getForm().setForgotPassword("Resetear clave");
        i18n.getErrorMessage().setTitle("Datos no válidos");
        i18n.getErrorMessage()
                .setMessage("Registra de nuevo el dato del usuario y la clave");
        i18n.setAdditionalInformation(
                " Informática 01-03-2021");
        return i18n;
    }
}
