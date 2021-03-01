/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ctrl;

import es.sacyl.gsa.inform.bean.FuncionalidadBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;

/**
 *
 * @author JuanNieto
 */
public class UsuarioCtrl {
/**
 * 
 * @param usuario
 * @param fun
 * @return 
 */
    public Boolean tieneLaFuncionalidad(UsuarioBean usuario, FuncionalidadBean fun) {
        Boolean laTiene = false;
        for (FuncionalidadBean funcionaliad : usuario.getFucionalidadesArrayList()) {
            if (funcionaliad.getId().equals(fun.getId())) {
                laTiene = true;
            }
        }
        return laTiene;
    }
    
   
}
