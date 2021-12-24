package data;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Classe per a bolcar els resultats de l'avaluacio
 * dels algorismes de recomanacio a disc en format csv
 * 
 * @author Roberto Navarro Morales
 *
 */
public class CtrlAvaluacioCSV {
private static CtrlAvaluacioCSV singletonObject;
	
	public static CtrlAvaluacioCSV getInstance() 
	{
		if (singletonObject == null)
			singletonObject = new CtrlAvaluacioCSV();
		return singletonObject;
	}
	
	private CtrlAvaluacioCSV()
	{
	}
	
	/**
	 * Bolcar les avaluacions a csv
	 * 
	 * @param filename nom del fitxer (extensio .csv)
	 * @param delimiter delimitador que s'ha de fer servir per a separar els elements de la llista
	 * @param avaluacions les linies del csv
	 * @throws Exception
	 */
	public void dump(String filename, String delimiter, ArrayList<String[]> avaluacions) throws Exception
	{
		PrintStream fileStream = new PrintStream(new File("./DATA/" + filename));
		
		fileStream.println("userId" + delimiter + "avaluacio");
		
		for (String[] line : avaluacions) {
			String line_s = new String();
			for (int i = 0; i < 2; i++) 
				line_s += line[i] + delimiter;
			
			fileStream.println(line_s.substring(0, line_s.length() - delimiter.length()));
		}
		
		fileStream.close();
	}
}
