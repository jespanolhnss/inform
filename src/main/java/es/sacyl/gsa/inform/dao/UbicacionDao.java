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
                + " FROM ubicaciones u "
                + " LEFT JOIN ubicaciones padre ON padre.id=u.idpadre"
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
                ubicacionBean.setCentro(new CentroDao().getPorId(rs.getLong("ubicacionescentro")));
            } else {
                ubicacionBean.setCentro(centroBean);
            }
            if (rs.getLong("ubicacionesidpadre") != new Long(0)) {
                ubicacionBean.setPadre(new UbicacionDao().getPorId(rs.getLong("ubicacionesidpadre")));
            }
            ubicacionBean.setNivel(rs.getInt("ubicacionesnivel"));
            // tiene que recuperar el padre para obtener el nombre completo en el arbol
            ubicacionBean.setDescripcionFull(new UbicacionDao().getNombreCompleto(ubicacionBean));
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
        try {
            connection = super.getConexionBBDD();
            /*
            sql = " SELECT id as ubicacionesid , centro as ubicacionescentro, descripcion as ubicacionesdescripcion,"
                    + " idpadre ubicacionesidpadre, nivel  as ubicacionesnivel  "
                    + " FROM ubicaciones u "
                    + " WHERE  id='" + id + "'";
             */
            sql = sql.concat(" AND u.id=" + id);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    ubicacionBean = getRegistroResulset(resulSet, null);
                }
                statement.close();
            }
            //  System.err.println(sql);
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
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
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO  ubicaciones  (id,centro,descripcion,idpadre, nivel  ) "
                    + " VALUES "
                    + "(?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
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
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
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
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE   ubicaciones SET centro=?,descripcion=?,idpadre=?, nivel = ? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

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
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
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
        try {
            connection = super.getConexionBBDD();
            sql = " DELETE FROM  ubicaciones WHERE ID='" + ubicacionBean.getId() + "'";
            Statement statement = connection.createStatement();
            insertadoBoolean = statement.execute(sql);
            insertadoBoolean = true;
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
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
        try {
            connection = super.getConexionBBDD();
            if (texto != null && !texto.isEmpty()) {
                sql = sql.concat(" AND   UPPER(u.descripcion) like'%" + texto.toUpperCase() + "%'  ");
            }
            if (centroBean != null && centroBean.getId() != null) {
                sql = sql.concat(" AND  u.centro=" + centroBean.getId());
            }
            sql = sql.concat(" ORDER BY  u.centro,  u.descripcion  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet, centroBean));
            }
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
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
        if (centroBean != null) {
            try {
                connection = super.getConexionBBDD();
                sql = sql.concat(" AND  u.centro=" + centroBean.getId());
                sql = sql.concat(" AND  u.idpadre IS null ");
                sql = sql.concat(" ORDER BY  ubicacionescentro,  ubicacionesdescripcion  ");
                Statement statement = connection.createStatement();
                ResultSet resulSet = statement.executeQuery(sql);
                while (resulSet.next()) {
                    lista.add(getRegistroResulset(resulSet, centroBean));
                }
                statement.close();
                LOGGER.debug(sql);
            } catch (SQLException e) {
                LOGGER.error(sql + Utilidades.getStackTrace(e));
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
        try {
            connection = super.getConexionBBDD();

            sql = sql.concat(" AND  u.idpadre = " + ubicacionBean.getId());
            sql = sql.concat(" ORDER BY  ubicacionescentro,  ubicacionesdescripcion  ");
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                lista.add(getRegistroResulset(resulSet, ubicacionBean.getCentro()));
            }
            statement.close();
            LOGGER.debug(sql);
        } catch (SQLException e) {
            LOGGER.error(sql + Utilidades.getStackTrace(e));
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
    public String getNombreCompleto(UbicacionBean ubicacion) {
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

    /**
     *
     * @param codigo
     * @return
     */
    @Override
    public UbicacionBean getPorCodigo(String codigo) {
        return null;
    }
}
