package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import data.CtrlDades;
import domain.algorismes.Hibrid;

/**
 * Controlador de les recomanacions
 * 
 * @author Roberto Navarro Morales
 */
public class CtrlRecomanacio {

    /** Nomes es permet una unica instancia global */
    private static CtrlRecomanacio singletonObject;
    
    // Controladors relacionats
    
    /** Controlador de valoracions */
    private CtrlValoracio ctrlValoracio;
    
    /** Controlador dels usuaris */
    private CtrlUsuari ctrlUsuari;
    
    /** Controlador de dades */
    private CtrlDades ctrlDades;
    
    /** Controlador dels items */
    private CtrlItem ctrlItem;

    /** Objecte encarregat de classificar els usuaris */
    private Classificacio classificacio;

    /** Recomanacions pels usuaris (el seu id es la clau del map */
    private HashMap<Integer, ArrayList<Recomanacio>> recomanacions;

    /**
     * Obtenir la instancia unica global del patro singleton
     * @return instancia unica
     */
    public static CtrlRecomanacio getInstance() {
        if (singletonObject == null) {
            singletonObject = new CtrlRecomanacio();
        }
        return singletonObject;
    }

    /**
     *  Constructor per defecte
     */
    public CtrlRecomanacio() {
        this.classificacio = null;
        this.ctrlValoracio = null;
        this.ctrlDades = null;
        this.ctrlUsuari = null;
        this.ctrlItem = null;
        this.recomanacions = new HashMap<>();
    }

    public void setCtrlValoracio(CtrlValoracio ctrlValoracio) {
        this.ctrlValoracio = ctrlValoracio;
    }

    public void setCtrlUsuari(CtrlUsuari ctrlUsuari) {
        this.ctrlUsuari = ctrlUsuari;
    }

    public void setCtrlDades(CtrlDades ctrlDades) {
        this.ctrlDades = ctrlDades;
    }

    public void setCtrlItem(CtrlItem ctrlItem) {
        this.ctrlItem = ctrlItem;
    }

    /**
     * Classifica els usuaris
     *
     * @return 0 correcte, -1 s'ha produit un error
     */
    public int classifica() {
        HashMap<Integer, HashMap<Integer, Valoracio>> valoracions = ctrlValoracio.getValoracionsPerUsuari();
        
        // Nova classificiacio, k recomanada 50
        // Nomes es consideraran la meitat superior dels items ordenats 
        // De manera descendent per quantitat de valoracions
        this.classificacio = new Classificacio(0.5, 50);

        // Afegir les valoracions disponibles
        for (Map.Entry<Integer, HashMap<Integer, Valoracio>> kv : valoracions.entrySet()) {
            for (Map.Entry<Integer, Valoracio> ikv : kv.getValue().entrySet()) {
                this.classificacio.afegirValoracio(kv.getKey(), ikv.getKey(), ikv.getValue().getPuntuacio());
            }
        }
        
        // Classificacio
        try {
            this.classificacio.classifica();
        } catch (Exception ex) {
            return -1;
        }

        return 0;
    }

    /**
     * S'executa l'algorisme de recomanacio per l'usuari donat
     *
     * @param idUsuari
     * @return 0 si ok -1 si hi ha hagut algun error a la recomanacio -2 si
     * l'usuari no estava classificat
     */
    public int recomana(int idUsuari) {
        // Esborra les recomanacions anteriors
        if (recomanacions.containsKey(idUsuari))
            recomanacions.remove(idUsuari);
        
        Hibrid hibrid = null;
        try {
            // Si no hi ha cap usuari similar provar a reclassificar
            //if (classificacio == null || classificacio.getUsuarisSimilars(idUsuari).isEmpty())
            classifica();

            // Obtenir els items valorats per part d'un usuari amb una nota
            // superior a un 3.5
            HashMap<String, Set<Item>> mAllItems = new HashMap<>();
            Set<Item> itemsValorats = ctrlValoracio.getItemsValorats(idUsuari, 3.5);

            for (String tipusItem : ctrlItem.getTipusItems().keySet()) {
                Set<Item> allItems = ctrlItem.getItems(tipusItem);
                mAllItems.put(tipusItem, allItems);
            }

            // Resol l'algorisme hibrid
            hibrid = new Hibrid(
                    idUsuari, 
                    ctrlValoracio.getItemsValorats(idUsuari, 0),
                    itemsValorats, 
                    mAllItems, 
                    ctrlItem.getTipusItems().keySet(), 
                    this.classificacio
            );
            hibrid.resol();

        } catch (Exception ex) {
            return -1;
        }

        // Proces de les recomanacions obtingudes
        HashMap<Integer, Double> pred = hibrid.getItemsRating();
        ArrayList<Item> items = hibrid.getItemSort();

        ArrayList<Recomanacio> recArray = new ArrayList<>();
        for (Item item : items) {
            Recomanacio rec = new Recomanacio(idUsuari, item.getId(), pred.get(item.getId()));
            recArray.add(rec);
        }

        this.recomanacions.put(idUsuari, recArray);

        return 0;
    }

    /**
     * Avaluacio d'un conjunt de recomanacions
     * @param filenameUnknown fitxer amb les valoracions esperades per a comparar
     * @return 0 si ha estat correcte -1 altrament
     */
    public int avaluacioRecomanacions(String filenameUnknown) {
        try {
            Avaluacio aval = new Avaluacio(ctrlDades.carregarValoracionsCSV(filenameUnknown));

            ArrayList<String[]> pred = new ArrayList<>();
            ArrayList<String[]> avaluacions = new ArrayList<>();

            HashMap<String, Set<Item>> mAllItems = new HashMap<>();
            for (String tipusItem : ctrlItem.getTipusItems().keySet()) {
                Set<Item> allItems = ctrlItem.getItems(tipusItem);
                mAllItems.put(tipusItem, allItems);
            }

            for (Integer uid : ctrlUsuari.getUsuaris().keySet()) {
                Set<Item> itemsValorats = ctrlValoracio.getItemsValorats(uid, 3.5);

                Hibrid hibrid = new Hibrid(
                        uid, 
                        ctrlValoracio.getItemsValorats(uid, 0),
                        itemsValorats, 
                        mAllItems, 
                        ctrlItem.getTipusItems().keySet(), 
                        this.classificacio
                );
                hibrid.resol();

                ArrayList<Item> ord = hibrid.getItemSort();
                HashMap<Integer, Double> predMap = hibrid.getItemsRating();

                for (Item item : ord) {
                    String[] s = new String[3];
                    s[0] = Integer.toString(uid);
                    s[1] = Integer.toString(item.getId());
                    s[2] = Double.toString(predMap.get(item.getId()));

                    pred.add(s);
                }

                double res = aval.avalua(uid, pred);
                String[] s = new String[2];
                s[0] = Integer.toString(uid);
                s[1] = Double.toString(res);

                avaluacions.add(s);
            }

            ctrlDades.saveAvaluacio(avaluacions);
            //ctrlDades.saveValoracionsCSV(pred, "prediccions.csv");

        } catch (Exception ex) {
            return -1;
        }

        return 0;
    }

    /**
     * Carregar d'un fitxer JSON el conjunt de recomanacions i la classificacio
     */
    public void carregar() {
        carregarRecomanacions();
        carregarClassificacio();
    }

    /**
     * Guardar a un fitxer JSON el conjunt de recomanacions i la classificacio
     */
    public void guardar() {
        guardarRecomanacions();
        guardarClassificacio();
    }
    
    /**
     * Carregar les recomanacions d'un usuari concret
     * @param idUsuari identificador de l'usuari
     * @param filename nom del fitxer (.json)
     * @return 0 si tot OK, -1 altrament
     */
    public int carregarRecomanacions(int idUsuari, String filename)
    {
        try {
            List<Recomanacio> recomList = ctrlDades.loadGenericFullPath(Recomanacio.class, filename, "recomanacions");
            if (this.recomanacions.containsKey(idUsuari))
                this.recomanacions.remove(idUsuari);
            
            this.recomanacions.put(idUsuari, new ArrayList<>());
            
            for (Recomanacio r : recomList) {
                if (r.getIdUsuari() != idUsuari)
                    continue;
                this.recomanacions.get(r.getIdUsuari()).add(r);
            }
        } catch (Exception ex) {
            return -1;
        }

        return 0;
    }
    
    /**
     * Guardar les recomanacions d'un usuari concret
     * @param idUsuari identificador de l'usuari
     * @param filename nom del fitxer (.json)
     * @return 0 si tot OK, -1 altrament
     */
    public int guardarRecomanacions(int idUsuari, String filename)
    {
        ArrayList<Recomanacio> recList = new ArrayList<>();
        if (this.recomanacions.containsKey(idUsuari)) {
            for (Recomanacio rec : this.recomanacions.get(idUsuari)) 
                recList.add(rec);
        }

        try {
            ctrlDades.saveGenericFullPath(recList, Recomanacio.class, filename, "recomanacions");
        } catch (Exception ex) {
            return -1;
        }

        return 0;
    }

    private int carregarClassificacio() {
        try {
            List<Classificacio> clList = ctrlDades.loadGeneric(Classificacio.class, "classificacio.json", "classificacio");
            this.classificacio = clList.get(0);

            HashMap<Integer, HashMap<Integer, Valoracio>> valoracions = ctrlValoracio.getValoracionsPerUsuari();
            for (Map.Entry<Integer, HashMap<Integer, Valoracio>> kv : valoracions.entrySet()) {
                for (Map.Entry<Integer, Valoracio> ikv : kv.getValue().entrySet()) {
                    this.classificacio.afegirValoracio(kv.getKey(), ikv.getKey(), ikv.getValue().getPuntuacio());
                }
            }
        } catch (Exception ex) {
            return -1;
        }

        return 0;
    }

    private int guardarClassificacio() {
        try {
            List<Classificacio> cl = new ArrayList<>();
            cl.add(this.classificacio);

            ctrlDades.saveGeneric(cl, Classificacio.class, "classificacio.json", "classificacio");
        } catch (Exception ex) {
            return -1;
        }

        return 0;
    }

    private int carregarRecomanacions() {
        try {
            List<Recomanacio> recomList = ctrlDades.loadGeneric(Recomanacio.class, "recomanacions.json", "recomanacions");
            for (Recomanacio r : recomList) {
                int idUsuari = r.getIdUsuari();
                if (!this.recomanacions.containsKey(idUsuari)) {
                    this.recomanacions.put(idUsuari, new ArrayList<Recomanacio>());
                }

                this.recomanacions.get(r.getIdUsuari()).add(r);
            }
        } catch (Exception ex) {
            return -1;
        }

        return 0;
    }

    private int guardarRecomanacions() {
        ArrayList<Recomanacio> recomanacions = new ArrayList<>();
        for (ArrayList<Recomanacio> rec : this.recomanacions.values()) {
            recomanacions.addAll(rec);
        }

        try {
            ctrlDades.saveGeneric(recomanacions, Recomanacio.class, "recomanacions.json", "recomanacions");
        } catch (Exception ex) {
            return -1;
        }

        return 0;
    }

    public ArrayList<ArrayList<Integer>> getGrups() {
        return this.classificacio.getClusters();
    }

    /**
     * Obte un conjunt de com a molt k recomanacions
     * @param idUsuari identificador de l'usuari
     * @param k el maxim de recomanacions
     * @param exec si s'ha de forcar a rellancar els algorismes
     * @return el conjunt de recomanacions
     */
    public List<Recomanacio> getRecomanacions(int idUsuari, int k, boolean exec) {
        if (!recomanacions.containsKey(idUsuari) || exec)
            recomana(idUsuari);

        if (!recomanacions.containsKey(idUsuari))
            return new ArrayList<>();

        ArrayList<Recomanacio> rec = recomanacions.get(idUsuari);
        return rec.subList(0, Math.min(k, rec.size()));
    }

    public void logClassificacio() {
        System.out.println(classificacio.toString());
    }
}
