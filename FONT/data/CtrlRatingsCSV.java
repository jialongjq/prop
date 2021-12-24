package data;

import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Classe per a tractar els fitxers csv que contenen
 * les tripletes (usuari,item,rating)
 * 
 * @author Roberto Navarro Morales
 *
 */
public class CtrlRatingsCSV {
	private static CtrlRatingsCSV singletonObject;
	
	public static CtrlRatingsCSV getInstance() 
	{
		if (singletonObject == null)
			singletonObject = new CtrlRatingsCSV();
		return singletonObject;
	}
	
	private CtrlRatingsCSV()
	{
	}
	
	/**
	 * Carrega del fitxer de ratings en format csv
	 * 
	 * @param filename nom del fitxer (extensio .csv)
	 * @param delimiter serparador dels camps
	 * @return les linies carregades
	 * @throws Exception
	 */
	public ArrayList<String[]> load(String filename, String delimiter) throws Exception
	{
		ArrayList<String[]> result = new ArrayList<>();
		
		FileReader fr = new FileReader(filename);
		Scanner sc = new Scanner(fr);
		
		// Consumir el header
		// Determinar ordre userId itemId rating
		String header = sc.nextLine();
		ArrayList<String> header_spl = CSVUtil.parseLine(header, delimiter.charAt(0));
		if (header_spl.size() != 3) {
			sc.close();
			throw new Exception("S'esperan exactament 3 valors per fila " + filename);
		}
		HashMap<String, Integer> columnOrder = new HashMap<>();
		for (int i = 0; i < 3; ++i) {
			columnOrder.put(header_spl.get(i), i);
		}
		
		while (sc.hasNextLine()) {
			String line = new String(sc.nextLine());
			ArrayList<String> line_spl = CSVUtil.parseLine(line, delimiter.charAt(0));
			
			if (line_spl.size() != 3) {
				sc.close();
				throw new Exception("S'esperan exactament 3 valors per fila " + filename);
			}
			
			String[] record = new String[] {line_spl.get(columnOrder.get("userId")), line_spl.get(columnOrder.get("itemId")), line_spl.get(columnOrder.get("rating"))};
			result.add(record);
		}
		
		sc.close();
		return result;	
	}
	
	/**
	 * Bolcar les valoracions a csv
	 * 
	 * @param filename nom del fitxer (extensio .csv)
	 * @param delimiter delimitador que s'ha de fer servir per a separar els elements de la llista
	 * @param ratings les linies del csv
	 * @throws Exception
	 */
	public void dump(String filename, String delimiter, ArrayList<String[]> ratings) throws Exception
	{
		PrintStream fileStream = new PrintStream(new File(filename));
		
		fileStream.println("userId" + delimiter + "itemId" + delimiter + "rating");
		
		for (String[] line : ratings) {
			String line_s = new String();
			for (int i = 0; i < 3; i++) 
				line_s += line[i] + delimiter;
			
			fileStream.println(line_s.substring(0, line_s.length() - delimiter.length()));
		}
		
		fileStream.close();
	}
}
