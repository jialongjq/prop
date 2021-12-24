package domain.algorismes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import domain.Classificacio;
import domain.Item;

/**
 * Algorisme hibrid: Collaborative + Content based
 * 
 * @author Roberto Navarro Morales
 */
public class Hibrid implements Algorisme {

    /**
     * Algorisme Slope One per a predir scores a partir de scores
     * dels usuaris similars a l'actual
     */
    private SlopeOne slopeOne;
    
    /**
     * Tipus de items sobre els que es volen fer les recomanacions
     */
    private Set<String> tipusItems;
    
    /**
     * Identificador de l'usuari pel qual es volen fer les recomanacions
     */
    private int idUsuari;

    /**
     * Items Valorats per part de l'usuari a partir d'una valoracio minima
     */
    private Set<Item> itemsValoratsSup;
    
    /**
     * Tots els items valorats per part d'un usuari
     */
    private Set<Item> itemsValoratsTot;
    
    /**
     * Conjunt absolut dels items classificat pel seu tipus
     */
    private HashMap<String, Set<Item>> conjuntItems;

    /**
     * Items recomanats en ordre de rellevancia
     */
    private ArrayList<Item> items;
    
    /**
     * Puntuacio predita per a cada item, la clau es el seu id
     */
    private HashMap<Integer, Double> predScore;
    
    /**
     * Els items classificats pel seu identificador
     */
    private HashMap<Integer, Item> idToItem;

    /**
     * Constructor 
     * 
     * @param idUsuari usuari sobre el que es vol executar l'algorisme
     * @param itemsValoratsTot conjunt dels items valorats per part de l'usuari
     * @param itemsValoratsSup conjunt dels items valorats per part de l'usuari amb una valoracio minima
     * @param conjuntItems conjunt total dels items
     * @param tipusItems tipus dels items sobre els que es vol executar l'algorisme
     * @param classificacio classificacio dels usuaris
     */
    public Hibrid(
            int idUsuari,
            Set<Item> itemsValoratsTot,
            Set<Item> itemsValoratsSup,
            HashMap<String, Set<Item>> conjuntItems,
            Set<String> tipusItems,
            Classificacio classificacio
    ) {
        this.slopeOne = new SlopeOne();
        this.tipusItems = tipusItems;
        this.idUsuari = idUsuari;

        this.slopeOne.setClassificacio(classificacio);
        this.itemsValoratsSup = itemsValoratsSup;
        this.itemsValoratsTot = itemsValoratsTot;
        
        this.conjuntItems = conjuntItems;

        this.items = new ArrayList<>();
        this.predScore = new HashMap<>();
        this.idToItem = new HashMap<>();
    }
    
    /**
     * Obtencio dels k items mes propers als ja valorats
     * 
     * @param normPred prediccions nomralitzades [out]
     * @param k cota superior de la quantitat dels items que obtindran
     * @param itemsVal items valorats per l'usuari
     * @param cjtItems conjunt total dels items
     */
    private void getNormalizedPredictionsCbf(
            HashMap<Integer, Double> normPred,
            int k,
            Set<Item> itemsVal,
            Set<Item> cjtItems) {

        ContentBasedFiltering cbf = new ContentBasedFiltering(k, itemsVal, cjtItems);
        cbf.resol();

        ArrayList<Item> sortItems = cbf.getResult();
        HashMap<Integer, Double> itemDis = cbf.getRatingItem();

        if (sortItems.isEmpty())
            return;

        // Obtencio del mes llunya i el mes proper
        double minDist = itemDis.get(sortItems.get(0).getId());
        double maxDist = itemDis.get(sortItems.get(sortItems.size() - 1).getId());

        for (Item it : sortItems) {
            if (itemsValoratsTot.contains(it))
                continue;
            
            double d = itemDis.get(it.getId());
            double normInv = (d - minDist) / (maxDist - minDist);
            double norm = 1.0 - normInv;

            normPred.put(it.getId(), norm);
        }
    }
    
    /**
     * Resolucio de l'algorisme hibrid
     */
    @Override
    public void resol() {
        items.clear();
        predScore.clear();
        idToItem.clear();

        // Collaborative
        slopeOne.setUsuari(idUsuari);
        slopeOne.resol();
        HashMap<Integer, Double> predCol = slopeOne.getPredValoracions();
        HashMap<Integer, Double> cbfVal = new HashMap<>();

        // Content based
        for (String tipusItem : tipusItems) {
            Set<Item> cjtItem = conjuntItems.get(tipusItem);
            HashSet<Integer> itemsVIds = new HashSet<>();

            for (Item i : itemsValoratsSup) 
                itemsVIds.add(i.getId());

            int k = 0;
            for (Item i : cjtItem) {
                if (!itemsVIds.contains(i.getId()))
                    ++k;

                idToItem.put(i.getId(), i);
            }
            
            HashSet<Item> itemsValsOfType = new HashSet<>();
            for (Item itValorat : itemsValoratsSup) {
                if (itValorat.getTipusItem().getNom().equals(tipusItem))
                    itemsValsOfType.add(itValorat);
            }
            
            getNormalizedPredictionsCbf(cbfVal, k, itemsValsOfType, cjtItem);
        }

        for (Map.Entry<Integer, Double> kv : predCol.entrySet()) {

            // Valor del colaboratiu
            double res = kv.getValue() / 5.;

            // Valor del content-based
            if (cbfVal.containsKey(kv.getKey())) {
                double valCb = cbfVal.get(kv.getKey());

                res = (valCb + res) / 2.;
            }

            predScore.put(kv.getKey(), roundToFive(res));
        }
        
        // En el cas de que el colaboratiu no tingui resultats
        if (predCol.isEmpty()) {
            for (Map.Entry<Integer, Double> kv : cbfVal.entrySet()) 
                predScore.put(kv.getKey(), roundToFive(kv.getValue()));
        }

        cbfVal.clear();

        for (Map.Entry<Integer, Double> kv : cbfVal.entrySet()) {
            if (!predScore.containsKey(kv.getKey())) {
                continue;
            }

            predScore.put(kv.getKey(), roundToFive(kv.getValue()));
        }

        ArrayList<Entry<Integer, Double>> list = new ArrayList<>(predScore.entrySet());
        list.sort(Entry.comparingByValue());

        for (int i = list.size() - 1; i >= 0; i--) {
            items.add(idToItem.get(list.get(i).getKey()));
        }
    }

    private double roundToFive(double valueBasedOne) {
        return Math.round(valueBasedOne * 5. * 2.) / 2.;
    }

    public ArrayList<Item> getItemSort() {
        return items;
    }

    public HashMap<Integer, Double> getItemsRating() {
        return predScore;
    }
}
