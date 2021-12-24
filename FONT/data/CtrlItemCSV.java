package data;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Classe per a carregar i inferir els atributs
 * i els tipus d'atributs d'un dataset
 * 
 * @author Roberto Navarro Morales
 *
 */
public class CtrlItemCSV {
	
	/**
	 * Objecte unic del parsejador
	 */
	private static CtrlItemCSV singletonObject;
	
	/**
	 * Els objectes que s'han llegit, per a cada objecte
	 * hi ha un map on la clau es el nom de la propietat i 
	 * el valor es el valor que prendria la propietat
	 */
	private List<HashMap<String, String>> objects;
	
	/**
	 * Propietats trobades amb el seu tipus
	 */
	private HashMap<String, Class<?>> fields;
	
	/**
	 * Mapejat de les columnes dels csv als atributs
	 */
	private HashMap<Integer, String> fieldOrder;
	
	/**
	 * Prioritat dels tipus a l'hora de parsejar un atribut
	 */
	private static Map<Class<?>, Integer> typesHierarchy;
	static {
		typesHierarchy = new HashMap<Class<?>, Integer>();
		typesHierarchy.put(Boolean.class, Integer.valueOf(0));
		typesHierarchy.put(Integer.class, Integer.valueOf(1));
		typesHierarchy.put(Double.class, Integer.valueOf(2));
		typesHierarchy.put(String.class, Integer.valueOf(3));
		typesHierarchy.put(Set.class, Integer.valueOf(4));
	}
	
	public static CtrlItemCSV getInstance() 
	{
		if (singletonObject == null)
			singletonObject = new CtrlItemCSV();
		return singletonObject;
	}

	private CtrlItemCSV() 
	{
		this.objects = new LinkedList<HashMap<String, String>>();
		this.fields = new HashMap<String, Class<?>>();
		this.fieldOrder = new HashMap<Integer, String>();
	}
	
	public List<HashMap<String, String>> getObjects()
	{
		return objects;
	}
	
	public HashMap<String, Class<?>> getFields()
	{
		return fields;
	}
	
	public HashMap<Integer, String> getFieldOrder() {
		return fieldOrder;
	}

	/**
	 * Carrega del dataset
	 * 
	 * @param filename nom del fitxer (extensio .csv)
	 * @throws Exception
	 */
	public void load(String filename) throws Exception
	{
		// Neteja de les propietats de carregues antigues
		objects.clear();
		fields.clear();
		fieldOrder.clear();
		
		// Carrega dels items
		loadItems(filename, ",");
	}
	
	/**
	 * Carrega dels items del dataset
	 * 
	 * @param filename nom de l'atribut amb l'extensio
	 * @param delimiter separador de les columnes
	 * @throws Exception
	 */
	private void loadItems(String filename, String delimiter) throws Exception
	{
		FileReader fr = new FileReader(filename);
		Scanner sc = new Scanner(fr);
		
		if (!sc.hasNextLine()) {
			sc.close();
			throw new Exception("No es pot carregar el 'header' de " + filename);
		}
		
		String header = new String(sc.nextLine());
		ArrayList<String> atrs_arr = CSVUtil.parseLine(header, delimiter.charAt(0));
		
		// Inicialitzar l'ordre dels atributs
		for (int i = 0; i < atrs_arr.size(); i++)
			fieldOrder.put(Integer.valueOf(i), atrs_arr.get(i));
			
		// Carregar els items i inferir el tipus dels atributs
		while (sc.hasNextLine()) {
			String line = new String(sc.nextLine());
			ArrayList<String> fields = CSVUtil.parseLine(line, delimiter.charAt(0));
			
			HashMap<String, String> fieldsMap = new HashMap<String, String>();
			for (int i = 0; i < fields.size(); i++) {
				String field = fields.get(i);
				String fieldName = fieldOrder.get(Integer.valueOf(i));
				
				// Emparellar el valor amb la columna
				fieldsMap.put(fieldName, field);
				
				// Inferir el tipus
				Class<?> atrType = inferType(field);
				
				if (!this.fields.containsKey(fieldName))
					this.fields.put(fieldName, atrType);
				else {
					// Reemplaca si ocupa un lloc mes alt a la jerarquia
					Class<?> currDeduced = this.fields.get(fieldName);
					
					if (typesHierarchy.get(currDeduced) < typesHierarchy.get(atrType))
						this.fields.put(fieldName, currDeduced);
				}
			}
			
			this.objects.add(fieldsMap);
		}
		
		sc.close();
	}
	
	/**
	 * Deduir el tipus d'un valor
	 * 
	 * @param s el valor en string sense tractar
	 * @return el tipus del valor deduit
	 */
	private static Class<?> inferType(String s)
	{		
		// Cast de mes a menys restrictiu
		if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"))
			return Boolean.class;
	 
		if (s.split("\\.").length > 1) {
			try {
				Double.parseDouble(s);
				return Double.class;
			}
			catch (NumberFormatException e) {}	
		}
		
		try {
			Integer.parseInt(s);
			return Integer.class;
		}
		catch (NumberFormatException e) {}
		
		if (CSVUtil.parseLine(s, ';').size() > 1)
			return Set.class;
		
		return String.class;
	}
}
