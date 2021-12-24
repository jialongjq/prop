package domain;

import java.util.Iterator;

import static domain.TipusItem.TAtribut.DESCRIPTIU;

/**
 * @author Jia Long Ji Qiu
 */

public class AtributDescriptiu extends Atribut {
    private String valor;
   
    public AtributDescriptiu(String valor) {
        this.valor = valor;
        this.tAtribut = TipusItem.TAtribut.DESCRIPTIU;
    }

    public Object getValor() {
        return valor;
    }

    public TipusItem.TAtribut getTAtribut() { return tAtribut; }

    @Override
    public String toString() {
        return valor;
    }
}
