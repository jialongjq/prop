package domain;

/**
 * @author Jia Long Ji Qiu
 */

public class AtributNumeric extends Atribut {
    private double valor;
   
    public AtributNumeric(double valor) {
        this.valor = valor;
        this.tAtribut = TipusItem.TAtribut.NUMERIC;
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
