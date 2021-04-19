package es.sacyl.gsa.inform.dao;

import com.vaadin.flow.server.VaadinSession;
import es.sacyl.gsa.inform.bean.CategoriaBean;
import es.sacyl.gsa.inform.bean.FuncionalidadBean;
import es.sacyl.gsa.inform.bean.ParametroBean;
import es.sacyl.gsa.inform.bean.UsuarioBean;
import es.sacyl.gsa.inform.bean.UsuarioPeticionAppBean;
import es.sacyl.gsa.inform.bean.UsuarioPeticionBean;
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
                + ",usu.idcategoria as usuarioidcategoria,usu.movil as usuariomovil"
                + ",usu.mailprivado as usuariomailprivado,usu.telegram as usuariotegegram"
                + ",usu.solicita as usuariosolicita"
                + ",uc.id as usuarioscategoriaid, uc.CODIGOPERSIGO as usuarioscategoriacodigo"
                + ",uc.nombre as usuarioscategoriaanombre,uc.estado as usuarioscategoriaestado  "
                + " FROM usuarios usu  "
                + " LEFT JOIN categorias uc ON uc.id=usu.idcategoria "
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
            usuario.setCategoria(new CategoriaDao().getPorId(resulSet.getLong("usuarioidcategoria")));
            usuario.setGfh(new GfhDao().getPorId(resulSet.getLong("usuarioidgfh")));
            usuario.setEstado(resulSet.getInt("usuarioestado"));
            usuario.setCorreoPrivadoUsuario(resulSet.getString("usuariomailprivado"));
            usuario.setMovilUsuario(resulSet.getString("usuariomovil"));
            usuario.setTelegram(resulSet.getString("usuariotegegram"));
            usuario.setSolicita(resulSet.getString("usuariosolicita"));

            usuario.setCategoria(CategoriaDao.getRegistroResulset(resulSet));

        } catch (SQLException e) {
            logger.error(Utilidades.getStackTrace(e));
        }
        return usuario;
    }

    /**
     *
     * @param resulSet
     * @return Este méto se usa cuando una tabla tiene el usucambio y un campo
     * usuarionormal. Monta la sql con otros nombre de columnas usueid, usue....
     */
    public static UsuarioBean getRegistroResulsetUsuario(ResultSet resulSet) {
        UsuarioBean usuario = null;
        try {
            usuario = new UsuarioBean();
            usuario.setId(resulSet.getLong("usueid"));
            usuario.setDni(resulSet.getString("usuedni"));
            usuario.setApellido1(resulSet.getString("usueapellido1"));
            usuario.setApellido2(resulSet.getString("usueapellido2"));
            usuario.setNombre(resulSet.getString("usuenombre"));
            usuario.setMail(resulSet.getString("usuemail"));
            usuario.setTelefono(resulSet.getString("usuetelefon"));
            usuario.setCategoria(new CategoriaDao().getPorId(resulSet.getLong("usuarioidcategoria")));
            usuario.setGfh(new GfhDao().getPorId(resulSet.getLong("usuarioidgfh")));
            usuario.setEstado(resulSet.getInt("usueestado"));

        } catch (SQLException e) {
            LOGGER.error(Utilidades.getStackTrace(e));
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
                statement.close();
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
        String sqlDni = sql;
        try {
            connection = super.getConexionBBDD();
            sqlDni = sqlDni.concat(" AND usu.dni='" + dni + "'");
            logger.debug(sqlDni);
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(sqlDni);
                if (resulSet.next()) {
                    usuario = getRegistroResulset(resulSet);
                    if (conFunciolidades == true) {
                        usuario.setFucionalidadesArrayList(new FuncionalidadDAO().getListaFuncioUsuarioAl(usuario));
                    }
                }
                statement.close();
            }
        } catch (SQLException e) {
            logger.error(sqlDni + Utilidades.getStackTrace(e));
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
            String inserta;
            inserta = " UPDATE  usuarios " + 
                    "SET dni=?,apellido1=?,apellido2=?,nombre=?, mail=? " + 
                    ", telefono=?,idcategoria=?,idgfh=?,usucambio=?,fechacambio=? " + 
                    ",estado=?, movil=?,mailprivado=?,telegram=?, solicita =? "
                    + " WHERE id=?  ";
            PreparedStatement statement = connection.prepareStatement(inserta);

            statement.setString(1, usuarioBean.getDni());
            if (usuarioBean.getApellido1() != null) {
                statement.setString(2, usuarioBean.getApellido1());
            } else {
                statement.setNull(2, Types.CHAR);
            }            
            if (usuarioBean.getApellido2() != null) {
                statement.setString(3, usuarioBean.getApellido2());
            } else {
                statement.setNull(3, Types.CHAR);
            }            
            if (usuarioBean.getNombre() != null) {
                statement.setString(4, usuarioBean.getNombre());
            } else {
                statement.setNull(4, Types.CHAR);
            }            
            if (usuarioBean.getMail() != null) {
                statement.setString(5, usuarioBean.getMail());
            } else {
                statement.setNull(5, Types.CHAR);
            }            
            if (usuarioBean.getTelefono() != null) {
                statement.setString(6, usuarioBean.getTelefono());
            } else {
                statement.setNull(6, Types.CHAR);
            }            
            if (usuarioBean.getIdCategoria() != null) {
                statement.setLong(7, usuarioBean.getIdCategoria());
            } else {
                statement.setNull(7, Types.INTEGER);
            }            
            if (usuarioBean.getIdGfh() != null) {
                statement.setLong(8, usuarioBean.getIdGfh());
            } else {
                statement.setNull(8, Types.INTEGER);
            }
            if (usuarioBean.getUsucambio() == null) {
                statement.setLong(9, usuarioBean.getUsucambio().getId());
            } else {
                // si no hay usuario de cambio pone el mismo
                statement.setLong(9, UsuarioBean.USUARIO_SISTEMA.getId());
            }
            statement.setLong(10, Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
            if (usuarioBean.getEstado() != null) {
                statement.setInt(11, usuarioBean.getEstado());
            } else {
                statement.setNull(11, Types.INTEGER);
            }            
            if (usuarioBean.getMovilUsuario() != null) {
                statement.setString(12, usuarioBean.getMovilUsuario());
            } else {
                statement.setNull(12, Types.CHAR);
            }
            if (usuarioBean.getCorreoPrivadoUsuario() != null) {
                statement.setString(13, usuarioBean.getCorreoPrivadoUsuario());
            } else {
                statement.setNull(13, Types.CHAR);
            }
            if (usuarioBean.getTelegram() != null) {
                statement.setString(14, usuarioBean.getTelegram());
            } else {
                statement.setNull(14, Types.CHAR);
            } 
            if (usuarioBean.getSolicita() != null) {
                statement.setString(15, usuarioBean.getSolicita());
            } else {
                statement.setNull(14, Types.CHAR);
            } 
            statement.setLong(16, usuarioBean.getId());
            insertado = statement.executeUpdate() > 0;
            statement.close();
            logger.debug(inserta);
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
        //Long id = null;
        try {
            connection = super.getConexionBBDD();
            String inserta;
            inserta = " INSERT INTO usuarios " +
                    "(id,dni,apellido1,apellido2,nombre,mail,telefono,idcategoria " 
                    + ",idgfh,usucambio,fechacambio,estado " 
                    + ",movil,mailprivado,telegram,solicita)" 
                    + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ";
            PreparedStatement statement = connection.prepareStatement(inserta);
            statement.setLong(1, usuarioBean.getId());
            statement.setString(2, usuarioBean.getDni());
            if (usuarioBean.getApellido1() != null) {
                statement.setString(3, usuarioBean.getApellido1());
            } else {
                statement.setNull(3, Types.CHAR);
            }            
            if (usuarioBean.getApellido2() != null) {
                statement.setString(4, usuarioBean.getApellido2());
            } else {
                statement.setNull(4, Types.CHAR);
            }            
            if (usuarioBean.getNombre() != null) {
                statement.setString(5, usuarioBean.getNombre());
            } else {
                statement.setNull(5, Types.CHAR);
            }            
            if (usuarioBean.getMail() != null) {
                statement.setString(6, usuarioBean.getMail());
            } else {
                statement.setNull(6, Types.CHAR);
            }            
            if (usuarioBean.getTelefono() != null) {
                statement.setString(7, usuarioBean.getTelefono());
            } else {
                statement.setNull(7, Types.CHAR);
            }            
            if (usuarioBean.getIdCategoria() != null) {
                statement.setLong(8, usuarioBean.getIdCategoria());
            } else {
                statement.setNull(8, Types.INTEGER);
            }            
            if (usuarioBean.getIdGfh()!= null) {
                statement.setLong(9, usuarioBean.getIdGfh());
            } else {
                statement.setNull(9, Types.INTEGER);
            }            
            if (usuarioBean.getUsucambio() != null) {
                statement.setLong(10, usuarioBean.getUsucambio().getId());
            } else {
                // si no hay usuario de cambio pone el mismo
                statement.setLong(10, UsuarioBean.USUARIO_SISTEMA.getId());
            }
            statement.setLong(11, Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
            statement.setInt(12, usuarioBean.getEstado());         
            if (usuarioBean.getMovilUsuario() != null) {
                statement.setString(13, usuarioBean.getMovilUsuario());
            } else {
                statement.setNull(13, Types.CHAR);
            }
            if (usuarioBean.getCorreoPrivadoUsuario() != null) {
                statement.setString(14, usuarioBean.getCorreoPrivadoUsuario());
            } else {
                statement.setNull(14, Types.CHAR);
            }
            if (usuarioBean.getTelegram() != null) {
                statement.setString(15, usuarioBean.getTelegram());
            } else {
                statement.setNull(15, Types.CHAR);
            }
            if (usuarioBean.getSolicita() != null) {
                statement.setString(16, usuarioBean.getSolicita());
            } else {
                statement.setNull(16, Types.CHAR);
            }            

            insertado = statement.executeUpdate() > 0;
            insertado = true;
            statement.close();
            logger.debug(inserta);
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
            sql = " UPDATE usuarios SET   usucambio='" + usuarioa.getId() + "',  fechacambio="
                    + Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))) + ",estado=" + ConexionDao.BBDD_ACTIVONO + " WHERE id="
                    + ob.getId();
            try (Statement statement = connection.createStatement()) {
                insertadoBoolean = statement.execute(sql);
                insertadoBoolean = true;
            }
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
            if (texto != null && !texto.isEmpty()) {
                if (Utilidades.isNumeric(texto.substring(0))) {
                    sql = sql.concat(" AND usu.dni like '" + texto + "%'");
                } else {
                    sql = sql.concat(" AND  (upper( usu.apellido1) like '" + texto.toUpperCase() + "%'  OR  upper(usu.apellido2) like '" + texto.toUpperCase() + "%')");
                }
            }
            sql = sql.concat(" AND usu.estado=" + ConexionDao.BBDD_ACTIVOSI);
            sql = sql.concat(" ORDER BY usu.apellido1,usu.apellido2,usu.nombre	");

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

    /**
     *
     * @param dni
     * @param apellido1
     * @param apellido2
     * @param nombre
     * @param categoriaBean
     * @param registros
     * @return
     */
    public ArrayList<UsuarioBean> getLista(String dni, String apellido1, String apellido2, String nombre,
            CategoriaBean categoriaBean, Integer registros) {
        Connection connection = null;
        ArrayList<UsuarioBean> lista = new ArrayList<>();
        String sqlusu = sql;
        try {
            connection = super.getConexionBBDD();
            if (apellido1 != null && !apellido1.isEmpty()) {
                sqlusu = sqlusu.concat(" AND UPPER(apellido1) LIKE '" + apellido1.toUpperCase() + "%'");
            }
            if (apellido2 != null && !apellido2.isEmpty()) {
                sqlusu = sqlusu.concat(" AND UPPER(apellido2) LIKE '" + apellido2.toUpperCase() + "%'");
            }
            if (nombre != null && !nombre.isEmpty()) {
                sqlusu = sqlusu.concat(" AND UPPER(nombre) LIKE '" + nombre.toUpperCase() + "%'");
            }
            if (dni != null && !dni.isEmpty()) {
                sqlusu = sqlusu.concat(" AND  UPPER(dni) LIKE '" + dni.toUpperCase() + "%'");
            }
            if (categoriaBean != null && categoriaBean.getId() != null) {
                sqlusu = sqlusu.concat(" AND idcategoria ='" + categoriaBean.getId() + "'");
            }
            if (registros != 0) {
                sqlusu = sqlusu.concat(" AND rownum<=" + registros);
            }
            sqlusu = sqlusu.concat(" AND usu.estado=" + ConexionDao.BBDD_ACTIVOSI);
            sqlusu = sqlusu.concat(" ORDER BY usu.apellido1,usu.apellido2,usu.nombre	");

            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sqlusu);
            while (resulSet.next()) {
                UsuarioBean usuario = getRegistroResulset(resulSet);
                lista.add(usuario);
            }
            statement.close();
            logger.debug(sqlusu);
        } catch (SQLException e) {
            logger.error(sqlusu + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return lista;
    }

    public UsuarioBean getUsuarioResulSetJimena(ResultSet resulSet, UsuarioBean usuarioParam) {
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

    @Override
    public UsuarioBean getPorCodigo(String codigo) {
        return null;
    }

    public ArrayList<UsuarioBean> getInformaticos() {

        ArrayList<UsuarioBean> listaUsuarios = new ArrayList<>();
        String dnis = new ParametroDao().getPorCodigo(ParametroBean.USR_INFORMATICOS).getValor();
        String[] dni = dnis.split(",");
        for (String undni : dni) {
            UsuarioBean usu = getUsuarioDni(undni, Boolean.FALSE);
            if (usu != null) {
                listaUsuarios.add(usu);
            }
        }
        return listaUsuarios;
    }

    public UsuarioBean getUsuarioPersigo(String value) {

        Connection connection = null;
        UsuarioBean usuario = new UsuarioBean();

        try {
            connection = super.getConexionBBDD();
            String select = " select nif,ape1,ape2,nombre,mail FROM usuariospersigo where nif = '" + value + "'";
            try (Statement statement = connection.createStatement()) {
                ResultSet resulSet = statement.executeQuery(select);
                while (resulSet.next()) {
                    usuario.setDni(resulSet.getString("nif"));
                    usuario.setApellido1(resulSet.getString("ape1"));
                    usuario.setApellido2(resulSet.getString("ape2"));
                    usuario.setNombre(resulSet.getString("nombre"));
                    usuario.setMail(resulSet.getString("mail"));
                }
            }
            logger.debug(select);
        } catch (SQLException e) {
            logger.error(sql + Utilidades.getStackTrace(e));
        } catch (Exception e) {
            logger.error(Utilidades.getStackTrace(e));
        } finally {
            this.doCierraConexion(connection);
        }
        return usuario;
    }

    public boolean doGrabaPeticion(UsuarioBean usuario, UsuarioPeticionBean peticion) {
        Connection connection = null;
        boolean insertado = false;
        peticion.setId(getSiguienteId("usuariospeticiones"));
        peticion.setIdusuario(usuario.getId());
        
        try {
            connection = super.getConexionBBDD();
            String insertar;
            insertar = "insert into usuariospeticiones " + 
                    "(id,idpeticionario,idusuario,estado,fechasolicitud,centro,comentario,tipo) " +
                    "values (?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(insertar);
            statement.setLong(1, peticion.getId());
            statement.setLong(2, peticion.getIdpeticionario());
            statement.setLong(3, peticion.getIdusuario());
            statement.setInt(4, ConexionDao.BBDD_ACTIVONO);
            statement.setInt(5, Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))));
            if (peticion.getCentros() != null) {
                statement.setString(6, peticion.getCentros());
            } else {
                statement.setNull(6, Types.CHAR);
            }
            if (peticion.getComentario() != null) {
                statement.setString(7, peticion.getComentario());
            } else {
                statement.setNull(7, Types.CHAR);
            }            
            if (peticion.getTipo() != null) {
                statement.setString(8, peticion.getTipo());
            } else {
                statement.setNull(9, Types.CHAR);
            }           
            insertado = statement.executeUpdate() > 0;
            statement.close();
            logger.debug(insertar);           
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

    public boolean doGrabaPeticionApp(UsuarioPeticionBean peticionBean, ArrayList<UsuarioPeticionAppBean> arrayListAplicaciones) {
        Connection connection = null;
        boolean insertado = false;
        
        for (UsuarioPeticionAppBean peticionAppBean : arrayListAplicaciones) {        
            peticionAppBean.setId(getSiguienteId("usuariospeticionesapp"));
            peticionAppBean.setIdPeticion(peticionBean.getId());
            
            try {
                connection = super.getConexionBBDD();
                String insertar; 
                insertar = "insert into usuariospeticionesapp " + 
                        "(id,idpeticion,idaplicacion,idperfil,tipo,comentario) " +
                        "values (?,?,?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(insertar);
                statement.setLong(1, peticionAppBean.getId());
                if (peticionAppBean.getIdPeticion() != null) {
                    statement.setLong(2, peticionAppBean.getIdPeticion());
                } else {
                    statement.setNull(2, Types.INTEGER);
                }                
                if (peticionAppBean.getIdAplicacion() != null) {
                    statement.setLong(3, peticionAppBean.getIdAplicacion());
                } else {
                    statement.setNull(3, Types.INTEGER);
                }                
                if (peticionAppBean.getPerfil() != null) {
                    statement.setLong(4, peticionAppBean.getIdPerfil());
                } else {
                    statement.setNull(4, Types.INTEGER);
                }                
                if (peticionAppBean.getTipo() != null) {
                    statement.setString(5, peticionAppBean.getTipo());
                } else {
                    statement.setNull(5, Types.CHAR);
                }
                if (peticionAppBean.getComentario() != null) {
                    statement.setString(6, peticionAppBean.getComentario());
                } else {
                    statement.setNull(6, Types.CHAR);
                }               
                insertado = statement.executeUpdate() > 0;
                insertado = true;
                statement.close();
                logger.debug(insertar);  
            } catch (SQLException e) {
                logger.error(Utilidades.getStackTrace(e));
                logger.error(ConexionDao.ERROR_BBDD_SQL, e);
            } catch (Exception e) {
                logger.error(Utilidades.getStackTrace(e));
            } finally {
                this.doCierraConexion(connection);
            }            
        }       
        
            return insertado;     
        
        }  
    
    /**
     *
     * @param texto
     * @return
     */
    public ArrayList<UsuarioBean> getListaPeticiones(Long id) {
        Connection connection = null;
        ArrayList<UsuarioBean> lista = new ArrayList<>();
        
        try {
            connection = super.getConexionBBDD();
            
            if (id != null) {
                //sql = sql.concat(" AND up.idpeticionario = '" + texto + "'");   
                sql = sql.concat(" and usu.id in (select idusuario from usuariospeticiones where idpeticionario = " + id + ")");
            }           
            //select = select.concat(" ORDER BY usu.apellido1,usu.apellido2,usu.nombre");

            Statement statement = connection.createStatement();
            ResultSet resulSet = statement.executeQuery(sql);
            while (resulSet.next()) {
                UsuarioBean peticion = getRegistroResulset(resulSet);                
                lista.add(peticion);
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

        
              
    
}
