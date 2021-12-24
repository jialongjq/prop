package domain;

import data.CtrlDades;
import java.util.ArrayList;
import utils.Hash;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jia Long Ji Qiu
 */

public class CtrlValoracio {
    private static CtrlValoracio singletonObject;
    private CtrlItem ctrlItem;
    private CtrlUsuari ctrlUsuari;
    private HashMap<Integer, HashMap<Integer, Valoracio>> valoracionsItem;
    private HashMap<Integer, HashMap<Integer, Valoracio>> valoracionsUsuari;

    public static CtrlValoracio getInstance() throws Exception {
        if (singletonObject == null)
            singletonObject = new CtrlValoracio();
        return singletonObject;
    }

    /**
     * Constructora privada del controlador de valoracio
     * @throws Exception
     */
    private CtrlValoracio() throws Exception {
        //ctrlItem = CtrlItem.getInstance();
        //ctrlUsuari = CtrlUsuari.getInstance();
        valoracionsItem = new HashMap<>();
        valoracionsUsuari = new HashMap<>();
    }

    /**
     * Setter del controlador d'item
     * @param ctrlItem controlador d'item
     */
    public void setCtrlItem(CtrlItem ctrlItem) { this.ctrlItem = ctrlItem; }

    /**
     * Setter del controlador d'usuari
     * @param ctrlUsuari controlador d'usuari
     */
    public void setCtrlUsuari(CtrlUsuari ctrlUsuari) { this.ctrlUsuari = ctrlUsuari; }

    /**
     * Afegeix una valoracio del item amb id "idItem" per l'usuari amb id "idUsuari"
     * @param idItem id de l'item valorat
     * @param idUsuari id de l'usuari que valora
     * @param puntuacio valor de la valoracio
     * @return 0 si s'ha afegit la valoracio satisfactoriament
     *         -1 si no existeix l'item
     *         -2 si no existeix l'usuari
     *         -3 si la valoracio ja existeix
     */
    public int afegirValoracio(int idItem, int idUsuari, double puntuacio) {
        if (!ctrlItem.existsItem(idItem)) return -1;
        if (!ctrlUsuari.existsUsuari(idUsuari)) return -2;
        if (valoracionsItem.containsKey(idItem) && valoracionsItem.get(idItem).containsKey(idUsuari)) return -3;
        if (valoracionsUsuari.containsKey(idUsuari) && valoracionsUsuari.get(idUsuari).containsKey(idItem)) return -4;

        Valoracio auxValoracio = new Valoracio(idItem, idUsuari, puntuacio);
        HashMap<Integer, Valoracio> auxValoracioItem;
        if (valoracionsItem.get(idItem) == null) auxValoracioItem = new HashMap<>();
        else auxValoracioItem = valoracionsItem.get(idItem);
        auxValoracioItem.put(idUsuari, auxValoracio);
        valoracionsItem.put(idItem, auxValoracioItem);

        HashMap<Integer, Valoracio> auxValoracioUsuari;
        if (valoracionsUsuari.get(idUsuari) == null) auxValoracioUsuari = new HashMap<>();
        else auxValoracioUsuari = valoracionsUsuari.get(idUsuari);
        auxValoracioUsuari.put(idItem, auxValoracio);
        valoracionsUsuari.put(idUsuari, auxValoracioUsuari);
        return 0;
    }

    /**
     * Modifica la puntuacio d'una valoracio de l'item amb id "idItem" per l'usuari amb id "idUsuari"
     * @param idItem id de l'item valorat
     * @param idUsuari id de l'usuari que valora
     * @param puntuacio valor de la nova valoracio
     * @return 0 si s'ha modificat la puntuacio satisfactoriament
     *         -1 si no existeix l'item
     *         -2 si no existeix l'usuari
     *         -3 si la valoracio no existeix
     */
    public int modificarValoracio(int idItem, int idUsuari, double puntuacio) {
        if (!ctrlItem.existsItem(idItem)) return -1;
        if (!ctrlUsuari.existsUsuari(idUsuari)) return -2;
        if (!(valoracionsItem.containsKey(idItem) && valoracionsItem.get(idItem).containsKey(idUsuari))) return -3;
        if (!(valoracionsUsuari.containsKey(idUsuari) && valoracionsUsuari.get(idUsuari).containsKey(idItem))) return -4;

        Valoracio auxValoracio = new Valoracio(idItem, idUsuari, puntuacio);

        HashMap<Integer, Valoracio> auxValoracioItem = valoracionsItem.get(idItem);
        auxValoracioItem.put(idUsuari, auxValoracio);
        valoracionsItem.put(idItem, auxValoracioItem);

        HashMap<Integer, Valoracio> auxValoracioUsuari = valoracionsUsuari.get(idUsuari);
        auxValoracioUsuari.put(idItem, auxValoracio);
        valoracionsUsuari.put(idUsuari, auxValoracioUsuari);

        return 0;
    }

    /**
     * Elimina la valoracio de l'item amb id "idItem" per l'usuari amb id "idUsuari"
     * @param idItem id de l'item valorat
     * @param idUsuari id de l'usuari que valora
     * @return 0 si s'ha eliminat la valoracio satisfactoriament
     *         -1 si la valoracio no existeix
     */
    public int eliminarValoracio(int idItem, int idUsuari) {
        if (!(valoracionsItem.containsKey(idItem) && valoracionsItem.get(idItem).containsKey(idUsuari))) return -1;
        if (!(valoracionsUsuari.containsKey(idUsuari) && valoracionsUsuari.get(idUsuari).containsKey(idItem))) return -1;
        valoracionsItem.get(idItem).remove(idUsuari);
        if (valoracionsItem.get(idItem).isEmpty()) valoracionsItem.remove(idItem);
        valoracionsUsuari.get(idUsuari).remove(idItem);
        if (valoracionsUsuari.get(idUsuari).isEmpty()) valoracionsUsuari.remove(idUsuari);
        return 0;
    }

    /**
     * Elimina totes les valoracions fetes per l'usuari amb id "idUsuari"
     * @param idUsuari id de l'usuari del que es vol eliminar les valoracions
     * @return 0 si s'ha eliminat correctament
     *         -1 si no existeix l'usuari amb l'id especificat
     */
    public int eliminarValoracionsUsuari(int idUsuari) {
        if (!ctrlUsuari.existsUsuari(idUsuari)) return -1;
        if (!valoracionsUsuari.containsKey(idUsuari)) return 0;
        // Primer eliminem les valoracions de l'usuari dins de valoracionsItem, a partir de valoracionsUsuari
        for (int idItem : valoracionsUsuari.get(idUsuari).keySet()) {
            valoracionsItem.get(idItem).remove(idUsuari);
            if (valoracionsItem.get(idItem).isEmpty()) valoracionsItem.remove(idItem);
        }
        valoracionsUsuari.remove(idUsuari);
        return 0;
    }

    /**
     * Elimina totes les valoracions que s'han fet a l'item amb id "idItem"
     * @param idItem id de l'item del qual es vol eliminar les valoracions
     * @return 0 si s'ha eliminat correctament
     *         -1 si no existeix l'item amb l'id especificat
     */
    public int eliminarValoracionsItem(int idItem) {
        if (!ctrlItem.existsItem(idItem)) return -1;
        if (!valoracionsItem.containsKey(idItem)) return 0;
        // Primer eliminem les valoracions de l'item dins de valoracionsUsuari, a partir de valoracionsItem
        for (int idUsuari : valoracionsItem.get(idItem).keySet()) {
            valoracionsUsuari.get(idUsuari).remove(idItem);
            if (valoracionsUsuari.get(idUsuari).isEmpty()) valoracionsUsuari.remove(idUsuari);
        }
        valoracionsItem.remove(idItem);
        return 0;
    }
    
    public HashMap<Integer, HashMap<Integer, Valoracio>> getValoracionsPerUsuari()
    {
    	return valoracionsUsuari;
    }

    /**
     * Retorna els items valorats per un usuari en concret amb una puntuacio superior a un minim donat
     * @param idUsuari id de l'usuari del qual es vol obtenir les valoracions
     * @param puntuacioMin puntuacio minima que han de tenir les valoracions
     * @return conjunt d'items valorats per l'usuari
     */
    public Set<Item> getItemsValorats(int idUsuari, double puntuacioMin) {
        Set<Item> ret = new HashSet<>();
        if (valoracionsUsuari.containsKey(idUsuari)) {
            for (Integer idItem : valoracionsUsuari.get(idUsuari).keySet()) {
                if (valoracionsUsuari.get(idUsuari).get(idItem).getPuntuacio() >= puntuacioMin)
                    ret.add(ctrlItem.getItem(idItem));
            }
        }
        return ret;
    }
    
    /**
     * Obte la valoracio per un item per part d'un usuari
     * @param idUsuari identificador de l'usuari
     * @param idItem identificador del item
     * @return si te valoracio, la seva valoracio amb un valor mes gran o igual que 0,
     * en cas contrari retorna -1.0
     */
    public double getValoracio(int idUsuari, int idItem) 
    {
        if (!valoracionsUsuari.containsKey(idUsuari))
            return -1.0;
        
        HashMap<Integer, Valoracio> valUsuari = valoracionsUsuari.get(idUsuari);
        if (!valUsuari.containsKey(idItem))
            return -1.0;
        
        return valUsuari.get(idItem).getPuntuacio();
    }
    
    public int carregar()
    {
        valoracionsItem.clear();
        valoracionsUsuari.clear();
        
        try {
            CtrlDades ctrlDades = CtrlDades.getInstance();
            List<Valoracio> valoracions = ctrlDades.loadGeneric(Valoracio.class, "valoracions.json", "valoracions");
            
            for (Valoracio val : valoracions) {
                if (!valoracionsItem.containsKey(val.getIdItem()))
                    valoracionsItem.put(val.getIdItem(), new HashMap<>());
                
                valoracionsItem.get(val.getIdItem()).put(val.getIdUsuari(), val);
                
                if (!valoracionsUsuari.containsKey(val.getIdUsuari())) 
                    valoracionsUsuari.put(val.getIdUsuari(), new HashMap<>());
                
                valoracionsUsuari.get(val.getIdUsuari()).put(val.getIdItem(), val);
            }
        }
        catch (Exception ex) {
            return -1;
        }
        
        return 0;
    }
    
    public int guardar()
    {
        ArrayList<Valoracio> valoracions = new ArrayList<>();
        for(HashMap<Integer, Valoracio> vals : valoracionsItem.values()) 
            valoracions.addAll(vals.values());
        
        try {
            CtrlDades ctrlDades = CtrlDades.getInstance();
            ctrlDades.saveGeneric(valoracions, Valoracio.class, "valoracions.json", "valoracions");
        }
        catch (Exception ex) {
            return -1;
        }
        
        return 0;
    }

    /**
     * Output de totes les valoracions per item
     */
    public void printValoracionsItem() {
        for (Integer i : valoracionsItem.keySet()) {
            System.out.println("[Valoracions d'item id="+i+"]");
            for (Integer j : valoracionsItem.get(i).keySet()) {
                System.out.println(valoracionsItem.get(i).get(j));
            }
        }
    }

    /**
     * Output de totes les valoracions per usuari
     */
    public void printValoracionsUsuari() {
        for (Integer i : valoracionsUsuari.keySet()) {
            System.out.println("[Valoracions d'usuari id="+i+"]");
            for (Integer j : valoracionsUsuari.get(i).keySet()) {
                System.out.println(valoracionsUsuari.get(i).get(j));
            }
        }
    }
}
