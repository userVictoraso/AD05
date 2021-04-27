package ApartadoDos;

import ApartadoUno.Conexion;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;

import java.lang.reflect.InvocationTargetException;

public class EjercicioDos {

    /**Se quiere saber que libros están prestados actualmente. Para ello añade el elemento prestado
     * con valor válido para el rango de libros prestados e invalido para el rango de libros devueltos.**/

    public static void main(String[] args) throws ClassNotFoundException, XMLDBException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class cl = Class.forName(Conexion.DRIVER);
        Database database = (Database) cl.getDeclaredConstructor().newInstance();
        database.setProperty("create-database", "true");
        DatabaseManager.registerDatabase(database);

        Collection col = null;

        try {
            col = DatabaseManager.getCollection(Conexion.URI + Conexion.COLLECTION, Conexion.USERNAME, Conexion.PASSWORD);
            XPathQueryService xpqs = (XPathQueryService) col.getService("XPathQueryService", "1.0");
            xpqs.setProperty("indent", "yes");
            ResourceSet result = xpqs.query("/libros/libro");
            ResourceIterator i = result.getIterator();
            Resource res = null;
            int iterator =1;
            while (i.hasMoreResources()) {
                iterator++;
                res = i.nextResource();
                System.out.println(res.getContent());
                result = xpqs.query("update insert <prestado>" + getCondition() + "</prestado> into /libros/libro[position()= " + iterator +"]");
            }
        } finally {
            if (col != null) {
                try {
                    col.close();
                } catch (XMLDBException xe) {
                    xe.printStackTrace();
                }
            }
        }
    }
    private static String getCondition() {
        String condition = "no disponible";
        int random = (int) ((Math.random() * 10) + 1);
        if(random <=5){
            condition = "valido";
        }
        if(random >6){
            condition = "invalido";
        }
        return condition;
    }
}
