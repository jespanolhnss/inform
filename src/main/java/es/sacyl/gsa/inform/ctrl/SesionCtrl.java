/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ctrl;

import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.util.Constantes;

/**
 *
 * @author 06551256M
 */
public class SesionCtrl {
 /**
  * 
  * @param usuarioBean 
  */
    public static void doCreaSesionUsuario(UsuarioBean  usuarioBean){
         VaadinSession.getCurrent().setAttribute(Constantes.SESSION_USERNAME, usuarioBean);
    }
    /**
     * 
     * @return 
     */
    public static UsuarioBean getSesionUsuario(){
      return  (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
    }
}
