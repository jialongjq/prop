package data;

import java.util.ArrayList;

/**
 * Utilitat per a parsejar una linea d'un CSV
 * 
 * @author Roberto Navarro Morales
 *
 */
public class CSVUtil {
	
	private CSVUtil()
	{
		
	}
	
	/**
	 * Parsejar una linea separada per un delimitador
	 * 
	 * @param line 
	 * @param delimiter
	 * @return el split de la linea
	 */
	public static ArrayList<String> parseLine(String line, char delimiter)
	{
		ArrayList<String> splitted = new ArrayList<String>();
		
		boolean stringEscape = false;
		int start = 0;
		
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (!stringEscape && c == delimiter) {
				splitted.add(line.substring(start, i));
				start = i + 1;
			}
			
			if (!stringEscape && c == '"')
				stringEscape = true;
			else if (stringEscape && c == '"')
				stringEscape = false;
		}
		
		if (start != line.length())
			splitted.add(line.substring(start, line.length()));
		
		return splitted;
	}
}
