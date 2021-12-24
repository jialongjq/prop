package domain.drivers;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

import data.CSVUtil;
import domain.Classificacio;

public class DriverClassificacio {
	private static Classificacio classificacio;
	
	private static void testClassifica(int k, ArrayList<String[]> ratings)
	{
		TreeSet<Integer> usuaris = new TreeSet<Integer>();
				
		classificacio = new Classificacio(1.0f, k);
		
		for (String[] line : ratings) {
			int idUsuari = Integer.parseInt(line[0]);
			int idItem = Integer.parseInt(line[1]);
			double valoracio = Double.parseDouble(line[2]);
			
			classificacio.afegirValoracio(idUsuari, idItem, valoracio);
			usuaris.add(idUsuari);
		}
		
		try {
			classificacio.classifica();
		}
		catch (Exception ex) {
			System.out.println("S'ha produit un error classificant els usuaris" + ex.toString());
		}
		
		System.out.println("\r\nUsuaris similars");
		System.out.println("-----------------");
		for (Integer idUsuari : usuaris) {
			try {
				ArrayList<Integer> similars = classificacio.getUsuarisSimilars(idUsuari);
				
				System.out.print("Similars a " + idUsuari + " -> [ ");
				for (Integer sim : similars) 
					System.out.print(sim + " ");
				
				System.out.println("]");
			}
			catch (Exception ex) {
				System.out.println("S'ha produit un error en obtenir els usuaris similars a " + 
						idUsuari + ": " + ex.toString());
			}
		}
	}
	
	private static ArrayList<String[]> parseFile(String filename) throws Exception
	{
		ArrayList<String[]> result = new ArrayList<String[]>();
		
		FileReader fr = new FileReader("./DATA/driver-classificacio/" + filename);
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
	
	public static void main(String[] args)
	{
		try {
			ArrayList<String[]> ratings = parseFile("valoracions.csv");
			
			System.out.println("Valoracions en format idUsuari, idItem, valoracio");
			System.out.println("--------------------------------------------------");
			for (String[] line : ratings) {
				for (int i = 0; i < line.length; i++) {
					System.out.print(line[i] + " ");
				}
				
				System.out.println();
			}
			
			testClassifica(2, ratings);
		}
		catch (Exception ex) {
			System.out.println("S'ha produit un error en la classificacio: " + ex.toString());
		}
	}
}
