package domain;
import data.CSVUtil;
import data.CtrlDades;
import domain.TipusItem.TAtribut;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 * @author Jia Long Ji Qiu
 */

public class CtrlDomini {
    private static CtrlDomini singletonObject;
    private static CtrlDades ctrlDades;
    private static CtrlUsuari ctrlUsuari;
    private static CtrlItem ctrlItem;
    private static CtrlValoracio ctrlValoracio;
    private static CtrlRecomanacio ctrlRecomanacio;
    
    private boolean dirtyFlag;

    public static CtrlDomini getInstance() throws Exception {
        if (singletonObject == null)
            singletonObject = new CtrlDomini();
        return singletonObject;
    }

    private CtrlDomini() throws Exception {
        ctrlDades = CtrlDades.getInstance();
        ctrlItem = CtrlItem.getInstance();
        ctrlUsuari = CtrlUsuari.getInstance();
        ctrlValoracio = CtrlValoracio.getInstance();
        ctrlRecomanacio = CtrlRecomanacio.getInstance();

        ctrlItem.setCtrlDades(ctrlDades);
        ctrlItem.setCtrlValoracio(ctrlValoracio);
        ctrlUsuari.setCtrlDades(ctrlDades);
        ctrlUsuari.setCtrlValoracio(ctrlValoracio);
        ctrlValoracio.setCtrlItem(ctrlItem);
        ctrlValoracio.setCtrlUsuari(ctrlUsuari);
        ctrlRecomanacio.setCtrlValoracio(ctrlValoracio);
        ctrlRecomanacio.setCtrlDades(ctrlDades);
        ctrlRecomanacio.setCtrlUsuari(ctrlUsuari);
        ctrlRecomanacio.setCtrlItem(ctrlItem);
        
        dirtyFlag = true;
    }

    public int registrarUsuari(String nom, String contrasenya1, String contrasenya2, boolean admin) throws Exception {
        return ctrlUsuari.registrarUsuari(nom, contrasenya1, contrasenya2, admin);
    }

    public int registrarUsuariId(int id, String nom, String contrasenya1, String contrasenya2, boolean admin) throws Exception {
        return ctrlUsuari.registrarUsuariId(id, nom, contrasenya1, contrasenya2, admin);
    }

    public int loginUsuari(String nom, String contrasenya) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return ctrlUsuari.loginUsuari(nom, contrasenya);
    }

    public int logoutUsuari() {
        return ctrlUsuari.logoutUsuari();
    }

    public int canviarContrasenya(String contrasenya1, String contrasenya2) throws Exception {
        return ctrlUsuari.canviarContrasenya(contrasenya1, contrasenya2);
    }

    public int canviarNom(String nom) throws Exception {
        return ctrlUsuari.canviarNom(nom);
    }

    public int eliminarUsuari(int id) {
        return ctrlUsuari.eliminarUsuari(id);
    }

    public int getIdUsuariActiu() {
        return ctrlUsuari.getIdUsuariActiu();
    }
    
    public String getNomUsuariActiu() {
        if (ctrlUsuari.getIdUsuariActiu() == -1) return "";
        return ctrlUsuari.getUsuaris().get(ctrlUsuari.getIdUsuariActiu()).getNom();
    }   
    
    public boolean isUsuariActiuAdmin()
    {
        int idUsuariActiu = ctrlUsuari.getIdUsuariActiu();
        return ctrlUsuari.getUsuaris().get(idUsuariActiu).isAdmin();
    }

    public int crearTipusItem(String nomTipusItem) {
        return ctrlItem.crearTipusItem(nomTipusItem);
    }
    
    public int crearTipusItemWithId(String nomTipusItem) {
        return ctrlItem.crearTipusItemWithId(nomTipusItem);
    }

    public int eliminarTipusItem(String nomTipusItem) { return ctrlItem.eliminarTipusItem(nomTipusItem);}

    public int carregarItemsCSV(String nomTipusItem, String filename) throws Exception {
        return ctrlItem.carregarItemsCSV(nomTipusItem, filename);
    }
    
    public int afegirTipusAtribut(String nomTipusItem, String nomTipusAtribut, String tipus) {
        if (tipus.equals("Boolea")) return ctrlItem.afegirTipusAtribut(nomTipusItem, nomTipusAtribut, TipusItem.TAtribut.BOOLEA);
        if (tipus.equals("Numeric")) return ctrlItem.afegirTipusAtribut(nomTipusItem, nomTipusAtribut, TipusItem.TAtribut.NUMERIC);
        if (tipus.equals("Descriptiu")) return ctrlItem.afegirTipusAtribut(nomTipusItem, nomTipusAtribut, TipusItem.TAtribut.DESCRIPTIU);
        return ctrlItem.afegirTipusAtribut(nomTipusItem, nomTipusAtribut, TipusItem.TAtribut.CATEGORIC);
    }

    public int canviarNomTipusAtribut(String nomTipusItem, String nomTipusAtribut1, String nomTipusAtribut2) {
        return ctrlItem.canviarNomTipusAtribut(nomTipusItem, nomTipusAtribut1, nomTipusAtribut2);
    }

    public int eliminarTipusAtribut(String nomTipusItem, String nomTipusAtribut) {
        return ctrlItem.eliminarTipusAtribut(nomTipusItem, nomTipusAtribut);
    }

    public int crearItem(int id, String nomTipusItem) {
        return ctrlItem.crearItem(id, nomTipusItem);
    }

    public int eliminarItem(int id) {
        return ctrlItem.eliminarItem(id);
    }

    private String TipusValor(String valor) {
        String tipus;
        if ("true".equals(valor) || "false".equals(valor)) {
            return "Boolea";
        }
        try {
            Double.parseDouble(valor);
            return "Numeric";
        }
        catch (NumberFormatException e) {}
        try {
            Integer.parseInt(valor);
            return "Numeric";
        }
        catch (NumberFormatException e) {}
        if (CSVUtil.parseLine(valor, ';').size() > 1) return "Categoric";
        return "Descriptiu";
    }

    public int afegirAtribut(int id, String nomTipusAtribut, String valor) {
        String tAtribut = TipusValor(valor);
        if (tAtribut == "Numeric") return ctrlItem.afegirAtribut(id, nomTipusAtribut, new AtributNumeric(Double.parseDouble(valor)));
        else if (tAtribut == "Boolea") return ctrlItem.afegirAtribut(id, nomTipusAtribut, new AtributBoolea(Boolean.parseBoolean(valor)));
        else if (tAtribut == "Descriptiu") return ctrlItem.afegirAtribut(id, nomTipusAtribut, new AtributDescriptiu(valor));
        else {
            AtributCategoric aCategoric = new AtributCategoric();
            String[] splitted = valor.split(";");
            for (String s : splitted) aCategoric.afegirValor(s);
            return ctrlItem.afegirAtribut(id, nomTipusAtribut,  aCategoric);
        }
    }


    public int modificarAtribut(int id, String nomTipusAtribut, String valor) {
        String tAtribut = TipusValor(valor);
        if (tAtribut == "Numeric") return ctrlItem.modificarAtribut(id, nomTipusAtribut, new AtributNumeric(Double.parseDouble(valor)));
        else if (tAtribut == "Boolea") return ctrlItem.modificarAtribut(id, nomTipusAtribut, new AtributBoolea(Boolean.parseBoolean(valor)));
        else if (tAtribut == "Descriptiu") return ctrlItem.modificarAtribut(id, nomTipusAtribut, new AtributDescriptiu(valor));
        else {
            AtributCategoric aCategoric = new AtributCategoric();
            String[] splitted = valor.split(";");
            for (String s : splitted) aCategoric.afegirValor(s);
            return ctrlItem.modificarAtribut(id, nomTipusAtribut,  aCategoric);
        }
    }

    public int eliminarAtribut(int id, String nomTipusAtribut) {
        return ctrlItem.eliminarAtribut(id, nomTipusAtribut);
    }

    public int afegirValoracio(int idItem, int idUsuari, double puntuacio) {
        this.dirtyFlag = true;
        return ctrlValoracio.afegirValoracio(idItem, idUsuari, puntuacio);
    }

    public int modificarValoracio(int idItem, int idUsuari, double puntuacio) {
        this.dirtyFlag = true;
        return ctrlValoracio.modificarValoracio(idItem, idUsuari, puntuacio);
    }
    
    public double getValoracio(int idItem, int idUsuari) {
        return ctrlValoracio.getValoracio(idUsuari, idItem);
    }

    public int eliminarValoracio(int idItem, int idUsuari) {
        this.dirtyFlag = true;
        return ctrlValoracio.eliminarValoracio(idItem, idUsuari);
    }

    public int eliminarValoracionsItem(int idItem) {
        return ctrlValoracio.eliminarValoracionsItem(idItem);
    }

    public int eliminarValoracionsUsuari(int idUsuari) {
        return ctrlValoracio.eliminarValoracionsUsuari(idUsuari);
    }
    
    public int classificaUsuaris() 
    {
    	return ctrlRecomanacio.classifica();
    }
    
    public ArrayList<ArrayList<Usuari>> getClassificacio()
    {
    	ArrayList<ArrayList<Usuari>> classificacio = new ArrayList<ArrayList<Usuari>>();
    	
    	ArrayList<ArrayList<Integer>> grups = ctrlRecomanacio.getGrups();
    	for (ArrayList<Integer> grup : grups) {
    		ArrayList<Usuari> usuaris = new ArrayList<Usuari>();
    		
    		for (Integer idUsuari : grup) 
    			usuaris.add(ctrlUsuari.getUsuaris().get(idUsuari));
    		
    		classificacio.add(usuaris);
    	}
    	
    	return classificacio;
    }
    
    public List<Recomanacio> getKRecomanacions(int idUsuari, int k)
    {
        this.dirtyFlag = false;
    	return ctrlRecomanacio.getRecomanacions(idUsuari, k, dirtyFlag);
    }
    
    public List<Integer> getKRecomanacionsIds(int idUsuari, int k) {
        List<Integer> ids = new ArrayList<>();
        List<Recomanacio> recomanacions = ctrlRecomanacio.getRecomanacions(idUsuari, k, dirtyFlag);
        
        for (Recomanacio rec : recomanacions)
            ids.add(rec.getIdItem());
        
        this.dirtyFlag = false;
        return ids;
    }
    
    public void resetDirtyFlag()
    {
        this.dirtyFlag = true;
    } 
    
    /**
     * Avaluacio de les recomanacions a partir d'un fitxer de valoracions conegudes
     * @param filenameUnknown
     * @return 0 si ok -1 si no ok
     */
    public int avaluarRecomanacions(String filenameUnknown)
    {
    	return ctrlRecomanacio.avaluacioRecomanacions(filenameUnknown);
    }

    /**
     * Carregar un dataset de valoracions d'un csv
     * @param min valor minim possible de les valoracions
     * @param max valor maxim possible de les valoracions
     * @param filename arxiu d'on es llegira
     * @return -1 si hi ha algun item que no existeix, -2 amb error desconegut, 0 altrament
     */
    public int carregarValoracionsCSV(double min, double max, String filename) {
        try {
            ArrayList<String[]> valoracions = ctrlDades.carregarValoracionsCSV(filename);
            ArrayList<Integer> usuarisNous = new ArrayList<>();
            for (String[] fila : valoracions) {
                int userId = parseInt(fila[0]);
                if (!ctrlUsuari.existsUsuari(userId)) {
                    ctrlUsuari.registrarUsuariId(userId, "dummy_" + userId, "dummy", "dummy", false);
                    usuarisNous.add(userId);
                }
                int itemId = parseInt(fila[1]);
                if (!ctrlItem.existsItem(itemId)) {
                    for (Integer idNou : usuarisNous) ctrlUsuari.eliminarUsuari(idNou);
                    return -1;
                }
                // Normalizar a rango 0..5
                double rating = (parseDouble(fila[2])-min)/(max - min) * 5;
                int retVal = ctrlValoracio.afegirValoracio(itemId, userId, rating);
                if (retVal < 0) return retVal;
            }
        }
        catch (Exception ex) {
            return -2;
        }
        return 0;
    }
    
    public int guardarValoracionsCSV(String filename)
    {
        ArrayList<String[]> res = new ArrayList<>();
        for (Usuari u : ctrlUsuari.getUsuaris().values()) {
            for (Item i : ctrlItem.getItems().values()) {
                double val = ctrlValoracio.getValoracio(u.getId(), i.getId());
                if (val < 0)
                    continue;
                
                String[] s = new String[3];
                s[0] = Integer.toString(u.getId());
                s[1] = Integer.toString(i.getId());
                s[2] = Double.toString(val);
                
                res.add(s);
            }
        }
        
        try {
            ctrlDades.saveValoracionsCSV(res, filename);
        }
        catch (Exception ex) {
            return -1;
        }
        
        return 0;
    }

    public Item getItem(Integer idItem) {
        return ctrlItem.getItem(idItem);
    }
    
    public String getTAtribut(String nomTipusItem, String nomTipusAtribut) {
        TAtribut Ta= ctrlItem.getTipusItems().get(nomTipusItem).getTAtribut(nomTipusAtribut);
        if (Ta.equals(TAtribut.BOOLEA)) return "Boolea";
        if (Ta.equals(TAtribut.CATEGORIC)) return "Categoric";
        if (Ta.equals(TAtribut.DESCRIPTIU)) return "Descriptiu";
        return "Numeric";        
    }
    
    public String getValor(Integer idItem, String nomTipusAtribut) {
        return ctrlItem.getItem(idItem).getAtribut(nomTipusAtribut).getValor().toString();
    }
    
    public List<String> getNomsItems(String nomTipusItem) {
         List<String> nomItems = new ArrayList<String>();
        HashMap<Integer, Item>  items = ctrlItem.getItems();
            for (Integer idItem : items.keySet()) {
                Item item = items.get(idItem);
                if (item.getTipusItem().getNom().equals(nomTipusItem)) nomItems.add(Integer.toString(item.getId()));
            }
            return nomItems;
    }

    public List<String> getNomTipusItems() {
        List<String> nomTipusItem = new ArrayList<>();
        HashMap<String, TipusItem>  tipusItems = ctrlItem.getTipusItems();
        for (String nomTipusI: tipusItems.keySet()) {
            nomTipusItem.add(nomTipusI);
        }
        return nomTipusItem;
    }
    
    public Set<Item> getItems(String nomTipusItem) {
        return ctrlItem.getItems(nomTipusItem);
    }

    public Set<Item> getItemsValorats(int idUsuari, double puntuacioMin) {
        return ctrlValoracio.getItemsValorats(idUsuari, puntuacioMin);
    }
    
    public List<String> getTipusAtributs(String nomTipusItem) {
        return ctrlItem.getTipusAtributs(nomTipusItem);
    }
    
    public List<String> getNomsAtributsItem(int idItem) {
        List<String> nomAtributs = new ArrayList<>();
        HashMap<String, Atribut> atributs = ctrlItem.getItem(idItem).getAtributs();
        for (String nomAtribut : atributs.keySet()) {
            nomAtributs.add(nomAtribut);
        }
        return nomAtributs;
    }
    
    public Item getItem(int idItem) {
        return ctrlItem.getItem(idItem);
    }
    
    public HashMap<String, Integer> getUsuaris()
    {
        HashMap<String, Integer> users = new HashMap<>();
        for (Usuari u : ctrlUsuari.getUsuaris().values())
            users.put(u.getNom(), u.getId());
        
        return users;
    }
    
    public HashMap<String, HashMap<String, String>> getItemStrings(String nomTipusItem)
    {
        return ctrlItem.getItemStrings(nomTipusItem);
    }
    
    public boolean teAtributDefinit(int idItem, String nomAtribut)
    {
        return ctrlItem.teAtributDefinit(idItem, nomAtribut);
    }
    
    public void carregar()
    {
    	//ctrlRecomanacio.carregar();
        ctrlItem.carregar();
        ctrlValoracio.carregar();
    }
    
    public void guardar()
    {
        ctrlItem.guardar();
        ctrlValoracio.guardar();
    	//ctrlRecomanacio.guardar();
    }
    
    public int carregarUsuaris() {
        try {
            ctrlUsuari.carregarUsuaris();
        }
        catch (Exception ex) {
            return -1;
        }
        
        return 0;
    }
    
    public int guardarUsuaris() {
        try {
            ctrlUsuari.guardarUsuaris();
        }
        catch (Exception ex) {
            return -1;
        }
        
        return 0;
    }
    
    public int carregarRecomanacions(String filename)
    {
        return ctrlRecomanacio.carregarRecomanacions(ctrlUsuari.getIdUsuariActiu(), filename);
    }
    
    public int guardarRecomanacions(String filename)
    {
        return ctrlRecomanacio.guardarRecomanacions(ctrlUsuari.getIdUsuariActiu(), filename);
    }
    
    public boolean existsUsuari(int id) 
    { 
    	return ctrlUsuari.existsUsuari(id); 
    }
}
