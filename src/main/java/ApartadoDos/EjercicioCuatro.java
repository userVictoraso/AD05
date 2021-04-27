package ApartadoDos;

import ApartadoUno.Conexion;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;

import java.lang.reflect.InvocationTargetException;

public class EjercicioCuatro {

    /**Actualiza el elemento prestado de todos los libros para que pase a llamarse enprestamo.**/

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
            ResourceSet result = xpqs.query("//libros/libro/titulo");
            ResourceIterator i = result.getIterator();
            Resource res = null;
            int iterator = 1;
            while (i.hasMoreResources()) {
                iterator++;
                res = i.nextResource();
                System.out.println(res.getContent());
                result = xpqs.query("update value //libros/libro/prestado with 'enprestamo'");
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
}
