package domain;

/**
 * @author Jia Long Ji Qiu
 */

public class AtributBoolea extends Atribut {
    private boolean valor; 

    public AtributBoolea(boolean valor)  {
    	this.tAtribut = TipusItem.TAtribut.BOOLEA;
        this.valor = valor;
    }

    public Object getValor() {
        return valor; 
    }

    public TipusItem.TAtribut getTAtribut() { return tAtribut; }

    @Override
    public String toString() {
        return "" + valor;
    }
}
