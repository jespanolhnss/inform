package es.sacyl.gsa.inform.dao;

import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.FuncionalidadBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.util.Constantes;
import es.sacyl.gsa.inform.util.Utilidades;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Class UsuarioDao.
 *
 * @author Juan Nieto
 */
public class UsuarioDao extends ConexionDao implements Serializable, ConexionInterface<UsuarioBean> {

    private static final Logger LOGGER = LogManager.getLogger(UsuarioDao.class);
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LogManager.getLogger(UsuarioDao.class);

    /**
     * Instantiates a new usuario dao.
     */
    public UsuarioDao() {
        super();

        sql = " SELECT usu.id as usuarioid,usu.dni as usuariodni,usu.apellido1 as usuarioapellido1"
                + ",usu.apellido2 as usuarioapellido2,usu.nombre as usuarionombre"
                + ",usu.estado as usuarioestado,usu.usucambio as usuariousucambio"
                + ",usu.fechacambio as usuariofechacambio,usu.mail as usuariomail"
                + ",usu.telefono as usuariotelefon,usu.idgfh as usuarioidgfh"
                + ",usu.idcategoria as usuarioidcategoria"
                + " FROM usuarios usu "
                + " WHERE 1=1";

    }

    /**
     * Gets the usuario resulset.
     *
     * @param resulSet the resul set
     * @return the usuario resulset
     */
    @Override
    public UsuarioBean getRegistroResulset(ResultSet resulSet) {
        UsuarioBean usuario = null;
        try {
            usuario = new UsuarioBean();
            usuario.setId(resulSet.getLong("usuarioid"));
            usuario.setDni(resulSet.getString("usuariodni"));
            usuario.setApellido1(resulSet.getString("usuarioapellido1"));
            usuario.setApellido2(resulSet.getString("usuarioapellido2"));
            usuario.setNombre(resulSet.getString("usuarionombre"));
            usuario.setMail(resulSet.getString("usuariomail"));
            usuario.setTelefono(resulSet.getString("usuariotelefon"));
            usuario.setEstado(resulSet.getInt("usuarioestado"));
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }
        return usuario;
    }

    @Override
    public UsuarioBean getPorId(Long id) {
        Connection connection = null;
        UsuarioBean usuario = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND usu.id='" + id + "'");
            logger.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    usuario = getRegistroResulset(resulSet);
                    //  if (conFunciolidades == true) {
                    //     usuario.setFucionalidadesArrayList(new FuncionalidadDAO().getListaFuncioUsuarioAl(usuario));
                    // }
                }
            }
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return usuario;

    }

    /**
     * Gets the usuario userid.
     *
     * @param userid the userid
     * @param conFunciolidades
     * @return the usuario userid
     */
    public UsuarioBean getUsuarioDni(String dni, Boolean conFunciolidades) {
        Connection connection = null;
        UsuarioBean usuario = null;
        try {
            connection = super.getConexionBBDD();
            sql = sql.concat(" AND usu.dni='" + dni + "'");
            logger.debug(sql);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sql);
                if (resulSet.next()) {
                    usuario = getRegistroResulset(resulSet);
                    if (conFunciolidades == true) {
                        usuario.setFucionalidadesArrayList(new FuncionalidadDAO().getListaFuncioUsuarioAl(usuario));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return usuario;
    }

    @Override
    public boolean doGrabaDatos(UsuarioBean usuarioBean) {
        boolean grabado = false;

        if (usuarioBean.getId().equals(new Long(0))) {
            usuarioBean.setId(getSiguienteId("usuarios"));
            grabado = this.doInsertaDatos(usuarioBean);
        } else {
            grabado = this.doActualizaDatos(usuarioBean);
            //  actualizado = doActualizaFuncionalidad(usuarioBean);
        }
        return grabado;
    }

    public boolean doActualizaFuncionalidad(UsuarioBean usuario) {
        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            UsuarioBean usuarioa = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
            connection = super.getConexionBBDD();

            sql = " UPDATE   us_funcionalidad SET usucambio=" + usuarioa.getId() + ",  fechacambio="
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ",estado=" + ConexionDao.BBDD_ACTIVONO
                    + " WHERE idusuario=" + usuario.getId();
            Statement statement = connection.createStatement();
            logger.debug(sql);
            insertadoBoolean = statement.execute(sql);

            Iterator<String> iterator = usuario.getFuncionalidadStrings().iterator();
            while (iterator.hasNext()) {
                String fun = iterator.next();
                FuncionalidadBean funcionalidad = new FuncionalidadDAO().getPorDescripcion(fun);
                Long id = this.getSiguienteId("us_funcionalidad");
                sql = " INSERT INTO us_funcionalidad "
                        + " (id,idusuario,idfuncionalidad,permitida,estado,fechacambio,usucambio) " + " VALUES (" + id
                        + "," + usuario.getId() + "," + funcionalidad.getId() + ",1," + ConexionDao.BBDD_ACTIVOSI + ","
                        + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "," + usuario.getId() + ")";
                insertadoBoolean = statement.execute(sql);
                logger.debug(sql);
            }

            insertadoBoolean = true;
            statement.close();

        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertadoBoolean;
    }

    /**
     *
     * @param autonomiaBean
     * @return
     */
    @Override
    public boolean doActualizaDatos(UsuarioBean usuarioBean) {
        Connection connection = null;
        boolean insertado = false;
        Long id = null;
        try {
            connection = super.getConexionBBDD();
            sql = " UPDATE  usuarios  SET dni=?,apellido1=?,apellido2=?,nombre=?"
                    + ", mail=?,telefono=?,usucambio=?,fechacambio=?,estado=? "
                    + " WHERE id=?  ";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, usuarioBean.getDni());
            statement.setString(2, usuarioBean.getApellido1());
            statement.setString(3, usuarioBean.getApellido2());
            statement.setString(4, usuarioBean.getNombre());
            statement.setString(5, usuarioBean.getMail());
            statement.setString(6, usuarioBean.getTelefono());
            if (usuarioBean.getUsucambio() == null) {
                statement.setLong(7, usuarioBean.getUsucambio().getId());
            } else {
                statement.setNull(7, Types.INTEGER);
            }
            statement.setLong(8, Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
            statement.setInt(9, usuarioBean.getEstado());
            statement.setLong(10, usuarioBean.getId());
            insertado = statement.executeUpdate() > 0;
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
            logger.error(ConexionDao.ERROR_BBDD_SQL, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertado;
    }

    public boolean actualizaMailTf(UsuarioBean usuario) {
        Connection connection = null;
        Boolean actualizado = false;
        if (!usuario.getMail().isEmpty() && !usuario.getTelefono().isEmpty()) {
            try {

                connection = super.getConexionBBDD();

                sql = " UPDATE usuarios SET mail='" + usuario.getMail() + "', telefono='" + usuario.getTelefono()
                        + "',   fechacambio=" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + " WHERE id="
                        + usuario.getId();
                Statement statement = connection.createStatement();
                actualizado = statement.execute(sql);
                actualizado = true;
                statement.close();
                logger.debug(sql);
            } catch (SQLException e) {
                logger.error(sql + Utilidades.getStackTrace(e));
            } catch (Exception e) {
                logger.error(Utilidades.getStackTrace(e));
            } finally {
                this.doCierraConexion(connection);
            }
        }

        return actualizado;
    }

    /**
     *
     * @param usuarioBean
     * @return
     */
    @Override
    public boolean doInsertaDatos(UsuarioBean usuarioBean) {
        Connection connection = null;
        boolean insertado = false;
        Long id = null;
        try {
            connection = super.getConexionBBDD();
            sql = " INSERT INTO usuarios (id,dni,apellido1,apellido2,nombre, mail,telefono,usucambio,fechacambio,estado) "
                    + " VALUES (?,?,?,?,?,?,?,?,?,?)  ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, usuarioBean.getId());
            statement.setString(2, usuarioBean.getDni());
            statement.setString(3, usuarioBean.getApellido1());
            statement.setString(4, usuarioBean.getApellido2());
            statement.setString(5, usuarioBean.getNombre());
            statement.setString(6, usuarioBean.getMail());
            statement.setString(7, usuarioBean.getTelefono());
            if (usuarioBean.getUsucambio() == null) {
                statement.setLong(8, usuarioBean.getUsucambio().getId());
            } else {
                statement.setNull(8, Types.INTEGER);
            }
            statement.setLong(9, Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
            statement.setInt(10, usuarioBean.getEstado());
            insertado = statement.executeUpdate() > 0;
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
            logger.error(ConexionDao.ERROR_BBDD_SQL, e);
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return insertado;
    }

    @Override
    public boolean doBorraDatos(UsuarioBean ob) {

        Connection connection = null;
        Boolean insertadoBoolean = false;
        try {
            UsuarioBean usuarioa = (UsuarioBean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USERNAME);
            connection = super.getConexionBBDD();

            sql = " UPDATE usuarios SET   usucambio='" + usuarioa.getDni() + "',  fechacambio="
                    + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ",estado=" + ConexionDao.BBDD_ACTIVONO + " WHERE id="
                    + usuarioa.getId();
            Statement statement = connection.createStatement();
            insertadoBoolean = statement.execute(sql);
            insertadoBoolean = true;
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
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
    public ArrayList<UsuarioBean> getLista(String texto) {
        Connection connection = null;
        ArrayList<UsuarioBean> lista = new ArrayList<>();

        try {
            connection = super.getConexionBBDD();

            sql = sql.concat("AND estado = " + ConexionDao.BBDD_ACTIVOSI);
            sql = sql.concat(" ORDER BY apellido1,apellido2,nombre	");

            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                UsuarioBean usuario = getRegistroResulset(resulSet);
                lista.add(usuario);
            }
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    public UsuarioBean getUsuairoResulSetJimena(ResultSet resulSet, UsuarioBean usuarioParam) {
        UsuarioBean usuario = null;
        if (usuarioParam != null) {
            usuario = usuarioParam;
        } else {
            try {
                usuario = new UsuarioBean();
                usuario.setDni(resulSet.getString("usuuserid"));
                usuario.setApellido1(resulSet.getString("usuapellido1"));
                usuario.setApellido2(resulSet.getString("usuapellido2"));
                usuario.setNombre(resulSet.getString("usunombre"));
                usuario.setEstado(resulSet.getInt("usuestado"));
            } catch (SQLException e) {
                logger.error(Utilidades.getStackTrace(e));
            }
        }
        return usuario;
    }

    public ArrayList<UsuarioBean> getUsuariosFiltro(String texto, String campo) {
        Connection connection = null;
        ArrayList<UsuarioBean> listaUsuarios = new ArrayList<>();
        try {
            connection = super.getConexionBBDD();
            sql = " SELECT * FROM usuarios WHERE  1=1 and nombre = 'JUAN' ";
//            if (texto != null && !texto.isEmpty() && campo != null && !campo.isEmpty()) {
//                sql = sql.concat(" AND  " + campo + " = " + texto.toUpperCase() + "");
//            }
            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                listaUsuarios.add(getRegistroResulset(resulSet));
            }
            statement.close();
            logger.debug(sql);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return listaUsuarios;
    }

    @Override
    public UsuarioBean getPorCodigo(String codigo) {
        return null;
    }

    public ArrayList<UsuarioBean> getInformaticos() {
        Connection connection = null;
        ArrayList<UsuarioBean> listaUsuarios = new ArrayList<>();
        UsuarioBean juan = new UsuarioBean();
        juan.setId(new Long(1));
        juan.setApellido1("Nieto");
        juan.setApellido2("Pajares");
        juan.setNombre("Juan");
        listaUsuarios.add(juan);
        UsuarioBean antonio = new UsuarioBean();
        antonio.setId(new Long(1651));
        antonio.setApellido1("Hurtado");
        antonio.setApellido2("Losáñez");
        antonio.setNombre("Antonio");
        listaUsuarios.add(antonio);
        return listaUsuarios;
    }

}
