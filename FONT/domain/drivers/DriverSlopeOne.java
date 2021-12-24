package domain.drivers;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import data.CSVUtil;
import domain.Classificacio;
import domain.algorismes.SlopeOne;

public class DriverSlopeOne {
	
	private static Classificacio classificacio;
	private static HashMap<Integer, Integer> usuariCluster;
	private static TreeMap<Integer, ArrayList<Integer>> clusterUsuaris;
	private static HashSet<Integer> usuarisValoracions;
	
	public static void testResol()
	{
		System.out.println("Classificacio imposada");
		System.out.println(classificacio.toString());
		System.out.println();
		
		SlopeOne slopeOne = new SlopeOne();
		slopeOne.setClassificacio(classificacio);
		
		System.out.println("Prediccions");
		for (Integer uId : usuariCluster.keySet()) {
			if (!usuarisValoracions.contains(uId)) { 
				System.out.println("No es pot predir res per un usuari sense items " + uId);
				continue;
			}
			
			slopeOne.setUsuari(uId);
			slopeOne.resol();
			
			System.out.println("Prediccio usuari " + uId);
			HashMap<Integer, Double> prediccions = slopeOne.getPredValoracions();
			if (prediccions.isEmpty())
				System.out.println("No es pot predir cap item");
			else {
				for (Map.Entry<Integer, Double> kv : prediccions.entrySet())
					System.out.println(kv.getKey() + ": " + kv.getValue());
			}
		}
	}
	
	private static ArrayList<String[]> parseFile(String filename, int values) throws Exception
	{
		ArrayList<String[]> result = new ArrayList<String[]>();
		
		FileReader fr = new FileReader("./DATA/driver-slopeone/" + filename);
		Scanner sc = new Scanner(fr);
		
		String header = sc.nextLine();
		ArrayList<String> header_spl = CSVUtil.parseLine(header, ',');
		if (header_spl.size() != values) {
			sc.close();
			throw new Exception("S'esperan exactament " + values + " valors per fila " + filename);
		}
		
		while (sc.hasNextLine()) {
			String line = new String(sc.nextLine());
			ArrayList<String> line_spl = CSVUtil.parseLine(line, ',');
			
			if (line_spl.size() != values) {
				sc.close();
				throw new Exception("S'esperan exactament " + values + " valors per fila " + filename);
			}
			
			String[] arr = new String[values];
			for (int i = 0; i < values; i++)
				arr[i] = line_spl.get(i);
			
			result.add(arr);
		}
		
		sc.close();
		return result;
	}
		
	public static void main(String[] args)
	{
		usuariCluster = new HashMap<Integer, Integer>();
		clusterUsuaris = new TreeMap<Integer, ArrayList<Integer>>();
		
		ArrayList<ArrayList<Integer>> clusters = new ArrayList<ArrayList<Integer>>();
		try {
			ArrayList<String[]> rawData = parseFile("clusters.csv", 2);
			for (String[] d : rawData) {
				int usuari = Integer.parseInt(d[0]);
				int cluster = Integer.parseInt(d[1]) - 1;
				
				usuariCluster.put(usuari, cluster);
				
				if (!clusterUsuaris.containsKey(cluster)) 
					clusterUsuaris.put(cluster, new ArrayList<Integer>());
				
				clusterUsuaris.get(cluster).add(usuari);
			}
			
			for (int i = 0; i < clusterUsuaris.size(); i++) {
				if (clusterUsuaris.containsKey(i)) 
					 clusters.add(clusterUsuaris.get(i));
				else
					throw new Exception("Els clusters han de ser correlatius");
			}
		}
		catch (Exception ex)
		{
			System.out.println("S'ha produit un error obtenint els clusters predefinits " + ex.toString());
			return;
		}
		
		classificacio = new Classificacio(1, clusters.size());
		classificacio.stubClassificacio(clusters);
		
		usuarisValoracions = new HashSet<Integer>();
		
		try {
			ArrayList<String[]> rawData = parseFile("valoracions.csv", 3);
			for (String[] d : rawData) {
				int usuari = Integer.parseInt(d[0]);
				int item = Integer.parseInt(d[1]);
				double valoracio = Double.parseDouble(d[2]);
				
				classificacio.afegirValoracio(usuari, item, valoracio);
				usuarisValoracions.add(usuari);
			}
		}
		catch (Exception ex)
		{
			System.out.println("S'ha produit un error obtenint les valoracions " + ex.toString());
			return;
		}
		
		
		testResol();
	}
}
