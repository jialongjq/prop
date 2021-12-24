package domain.drivers;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import data.CSVUtil;
import domain.algorismes.KMeans;


public class DriverKMeans {
	
	public static void testResolRandom(ArrayList<ArrayList<Double>> data, int k)
	{
		try {
			KMeans kmeans = new KMeans(3, data, null);
			kmeans.setVerbose();
			kmeans.resol();

			System.out.println("\r\nBaricentres finals");
			for (ArrayList<Double> d : kmeans.getBaricentres()) 
				System.out.println(Arrays.toString(d.toArray()));
			
			System.out.println("\r\nClusters finals");
			for (ArrayList<Integer> c : kmeans.getClusters()) 
				System.out.println(Arrays.toString(c.toArray()));
			
		} catch (Exception ex) {
			System.out.println("S'ha produit un error al kmeans " + ex.toString());
		}
	}
	
	public static void testResolFixed(ArrayList<ArrayList<Double>> data, ArrayList<ArrayList<Double>> baricentres, int k) 
	{
		try {
			KMeans kmeans = new KMeans(3, data, baricentres);
			kmeans.setVerbose();
			kmeans.resol();

			System.out.println("\r\nBaricentres finals");
			for (ArrayList<Double> d : kmeans.getBaricentres()) 
				System.out.println(Arrays.toString(d.toArray()));
			
			System.out.println("\r\nClusters finals");
			for (ArrayList<Integer> c : kmeans.getClusters()) 
				System.out.println(Arrays.toString(c.toArray()));
			
		} catch (Exception ex) {
			System.out.println("S'ha produit un error al kmeans " + ex.toString());
		}
	}
	
	private static ArrayList<String[]> parseFile(String filename) throws Exception
	{
		ArrayList<String[]> result = new ArrayList<String[]>();
		
		FileReader fr = new FileReader("./DATA/driver-kmeans/" + filename);
		Scanner sc = new Scanner(fr);
				
		while (sc.hasNextLine()) {
			String line = new String(sc.nextLine());
			ArrayList<String> line_spl = CSVUtil.parseLine(line, ',');
			
			if (line_spl.size() != 2) {
				sc.close();
				throw new Exception("S'esperan exactament 2 valors per fila " + filename);
			}
			
			String[] arr = new String[2];
			arr[0] = line_spl.get(0);
			arr[1] = line_spl.get(1);
			
			result.add(arr);
		}
		
		sc.close();
		return result;
	}
			
	
	public static void main(String[] args)
	{
		ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
		
		try {
			ArrayList<String[]> raw_data = parseFile("data.csv");
			for (String[] s_arr : raw_data) 	{
				ArrayList<Double> dArr = new ArrayList<Double>();
				
				for (int i = 0; i < s_arr.length; i++)
					dArr.add(Double.parseDouble(s_arr[i]));
				
				data.add(dArr);
			}
		}
		catch (Exception ex) {
			System.out.println("S'ha produit un error carregant els punts " + ex.toString());
		}
		
		System.out.println("PUNTS");
		System.out.println("------");
		
		for (ArrayList<Double> d : data) 
			System.out.println(Arrays.toString(d.toArray()));
		
		System.out.println("\r\nASSIGNACIO ALEATORIA");
		System.out.print("------");
		testResolRandom(data, 3);
		
		System.out.println("===========");
		
		ArrayList<ArrayList<Double>> baricentres = new ArrayList<ArrayList<Double>>();
		
		try {
			ArrayList<String[]> raw_data = parseFile("centroides.csv");
			for (String[] s_arr : raw_data) 	{
				ArrayList<Double> dArr = new ArrayList<Double>();
				
				for (int i = 0; i < s_arr.length; i++)
					dArr.add(Double.parseDouble(s_arr[i]));
				
				baricentres.add(dArr);
			}
		}
		catch (Exception ex) {
			System.out.println("S'ha produit un error carregant els centroides " + ex.toString());
		}
		
		if (baricentres.isEmpty()) {
			System.out.println("S'omet l'assignacio fixada");
			return;
		}
		
		System.out.println("ASSIGNACIO FIXADA");
		System.out.print("------");
		testResolFixed(data, baricentres, 3);
	}
}
