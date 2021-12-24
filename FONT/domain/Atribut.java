package domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import data.JsonBaseDeduce;
import data.JsonIgnore;

/**
 * @author Jia Long Ji Qiu
 */

public abstract class Atribut {
	@JsonBaseDeduce
	protected TipusItem.TAtribut tAtribut;
	
	@JsonIgnore
	public static Map<TipusItem.TAtribut, Class<?>> baseDeduce;
	static {
		baseDeduce = new HashMap<TipusItem.TAtribut, Class<?>>();
		baseDeduce.put(TipusItem.TAtribut.NUMERIC, AtributNumeric.class);
		baseDeduce.put(TipusItem.TAtribut.BOOLEA, AtributBoolea.class);
		baseDeduce.put(TipusItem.TAtribut.DESCRIPTIU, AtributDescriptiu.class);
		baseDeduce.put(TipusItem.TAtribut.CATEGORIC, AtributCategoric.class);
	}

	public abstract Object getValor();
    public abstract TipusItem.TAtribut getTAtribut();
}
