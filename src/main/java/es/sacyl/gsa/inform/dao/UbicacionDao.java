/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.sacyl.gsa.inform.dao;

import es.sacyl.gsa.inform.bean.CentroBean;
import es.sacyl.gsa.inform.bean.UbicacionBean;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author juannietopajares
 */
public class UbicacionDao extends ConexionDao implements Serializable, ConexionInterface<UbicacionBean> {

    private static final Logger LOGGER = LogManager.getLogger(UbicacionDao.class);
    private static final long serialVersionUID = 1L;

    public UbicacionDao() {
        super();
        sql = " SELECT u.id as ubicacionesid , u.centro as ubicacionescentro, u.descripcion as ubicacionesdescripcion"
                + " ,u.idpadre ubicacionesidpadre, u.nivel  as ubicacionesnivel  "
                + " ,padre.id as padreid , padre.centro as padrecentro, padre.descripcion as padredescripcion"
                + " ,padre.idpadre padreidpadre, padre.nivel  as padrenivel  "
                + ", c.ID as centroid,c.CODAUTO as centrocodauto,c.CODGEREN   as centrocodgeren"
                + " ,c.CODZONA as centrocodozona "
                + " ,c.CODIGO as centrocodigo, c.NOMCEN as centronumcen ,c.TIPOVIA as centrotipovia "
                + " ,c.CALLECEN as centrocallecen,c.NUMCALCEN as centronumcalcen,c.OTRDIRCEN as centrootrodircen"
                + " ,c.CODLOCAL as centrocodlocal "
                + " ,c.CPCENTRO as centrocpcentro,c.TELEPREV as centroteleprev,c.TIPOCENTRO as centrotipocentro"
                + " ,c.NIVATENCION as centronivatencion "
                + " ,c.estado as centroestado, c.mapgoogle as centromapggole, c.nomcorto as centronomcorto  "
                + " ,n.id as nivelid,n.codigo as nivelcodigo,n.descripcion as niveldescripcion,n.tipo as niveltipo"
                + " ,n.estado as nivelestado  "
                + " ,l.codigo as localidadcodigo,l.nombre localidadnombre,l.nombre localidadprovincia "
                + " ,p.codigo as provinciacodigo,p.nombre as provincianombre,p.nombre provinciacodauto "
                + " ,a.codigo as autonomiacodigo, a.nombre as autonomianombre,a.estado as autonomiaestado "
                + " ,g.codauto gerenciacodauto ,g.codigo   gerenciacodigo ,g.nombre  gerencianombre, g.tipovia   gerenciatipovia"
                + " , g.callesec   gerenciacallesec,g.numcalsec  gerencianumcalsec "
                + "  ,g.otrdomger  gerenciaotrdomger, g.cpger  gerenciacpger, g.localger   gerencialocalger, g.estado gerenciaestado  "
                + " ,z.codauto  zonacodauto  ,z.codgeren  zonacodgeren,z.codigo  zonacodigo,z.nombre  zonanombre,z.codprov zonacodprov "
                + " ,ct.id as centrotipoid, ct.descripcion as centrotipodescripcion,ct.estado as centrotipoestado  "
                + " ,na.id as niveltipotipoid, na.descripcion as niveltipodescripcion,na.estado  as niveltipoestado  "
                + " FROM ubicaciones u "
                + " LEFT JOIN ubicaciones padre ON padre.id=u.idpadre"
                + " LEFT JOIN CENTROS c  ON c.id=u.centro"
                + " LEFT JOIN nivelesatencion n ON n.id = c.NIVATENCION  "
                + " JOIN LOCALIDAD l ON l.codigo = c.CODLOCAL "
                + " JOIN PROVINCIA p ON p.codigo=l.codprov "
                + " JOIN CAUTONOM a On a.codigo=p.CODAUTO  "
                + " JOIN GERENCIA g ON g.codauto=a.codigo AND g.codigo=c.codgeren  "
                + " JOIN ZONAS z ON z.codauto=c.codauto AND z.codgeren=g.codigo AND z.codigo=c.codzona "
                + " JOIN centrostipo ct ON ct.id=c.tipocentro "
                + " JOIN  NIVELESATENCIONTIPO na  ON na.id =n.tipo"
                + " WHERE  1=1 ";
    }

    /**
     *
     * @param rs
     * @return
     */
    @Override
    public UbicacionBean getRegistroResulset(ResultSet rs) {
        return getRegistroResulset(rs, null);
    }

    /**
     *
     * @param rs
     * @param centroBean
     * @return
     */
    public UbicacionBean getRegistroResulset(ResultSet rs, CentroBean centroBean) {
        UbicacionBean ubicacionBean = new UbicacionBean();
        try {
            ubicacionBean.setId(rs.getLong("ubicacionesid"));
            ubicacionBean.setDescripcion(rs.getString("ubicacionesdescripcion"));
            if (centroBean == null) {
                //  ubicacionBean.setCentro(new CentroDao().getPorId(rs.getLong("ubicacionescentro")));
                ubicacionBean.setCentro(new CentroDao().getRegistroResulset(rs));
            } else {
                ubicacionBean.setCentro(centroBean);
            }
            if (rs.getLong("ubicacionesidpadre") != new Long(0)) {
                ubicacionBean.setPadre(getPorId(rs.getLong("ubicacionesidpadre")));
            }
            ubicacionBean.setNivel(rs.getInt("ubicacionesnivel"));
            // tiene que recuperar el padre para obtener el nombre completo en el arbol
            ubicacionBean.setDescripcionFull(getNombreCompleto(ubicacionBean, ubicacionBean.getCentro()));
        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        }
        return ubicacionBean;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public UbicacionBean getPorId(Long id) {
        Connection connection = null;
        UbicacionBean ubicacionBean = null;
        String sqlU = sql;
        try {
            connection = super.getConexionBBDD();
            sqlU = sqlU.concat(" AND u.id=" + id);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sqlU);
                if (resulSet.next()) {
                    ubicacionBean = getRegistroResulset(resulSet, null);
                }
                statement.close();
            }
            LOGGER.debug(sqlU);
        } catch (SQLException e) {
            LOGGER.error(sqlU + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return ubicacionBean;
    }

    /**
     *
     * @param ubicacionBean
     * @return
     */
    @Override
    public boolean doGrabaDatos(UbicacionBean ubicacionBean) {
        boolean actualizado = false;
        if (this.getPorId(ubicacionBean.getId()) == null) {
            ubicacionBean.setId(getSiguienteId("ubicaciones"));
            actualizado = this.doInsertaDatos(ubicacionBean);
        } else {
            actualizado = this.doActualizaDatos(ubicacionBean);
        }
        return actualizado;
    }

    /**
     *
     * @param ubicacionBean
     * @return
     */
    @Override
    public boolean doInsertaDatos(UbicacionBean ubicacionBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        String sqlU = "";
        try {
            connection = super.getConexionBBDD();
            sqlU = " INSERT INTO  ubicaciones  (id,centro,descripcion,idpadre, nivel  ) "
                    + " VALUES "
                    + "(?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sqlU);
            statement.setLong(1, ubicacionBean.getId());
            if (ubicacionBean.getCentro() != null && ubicacionBean.getId() != null) {
                statement.setLong(2, ubicacionBean.getCentro().getId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            if (ubicacionBean.getDescripcion() != null) {
                statement.setString(3, ubicacionBean.getDescripcion());
            } else {
                statement.setNull(3, Types.CHAR);
            }
            if (ubicacionBean.getPadre() != null && ubicacionBean.getPadre().getId() != null) {
                statement.setLong(4, ubicacionBean.getPadre().getId());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            if (ubicacionBean.getNivel() != null) {
                statement.setLong(5, ubicacionBean.getNivel());
            } else {
                statement.setNull(5, Types.INTEGER);
            }
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
            LOGGER.debug(sqlU);
        } catch (SQLException e) {
            LOGGER.error(sqlU + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    /**
     *
     * @param ubicacionBean
     * @return
     */
    @Override
    public boolean doActualizaDatos(UbicacionBean ubicacionBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        String sqlU = "";
        try {
            connection = super.getConexionBBDD();
            sqlU = " UPDATE   ubicaciones SET centro=?,descripcion=?,idpadre=?, nivel = ? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sqlU);

            if (ubicacionBean.getCentro() != null && ubicacionBean.getId() != null) {
                statement.setLong(1, ubicacionBean.getCentro().getId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            if (ubicacionBean.getDescripcion() != null) {
                statement.setString(2, ubicacionBean.getDescripcion());
            } else {
                statement.setNull(2, Types.CHAR);
            }
            if (ubicacionBean.getPadre() != null && ubicacionBean.getPadre().getId() != null) {
                statement.setLong(3, ubicacionBean.getPadre().getId());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            if (ubicacionBean.getNivel() != null) {
                statement.setLong(4, ubicacionBean.getNivel());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            statement.setLong(5, ubicacionBean.getId());
            insertadoBoolean = statement.executeUpdate() > 0;
            statement.close();
            LOGGER.debug(sqlU);
        } catch (SQLException e) {
            LOGGER.error(sqlU + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    /**
     *
     * @param ubicacionBean
     * @return
     */
    @Override
    public boolean doBorraDatos(UbicacionBean ubicacionBean) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        String sqlU = "";
        try {
            connection = super.getConexionBBDD();
            sqlU = " DELETE FROM  ubicaciones WHERE ID='" + ubicacionBean.getId() + "'";
            Statement statement = connection.createStatement();
            insertadoBoolean = statement.execute(sql);
            insertadoBoolean = true;
            statement.close();
            LOGGER.debug(sqlU);
        } catch (SQLException e) {
            LOGGER.error(sqlU + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    /**
     *
     * @param texto
     * @return
     */
    @Override
    public ArrayList<UbicacionBean> getLista(String texto) {
        return getLista(texto, null, null);
    }

    /**
     *
     * @param texto
     * @param centroBean
     * @param padre
     * @return
     */
    public ArrayList<UbicacionBean> getLista(String texto, CentroBean centroBean, UbicacionBean padre) {
        Connection connection = null;
        ArrayList<UbicacionBean> lista = new ArrayList<>();
        String sqlU = sql;
        try {
            connection = super.getConexionBBDD();
            if (texto != null && !texto.isEmpty()) {
                sqlU = sqlU.concat(" AND   UPPER(u.descripcion) like'%" + texto.toUpperCase() + "%'  ");
            }
            if (centroBean != null && centroBean.getId() != null) {
                sqlU = sqlU.concat(" AND  u.centro=" + centroBean.getId());
            }
            sqlU = sqlU.concat(" ORDER BY  u.centro,  u.descripcion  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sqlU);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet, centroBean));
            }
            statement.close();
            LOGGER.debug(sqlU);
        } catch (SQLException e) {
            LOGGER.error(sqlU + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    /**
     *
     * @param centroBean
     * @return
     */
    public ArrayList<UbicacionBean> getListaPadresCentro(CentroBean centroBean) {
        Connection connection = null;
        ArrayList<UbicacionBean> lista = new ArrayList<>();
        String sqlU = sql;
        if (centroBean != null) {
            try {
                connection = super.getConexionBBDD();
                sqlU = sqlU.concat(" AND  u.centro=" + centroBean.getId());
                sqlU = sqlU.concat(" AND  u.idpadre IS null ");
                sqlU = sqlU.concat(" ORDER BY  ubicacionescentro,  ubicacionesdescripcion  ");
                Statement statement = connection.createStatement();
                ResultSet resulSet = statement.executeQuery(sqlU);
                while (resulSet.next()) {
                    lista.add(getRegistroResulset(resulSet, centroBean));
                }
                statement.close();
                LOGGER.debug(sqlU);
            } catch (SQLException e) {
                LOGGER.error(sqlU + Utilidades.getStackTrace(e));
            } catch (Exception e) {
                LOGGER.error(Utilidades.getStackTrace(e));
            } finally {
                this.doCierraConexion(connection);
            }
        }
        return lista;
    }

    /**
     *
     * @param ubicacionBean
     * @return
     */
    public ArrayList<UbicacionBean> getListaHijos(UbicacionBean ubicacionBean) {
        Connection connection = null;
        ArrayList<UbicacionBean> lista = new ArrayList<>();
        String sqlU = sql;
        try {
            connection = super.getConexionBBDD();

            sqlU = sqlU.concat(" AND  u.idpadre = " + ubicacionBean.getId());
            sqlU = sqlU.concat(" ORDER BY  ubicacionescentro,  ubicacionesdescripcion  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sqlU);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet, ubicacionBean.getCentro()));
            }
            statement.close();
            LOGGER.debug(sqlU);
        } catch (SQLException e) {
            LOGGER.error(sqlU + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            LOGGER.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    /**
     *
     * @param ubicacion
     * @return
     */
    public String getNombreCompletoppp(UbicacionBean ubicacion, CentroBean centroBean) {
        String nombre = "";
        while (ubicacion != null && ubicacion.getPadre() != null) {
            if (ubicacion.getDescripcion() != null) {
                nombre = ">" + ubicacion.getDescripcion() + nombre;
            } else {
                nombre = ">" + nombre;
            }
            ubicacion = getPorId(ubicacion.getPadre().getId());
        }
        if (ubicacion != null && ubicacion.getDescripcion() != null) {
            nombre = ubicacion.getDescripcion() + nombre;
        }
        return nombre;
    }

    public String getNombreCompleto(UbicacionBean ubicacion, CentroBean centroBean) {
        String nombre = "";
        Connection connection = super.getConexionBBDD();
        try {
            while (ubicacion != null && ubicacion.getPadre() != null) {
                if (ubicacion.getDescripcion() != null) {
                    nombre = ">" + ubicacion.getDescripcion() + nombre;
                } else {
                    nombre = ">" + nombre;
                }
                String sqlU = sql + " AND u.id=" + ubicacion.getPadre().getId();
                LOGGER.debug(sqlU);
                try (Statement statement = connection.createStatement()) {
                    ResultSet resulSet = statement.executeQuery(sqlU);
                    if (resulSet.next()) {
                        ubicacion = new UbicacionBean();
                        ubicacion.setId(resulSet.getLong("ubicacionesid"));
                        ubicacion.setDescripcion(resulSet.getString("ubicacionesdescripcion"));
                        // ubicacion.setCentro(new CentroDao().getRegistroResulset(resulSet));
                        ubicacion.setCentro(centroBean);
                        if (resulSet.getLong("ubicacionesidpadre") != 0) {
                            // Solo con el id por que para montar el nombre no es necesario todos los campos
                            ubicacion.setPadre(new UbicacionBean(resulSet.getLong("ubicacionesidpadre")));
                        }
                        ubicacion.setNivel(resulSet.getInt("ubicacionesnivel"));
                    }
                    statement.close();
                }
            }
        } catch (Exception ex) {
            LOGGER.error(Utilidades.getStackTrace(ex));
        } finally {
            this.doCierraConexion(connection);
        }

        if (ubicacion != null && ubicacion.getDescripcion() != null) {
            nombre = ubicacion.getDescripcion() + nombre;
        }
        return nombre;
    }

    /**
     *
     * @param codigo
     * @return
     */
    @Override
    public UbicacionBean getPorCodigo(String codigo
    ) {
        return null;
    }
}
