package domain.drivers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import data.CtrlDades;
import domain.Avaluacio;
import domain.Classificacio;
import domain.CtrlDomini;
import domain.CtrlValoracio;
import domain.Item;
import domain.Valoracio;
import domain.algorismes.Hibrid;

public class DriverHibrid {
	private static CtrlDomini ctrlDomini;
	private static CtrlValoracio ctrlValoracio;
	
	public static void carrega()
	{
		try {
			ctrlDomini = CtrlDomini.getInstance();
	        ctrlValoracio = CtrlValoracio.getInstance();
	        
	        ctrlDomini.carregarItemsCSV("movies", "driver-hibrid/items.csv");
	        ctrlDomini.carregarValoracionsCSV(0, 5, "driver-hibrid/ratings.test.known.csv");
		}
		catch (Exception ex) {
			System.out.println("S'ha produit un error a la carrega dels fitxers " + ex.toString());
		}
	}
	
	public static Classificacio classifica()
	{
		HashMap<Integer, HashMap<Integer, Valoracio>> valoracions = ctrlValoracio.getValoracionsPerUsuari();
		
		Classificacio classificacio = new Classificacio(0.5, 50);
		
		for (Map.Entry<Integer, HashMap<Integer, Valoracio>> kv : valoracions.entrySet()) {
			for (Map.Entry<Integer, Valoracio> ikv : kv.getValue().entrySet()) 
				classificacio.afegirValoracio(kv.getKey(), ikv.getKey(), ikv.getValue().getPuntuacio());
		}
		
		try {
			classificacio.classifica();
		} catch (Exception ex) {
			System.out.println("S'ha produit un error a la classificacio: " + ex.toString());
		}
		
		return classificacio;
	}
	
	public static void avaluacio(Hibrid hibrid, int idUsuari)
	{
		try {
			CtrlDades ctrlDades = CtrlDades.getInstance();
    		Avaluacio aval = new Avaluacio(ctrlDades.carregarValoracionsCSV("driver-hibrid/ratings.test.unknown.csv"));
    		
    		ArrayList<String[]> pred = new ArrayList<String[]>();
    		ArrayList<String[]> avaluacions = new ArrayList<String[]>();
   
    			
			ArrayList<Item> ord = hibrid.getItemSort();
			HashMap<Integer, Double> predMap = hibrid.getItemsRating();
			
			for (Item i : ord) {
				String[] s = new String[3];
				s[0] = Integer.toString(idUsuari);
				s[1] = Integer.toString(i.getId());
				s[2] = Double.toString(predMap.get(i.getId()));
				
				pred.add(s);
			}
			
			double res = aval.avalua(idUsuari, pred);
			String[] s = new String[2];
			s[0] = Integer.toString(idUsuari);
			s[1] = Double.toString(res);
			
			avaluacions.add(s);
    		    		
    		ctrlDades.saveAvaluacio(avaluacions);
    		ctrlDades.saveValoracionsCSV(pred, "prediccions.csv");
    		
    	} catch (Exception ex) {
    		System.out.println("S'ha produit un error avaluant les prediccions " + ex.toString());
    	}
	}
	
	public static void run(int idUsuari)
	{
        System.out.println("[Algorisme aplicat a l'usuari amb id="+idUsuari+"]");
        Set<Item> itemsValorats = ctrlDomini.getItemsValorats(idUsuari, 3.5);
        Set<Item> itemsValoratsTot = ctrlDomini.getItemsValorats(idUsuari, 0);
        
        if (itemsValorats.isEmpty()) {
        	System.out.println("No es pot evaluar l'usuari perque no hi ha prou informacio o perque no existeix");
        	return;
        }

        System.out.println("[Items valorats per l'usuari amb id="+idUsuari+" amb una puntacio >= 3.5]: " + itemsValorats.size());
   
        Set<Item> conjuntItems = ctrlDomini.getItems("movies");
        System.out.println("[El subconjunt sera el conjunt sencer, " + conjuntItems.size() + " items afegits]");
        
        Classificacio classificacio = classifica();
        System.out.println("Classificacio");
        System.out.println("==============");
        System.out.println(classificacio.toString());
        
        HashMap<String, Set<Item>> mCjtItems = new HashMap<String, Set<Item>>();
        mCjtItems.put("movies", conjuntItems);
        
        HashSet<String> tipusItems = new HashSet<String>();
        tipusItems.add("movies");
        
        Hibrid hib = new Hibrid(idUsuari, itemsValoratsTot, itemsValorats, mCjtItems, tipusItems, classificacio);
        hib.resol();
        
        avaluacio(hib, idUsuari);
        
        System.out.println("==========");
        System.out.println("Fitxers de sortida");
        System.out.println("DATA/avaluacio.csv -> avaluacio de les recomanacions");
        System.out.println("DATA/prediccions.csv -> prediccions amb els scores");
        System.out.println("==========");
	}
	
	public static void main(String[] args)
	{
        System.out.println("[Execucio de l'algorisme hibrid]");
        carrega();
        
        Scanner sc = new Scanner(System.in);
        
        int opt = -1;
        while (opt != 2) {
        	System.out.println("Tria un a opcio");
        	System.out.println("1- Provar un usuari nou");
        	System.out.println("2- Sortir");
        	
        	opt = Integer.parseInt(sc.nextLine());
        	
        	if (opt == 1) {
        		System.out.println("Introdueix el identificador de l'usuari");
        		int idUsuari = Integer.parseInt(sc.nextLine());
        		run(idUsuari);
        	}
        }
        
        sc.close();
	}
}
