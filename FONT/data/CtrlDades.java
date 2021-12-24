package data;
import java.util.*;
import domain.*;


public class CtrlDades {
    private static CtrlDades singletonObject;
    private static CtrlItemCSV ctrlItemCSV;
    private static CtrlRatingsCSV ctrlRatingsCSV;
    private static CtrlAvaluacioCSV ctrlAvaluacioCSV;
    private static JsonParser parser;

    public static CtrlDades getInstance() {
        if (singletonObject == null)
            singletonObject = new CtrlDades();
        return singletonObject;
    }

    private CtrlDades() {
        ctrlItemCSV = CtrlItemCSV.getInstance();
        ctrlRatingsCSV = CtrlRatingsCSV.getInstance();
        ctrlAvaluacioCSV = CtrlAvaluacioCSV.getInstance();
        parser = JsonParser.getInstance();
    }

    public void carregarItemsCSV(String filename) throws Exception {
        ctrlItemCSV.load(filename);
    }

    public ArrayList<String[]> carregarValoracionsCSV(String filename) throws Exception {
        return ctrlRatingsCSV.load(filename, ",");
    }
    
    public void saveAvaluacio(ArrayList<String[]> avaluacions) throws Exception
    {
    	ctrlAvaluacioCSV.dump("avaluacio.csv", ",", avaluacions);
    }
    
    public void saveValoracionsCSV(ArrayList<String[]> valoracions, String nom) throws Exception
    {
    	ctrlRatingsCSV.dump(nom, ",", valoracions);
    }
    
    public <T> List<T> loadGeneric(Class<T> clazz, String arxiu, String array) throws Exception
    {
    	List<Object> objectList = parser.loadObjects(clazz, arxiu, array);
    	List<T> l = new ArrayList<>();
    	
    	for (Object o : objectList)
    		l.add((T) o);
    	
    	return l;
    }
    
    public <T> List<T> loadGenericFullPath(Class<T> clazz, String arxiu, String array) throws Exception
    {
    	List<Object> objectList = parser.loadObjectsFullPath(clazz, arxiu, array);
    	List<T> l = new ArrayList<>();
    	
    	for (Object o : objectList)
    		l.add((T) o);
    	
    	return l;
    }
    
    public <T> void saveGenericFullPath(List<T> objects, Class<T> clazz, String arxiu, String array) throws Exception
    {
    	parser.saveObjectsFullPath(objects, clazz, arxiu, array);
    }
    
    public <T> void saveGeneric(List<T> objects, Class<T> clazz, String arxiu, String array) throws Exception
    {
    	parser.saveObjects(objects, clazz, arxiu, array);
    }
    
    public HashMap<String, Class<?>> getTipusAtributs() {
        return ctrlItemCSV.getFields();
    }

    public HashMap<Integer, String> getOrdreTipusAtributs() {
        return ctrlItemCSV.getFieldOrder();
    }

    public List<HashMap<String, String>> getItems() {
        return ctrlItemCSV.getObjects();
    }
}
