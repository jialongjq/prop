package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * Classe per a avaluar la qualitat de les prediccions de l'algorisme
 *
 * @author Roberto Navarro Morales
 *
 */
public class Avaluacio {

    /**
     * Dades esperades de la prediccio en format: idUsuari,idItem,valoracio
     */
    private ArrayList<String[]> trueRawData;

    /**
     * Posicio (Value) que ocupa un item (Key: idItem) a les valoracions
     * esperades per un usuari determinat (ordenades)
     */
    private HashMap<Integer, Integer> trueItemPos;

    /**
     * Valoracio real d'un item (Key: itemId) per part d'un usuari concret
     */
    private HashMap<Integer, Double> trueVal;

    /**
     * Possició en la que es recomanaria un item determinat
     */
    private HashMap<Integer, Integer> predItemPos;

    /**
     * Constructor
     *
     * @param trueData prediccions esperades sense tractar han de seguir el
     * format: idUsuari, idItem, valoracio. Per tant, cada array de String ha de
     * tenir mida 3
     */
    public Avaluacio(ArrayList<String[]> trueData) {
        this.trueRawData = new ArrayList<String[]>(trueData);
        this.trueItemPos = new HashMap<Integer, Integer>();
        this.trueVal = new HashMap<Integer, Double>();
        this.predItemPos = new HashMap<Integer, Integer>();
    }

    /**
     * Retorna el ndcg d'un conjunt de recomanacions per un usuari
     *
     * @param idUsuari usuari que es vol avaluar
     * @param recomanacions les recomanacions que es volen avaluar
     * @return ndcg
     */
    public double avalua(int idUsuari, ArrayList<String[]> recomanacions) {
        this.trueItemPos.clear();
        this.trueVal.clear();
        this.predItemPos.clear();

        // Processar l'ordre de les dades reals
        processarOrdre(idUsuari, this.trueRawData, this.trueItemPos, null);

        // Processar l'ordre de les dades predites
        processarOrdre(idUsuari, recomanacions, this.predItemPos, this.trueItemPos.keySet());

        // Extreure les valoracions reals
        extreureValoracions(idUsuari, this.trueRawData, this.trueVal);

        // Calcular el DCG i el IDCG
        double dcg = 0;
        double idcg = 0;

        for (Map.Entry<Integer, Integer> predPos : predItemPos.entrySet()) {
            int idItem = predPos.getKey();
            int pred = predPos.getValue();

            if (!trueVal.containsKey(idItem)) {
                continue;
            }

            int real = trueItemPos.get(idItem);

            // Valoracio real de l'item
            double realVal = trueVal.get(idItem);

            dcg += (Math.pow(2.0, realVal) - 1.0) / log2(pred + 1);
            idcg += (Math.pow(2.0, realVal) - 1.0) / log2(real + 1);
        }

        if (dcg == 0 || idcg == 0) {
            return 0;
        }

        return Math.min(dcg, idcg) / Math.max(dcg, idcg);
    }

    /**
     * Determina l'ordre que ocupa un cert item si les prediccions per un usuari
     * estiguessin ordenades
     *
     * @param idUsuari l'usuari del que es volen saber els ordres
     * @param data les prediccions sense tractar (poden ser les reals)
     * @param ordreM el mapa on es guardarà l'ordre
     * @param acceptedItems el conjunt dels items que es poden tractar
     */
    private static void processarOrdre(int idUsuari, ArrayList<String[]> data, HashMap<Integer, Integer> ordreM, Set<Integer> acceptedItems) {
        ArrayList<String[]> res = new ArrayList<String[]>();
        for (String[] d : data) {
            int idItem = Integer.parseInt(d[1]);
            if (Integer.parseInt(d[0]) == idUsuari) {
                if (acceptedItems == null || acceptedItems.contains(idItem)) {
                    res.add(d);
                }
            }
        }

        Collections.sort(res, new Comparator<String[]>() {
            public int compare(String[] x, String[] y) {
                double xd = Double.parseDouble(x[2]);
                double yd = Double.parseDouble(y[2]);

                if (xd > yd) {
                    return 1;
                }
                if (xd == yd) {
                    return Integer.parseInt(x[1]) - Integer.parseInt(y[1]);
                }
                return -1;
            }
        });

        double lastVal = -1;
        int ordre = 0;
        for (int i = res.size() - 1; i >= 0; i--) {
            String[] d = res.get(i);
            double val = Double.parseDouble(d[2]);
            int idItem = Integer.parseInt(d[1]);

            if (val != lastVal) {
                lastVal = val;
                ordre++;
            }

            ordreM.put(idItem, ordre);
        }
    }

    /**
     * A partir de les dades sense tractar s'afegeixen les valoracions al map
     * rebut
     *
     * @param idUsuari usuari del que es volen recollir les valoracions
     * @param data dades sense tractar
     * @param valoracionsM mapa on es guardaran les valoracions
     */
    private static void extreureValoracions(int idUsuari, ArrayList<String[]> data, HashMap<Integer, Double> valoracionsM) {
        for (String[] d : data) {
            if (Integer.parseInt(d[0]) == idUsuari) {
                int itemId = Integer.parseInt(d[1]);
                double val = Double.parseDouble(d[2]);

                valoracionsM.put(itemId, val);
            }
        }
    }

    public HashMap<Integer, Integer> getOrdreEsperat() {
        return trueItemPos;
    }

    public HashMap<Integer, Integer> getOrdrePredit() {
        return predItemPos;
    }

    /**
     * Logarisme base 2 de x
     *
     * @param x
     * @return logarisme
     */
    private static double log2(int x) {
        return (Math.log(x) / Math.log(2));
    }
}
