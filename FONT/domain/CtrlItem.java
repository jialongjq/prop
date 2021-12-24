package domain;
import data.*;
import domain.TipusItem.TAtribut;
import java.util.*;

/**
 * @author Jia Long Ji Qiu
 */

public class CtrlItem {
    private static CtrlItem singletonObject;
    private CtrlDades ctrlDades;
    private CtrlValoracio ctrlValoracio;
    HashMap<Integer, Item> items;
    HashMap<String, TipusItem> tipusItems;

    public static CtrlItem getInstance() {
        if (singletonObject == null)
            singletonObject = new CtrlItem();
        return singletonObject;
    }

    private CtrlItem() {
        this.items = new HashMap<>();
        this.tipusItems = new HashMap<>();
    }

    public void setCtrlDades(CtrlDades ctrlDades) { this.ctrlDades = ctrlDades; }

    public void setCtrlValoracio(CtrlValoracio ctrlValoracio) { this.ctrlValoracio = ctrlValoracio; }

    public HashMap<Integer, Item> getItems() {
        return items;
    }
    
    public HashMap<String, TipusItem> getTipusItems()
    {
    	return tipusItems;
    }

    /**
     * Crea un tipus d'item donat un nom.
     * @param nomTipusItem Nom del tipus d'item que es vol crear
     * @return 0 si s'ha creat el tipus d'item correctament
     *         -1 si ja existeix un tipus d'item amb el nom especificat
     */
    public int crearTipusItem(String nomTipusItem) {
        if (tipusItems.containsKey(nomTipusItem)) return -1;
        TipusItem t = new TipusItem(nomTipusItem);
        tipusItems.put(nomTipusItem, t);
        return 0;
    }

    /**
     * Crea un tipus d'item donat un nom i l'atribut id
     * @param nomTipusItem Nom del tipus d'item que es vol crear
     * @return 0 si s'ha creat el tipus d'item correctament
     *         -1 si ja existeix un tipus d'item amb el nom especificat
     */
    public int crearTipusItemWithId(String nomTipusItem) {
        if (tipusItems.containsKey(nomTipusItem)) return -1;
        TipusItem t = new TipusItem(nomTipusItem);
        t.afegirTipusAtribut("id", TAtribut.NUMERIC);
        tipusItems.put(nomTipusItem, t);
       
        return 0;
    }
    
    /**
     * Elimina un tipus d'item donat un nom de tipus d'item.
     * @param nom Nom del tipus d'item que es vol eliminar
     * @return 0 si el tipus d'item s'ha eliminat correctament
     *         -1 si el tipus d'item especificat no existeix
     *         -2 si hi ha cap item amb el tipus d'item especificat
     */
    public int eliminarTipusItem(String nom) {
        if (!tipusItems.containsKey(nom)) return -1;
        for (Integer i : items.keySet()) {
            if (items.get(i).getTipusItem().getNom().equals(nom)) return -2;
        }
        tipusItems.remove(nom);
        return 0;
    }

    /**
     * Afegeix un tipus d'atribut al tipus d'item especificat
     * @param nomTipusItem Nom del tipus d'item al qual es vol afegir un tipus d'atribut
     * @param nomTipusAtribut Nom del tipus d'atribut
     * @param tAtribut Tipus del tipus d'atribut
     * @return 0 si el tipus d'atribut s'ha afegit correctament
     *         -1 si el tipus d'item especificat no existeix
     *         -2 si el tipus d'atribut especificat ja existeix
     */
    public int afegirTipusAtribut(String nomTipusItem, String nomTipusAtribut, TipusItem.TAtribut tAtribut) {
        if (!tipusItems.containsKey(nomTipusItem)) return -1; //System.out.println("ERROR: El tipus d'item especificat no existeix");
        if (tipusItems.get(nomTipusItem).getTipusAtributs().containsKey(nomTipusAtribut)) return -2; //System.out.println("ERROR: El tipus d'atribut especificat ja existeix");
        tipusItems.get(nomTipusItem).afegirTipusAtribut(nomTipusAtribut, tAtribut);
        return 0; // System.out.println("Tipus d'atribut afegit correctament");
    }

    /**
     * Canvia el nom d'un tipus d'atribut donat un nom de tipus d'item i el nou nom tipus d'atribut.
     * @param nomTipusItem Nom del tipus d'item que volem fer el canvi del nom del tipus d'atribut
     * @param nomTipusAtribut1 Nom tipus d'atribut que es vol substituir
     * @param nomTipusAtribut2 Nom tipus d'atribut que es vol posar
     * @return 0 si el nom del tipus d'atribut s'ha canviat correctament
     *         -1 si el tipus d'item especificat no existeix
     *         -2 si el tipus d'atribut especificat no existeix
     *         -3 si el nou nom del tipus d'atribut especificat ja esta utilitzat
     */
    public int canviarNomTipusAtribut(String nomTipusItem, String nomTipusAtribut1, String nomTipusAtribut2) {
        if (!tipusItems.containsKey(nomTipusItem)) return -1; //System.out.println("ERROR: El tipus d'item especificat no existeix");
        if (!tipusItems.get(nomTipusItem).getTipusAtributs().containsKey(nomTipusAtribut1)) return -2; //System.out.println("ERROR: El tipus d'atribut especificat no existeix");
        if (tipusItems.get(nomTipusItem).getTipusAtributs().containsKey(nomTipusAtribut2)) return -3; //System.out.println("ERROR: El nom del tipus d'atribut que volem posar ja està utilitzat");
        tipusItems.get(nomTipusItem).canviarNomTipusAtribut(nomTipusAtribut1, nomTipusAtribut2);
        for (Item i: items.values()) {
            if (i.getTipusItem().getNom().equals(nomTipusItem) && i.getAtributs().containsKey(nomTipusAtribut1)) {
                Atribut aux = i.getAtribut(nomTipusAtribut1);
                i.eliminarAtribut(nomTipusAtribut1);
                i.afegirAtribut(nomTipusAtribut2, aux);
            }
        }
        return 0; // System.out.println("Nom del tipus d'atribut canviat correctament");
    }

    /**
     * Elimina un tipus d'atribut donat un nom de tipus d'item i un nom tipus d'atribut.
     * @param nomTipusItem Nom del tipus d'item que volem fer l'eliminacio del tipus d'atribut
     * @param nomTipusAtribut Nom tipus d'atribut que es vol eliminar
     * @return  0 si el tipus d'atribut s'ha eliminat correctament
     *         -1 si el tipus d'item especificat no existeix
     *         -2 si el tipus d'atribut especificat no existeix
     */
    public int eliminarTipusAtribut(String nomTipusItem, String nomTipusAtribut) {
        if (!tipusItems.containsKey(nomTipusItem)) return -1; //System.out.println("ERROR: El tipus d'item especificat no existeix");
        if (!tipusItems.get(nomTipusItem).getTipusAtributs().containsKey(nomTipusAtribut)) return -2; //System.out.println("ERROR: El tipus d'atribut especificat no existeix");
        for (Integer i : items.keySet())
            if (items.get(i).getTipusItem().getNom().equals(nomTipusItem)) return -3; // System.out.println("ERROR: Existeixen items del tipus d'item especificat");
        tipusItems.get(nomTipusItem).eliminarTipusAtribut(nomTipusAtribut);
        return 0; // System.out.println("Tipus d'atribut eliminar correctament");
    }

    /**
     * Crear un item nou.
     * @param id Identificador de l'item que es vol crear
     * @param nomTipusItem Nom del tipus d'item que pertany l'item
     * @return  0 si l'item s'ha creat correctament
     *         -1 si item amb identificador id ja existeix
     *         -2 si el tipus d'item especificat no existeix
     */
    public int crearItem(int id, String nomTipusItem) {
        if(items.containsKey(id)) return -1; // System.out.println("Ja existeix l'item especificat");
        if (!tipusItems.containsKey(nomTipusItem)) return -2; // System.out.println("No existeix el tipus d'item especificat");
        Item item = new Item(id, tipusItems.get(nomTipusItem));
        items.put(id, item);
        return 0;
    }

    /**
     * Eliminar un item.
     * @param id Identificador de l'item que es vol eliminar
     * @return  0 si l'item s'ha eliminat correctament
     *          -1 si item amb identificador id no existeix
     */
    public int eliminarItem(int id) {
        if (!items.containsKey(id)) {
            return -1;
        }
        if (ctrlValoracio != null)  ctrlValoracio.eliminarValoracionsItem(id);
        items.remove(id);
        return 0;
    }

    /**
     * Afegir un atribut en un item.
     * @param id Identificador de l'item que es vol afegir un atribut nou
     * @param nomTipusAtribut Nom del tipus d'atribut que es vol afegir a l'item
     * @param valor Atribut que es vol afegir en l'item especificat
     * @return  0 si l'atribut s'ha afegit correctament
     *         -1 si el tipus d'item especificat no existeix
     *         -2 si el tipus d'atribut especificat ja existeix
     *         -3 Si el tipus d'atribut especificat no pertany el tipus d'item corresponent
     *         -4 Si el TAtribut del valor i el TAtribut del tipus d'atribut no coincideix
     */
    public int afegirAtribut(int id, String nomTipusAtribut, Atribut valor) {
        if (!items.containsKey(id)) return -1; // System.out.println("ERROR: L'item amb l'id especificat no existeix");
        if (items.get(id).getAtributs().containsKey(nomTipusAtribut)) return -2; // System.out.println("ERROR: El tipus d'atribut especificat ja existeix");
        if (!tipusItems.get(items.get(id).getTipusItem().getNom()).getTipusAtributs().containsKey(nomTipusAtribut)) return -3; // System.out.println("ERROR: El tipus d'atribut especificat no existeix al tipus d'item corresponent");
        if (!valor.getTAtribut().equals(tipusItems.get(items.get(id).getTipusItem().getNom()).getTipusAtributs().get(nomTipusAtribut))) return -4; // System.out.println("ERROR: El TAtribut no coincideix");
        items.get(id).afegirAtribut(nomTipusAtribut, valor);
        return 0;
    }

    /**
     * Modificar un atribut en un item.
     * @param id Identificador de l'item que es vol modificar l'atribut
     * @param nomTipusAtribut Nom del tipus d'atribut que es vol modificar l'atribut
     * @param valor Atribut que es vol posar en el tipus d'atribut en l'item especificat
     * @return  0 si l'atribut s'ha modificat correctament
     *         -1 si l'item especificat no existeix
     *         -2 si el tipus d'atribut especificat no existeix
     *         -3 Si el TAtribut del valor i el TAtribut del tipus d'atribut no coincideix
     */
    public int modificarAtribut(int id, String nomTipusAtribut, Atribut valor) {
        if (!items.containsKey(id)) return -1; // System.out.println("ERROR: L'item amb l'id especificat no existeix");
        if (!items.get(id).getAtributs().containsKey(nomTipusAtribut)) return -2; // System.out.println("ERROR: El tipus d'atribut especificat no existeix");
        if (!valor.getTAtribut().equals(tipusItems.get(items.get(id).getTipusItem().getNom()).getTipusAtributs().get(nomTipusAtribut))) return -3; // System.out.println("ERROR: El TAtribut no coincideix");
        items.get(id).modificarAtribut(nomTipusAtribut, valor);
        return 0;
    }

    /**
     * Eliminar un atribut en un item.
     * @param id Identificador de l'item que es vol eliminar l'atribut
     * @param nomTipusAtribut Nom del tipus d'atribut que es vol eliminar
     * @return  0 si l'atribut s'ha eliminat correctament
     *         -1 si l'item especificat no existeix
     *         -2 si el tipus d'atribut especificat no existeix
     */
    public int eliminarAtribut(int id, String nomTipusAtribut) {
        if (!items.containsKey(id)) return -1; // System.out.println("ERROR: L'item amb l'id especificat no existeix");
        if (!items.get(id).getAtributs().containsKey(nomTipusAtribut)) return -2; // System.out.println("ERROR: El tipus d'atribut especificat no existeix");
        items.get(id).eliminarAtribut(nomTipusAtribut);
        return 0;
    }

    public List<String> getTipusAtributs(String nomTipusItem) {
        List<String> nTipusAtributs = new ArrayList<String>();
        HashMap<String, TipusItem.TAtribut> tipusAtributs = tipusItems.get(nomTipusItem).getTipusAtributs();
        for (String nomTipusAtribut : tipusAtributs.keySet()) nTipusAtributs.add(nomTipusAtribut);
        return nTipusAtributs;
    }
    /**
     * Carregar un nou tipus d'item amb els seus items des d'un fitxer tipus csv
     * l'objecte tipus d'item pot existir pero no ha de tenir atributs (excepte el id) ni items 
     * associats.
     * @param nomTipusItem nom del tipus d'item que es vol crear
     * @param filename Nom del fitxer.csv on esta guardada questes dades.
     * @return  0 tot correcte
     *          -1 el tipus de item ja te atributs definits 
     *          -2 aquest tipus de item ja tenia items associats
     *          -3 no existeix l'atribut id al csv donat
     *          -4 s'ha trobat un id ja existent
     */
    public int carregarItemsCSV(String nomTipusItem, String filename) throws Exception {
        TipusItem tipusItem = null;
        if (tipusItems.containsKey(nomTipusItem)) {
            tipusItem = tipusItems.get(nomTipusItem);
            
            HashMap<String, TAtribut> tAtributs = tipusItem.getTipusAtributs();
            if (!(tAtributs.isEmpty() || (tAtributs.size()) == 1 && tAtributs.containsKey("id")))
                return -1;
            
            if (this.getItems(tipusItem.getNom()).size() > 0)
                return -2;
        }
        else tipusItem = new TipusItem(nomTipusItem);
        ctrlDades.carregarItemsCSV(filename);
        
        // Validar que tots els items tenen el camp id abans de comencar a crear objectes
        List<HashMap<String, String>> objects = ctrlDades.getItems();
        for (HashMap<String, String> ob : objects) {
            if (!ob.containsKey("id")) return -3;
            
            String id = ob.get("id");
            if (id.isBlank() || id.isEmpty()) return -3;
            
            try {
                if (items.containsKey(Integer.parseInt(id)))
                    return -4;
            } catch (NumberFormatException e) {
                return -3;
            }
        }
        
        if (tipusItem.getTipusAtributs().containsKey("id"))
            tipusItem.eliminarTipusAtribut("id");
        
        HashMap<String, Class<?>> fields = ctrlDades.getTipusAtributs();
        HashMap<String, TipusItem.TAtribut> aux = new HashMap<>();
        for (String nomTipusAtribut : fields.keySet()) {
            TipusItem.TAtribut t;
            Class<?> classe = fields.get(nomTipusAtribut);
            if (classe == Boolean.class) {
                t = TipusItem.TAtribut.BOOLEA;
            }
            else if (classe == Set.class) {
                t = TipusItem.TAtribut.CATEGORIC;
            }
            else if (classe == Double.class || classe == Integer.class) {
                t = TipusItem.TAtribut.NUMERIC;
            }
            else t = TipusItem.TAtribut.DESCRIPTIU;
            aux.put(nomTipusAtribut, t);
        }
        tipusItem.setTipusAtributs(aux);
        tipusItems.put(nomTipusItem, tipusItem);
        
        for (int i = 0; i < objects.size(); ++i) {
            HashMap<String, String> atrs = objects.get(i);         
            int id = Integer.parseInt(atrs.get("id"));
            Item auxItem = new Item(id, tipusItem);
            for (String nomAtribut : atrs.keySet()) {
                TipusItem.TAtribut t = tipusItem.getTAtribut(nomAtribut);
                if (t == TipusItem.TAtribut.BOOLEA) {
                    boolean valor = Boolean.parseBoolean(atrs.get(nomAtribut));
                    AtributBoolea aBoolea = new AtributBoolea(valor);
                    auxItem.afegirAtribut(nomAtribut, aBoolea);
                } else if (t == TipusItem.TAtribut.NUMERIC) {
                    double valor = Double.parseDouble(atrs.get(nomAtribut));
                    AtributNumeric aNumeric = new AtributNumeric(valor);
                    auxItem.afegirAtribut(nomAtribut, aNumeric);
                } else if (t == TipusItem.TAtribut.CATEGORIC) {
                    String valor = atrs.get(nomAtribut);
                    String[] splitted = valor.split(";");
                    AtributCategoric aCategoric = new AtributCategoric();
                    for (String s : splitted) aCategoric.afegirValor(s);
                    auxItem.afegirAtribut(nomAtribut, aCategoric);
                } else {
                    String valor = atrs.get(nomAtribut);
                    AtributDescriptiu aDescriptiu = new AtributDescriptiu(valor);
                    auxItem.afegirAtribut(nomAtribut, aDescriptiu);
                }
            }
            items.put(id, auxItem);
        }
        
        return 0;
    }

    /**
     * Comprova si existeix un item amb id "idItem".
     * @param idItem nom de l'item que es vol comprovar
     * @return  true si el item especificat existeix
     *          false si el item especificat no existeix
     */
    public boolean existsItem(int idItem) {
        return items.containsKey(idItem);
    }

    public Item getItem(int idItem) { return items.get(idItem); }


    public Set<Item> getItems(String nomTipusItem) {
        Set<Item> ret = new HashSet<>();
        for (Integer idItem : items.keySet()) {
            Item item = items.get(idItem);
            if (item.getTipusItem().getNom().equals(nomTipusItem)) ret.add(item);
        }
        return ret;
    }
    
    /**
     * Retorna els items convertits a string
     * @param nomTipusItem el nom de tipus de item
     * @return un map on la primera clau es el id del item
     *          i la segona es el nom de l'atribut
     */
    public HashMap<String, HashMap<String, String>> getItemStrings(String nomTipusItem)
    {
        HashMap<String, HashMap<String, String>> m = new HashMap<String, HashMap<String, String>>();
        if (!tipusItems.containsKey(nomTipusItem))
            return m;
        
        TipusItem ti = tipusItems.get(nomTipusItem);
        HashMap<String, TAtribut> tipusAtr = ti.getTipusAtributs();
        
        Set<Item> items = getItems(nomTipusItem);
        for (Item it : items) {
            HashMap<String, String> mAtrs = new HashMap<String, String>();
           
            for (String atrName : tipusAtr.keySet()) {
                String val = "";
                if (it.existsAtribut(atrName))
                    val = it.getAtribut(atrName).toString();
               
                mAtrs.put(atrName, val);
            }
            
            m.put(Integer.toString(it.getId()), mAtrs);
        }
        
        return m;
    }
    
    public HashMap<String, TAtribut> getTipusAtribut(String  nomTipusItem) {
        return tipusItems.get(nomTipusItem).getTipusAtributs();
    }
    
    public boolean teAtributDefinit(int idItem, String nomAtribut)
    {
        if (!this.items.containsKey(idItem))
            return false;
        
        return this.items.get(idItem).existsAtribut(nomAtribut);
    }

    public void printTipusItems() {
        for (String s : tipusItems.keySet()) {
            System.out.println(tipusItems.get(s));
            System.out.println();
        }
    }
    public void printItems() {
        int count = 0;
        for (Integer id : items.keySet()) {
            ++count;
            System.out.println(items.get(id));
            System.out.println();
        }
    }
    
    public int carregar()
    {
        items.clear();
        tipusItems.clear();
        
        // Carregar els tipus de items
        try {
            List<TipusItem> tItems = ctrlDades.loadGeneric(TipusItem.class, "tipus_items.json", "tipus_items");
            for (TipusItem tItem : tItems) 
                this.tipusItems.put(tItem.getNom(), tItem);
        }
        catch (Exception ex) {
            return -1;
        } 
        
        // Carregar els items
        try {
            List<Item> itemList = ctrlDades.loadGeneric(Item.class, "items.json", "items");
            for (Item it : itemList) {
                Double d = (Double)it.getAtribut("id").getValor();
                this.items.put(d.intValue(), it);
            }
        }
        catch (Exception ex) {
            return -1;
        }
        
        return 0;
    }
    
    public int guardar()
    {
        // Guardar tipus d'items
        ArrayList<TipusItem> tItem = new ArrayList<>();
        tItem.addAll(this.tipusItems.values());
        
        try {
            ctrlDades.saveGeneric(tItem, TipusItem.class, "tipus_items.json", "tipus_items");
        }
        catch (Exception ex) {
            return -1;
        }
        
        // Guardar els items
        ArrayList<Item> items = new ArrayList<>();
        items.addAll(this.items.values());
        
        try {
            ctrlDades.saveGeneric(items, Item.class, "items.json", "items");
        }
        catch (Exception ex) {
            return -1;
        }
        
        return 0;
    }
}
