package domain;

import java.util.HashMap;
import java.util.Map;

import data.JsonAdd;
import data.JsonIgnore;
import data.JsonKeyValue;

/**
 * @author Jia Long Ji Qiu
 */

public class Item {
    private int id;
    
    private TipusItem tipusItem;
    
    @JsonAdd("afegirAtribut")
    @JsonKeyValue()
    HashMap<String, Atribut> atributs;

    /**
     * Contructora d'un item donat un id i el seu tipus d'item
     * @param id id de l'item
     * @param tipusItem tipus d'item
     */
    public Item(int id, TipusItem tipusItem) {
        this.id = id; 
        this.tipusItem = tipusItem;
        this.atributs = new HashMap<>();
    }

    /**
     * Getter de l'id d'un item
     * @return id d'un item
     */
    public int getId() { return id; }

    /**
     * Getter del tipus d'item d'un item
     * @return tipus d'item d'un item
     */
    public TipusItem getTipusItem() { return tipusItem; }

    /**
     * Getter dels atributs d'un item
     * @return atributs d'un item
     */
    public HashMap<String, Atribut> getAtributs() { return atributs; }

    /**
     * Getter d'un atribut donat el nom de l'atribut
     * @param nomAtribut nom de l'atribut
     * @return l'atribut
     */
    public Atribut getAtribut(String nomAtribut) { return atributs.get(nomAtribut); }

    /**
     * Exists d'un atribut donat el nom de l'atribut
     * @param nomAtribut nom de l'atribut
     * @return true si existeix
     *         false si no
     */
    public boolean existsAtribut(String nomAtribut) { return atributs.containsKey(nomAtribut); }

    /**
     * Afegeix un atribut a un item donat el nom de l'atribut i l'atribut
     * @param nomTipusAtribut nom de l'atribut
     * @param atribut valor
     */
    public void afegirAtribut(String nomTipusAtribut, Atribut atribut) {
        // Hauria de ser precondicio pero per si de cas
        if (tipusItem.getTipusAtributs().containsKey(nomTipusAtribut) && tipusItem.getTAtribut(nomTipusAtribut) == atribut.getTAtribut())
            atributs.put(nomTipusAtribut, atribut);
    }

    /**
     * Modifica l'atribut d'un item donat el nom de l'atribut
     * @param nomTipusAtribut nom de l'atribut
     * @param atribut valor
     */
    public void modificarAtribut(String nomTipusAtribut, Atribut atribut) {
        // Hauria de ser precondicio pero per si de cas
        if (atributs.containsKey(nomTipusAtribut) && atribut.getTAtribut() == atributs.get(nomTipusAtribut).getTAtribut())
            atributs.put(nomTipusAtribut, atribut);
    }

    /**
     * Elimina un atribut donat el seu nom
     * @param nomTipusAtribut nom de l'atribut
     */
    public void eliminarAtribut(String nomTipusAtribut) {
        atributs.remove(nomTipusAtribut);
    }

    @Override
    public String toString() {
        String ret = "Item [id=" + id + ", tipus=" + tipusItem.getNom() + "]";
        for (String nomAtribut : atributs.keySet()) {
            ret += "\nnomAtribut="+nomAtribut+", valor="+atributs.get(nomAtribut);
        }
        return ret;
    }
}
