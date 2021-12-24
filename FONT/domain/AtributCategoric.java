package domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import data.JsonAdd;

import static domain.TipusItem.TAtribut.CATEGORIC;

/**
 * @author Jia Long Ji Qiu
 */

public class AtributCategoric extends Atribut {
	
    @JsonAdd("afegirValor")
    private Set<String> valor;
   
    public AtributCategoric() {
        this.tAtribut = TipusItem.TAtribut.CATEGORIC;
        this.valor = new HashSet<String>();
    }
    
    public void afegirValor(String categoria)
    {
    	this.valor.add(categoria);
    }

    public Object getValor() {
        return valor; 
    }

    public TipusItem.TAtribut getTAtribut() { return tAtribut; }

    @Override
    public String toString() {
        String ret = "";
        Iterator<String> it = valor.iterator();
        if (it.hasNext()) ret += it.next();
        while (it.hasNext()) {
            ret += ";" + it.next();
        }
        return ret;
    }
}
