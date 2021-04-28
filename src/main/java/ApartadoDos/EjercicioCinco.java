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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EjercicioCinco {

    /**
     * Borra un libro cuya fecha del último préstamo sea anterior al año 2005.
     **/
    private static String selectedID;
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
                Date dateBefore = new SimpleDateFormat("dd-MM-yyyy").parse("31-12-2005");
                Date dataReal;
                selectedID = "0";
                for (int k = 1; k < prestamos.getPrestamo().size(); k++) {
                    dataReal = new SimpleDateFormat("dd-MM-yyyy").parse(prestamos.getPrestamo().get(k).getFechaprestamo());
                    if (dataReal.before(dateBefore)) {
                        selectedID = prestamos.getPrestamo().get(k).getLibro();
                    }
                }
                ResourceSet resultDelete = xpqs.query("update delete //prestamos/prestamo[libro = " + selectedID + "]");
            } catch (JAXBException | ParseException e) {
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
