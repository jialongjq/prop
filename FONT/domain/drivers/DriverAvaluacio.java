package domain.drivers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import data.CSVUtil;
import domain.Avaluacio;

public class DriverAvaluacio {
	private static String printOrdre(Avaluacio avaluacio, HashMap<Integer, Integer> ordreM) 
	{
		ArrayList<Integer> items = new ArrayList<Integer>();
		for (Integer itemId : ordreM.keySet()) 
			items.add(itemId);
		
		Collections.sort(items, new Comparator<Integer>() {
			public int compare(Integer x, Integer y)
			{
				return ordreM.get(x) - ordreM.get(y);
			}
		});
		
		String s = "";
		
		Queue<Integer> grupBuffer = new LinkedList<Integer>();
		int ordre = 0;
		for (Integer itemId : items) {
			if (ordreM.get(itemId) != ordre) {
				if (!grupBuffer.isEmpty()) {
					s += ordre + ": [ ";
					while (!grupBuffer.isEmpty())
						s += grupBuffer.poll() + " ";
					
					s += "] ";
				}
				
				ordre = ordreM.get(itemId);
			}
			
			grupBuffer.add(itemId);
		}
		
		if (!grupBuffer.isEmpty()) {
			s += ordre + ": [ ";
			while (!grupBuffer.isEmpty())
				s += grupBuffer.poll() + " ";
			
			s += "] ";
		}
		
		return s;
	}
	
	private static ArrayList<String[]> parseFile(String filename) throws Exception
	{
		ArrayList<String[]> result = new ArrayList<String[]>();
		
		FileReader fr = new FileReader("./DATA/driver-avaluacio/" + filename);
		Scanner sc = new Scanner(fr);
		
		String header = sc.nextLine();
		ArrayList<String> header_spl = CSVUtil.parseLine(header, ',');
		if (header_spl.size() != 3) {
			sc.close();
			throw new Exception("S'esperan exactament 3 valors per fila " + filename);
		}
		
		while (sc.hasNextLine()) {
			String line = new String(sc.nextLine());
			ArrayList<String> line_spl = CSVUtil.parseLine(line, ',');
			
			if (line_spl.size() != 3) {
				sc.close();
				throw new Exception("S'esperan exactament 3 valors per fila " + filename);
			}
			
			String[] arr = new String[3];
			arr[0] = line_spl.get(0);
			arr[1] = line_spl.get(1);
			arr[2] = line_spl.get(2);
			
			result.add(arr);
		}
		
		sc.close();
		return result;
	}
		
	public static void testAvaluaOrdreCorrecte()
	{
		try {
			ArrayList<String[]> trueData = parseFile("avaluacio.correcte.true.csv");
			ArrayList<String[]> predData = parseFile("avaluacio.correcte.pred.csv");
			
			Avaluacio avaluacio = new Avaluacio(trueData);
			
			double val = avaluacio.avalua(1, predData);
			
			System.out.println("Ordre esperat = { " + printOrdre(avaluacio, avaluacio.getOrdreEsperat()) + "}");
			System.out.println("Ordre predit  = { " + printOrdre(avaluacio, avaluacio.getOrdrePredit()) + "}");
			System.out.printf("Avaluacio: %.4f\r\n", val);
		} 
		catch (Exception ex)
		{
			System.out.println("Excepcio evaluant el cas d'avaluacio correcta " + ex.toString());
		}
	}
	
	public static void testAvaluaOrdreIncorrecte()
	{
		try {
			ArrayList<String[]> trueData = parseFile("avaluacio.incorrecte.true.csv");
			ArrayList<String[]> predData = parseFile("avaluacio.incorrecte.pred.csv");
		
			Avaluacio avaluacio = new Avaluacio(trueData);
			
			double val = avaluacio.avalua(1, predData);
			
			System.out.println("Ordre esperat = { " + printOrdre(avaluacio, avaluacio.getOrdreEsperat()) + "}");
			System.out.println("Ordre predit  = { " + printOrdre(avaluacio, avaluacio.getOrdrePredit()) + "}");
			System.out.printf("Avaluacio: %.4f\r\n", val);
		}
		catch (Exception ex)
		{
			System.out.println("Excepcio evaluant el cas d'avaluacio incorrecte " + ex.toString());
		}
	}
	
	public static void main(String[] argv)
	{
		System.out.println("Prova d'una avaluacio correcta");
		System.out.println("Fitxers: avaluacio.correcte.true.csv / avaluacio.correcte.pred.csv");
		testAvaluaOrdreCorrecte();
		
		System.out.println("=======");
		
		System.out.println("Prova d'una avaluacio incorrecta");
		System.out.println("Fitxers: avaluacio.incorrecte.true.csv / avaluacio.incorrecte.pred.csv");
		testAvaluaOrdreIncorrecte();
	}
}
