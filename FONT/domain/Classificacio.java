package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

import data.JsonAdd;
import data.JsonIgnore;
import data.JsonKeyValue;
import domain.algorismes.KMeans;

/**
 * Classe que gestiona la classificacio dels usuaris segons la seva similitud
 *
 * @author Roberto Navarro Morales
 *
 */
public class Classificacio {

    /**
     * Valoracions per a cada usuari, la clau del map extern es correspon a l'id
     * de l'ususari mentre que la de l'intern a l'id de l'item
     */
    @JsonIgnore
    private HashMap<Integer, HashMap<Integer, Double>> valoracions;

    /**
     * Per a cada usuari (Key: id usuari) es guarda el cluster al que pertany
     * (la possició a la llista 'clusters'
     */
    @JsonAdd("recuperarClassificacioUsuari")
    @JsonKeyValue
    private HashMap<Integer, Integer> usuariCluster;

    /**
     * Clusters amb els usuaris, la primera llista tindra mida k mentre que la
     * segona llista sera la dels usuaris que conformin el cluster
     */
    @JsonIgnore
    private ArrayList<ArrayList<Integer>> clusters;

    /**
     * Percentatge dels items mes valorats que es considerara representatiu
     */
    private double tall;

    /**
     * Parametre principal del k-means
     */
    private int k;

    /**
     * Preprocessament de les dades
     *
     * @return els (tall) items amb mes valoracions
     */
    private LinkedList<Integer> preproces() {
        // Cerca dels items mes valorats
        final HashMap<Integer, Integer> total = new HashMap<Integer, Integer>();

        PriorityQueue<Integer> pq = new PriorityQueue<Integer>(1, new Comparator<Integer>() {
            public int compare(Integer x, Integer y) {
                return total.get(x) - total.get(y);
            }
        });

        int nItems = 0;
        for (HashMap<Integer, Double> vals : valoracions.values()) {
            for (Map.Entry<Integer, Double> val : vals.entrySet()) {
                if (total.containsKey(val.getKey())) {
                    total.put(val.getKey(), total.get(val.getKey()) + 1);
                } else {
                    total.put(val.getKey(), 1);
                    nItems++;
                }
            }
        }

        if (nItems < 30) {
            tall = 1.;
        }

        k = Math.min(k, Math.max(5, (int) Math.floor(valoracions.keySet().size() * 0.1)));
        k = Math.min(k, valoracions.size());

        // Mida objectiu
        int target = (int) Math.ceil(nItems * tall);
        for (Map.Entry<Integer, Integer> tot : total.entrySet()) {
            pq.add(tot.getKey());

            if (pq.size() > target) {
                pq.poll();
            }
        }

        LinkedList<Integer> items = new LinkedList<Integer>();
        while (!pq.isEmpty()) {
            int itemId = pq.poll();
            items.add(0, itemId);

        }

        //for (Integer it : items) 
        //	System.out.println(it + ":\t" + total.get(it));
        return items;
    }

    /**
     * Constructor
     *
     * @param tall Percentatge dels items mes valorats que es considerara
     * representatiu
     * @param k parametre del k means
     */
    public Classificacio(double tall, int k) {
        this.tall = tall;
        this.k = k;

        usuariCluster = new HashMap<Integer, Integer>();
        valoracions = new HashMap<Integer, HashMap<Integer, Double>>();
        clusters = new ArrayList<ArrayList<Integer>>();
    }

    public void setK(int k) {
        this.k = k;
    }

    /**
     * Fer una classificacio nova
     *
     * @throws Exception
     */
    public void classifica() throws Exception {
        // Netejar els resultats de classificacions previes
        usuariCluster.clear();
        clusters.clear();

        LinkedList<Integer> items = preproces();

        HashMap<Integer, Integer> matToUsuari = new HashMap<Integer, Integer>();

        // Creacio de la matriu de dades, cada fila es un usuari i cada columna un item
        // cada valor esta normalitzat entre 0 i 1
        ArrayList<ArrayList<Double>> dades = new ArrayList<ArrayList<Double>>();

        for (Map.Entry<Integer, HashMap<Integer, Double>> keyVal : valoracions.entrySet()) {
            ArrayList<Double> vec = new ArrayList<Double>();

            // Mantenir la possicio de l'usuari
            matToUsuari.put(dades.size(), keyVal.getKey());

            for (Integer itemId : items) {
                double val = 0.0;
                if (keyVal.getValue().containsKey(itemId)) {
                    val = keyVal.getValue().get(itemId);
                }

                val /= 5.;
                vec.add(val);
            }

            dades.add(vec);
        }

        KMeans kmeans = new KMeans(k, dades, null);
        //kmeans.setVerbose();
        kmeans.resol();

        ArrayList<ArrayList<Integer>> clustersRaw = null;

        // Mentre existeixin clusters amb pocs usuaris
        // es torna executar l'algorisme imposant els nous baricentres
        boolean end = false;
        while (!end) {

            clustersRaw = kmeans.getClusters();
            int maxSize = 0;
            for (ArrayList<Integer> cluster : clustersRaw) {
                maxSize = Math.max(maxSize, cluster.size());
            }

            // S'esborren els pocs representatius
            int lim = Math.max(2, (int) Math.floor(maxSize * 0.1));

            ArrayList<Integer> clustersRepr = new ArrayList<Integer>();

            // Preservar els clusters representatius
            for (int i = 0; i < clustersRaw.size(); i++) {
                if (clustersRaw.get(i).size() >= lim) {
                    clustersRepr.add(i);
                }
            }

            if (clustersRepr.size() < clustersRaw.size()) {
                if (clustersRepr.size() > 1) {
                    ArrayList<ArrayList<Double>> baricentres = kmeans.getBaricentres();
                    ArrayList<ArrayList<Double>> newBaricentres = new ArrayList<ArrayList<Double>>();

                    for (Integer ci : clustersRepr) {
                        newBaricentres.add(new ArrayList<Double>(baricentres.get(ci)));
                    }

                    kmeans = new KMeans(newBaricentres.size(), dades, newBaricentres);
                    //kmeans.setVerbose();
                    kmeans.resol();
                } else {
                    if (--k == 0) {
                        end = true;
                    } else {
                        kmeans = new KMeans(k, dades, null);
                        //kmeans.setVerbose();
                        kmeans.resol();
                    }
                }
            } else {
                end = true;
            }
        }

        // Actualitza el valor k
        this.k = clustersRaw.size();

        for (int j = 0; j < clustersRaw.size(); j++) {
            ArrayList<Integer> clusterRaw = clustersRaw.get(j);
            ArrayList<Integer> cluster = new ArrayList<Integer>();

            for (Integer i : clusterRaw) {
                int usuariId = matToUsuari.get(i);
                cluster.add(usuariId);
                usuariCluster.put(usuariId, j);
            }

            clusters.add(cluster);
        }
    }

    /**
     * Afegeix una valoracio d'un usuari a un item
     *
     * @param idUsuari
     * @param idItem
     * @param valoracio numeric entre 0 i 5
     */
    public void afegirValoracio(int idUsuari, int idItem, double valoracio) {
        if (valoracions.containsKey(idUsuari)) {
            HashMap<Integer, Double> items = valoracions.get(idUsuari);

            // Si existeix es reemplaca
            items.put(idItem, valoracio);
        } else {
            HashMap<Integer, Double> items = new HashMap<Integer, Double>();
            items.put(idItem, valoracio);

            valoracions.put(idUsuari, items);
        }
    }

    /**
     * Obtnir els usuaris similars a l'usuari amb id = idUsuari
     *
     * @param idUsuari usuari del que es volen obtenir els similars
     * @return els usuaris similars a ell (inclos) si està classificat
     * @throws Exception s'ha de refer la classificacio
     */
    public ArrayList<Integer> getUsuarisSimilars(int idUsuari) throws Exception {
        if (!usuariCluster.containsKey(idUsuari)) {
            return new ArrayList<>();
        }

        ArrayList<Integer> usuarisCluster = clusters.get(usuariCluster.get(idUsuari));
        ArrayList<Integer> l = new ArrayList<>();
        for (Integer idU : usuarisCluster) {
            if (idU != idUsuari)
                l.add(idU);
        }
            
        return l;
    }

    public HashMap<Integer, Integer> getUsuariCluster() {
        return usuariCluster;
    }

    public int getK() {
        return k;
    }

    public double getTall() {
        return tall;
    }

    public ArrayList<ArrayList<Integer>> getClusters() {
        return clusters;
    }

    public HashMap<Integer, HashMap<Integer, Double>> getValoracions() {
        return valoracions;
    }

    /**
     * Afegir un cluster previament processat per l'algorisme
     *
     * @param cluster
     * @throws Exception
     */
    public void recuperarClassificacioUsuari(Integer idUsuari, Integer cluster) throws Exception {
        this.usuariCluster.put(idUsuari, cluster);

        while (clusters.size() <= cluster) {
            clusters.add(new ArrayList<Integer>());
        }

        clusters.get(cluster).add(idUsuari);
    }

    @Override
    public String toString() {
        String res = "k=" + k + "\n";
        res += "tall=" + tall + "\n";

        int i = 0;
        for (ArrayList<Integer> cluster : clusters) {
            res += "c" + i++ + "[ ";
            for (Integer idUsuari : cluster) {
                res += idUsuari + " ";
            }

            res += "]\n";
        }

        return res;
    }

    public void stubClassificacio(ArrayList<ArrayList<Integer>> clusters) {
        this.clusters = clusters;
        this.usuariCluster.clear();

        for (int i = 0; i < clusters.size(); i++) {
            for (Integer idUsuari : clusters.get(i)) {
                this.usuariCluster.put(idUsuari, i);
            }
        }
    }
}
