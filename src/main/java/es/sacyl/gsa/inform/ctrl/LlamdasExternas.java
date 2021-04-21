/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.ctrl;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.LopdIncidenciaBean;
import es.sacyl.gsa.inform.bean.LopdSujetoBean;
import es.sacyl.gsa.inform.bean.LopdTipoBean;
import es.sacyl.gsa.inform.bean.PacienteBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.dao.JimenaDao;
import es.sacyl.gsa.inform.dao.PacienteDao;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.ui.lopd.FrmLopdIncidenciaNueva;
import es.sacyl.gsa.inform.ui.usuarios.FrmUsuariosPedir;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 06551256M
 */
public final class LlamdasExternas {

    private UsuarioBean usuarioBean = null;
    private PacienteBean pacienteBean = null;
    private GfhBean ServicioBean = null;
    private VerticalLayout contenedorFormularios = new VerticalLayout();
    private final String apl, nhc, usr, tipo, serv, icu;
    public static String LOPDNUEVA = "LOPDNUEVA";
    public QueryParameters queryParameters;

    /**
     *
     * @param qm
     * @param cf
     */
    public LlamdasExternas(QueryParameters qm, VerticalLayout cf) {
        queryParameters = qm;
        contenedorFormularios = cf;
        apl = getParametroLlamada("APL");
        usr = getParametroLlamada("USR");
        nhc = getParametroLlamada("NHC");
        tipo = getParametroLlamada("TIPO");
        serv = getParametroLlamada("SERV");
        icu = getParametroLlamada("ICU");
        if (usr != null) {
            usuarioBean = new UsuarioDao().getUsuarioDni(usr, Boolean.FALSE);
            if (usuarioBean == null) {
                usuarioBean = new JimenaDao().getUsuarioBean(usr);
                usuarioBean.setId(new Long(0));
                new UsuarioDao().doGrabaDatos(usuarioBean);
            }
            SesionCtrl.doCreaSesionUsuario(usuarioBean);
        }
        if (nhc != null) {
            pacienteBean = new JimenaDao().getPaciente(nhc);
            if (new PacienteDao().getPacienteNhc(pacienteBean.getNumerohc()) == null) {
                new PacienteDao().insertaPaciente(pacienteBean);
            }
        }
        switch (tipo) {
            case "LOPDNUEVA":
                doPantallaLopd();
                break;
                
            case "PEDIRUSUARIO":
                doPantallaPedirUsuario();
                break;
        }
    }

    /**
     *
     * @param parametro
     * @return
     */
    public String getParametroLlamada(String parametro) {
        String valor = null;
        Map<String, List<String>> listParam = queryParameters.getParameters();
        List<String> appList = listParam.get(parametro);
        if (appList != null && appList.size() > 0) {
            valor = appList.get(0);
        }
        return valor;
    }

    /**
     *
     */
    public void doPantallaLopd() {
        contenedorFormularios.removeAll();
        LopdIncidenciaBean lopdIncidenciaBean = new LopdIncidenciaBean();
        lopdIncidenciaBean.setPaciente(pacienteBean);
        lopdIncidenciaBean.setUsuarioRegistra(usuarioBean);
        lopdIncidenciaBean.setUsuCambio(usuarioBean);
        lopdIncidenciaBean.setTipo(LopdTipoBean.LOPDTIPOPORDEFECTO);
        lopdIncidenciaBean.setSujeto(LopdSujetoBean.SUJETO_PACIENTE);
        lopdIncidenciaBean.setFechaHora(LocalDateTime.now());
        FrmLopdIncidenciaNueva frmLopdIncidenciaNueva = new FrmLopdIncidenciaNueva(lopdIncidenciaBean);
        frmLopdIncidenciaNueva.getBotonAyuda().setVisible(Boolean.FALSE);
        frmLopdIncidenciaNueva.getBotonBorrar().setVisible(Boolean.FALSE);
        frmLopdIncidenciaNueva.getBotonLimpiar().setVisible(Boolean.FALSE);
        frmLopdIncidenciaNueva.getBotonImprimir().setVisible(Boolean.FALSE);
        frmLopdIncidenciaNueva.getBuscador().setVisible(Boolean.FALSE);

        frmLopdIncidenciaNueva.getDni().setReadOnly(true);
        frmLopdIncidenciaNueva.getApellidosNombre().setReadOnly(true);
        //     frmLopdIncidenciaNueva.getMail().setReadOnly(true);
        //     frmLopdIncidenciaNueva.getTelefono().setReadOnly(true);
        frmLopdIncidenciaNueva.getComboSujeto().setReadOnly(Boolean.TRUE);

        contenedorFormularios.add(frmLopdIncidenciaNueva);
    }

    private void doPantallaPedirUsuario() {
        contenedorFormularios.removeAll();
        FrmUsuariosPedir pedirUsuario = new FrmUsuariosPedir();
        if (usuarioBean.getSolicita().equals("S")) {
            contenedorFormularios.add(pedirUsuario);            
        } else {
            contenedorFormularios.add(new H3("SOLO USUARIOS AUTORIZADOS PUEDEN PEDIR ACCESO"));
        }        
    }
}