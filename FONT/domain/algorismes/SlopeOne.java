package domain.algorismes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import domain.Classificacio;

import java.util.Set;

/**
 * Algorisme slope one
 *
 * @author Roberto Navarro Morales
 *
 */
public class SlopeOne implements Algorisme {

    private Classificacio classificacio;
    private HashMap<Integer, HashMap<Integer, Double>> valoracions;
    private HashSet<Integer> itemsDefinits;
    private int idUsuari;

    private HashMap<Integer, Double> prediccions;
    private ArrayList<Integer> itemsOrd;

    public SlopeOne() {
        prediccions = new HashMap<Integer, Double>();
        itemsOrd = new ArrayList<Integer>();
        itemsDefinits = new HashSet<Integer>();
    }

    public void setClassificacio(Classificacio clas) {
        classificacio = clas;
    }

    public void setUsuari(int idUsuari) {
        this.idUsuari = idUsuari;
    }

    /**
     * Reunir les valoracions dels usuaris similars i els items valorats
     * 
     * @return si s'ha pogut preprocessar correctament, en cas contrari
     * s'aborta directment
     */
    private boolean preproces() {

        // Obte els usuaris similars a un usuari determinat
        ArrayList<Integer> usuaris = null;
        try {
            usuaris = classificacio.getUsuarisSimilars(idUsuari);
            
            if (usuaris.isEmpty()) return false;
            
        } catch (Exception e) {
            return false;
        }

        HashMap<Integer, HashMap<Integer, Double>> totValoracions = classificacio.getValoracions();
        usuaris.add(idUsuari);
        
        // Obtenir les valoracions per part de l'usuari
        valoracions = new HashMap<>();
        for (Integer idU : usuaris) {
            HashMap<Integer, Double> valoracionsUsuari = new HashMap<>(totValoracions.get(idU));
            valoracions.put(idU, valoracionsUsuari);

            for (Integer idItem : valoracionsUsuari.keySet()) {
                itemsDefinits.add(idItem);
            }
        }
        
        return true;
    }

    /**
     * Estimar valoracio per un item
     *
     * @param idItem identificador de l'item, no l'ha d'haver valorat l'usuari
     * en questio
     * @return la valoracio estimada, una valoracio negativa indica que no es
     * pot determinar
     */
    private double estimaValoracio(int idItem) {
        // Otenir tots els items que ha valorat l'usuari actual
        Set<Integer> itemsValorats = valoracions.get(idUsuari).keySet();

        // Per a cada item i que hagi valorat l'usuari calcular la desviacio d'altres usuaris 
        // que si que tenen el item amb idItem i el item i
        int tot = 0;
        double pred = 0.0;

        for (Integer i : itemsValorats) {
            int nUsuaris = 0;
            double diff = 0.0;
            for (Map.Entry<Integer, HashMap<Integer, Double>> valUsuari : valoracions.entrySet()) {

                // Si l'usuari no ha valorat el item buscat o el i no fer res
                if (!valUsuari.getValue().containsKey(idItem) || !valUsuari.getValue().containsKey(i)) {
                    continue;
                }

                double val_a = valUsuari.getValue().get(idItem);
                double val_b = valUsuari.getValue().get(i);

                // Calcular la diferencia
                diff += val_a - val_b;
                ++nUsuaris;
            }

            if (nUsuaris > 0) {
                diff /= nUsuaris;
                pred += (diff + valoracions.get(idUsuari).get(i)) * nUsuaris;
                tot += nUsuaris;
            }
        }

        if (tot == 0) 
            return -Double.MAX_VALUE;

        return pred / tot;
    }

    @Override
    public void resol() {
        prediccions.clear();
        itemsOrd.clear();
        itemsDefinits.clear();

        boolean success = preproces();
        if (!success) return;

        // Estima la valoracio per tots els items
        for (Integer it : itemsDefinits) {
            if (valoracions.get(idUsuari).containsKey(it)) {
                continue;
            }

            double pred = estimaValoracio(it);
            if (Math.abs(pred - (-Double.MAX_VALUE)) < 1e-2) {
                continue;
            }

            prediccions.put(it, Math.round(Math.min(Math.max(pred, 0), 5) * 2) / 2.0);
        }

        ArrayList<Entry<Integer, Double>> list = new ArrayList<>(prediccions.entrySet());
        list.sort(Entry.comparingByValue());

        for (int i = list.size() - 1; i >= 0; i--) {
            itemsOrd.add(list.get(i).getKey());
        }
    }

    public ArrayList<Integer> getItemsOrd() {
        return itemsOrd;
    }

    public HashMap<Integer, Double> getPredValoracions() {
        return prediccions;
    }
}
