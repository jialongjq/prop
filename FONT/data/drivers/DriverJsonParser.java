package data.drivers;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import data.JsonBaseDeduce;
import data.JsonParser;
import domain.*;
import domain.TipusItem.TAtribut;

public class DriverJsonParser {
	private static JsonParser jsonParser;
	
	private static void testCarregaSimple()
	{
		try {
			List<Object> l = jsonParser.loadObjects(Usuari.class, "usuaris.json", "usuaris");
			for (Object o : l)
				System.out.println(o.toString());
			}
		catch (Exception ex) {
			System.out.println("S'ha produit un error carregant el json: " + ex.toString());
		}
	}
	
	private static void testBolcatSimple()
	{
		Usuari u1 = new Usuari(1, "Admin",
				"72534c4a93ddc043fe3229ed46b1d526c4ccc747febdcd0f284f7f6057a37858",
				true);
		
		Usuari u2 = new Usuari(2, "Usuari 2",
				"72534c4a93ddc043fe3229ed46b1d526c4ccc747febdcd0f284f7f6057a37858",
				false);
		
		ArrayList<Usuari> l = new ArrayList<Usuari>();
		l.add(u1);
		l.add(u2);
		
		try {
			jsonParser.saveObjects(l, Usuari.class, "usuaris.json", "usuaris");
		}
		catch (Exception ex) {
			System.out.println("S'ha produit un error guardant el json: " + ex.toString());
		}
	}
	
	private static void testBolcatAnidat() 
	{
		TipusItem ti = new TipusItem("tipus1");
		ti.afegirTipusAtribut("numeric", TAtribut.NUMERIC);
		ti.afegirTipusAtribut("categoric", TAtribut.CATEGORIC);
		ti.afegirTipusAtribut("boolean", TAtribut.BOOLEA);
		
		Item item1 = new Item(1, ti);
		
		AtributNumeric atrnum = new AtributNumeric(15);
		item1.afegirAtribut("numeric", atrnum);
		
		AtributCategoric atrcat = new AtributCategoric();
		atrcat.afegirValor("categoria1");
		item1.afegirAtribut("categoric", atrcat);
		
		AtributBoolea atrbol = new AtributBoolea(true);
		item1.afegirAtribut("boolean", atrbol);
		
		
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(item1);
		
		try {
			jsonParser.saveObjects(items, Item.class, "items.json", "items");
		}
		catch (Exception ex) {
			System.out.println("S'ha produit un error guardant el json: " + ex.toString());
		}
	}
	
	private static void testCarregaAnidat() 
	{
		try {
			List<Object> l = jsonParser.loadObjects(Item.class, "items.json", "items");
			for (Object o : l) { 
				System.out.println(o.toString());
			}
		} catch (Exception ex) {
			System.out.println("S'ha produit un error carregant el json: " + ex.toString());
		}
	}

	public static void main(String[] args) throws Exception
	{
		jsonParser = JsonParser.getInstance();
		
		testBolcatSimple();
		System.out.println("Bolcat simple correcte");
		testCarregaSimple();
		System.out.println("Carrega simple correcta");
		
		testBolcatAnidat();
		System.out.println("Bolcat anidat correcte");
		testCarregaAnidat();
		System.out.println("Carrega anidat correcte");
	}
}
