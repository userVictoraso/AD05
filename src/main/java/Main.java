import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.XPathQueryService;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static final String queryOne = "//prestamo[libro = 12]/libro, count(//prestamos/prestamo[libro = 12])";

    private static final String DRIVER = "org.exist.xmldb.DatabaseImpl";
    private final static String URI = "xmldb:exist://vps456458.ovh.net:8080/exist/xmlrpc";
    private final static String COLLECTION = "/db/victorDB/";
    private final static String USERNAME = "admin";
    private final static String PASSWORD = "ertyGvf45m";

    public static void main(String[] args) throws ClassNotFoundException, XMLDBException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class cl = Class.forName(DRIVER);
        Database database = (Database) cl.getDeclaredConstructor().newInstance();
        database.setProperty("create-database", "true");
        DatabaseManager.registerDatabase(database);

        Collection col = null;

        try {
            col = DatabaseManager.getCollection(URI + COLLECTION, USERNAME, PASSWORD);
            XPathQueryService xpqs = (XPathQueryService) col.getService("XPathQueryService", "1.0");
            xpqs.setProperty("indent", "yes");
            ResourceSet result = xpqs.query(queryFourAp2());
            ResourceIterator i = result.getIterator();
            Resource res = null;
            while (i.hasMoreResources()) {
                res = i.nextResource();
                System.out.println(res.getContent());
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

    private static String queryOneAp1() {
        /**Para cada libro, obtener número de veces prestado.**/
        System.out.println("Indica el número del libro");
        String bookId = scanner.nextLine();
        String query = "//prestamo[libro = " + bookId + "]/libro, count(//prestamos/prestamo[libro = " + bookId + "])";
        return query;
    }

    private static String queryTwoAp1() {
        /**Listado de libros, publicados después del año 2000, junto con el número de veces que ha sido prestado.
         * Ordena el resultado por nombre del autor.**/
        String query = "//libro[publicacion > 2000]/titulo";
        return query;
    }

    private static String queryOneAp2() {
        /**Actualiza el año de edición del libro cuyo id es 2.**/
        //TODO update value //publicacion [id = 2] with 1700
        String query = "update value //publicacion[. = 1997] with 1777";
        return query;
    }

    private static String queryTwoAp2() {
        /**Se quiere saber que libros están prestados actualmente. Para ello añade el elemento prestado
         * con valor válido para el rango de libros prestados e invalido para el rango de libros devueltos.**/
        //1 = prestado; 0 = devuelto
        String query = "//libro[prestado = 1]/titulo";
        return query;
    }

    private static String queryThreeAp2() {
        /**Actualiza el elemento prestado creado en el paso anterior por un valor distinto.**/
        //TODO
        String query = "update value //prestado[. = 0] with 1";
        return query;
    }

    private static String queryFourAp2() {
        /**Actualiza el elemento prestado de todos los libros para que pase a llamarse enprestamo.**/
        //TODO
        String query = "update replace //prestado[. = 0 and . = 1] with 'enprestamo'";
        return query;
    }

    private static String queryFiveAp2() {
        /**Borra un libro cuya fecha del último préstamo sea anterior al año 2005.**/
        //TODO
        String query = "update delete /libro[publicacion = '1988']";
        return query;
    }


}
