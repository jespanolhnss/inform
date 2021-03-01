package es.sacyl.gsa.inform.dao;

/**
 *
 * @author juannietopajares
 */
public interface InterfazDao {

    /**
     * Graba datos del objeto en la base de datos.
     *
     * @param object the object
     * @return true, if successful
     */
    public interface grabaDatos<T> {
    };

    /**
     * Actualiza datos del objeto de referencia en la base de datos.
     *
     * @param mensajeparam the mensajeparam
     * @return true, if successful
     */
    //  public interface actualizaDatos<T> {     };
    /**
     * Inserta en la base de datos el objeto .
     *
     * @param mensajeparam the mensajeparam
     * @return true, if successful
     */
    //  public interface insertaDatos<T> {    };
    /**
     * Recupera un objeto bean a partir del objeto resulset.
     *
     * @param rs the rs
     * @return the registro resulset
     */
    //  public interface getRegistroResulset<T> {    };
    /**
     * A partir del id de la tabla recupera el objeto bean
     *
     * @param id the id
     * @return the registro id
     */
    // public interface getRegistroId<T> {     };
    /**
     * Construye la sentencia WHERE para las búsquedas de registros tabulados.
     *
     * @param cadena the cadena
     * @return the sql where
     */
    //  public interface getSqlWhere<T> {    };
    /**
     * Borra de la base de datos el objeto de referencia.
     *
     * @param objeto the objeto
     * @return true, if successful
     */
    //  public interface borraDatos<T> {    };
    //   public interface getListaRegistros<T> {    };
    //  public interface interfacegetListaRegistros<T, t> {    };
}
