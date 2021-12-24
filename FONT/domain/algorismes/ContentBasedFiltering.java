package domain.algorismes;

import java.util.*;

import domain.Item;
import utils.Pair;

/**
 * @author Jia Long Ji Qiu
 */

public class ContentBasedFiltering implements Algorisme {
    /**
     * Nombre d'items que es volen escollir amb el kNN selection
     */
    int k;
    /**
     * Conjunt d'items valorats a partir dels quals es vol trobar recomanacions
     */
    Set<Item> itemsValorats;
    /**
     * Conjunt d'items del qual es disposa per escollir les recomanacions
     */
    Set<Item> conjuntItems;
    /**
     * Llista dels k items recomanats aplicant l'algorisme de Content Based Filtering. Es guarden els items recomanats
     * per ordre de rellevancia
     */
    ArrayList<Item> result;
    
    HashMap<Integer, Double> ratingItem;

    /**
     * Constructora de la classe ContentBasedFiltering
     * @param k Indica el numero de recomanacions que es vol obtenir a partir de l'algorisme de Content Based Filtering
     * @param itemsValorats Conjunt d'items valorats a partir dels quals es vol trobar recomanacions
     * @param conjuntItems Conjunt d'items del qual es disposa per escollir les recomanacions
     */
    public ContentBasedFiltering(int k, Set<Item> itemsValorats, Set<Item> conjuntItems) {
        this.k = k;
        this.itemsValorats = itemsValorats;
        this.conjuntItems = conjuntItems;
        this.result = new ArrayList<>();
        this.ratingItem = new HashMap<Integer, Double>();
    }

    /**
     * Guarda a l'atribut privat result les k recomanacions que s'obtenen a partir del conjunt d'items valorats i el conjunt total d'items
     */
    private void getContentBasedFiltering() {
        PriorityQueue<Pair> pq = new PriorityQueue<>();
        Set<Integer> taken = new HashSet<>();

        HashMap<String, Integer> maximsCategorics = new HashMap<>();
        HashMap<String, Double> maximsNumerics = new HashMap<>();

        double distanciaMaxima = Distancia.calcularDistanciaMaxima(conjuntItems, maximsCategorics, maximsNumerics);

        /*for (String s : maximsCategorics.keySet()) {
            System.out.println("Maxim categoric atribut "+s+"="+maximsCategorics.get(s));
        }
        for (String s : maximsNumerics.keySet()) {
            System.out.println("Maxim numeric atribut "+s+"="+maximsNumerics.get(s));
        }*/

        HashSet<Integer> repetits = new HashSet<>();
        for (Item itemvalorat : itemsValorats) {
            repetits.add(itemvalorat.getId());
        }

        for (Item itemValorat : itemsValorats) {
            PriorityQueue<Pair> pq2 = new PriorityQueue<>();
            for (Item itemConjunt : conjuntItems) {
                if (!repetits.contains(itemConjunt.getId())) {
                    Pair p = new Pair(Distancia.calcularDistancia(itemValorat, itemConjunt, maximsNumerics, distanciaMaxima), itemConjunt);
                    pq2.add(p);
                }
            }
            int o = 0;
            //System.out.println("SIZE PQ2 ="+pq2.size());
            while (!pq2.isEmpty() && o < k) {
                Pair p = pq2.remove();
                pq.add(p);
                ++o;
            }
        }
        //System.out.println("SIZE PQ="+pq.size());
        int i = 0;
        while (!pq.isEmpty() && i < k) {
            Pair p = pq.remove();
            if (!taken.contains(p.getSecond().getId())) {
                //System.out.println("Item agafat: id="+p.getSecond().getId()+", distancia="+p.getFirst());
                result.add(p.getSecond());
                ratingItem.put(p.getSecond().getId(), p.getFirst());
                taken.add(p.getSecond().getId());
                ++i;
            }
        }
    }

    /**
     * Retorna el resultat de l'algorisme Content Based Filtering
     * @return k recomanacions obtinguts d'aplicar l'algorisme
     */
    public ArrayList<Item> getResult() { return result; }
    
    public HashMap<Integer, Double> getRatingItem() { return ratingItem; }

    /**
     * Aplica l'algorisme de Content Based Filtering
     */
    public void resol() {
        getContentBasedFiltering();
    }
}
