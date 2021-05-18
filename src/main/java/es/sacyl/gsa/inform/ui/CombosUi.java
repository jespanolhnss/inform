package es.sacyl.gsa.inform.ui;

import com.vaadin.flow.component.combobox.ComboBox;
import es.sacyl.gsa.inform.bean.AplicacionBean;
import es.sacyl.gsa.inform.bean.AutonomiaBean;
import es.sacyl.gsa.inform.bean.CategoriaBean;
import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.CentroTipoBean;
import es.sacyl.gsa.inform.bean.ComboBean;
import es.sacyl.gsa.inform.bean.DWIndicador;
import es.sacyl.gsa.inform.bean.DatoGenericoBean;
import es.sacyl.gsa.inform.bean.GerenciaBean;
import es.sacyl.gsa.inform.bean.GfhBean;
import es.sacyl.gsa.inform.bean.LocalidadBean;
import es.sacyl.gsa.inform.bean.LopdSujetoBean;
import es.sacyl.gsa.inform.bean.LopdTipoBean;
import es.sacyl.gsa.inform.bean.NivelesAtencionBean;
import es.sacyl.gsa.inform.bean.NivelesAtentionTipoBean;
import es.sacyl.gsa.inform.bean.ProveedorBean;
import es.sacyl.gsa.inform.bean.ProvinciaBean;
import es.sacyl.gsa.inform.bean.UbicacionBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.bean.VlanBean;
import es.sacyl.gsa.inform.bean.ZonaBean;
import es.sacyl.gsa.inform.dao.AplicacionDao;
import es.sacyl.gsa.inform.dao.AutonomiaDao;
import es.sacyl.gsa.inform.dao.CategoriaDao;
import es.sacyl.gsa.inform.dao.CentroDao;
import es.sacyl.gsa.inform.dao.CentroTipoDao;
import es.sacyl.gsa.inform.dao.ComboDao;
import es.sacyl.gsa.inform.dao.ConexionDao;
import es.sacyl.gsa.inform.dao.DWIndicadorDao;
import es.sacyl.gsa.inform.dao.GerenciaDao;
import es.sacyl.gsa.inform.dao.GfhDao;
import es.sacyl.gsa.inform.dao.JimenaDao;
import es.sacyl.gsa.inform.dao.LocalidadDao;
import es.sacyl.gsa.inform.dao.LopdTipoDao;
import es.sacyl.gsa.inform.dao.NivelesAtencionDao;
import es.sacyl.gsa.inform.dao.NivelesAtencionTipoDao;
import es.sacyl.gsa.inform.dao.ProveedorDao;
import es.sacyl.gsa.inform.dao.ProvinciaDao;
import es.sacyl.gsa.inform.dao.UsuarioDao;
import es.sacyl.gsa.inform.dao.VlanDao;
import es.sacyl.gsa.inform.dao.ZonaDao;
import java.util.ArrayList;
import org.joda.time.LocalDate;

/**
 *
 * @author 06551256M
 */
public class CombosUi {

    public static ArrayList<String> SINO = new ArrayList<String>() {
        {
            add("S");
            add("N");
        }
    };

    /**
     *
     * @param valor
     * @param cadena
     * @return
     */
    public ComboBox<AplicacionBean> getAplicacionCombo(AplicacionBean valor, String cadena, GfhBean gfhBean, ProveedorBean proveedorBean) {
        ComboBox<AplicacionBean> combo;
        combo = new ComboBox<>("Aplicaciones");
        combo.setItems(new AplicacionDao().getLista(cadena, gfhBean, proveedorBean, ConexionDao.BBDD_ACTIVOSI, false));
        combo.setItemLabelGenerator(AplicacionBean::getNombre);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setMinWidth("150px");
        combo.setMaxWidth("250px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor
     * @param cadena
     * @return
     */
    public ComboBox<AutonomiaBean> getAutonomiaCombo(AutonomiaBean valor, String cadena) {
        ComboBox<AutonomiaBean> combo;
        combo = new ComboBox<>("Autonomías");
        combo.setItems(new AutonomiaDao().getLista(cadena, ConexionDao.BBDD_ACTIVOSI));
        combo.setItemLabelGenerator(AutonomiaBean::getNombre);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setMinWidth("150px");
        combo.setMaxWidth("250px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param label
     * @param valor
     * @param grupo
     * @param anchoCadema
     * @return
     */
    public ComboBox<String> getCombodeTabla(String label, String valor, String grupo, Integer anchoCadema) {
        ComboBox<String> combo;
        combo = new ComboBox<>(label);
        ArrayList<String> lista = new ComboDao().getListaGrupos(grupo, anchoCadema);
        combo.setItems(lista);
        if (valor != null) {
            combo.setValue(valor);
        } else {
            if (lista.size() > 0) {
                combo.setValue(lista.get(0));
            }
        }
        combo.setWidth("150px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param autonomiaBean
     * @param provinciaBean
     * @param localidadBean
     * @param nivelesAtencionBean
     * @param centroTipoBean
     * @param nivelesAtentionTipoBean
     * @param valor
     * @return
     */
    public ComboBox<CentroBean> getCentroCombo(AutonomiaBean autonomiaBean, ProvinciaBean provinciaBean,
            LocalidadBean localidadBean, NivelesAtencionBean nivelesAtencionBean,
            CentroTipoBean centroTipoBean, NivelesAtentionTipoBean nivelesAtentionTipoBean, CentroBean valor) {
        ComboBox<CentroBean> combo = new ComboBox<>("Centros ");
        combo.setItems(new CentroDao().getLista(null, autonomiaBean, provinciaBean, localidadBean,
                nivelesAtencionBean, centroTipoBean, nivelesAtentionTipoBean, ConexionDao.BBDD_ACTIVOSI));
        combo.setItemLabelGenerator(CentroBean::getNomcenParaCombo);

        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setMaxWidth("300px");
        combo.setWidth("300px");
        combo.setMaxWidth("300px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor El valor por defecto que asigna al combo
     * @return
     */
    public ComboBox<CentroTipoBean> getCentroTipoCombo(CentroTipoBean valor) {
        ComboBox<CentroTipoBean> combo = new ComboBox<>("Tipos de Centros");
        ArrayList<CentroTipoBean> listaArrayList = new CentroTipoDao().getLista(null, ConexionDao.BBDD_ACTIVOSI);
        combo.setItems(listaArrayList);
        combo.setItemLabelGenerator(CentroTipoBean::getDescripcion);

        if (valor != null) {
            combo.setValue(valor);
        } else {
            /**
             * Valor por defecto definido en la tabla parámetros
             */
            CentroTipoBean centroTipoBean = CentroTipoDao.getCentroTipoDefecto();
            if (centroTipoBean != null) {
                combo.setValue(centroTipoBean);
            } else {
                if (listaArrayList.size() > 0) {
                    combo.setValue(listaArrayList.get(0));
                }
            }
        }
        combo.setWidth("150px");
        combo.setMaxWidth("160px");
        combo.setMinWidth("140px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor
     * @return
     */
    public ComboBox<CentroBean> getCentrosPrimariaCombo(CentroBean valor) {
        ComboBox<CentroBean> combo;
        combo = new ComboBox<>("Centros");
        combo.setItems(new JimenaDao().getListaCentrosPrimaria());
        combo.setItemLabelGenerator(CentroBean::getNomcen);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setWidth("150px");
        return combo;
    }

    /**
     *
     * @param valor
     * @return
     */
    public ComboBox<UsuarioBean> getInformaticosCombo(UsuarioBean valor) {
        ComboBox<UsuarioBean> combo;
        combo = new ComboBox<>("Técnicos");
        combo.setItems(new UsuarioDao().getInformaticos());
        combo.setItemLabelGenerator(UsuarioBean::getApellidosNombre);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setWidth("450px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param area
     * @param tipo
     * @return
     */
    public ComboBox<DWIndicador> getIndicadoresCombo(String area, String tipo) {
        ComboBox<DWIndicador> combo;
        combo = new ComboBox<>("Indicadores");
        combo.setItems(new DWIndicadorDao().getLista(area, null, tipo));
        combo.setItemLabelGenerator(DWIndicador::getNombre);
        combo.setWidth("200");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor
     * @param anchoCadema
     * @return
     */
    public ComboBox<String> getEquipoTipoCombo(String valor, Integer anchoCadema) {
        ComboBox<String> combo;
        combo = new ComboBox<>("Tipo equipo");
        ArrayList<String> lista = new ComboDao().getListaGrupos(ComboBean.TIPOEQUIPOMARCA, anchoCadema);
        combo.setItems(lista);
        if (valor != null) {
            combo.setValue(valor);
        } else {
            if (lista.size() > 0) {
                combo.setValue(lista.get(0));
            }
        }
        combo.setWidth("150px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor El valor por defecto que asinga al combo
     * @return
     */
    public ComboBox<NivelesAtencionBean> getNivelAtencionCombo(NivelesAtencionBean valor) {
        ComboBox<NivelesAtencionBean> combo = new ComboBox<>("Niveles de Atencion");
        combo.setItems(new NivelesAtencionDao().getLista(null, null, null, ConexionDao.BBDD_ACTIVOSI));
        combo.setItemLabelGenerator(NivelesAtencionBean::getDescripcion);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setWidth("300px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor El valor por defecto que asinga al combo
     * @return
     */
    public ComboBox<NivelesAtentionTipoBean> getNivelestTipoCombo(NivelesAtentionTipoBean valor) {
        ComboBox<NivelesAtentionTipoBean> combo = new ComboBox<>("Tipo Nivel Atención");
        combo.setItems(new NivelesAtencionTipoDao().getLista(null, ConexionDao.BBDD_ACTIVOSI));
        combo.setItemLabelGenerator(NivelesAtentionTipoBean::getDescripcion);

        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setWidth("300px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor
     * @param cadena
     * @param autonomia
     * @return
     */
    public ComboBox<ProvinciaBean> getProvinciaCombo(ProvinciaBean valor, String cadena, AutonomiaBean autonomia) {
        ComboBox<ProvinciaBean> combo;
        combo = new ComboBox<>("Provincias");
        combo.setItems(new ProvinciaDao().getLista(cadena, autonomia));
        combo.setItemLabelGenerator(ProvinciaBean::getNombre);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setMinWidth("100px");
        combo.setMaxWidth("150px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor
     * @param cadena
     * @return
     */
    public ComboBox<ProveedorBean> getProveedorCombo(ProveedorBean valor, String cadena) {
        ComboBox<ProveedorBean> combo = new ComboBox<>();
        combo = new ComboBox<>("Proveedor");
        ArrayList<ProveedorBean> lista = new ProveedorDao().getLista(null, null, ConexionDao.BBDD_ACTIVOSI);
        combo.setItems(lista);
        combo.setItemLabelGenerator(ProveedorBean::getNombre);
        if (valor != null) {
            combo.setValue(valor);
        } else {
            if (lista.size() > 0) {
                combo.setValue(lista.get(0));
            }
        }
        combo.setWidth("150px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param label
     * @param valor
     * @param lista
     * @param ancho
     * @return
     */
    public ComboBox<String> getStringCombo(String label, String valor, ArrayList<String> lista, String ancho) {
        ComboBox<String> combo = new ComboBox<>();
        if (label != null) {
            combo.setLabel(label);
        }
        combo.setItems(lista);
        if (valor != null) {
            combo.setValue(valor);
        } else {
            if (lista.size() > 0) {
                combo.setValue(lista.get(0));
            }
        }
        if (ancho != null) {
            combo.setWidth(ancho);
        }
        combo.setClearButtonVisible(true);
        return combo;
    }

    public ComboBox<String> getSiNoCombo(String label) {
        ComboBox<String> combo = new ComboBox<>();
        if (label != null) {
            combo.setLabel(label);
        }
        combo.setItems(SINO);

        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param desde
     * @param hasta
     * @return
     */
    public ComboBox<Integer> getAnoCombo(Integer desde, Integer hasta) {
        ComboBox<Integer> combo = new ComboBox<>();
        ArrayList<Integer> lista = new ArrayList<>();
        for (Integer i = desde; i <= hasta; i++) {
            lista.add(i);
        }
        combo.setItems(lista);
        combo.setValue(LocalDate.now().getYear());
        combo.setLabel("Año");
        combo.setItems(LocalDate.now().getYear());
        //  combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor
     * @param autonomiaBean
     * @return
     */
    public ComboBox<GerenciaBean> getGerenciaCombo(GerenciaBean valor, AutonomiaBean autonomiaBean, ProvinciaBean provinciaBean) {
        ComboBox<GerenciaBean> combo;
        combo = new ComboBox<>("Gerencias de Salud ");
        combo.setItems(new GerenciaDao().getLista(null, autonomiaBean, provinciaBean, ConexionDao.BBDD_ACTIVOSI));
        combo.setItemLabelGenerator(GerenciaBean::getNombre);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setMinWidth("200px");
        combo.setMaxWidth("250px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param grupo
     * @param valor
     * @param texto
     * @return
     */
    public ComboBox<String> getGrupoRamaCombo(String grupo, String valor, String texto) {
        ComboBox<String> combo = new ComboBox<>();
        combo = new ComboBox<>(texto);
        ArrayList<String> lista = new ComboDao().getListaGruposRama(grupo, 100);
        combo.setItems(lista);
        if (valor != null) {
            combo.setValue(valor);
        } else {
            if (lista.size() > 0) {
                combo.setValue(lista.get(0));
            }
        }
        combo.setWidth("150px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor
     * @param provinciaBean
     * @param cadena
     * @return
     */
    public ComboBox<LocalidadBean> getLocalidadCombo(LocalidadBean valor, AutonomiaBean autonomiaBean, ProvinciaBean provinciaBean, String cadena) {
        ComboBox<LocalidadBean> combo;
        combo = new ComboBox<>("Localidad");
        combo.setItems(new LocalidadDao().getLista(cadena, autonomiaBean, provinciaBean));
        combo.setItemLabelGenerator(LocalidadBean::getNombre);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setWidth("150px");
        combo.setClearButtonVisible(true);
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
        combo.setWidth("175px");
        combo.setMinWidth("175px");
        combo.setMaxWidth("175px");

        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor
     * @param sujeto
     * @param estado
     * @return
     */
    public ComboBox<LopdTipoBean> getLopdTipoCombo(LopdTipoBean valor, LopdSujetoBean sujeto, Boolean estado) {
        ComboBox<LopdTipoBean> combo;
        combo = new ComboBox<>("Tipo incidencia");
        ArrayList<LopdTipoBean> lista = new LopdTipoDao().getListaIncidenciaTipos(sujeto, null, null, estado);
        combo.setItems(lista);
        combo.setItemLabelGenerator(LopdTipoBean::getDescripcion);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setWidth("315px");
        combo.setMaxWidth("315px");
        return combo;
    }

    /**
     *
     * @param grupo
     * @param rama
     * @param valor
     * @param texto
     * @return
     */
    public ComboBox<String> getGrupoRamaComboValor(String grupo, String rama, String valor, String texto) {
        ComboBox<String> combo = new ComboBox<>();
        combo = new ComboBox<>(texto);
        ArrayList<String> lista = new ComboDao().getListaGruposRamaValor(grupo, rama, 100);
        combo.setItems(lista);
        if (valor != null) {
            combo.setValue(valor);
        } else {
            if (lista.size() > 0) {
                combo.setValue(lista.get(0));
            }
        }
        combo.setWidth("150px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor
     * @return
     */
    public ComboBox<GfhBean> getServicioComboJimena(GfhBean valor) {
        ComboBox<GfhBean> combo;
        combo = new ComboBox<>("Servicos");
        combo.setItems(new JimenaDao().getListaServicios());
        combo.setItemLabelGenerator(GfhBean::getCodigo);
        combo.setWidth("100px");
        combo.setMaxWidth("100px");
        combo.setMinWidth("60px");
        combo.setClearButtonVisible(true);
        if (valor != null) {
            combo.setValue(valor);
        }
        return combo;
    }

    /**
     *
     * @param nhc
     * @return
     */
    public ComboBox<GfhBean> getServicioComboJimenaPaciente(String nhc) {
        ComboBox<GfhBean> combo;
        combo = new ComboBox<>("Servicos");
        combo.setItems(new JimenaDao().getListaServiciosPaciente(nhc));
        combo.setItemLabelGenerator(GfhBean::getCodigo);
        combo.setWidth("100px");
        combo.setMaxWidth("100px");
        combo.setMinWidth("60px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param texto
     * @param centroBean
     * @param padre
     * @param valor
     * @return
     */
    public ComboBox<UbicacionBean> getUbicacionCombo(String texto, CentroBean centroBean, UbicacionBean padre, UbicacionBean valor
    ) {
        ComboBox<UbicacionBean> combo = new ComboBox<>("Ubicación Padre  ");
        //  combo.setItems(new UbicacionDao().getLista(texto, centroBean, padre));
        combo.setItemLabelGenerator(UbicacionBean::getDescripcionFull);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setWidth("300px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param texto
     * @param valor
     * @return
     */
    public ComboBox<CategoriaBean> getCategoriaCombo(String texto, CategoriaBean valor
    ) {
        ComboBox<CategoriaBean> combo = new ComboBox<>("Categoria  ");
        combo.setItems(new CategoriaDao().getLista(texto));
        combo.setItemLabelGenerator(CategoriaBean::getNombre);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setWidth("300px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    public ComboBox<VlanBean> getVlanCombo(String texto, VlanBean valor
    ) {
        ComboBox<VlanBean> combo = new ComboBox<>("Vlan  ");
        combo.setItems(new VlanDao().getLista(texto));
        combo.setItemLabelGenerator(VlanBean::getNombre);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setWidth("300px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param valor
     * @param texto
     * @return
     */
    public ComboBox<GfhBean> getServicioCombo(GfhBean valor, String texto) {
        ComboBox<GfhBean> combo = new ComboBox<>("Servicio");
        ArrayList<GfhBean> lista = new GfhDao().getLista(texto, ConexionDao.BBDD_ACTIVOSI);
        combo.setItems(lista);
        combo.setItemLabelGenerator(GfhBean::getDescripcion);
        if (valor != null) {
            combo.setValue(valor);
        } else {
            if (lista.size() > 0) {
                combo.setValue(lista.get(0));
            }
        }
        combo.setWidth("210px");
        combo.setMaxWidth("210px");
        combo.setMinWidth("210px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     *
     * @param autonomiaBean
     * @param provinciaBean
     * @param gerenciaBean
     * @param valor
     * @return
     */
    public ComboBox<ZonaBean> getZonaCombo(AutonomiaBean autonomiaBean, ProvinciaBean provinciaBean, GerenciaBean gerenciaBean, ZonaBean valor) {
        ComboBox<ZonaBean> combo;
        combo = new ComboBox<>("Zona Básica de Salud");
        combo.setItems(new ZonaDao().getLista(autonomiaBean, provinciaBean, gerenciaBean, null));
        combo.setItemLabelGenerator(ZonaBean::getNombre);
        if (valor != null) {
            combo.setValue(valor);
        }
        combo.setMinWidth("150px");
        combo.setMaxWidth("250px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     * Devuelve un listado con las categorias de Pérsigo
     *
     * @param valor
     * @return
     */
    public ComboBox<CategoriaBean> getCategoriasUsuarios(CategoriaBean valor) {
        ComboBox<CategoriaBean> combo;
        combo = new ComboBox<>("Categorias");
        if (valor != null) {
            combo.setItems(new CategoriaDao().getLista(valor.getNombre()));
        } else {
            combo.setItems(new CategoriaDao().getLista(null));
        }
        combo.setItemLabelGenerator(CategoriaBean::getNombre);
        combo.setMinWidth("150px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    /**
     * Devuelve un listado con los gfhs por código
     *
     * @param valor
     * @return
     */
    public ComboBox<GfhBean> getGfhPorCodigoUsuarios(GfhBean valor) {
        ComboBox<GfhBean> combo;
        combo = new ComboBox<>("Gfhs");
        if (valor != null) {
            combo.setItems(new GfhDao().getLista(valor.getCodigo(), ConexionDao.BBDD_ACTIVOSI));
        } else {
            combo.setItems(new GfhDao().getLista(null, ConexionDao.BBDD_ACTIVOSI));
        }
        combo.setItemLabelGenerator(GfhBean::getDescripcion);
        combo.setMinWidth("150px");
        combo.setClearButtonVisible(true);
        return combo;
    }

    public ComboBox<DatoGenericoBean> getGfhPersigo() {
        ComboBox<DatoGenericoBean> combo;
        combo = new ComboBox<>("Gfhs Pérsigo");
        combo.setItems(new UsuarioDao().getGfhPersigo());
        combo.setItemLabelGenerator(DatoGenericoBean::getValor);
        combo.setMinWidth("150px");
        combo.setClearButtonVisible(true);
        return combo;
    }
}
