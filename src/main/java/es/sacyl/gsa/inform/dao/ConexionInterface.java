
package es.sacyl.gsa.inform.dao;

import java.sql.ResultSet;
import java.util.ArrayList;



/**
 *
 * @author 06551256M
 */
interface ConexionInterface<T> {
    
  
    /**
     * 
     * @param rs
     * @return  Recupera un objeto bean a partir del ResultSet de una sql
     */
     public  T getRegistroResulset(ResultSet rs) ;
     
     /**
      * 
      * @param codigo
      * @return   Recupera un objeto bean asociado al código de la base de datos
      */
     public T getPorCodigo(String codigo) ;
     /**
      * 
      * @param id
      * @return  Recupera un objeto bean asociado la id de la base de datos
      */
      public T getPorId(Long id) ;
      
     public boolean doGrabaDatos(T ob) ;

      public boolean doInsertaDatos(T ob) ;
      
      public boolean doActualizaDatos(T ob) ;
      
        public boolean doBorraDatos(T ob) ;
      
         
        public ArrayList<T> getLista(String texto) ;

}