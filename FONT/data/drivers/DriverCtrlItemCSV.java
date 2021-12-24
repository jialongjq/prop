package data.drivers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.CtrlItemCSV;

public class DriverCtrlItemCSV {
	public static void main(String[] args)
	{
		CtrlItemCSV ctrlItemCSV = CtrlItemCSV.getInstance();
		
		try {
			ctrlItemCSV.load("items-data-driver.csv");
		}
		catch (Exception ex) {
			System.out.println("S'ha produit un error carregant el fitxer csv " + ex.toString());
			return;
		}
		
		HashMap<Integer, String> camps = ctrlItemCSV.getFieldOrder();
		System.out.println("Ordre dels atributs");
		System.out.println("--------------------");
		
		for (Map.Entry<Integer, String> kv : camps.entrySet())
			System.out.println(kv.getKey() + ": " + kv.getValue());
	
		HashMap<String, Class<?>> tipusCamps = ctrlItemCSV.getFields();
		System.out.println("\r\nTipus dels atributs");
		System.out.println("-----------------");
		
		for (Map.Entry<String, Class<?>> kv : tipusCamps.entrySet()) {
			String typeS = "";
			if (kv.getValue() == Boolean.class)
				typeS = "boolea";
			else if (kv.getValue() == Integer.class || kv.getValue() == Double.class)
				typeS = "numeric";
			else if (kv.getValue() == String.class)
				typeS = "descriptiu";
			else
				typeS = "categoric";
			
			System.out.println(kv.getKey() + ": " + typeS);
		}
		
		List<HashMap<String, String>> objects = ctrlItemCSV.getObjects();
		System.out.println("\r\nObjectes llegits");
		System.out.println("----------------");
		for (HashMap<String, String> ob : objects) {
			for (Map.Entry<String, String> kv : ob.entrySet())
				System.out.println(kv.getKey() + ": " + kv.getValue());
			System.out.println();
		}
	}
}
