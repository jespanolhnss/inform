/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.reports;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServletRequest;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.dao.ParametroDao;
import es.sacyl.gsa.inform.util.Constantes;

/**
 *
 * @author 06532775Q
 */
public class MasterExcel {
    
    protected String nombreDelFicheroExcel = null;
    protected String nombreExcelAbsoluto = null;
    protected String nombreExcelRelativo = null;
    protected String urlDelExcel = null;
    
    public void doActualizaNombreFicheros() {
        nombreExcelAbsoluto = Constantes.PDFPATHABSOLUTO + nombreDelFicheroExcel;
        nombreExcelRelativo = Constantes.PDFPATHRELATIVO + nombreDelFicheroExcel;
        String adr, port;
        VaadinRequest currentRequest = VaadinRequest.getCurrent();
        VaadinServletRequest vaadinServletRequest = null;
        if (currentRequest instanceof VaadinServletRequest) {
            vaadinServletRequest = (VaadinServletRequest) currentRequest;
            adr = vaadinServletRequest.getLocalAddr();
            if (adr.charAt(0) == "0".charAt(0)) {
                adr = "localhost";
            }
            port = Integer.toString(vaadinServletRequest.getLocalPort());
            urlDelExcel = "http://" + adr + ":" + port + nombreExcelRelativo;
        } else {
            adr = new ParametroDao().getPorCodigo(ParametroBean.URL_INSTANCIASERVIDOR).getValor();
            urlDelExcel = "http://" + adr + nombreExcelRelativo;
        }       
    }

    public String getNombreDelFicheroExcel() {
        return nombreDelFicheroExcel;
    }

    public void setNombreDelFicheroExcel(String nombreDelFicheroExcel) {
        this.nombreDelFicheroExcel = nombreDelFicheroExcel;
    }

    public String getNombreExcelAbsoluto() {
        return nombreExcelAbsoluto;
    }

    public void setNombreExcelAbsoluto(String nombreExcelAbsoluto) {
        this.nombreExcelAbsoluto = nombreExcelAbsoluto;
    }

    public String getNombreExcelRelativo() {
        return nombreExcelRelativo;
    }

    public void setNombreExcelRelativo(String nombreExcelRelativo) {
        this.nombreExcelRelativo = nombreExcelRelativo;
    }

    public String getUrlDelExcel() {
        return urlDelExcel;
    }

    public void setUrlDelExcel(String urlDelExcel) {
        this.urlDelExcel = urlDelExcel;
    }

}
