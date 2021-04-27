package ApartadoDos;

import ApartadoUno.Conexion;
import Model.Prestamos;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;

public class EjercicioCinco {

    /** Borra un libro cuya fecha del último préstamo sea anterior al año 2005. **/

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
            ResourceSet result = xpqs.query("//prestamos");
            ResourceIterator i = result.getIterator();
            Resource res;
            String xmlStr = null;

            while (i.hasMoreResources()) {
                res = i.nextResource();
                xmlStr = res.getContent().toString();
            }

            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(Prestamos.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Prestamos prestamos = (Prestamos) jaxbUnmarshaller.unmarshal(new StringReader(xmlStr));
                System.out.println(prestamos.getPrestamo().get(5).getFechadevolucion());
            } catch (JAXBException e) {
                e.printStackTrace();
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
