package unittests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import domain.Atribut;
import domain.AtributBoolea;
import domain.AtributCategoric;
import domain.AtributDescriptiu;
import domain.AtributNumeric;
import domain.Item;
import domain.TipusItem;
import domain.TipusItem.TAtribut;
import domain.algorismes.Distancia;

public class UnitTestDistancia {
	@Test
	public void testDistanciaCategorics1()
	{
		Set<String> a = Set.of("cat1", "cat2", "cat3");
		Set<String> b = Set.of("cat1", "cat2", "cat3");
		
		double result = Distancia.compararCategorics(a, b);
		assertEquals(3, result, 0);
	}
	
	@Test
	public void testDistanciaCategorics2()
	{
		Set<String> a = Set.of("cat1", "cat2", "cat3");
		Set<String> b = Set.of("cat1", "cat2", "cat4");
		
		double result = Distancia.compararCategorics(a, b);
		assertEquals(2, result, 0);
	}
	
	@Test
	public void testDistanciaCategoric3()
	{
		Set<String> a = Set.of("cat1", "cat2", "cat3");
		Set<String> b = Set.of();
		
		double result = Distancia.compararCategorics(a, b);
		assertEquals(0, result, 0);
	}
	
	@Test
	public void testDistanciaNumerics1()
	{
		double a = 5;
		double b = 0;
		double max = 5;
		
		double result = Distancia.compararNumerics(a, b, max);
		assertEquals(0, result, 0);
	}
	
	@Test
	public void testDistanciaNumerics2()
	{
		double a = 2;
		double b = 1;
		double max = 4;
		
		double result = Distancia.compararNumerics(a, b, max);
		assertEquals(0.75, result, 0);
	}
	
	@Test
	public void testDistanciaBooleans()
	{
		boolean a = true;
		boolean b = false;
		
		double result = Distancia.compararBooleans(a, b);
		assertEquals(0, result, 0);
	}
	
	@Test
	public void testDistanciaDescriptius()
	{
		String a = "test";
		String b = "test";
		
		double result = Distancia.compararDescriptius(a, b);
		assertEquals(1, result, 0);
	}
	
	@Test
	public void testCalcularDistanciaMaxima()
	{
		TipusItem ti = new TipusItem("tipus1");
		ti.afegirTipusAtribut("numeric", TAtribut.NUMERIC);
		ti.afegirTipusAtribut("descriptiu", TAtribut.DESCRIPTIU);
		ti.afegirTipusAtribut("categoria", TAtribut.CATEGORIC);
		ti.afegirTipusAtribut("boolean", TAtribut.BOOLEA);
		
		Item it1 = new Item(1, ti);
		{
			it1.afegirAtribut("descriptiu", new AtributDescriptiu("item1"));
			it1.afegirAtribut("numeric", new AtributNumeric(1));
			it1.afegirAtribut("boolean", new AtributBoolea(true));
			
			AtributCategoric ac = new AtributCategoric();
			ac.afegirValor("cat1");
			ac.afegirValor("cat2");
			it1.afegirAtribut("categoria", ac);
		}
		
		Item it2 = new Item(2, ti);
		{
			it2.afegirAtribut("descriptiu", new AtributDescriptiu("item2"));
			it2.afegirAtribut("numeric", new AtributNumeric(5));
			it2.afegirAtribut("boolean", new AtributBoolea(false));
			
			AtributCategoric ac = new AtributCategoric();
			ac.afegirValor("cat4");
			ac.afegirValor("cat3");
			it2.afegirAtribut("categoria", ac);
		}
		
		Set<Item> s = Set.of(it1, it2);
		HashMap<String, Integer> maxCat = new HashMap<String, Integer>();
		HashMap<String, Double> maxNum = new HashMap<String, Double>();
		
		double result = Distancia.calcularDistanciaMaxima(s, maxCat, maxNum);
		assertEquals(5, result, 0);
		assertEquals(1, maxCat.size(), 0);
		assertEquals(1, maxNum.size(), 0);
		
		for (Map.Entry<String, Integer> kv : maxCat.entrySet()) {
			assertEquals("categoria", kv.getKey());
			assertEquals(Integer.valueOf(2), kv.getValue());
		}
		
		for (Map.Entry<String, Double> kv : maxNum.entrySet()) {
			assertEquals("numeric", kv.getKey());
			assertEquals(Double.valueOf(5), kv.getValue());
		}
	}
	
	@Test
	public void testCalcularDistancia()
	{
		TipusItem ti = new TipusItem("tipus1");
		ti.afegirTipusAtribut("numeric", TAtribut.NUMERIC);
		ti.afegirTipusAtribut("descriptiu", TAtribut.DESCRIPTIU);
		ti.afegirTipusAtribut("categoria", TAtribut.CATEGORIC);
		ti.afegirTipusAtribut("boolean", TAtribut.BOOLEA);
		
		Item it1 = new Item(1, ti);
		{
			it1.afegirAtribut("descriptiu", new AtributDescriptiu("item1"));
			it1.afegirAtribut("numeric", new AtributNumeric(1));
			it1.afegirAtribut("boolean", new AtributBoolea(true));
			
			AtributCategoric ac = new AtributCategoric();
			ac.afegirValor("cat1");
			ac.afegirValor("cat2");
			it1.afegirAtribut("categoria", ac);
		}
		
		Item it2 = new Item(2, ti);
		{
			it2.afegirAtribut("descriptiu", new AtributDescriptiu("item2"));
			it2.afegirAtribut("numeric", new AtributNumeric(5));
			it2.afegirAtribut("boolean", new AtributBoolea(false));
			
			AtributCategoric ac = new AtributCategoric();
			ac.afegirValor("cat4");
			ac.afegirValor("cat3");
			it2.afegirAtribut("categoria", ac);
		}
		
		Set<Item> s = Set.of(it1, it2);
		HashMap<String, Integer> maxCat = new HashMap<String, Integer>();
		HashMap<String, Double> maxNum = new HashMap<String, Double>();
		
		double maxDist = Distancia.calcularDistanciaMaxima(s, maxCat, maxNum);
		double result = Distancia.calcularDistancia(it1, it2, maxNum, maxDist);
		
		assertEquals(4.8, result, 0);
	}
	
	public static void main(String[] args)
	{
		JUnitCore junit = new JUnitCore();
		junit.addListener(new TextListener(System.out));
		junit.run(UnitTestDistancia.class);
	}
}
