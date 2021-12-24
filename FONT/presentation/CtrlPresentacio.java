/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentation;

import domain.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;

/**
 *
 * @author yoqkn
 */
public class CtrlPresentacio {
    private static CtrlPresentacio singletonObject;
    private CtrlDomini ctrlDomini;
    private VistaLoginRegistre vistaLogin;
    private VistaGestioItems vistaGestioItems;
    private VistaGestioTipusItem vistaGestioTipusItem;
    //private static VistaGestioTipusAtribut vistaGestioTipusAtribut;
    private VistaGestioTipusAtribut vistaGestioTipusAtribut;

    public static CtrlPresentacio getInstance() throws Exception {
        if (singletonObject == null)
            singletonObject = new CtrlPresentacio();
        return singletonObject;
    }

    public CtrlPresentacio() throws Exception {
        ctrlDomini = CtrlDomini.getInstance();
    }

    public void inicialitzarPresentacio() throws Exception {
        ctrlDomini.carregarUsuaris();
         
        // Carregar dades de jsons
        ctrlDomini.carregar();
     
        vistaLoginRegistre();
    }
    
    public void vistaLoginRegistre()
    {
        vistaLogin = new VistaLoginRegistre(singletonObject);
        setupView(vistaLogin);
    }
    
    public void vistaPrincipal()
    {
        if (ctrlDomini.isUsuariActiuAdmin()) {
            VistaPrincipalAdmin vpa = new VistaPrincipalAdmin(singletonObject);
            setupView(vpa);
        }
        else {
            ctrlDomini.resetDirtyFlag();
            VistaPrincipalUsuari vpu = new VistaPrincipalUsuari(singletonObject);
            setupView(vpu);
        }
    }
    
    public void vistaGestioItems(String nomTipusItem) {
      vistaGestioItems = new VistaGestioItems(singletonObject, nomTipusItem);
      setupView(vistaGestioItems);
    }
    
    public void vistaGestioTipusItem() {
        vistaGestioTipusItem = new VistaGestioTipusItem(singletonObject);
        setupView(vistaGestioTipusItem);
    }

    public void vistaGestioTipusAtribut(String nomTipusItem) {
        vistaGestioTipusItem.setVisible(false);
        vistaGestioTipusAtribut = new VistaGestioTipusAtribut(singletonObject, nomTipusItem);
        setupView(vistaGestioTipusAtribut);
    }
    
    public void vistaGestioPerfil()
    {
        VistaGestioPerfil vgp = new VistaGestioPerfil(this);
        vgp.setVisible(true);
        setupView(vgp);
    }
    
    public void vistaGestioTipusAtributClose()
    {
        vistaGestioTipusAtribut.dispose();
        vistaGestioTipusItem.setVisible(true);
    }
    
    public int registarUsuari(String nom, String contrasenya, String confirmacioContrasenya) throws Exception {
        return ctrlDomini.registrarUsuari(nom, contrasenya, confirmacioContrasenya, false);
    }

    public int loginUsuari(String nom, String contrasenya) throws NoSuchAlgorithmException, InvalidKeySpecException, Exception {
        return ctrlDomini.loginUsuari(nom, contrasenya);
    }

    public int logoutUsuari() { return ctrlDomini.logoutUsuari(); }

    public int canviarContrasenya(String contrasenya1, String contrasenya2) throws Exception {
        return ctrlDomini.canviarContrasenya(contrasenya1, contrasenya2);
    }

    public int canviarNom(String nom) throws Exception {
        return ctrlDomini.canviarNom(nom);
    }

    public int eliminarUsuari(int id) {
        return ctrlDomini.eliminarUsuari(id);
    }

    public int getIdUsuariActiu() {
        return ctrlDomini.getIdUsuariActiu();
    }
    
    public String getNomUsuariActiu() {
        return ctrlDomini.getNomUsuariActiu();
    }

    public int crearTipusItem(String nomTipusItem) {
        return ctrlDomini.crearTipusItemWithId(nomTipusItem);
    }

    public int eliminarTipusItem(String nomTipusItem) {
        return ctrlDomini.eliminarTipusItem(nomTipusItem);
    }

    public int afegirTipusAtribut(String nomTipusItem, String nomTipusAtribut, String tipus) {
        return ctrlDomini.afegirTipusAtribut(nomTipusItem, nomTipusAtribut, tipus);
    }

    public int canviarNomTipusAtribut(String nomTipusItem, String nomTipusAtribut1, String nomTipusAtribut2) {
        return ctrlDomini.canviarNomTipusAtribut(nomTipusItem, nomTipusAtribut1, nomTipusAtribut2);
    }

    public int eliminarTipusAtribut(String nomTipusItem, String nomTipusAtribut) {
        return ctrlDomini.eliminarTipusAtribut(nomTipusItem, nomTipusAtribut);
    }

    public int crearItem(String id, String nomTipusItem) {
        try {
            int idItem = Integer.parseInt(id);
            return ctrlDomini.crearItem(idItem, nomTipusItem);
        }
        catch (NumberFormatException excepcion) {return -3;}
    }

    public int eliminarItem(String id) {
        try {
            int idItem = Integer.parseInt(id);
            return ctrlDomini.eliminarItem(idItem);
        }
        catch (NumberFormatException excepcion) {return -2;}
    }

    public int afegirAtribut(String id, String nomTipusAtribut, String valor) {
        try {
            int idItem = Integer.parseInt(id);
            return ctrlDomini.afegirAtribut(idItem, nomTipusAtribut, valor);
        }
        catch (NumberFormatException excepcion) {return -5;}
    }

    public int modificarAtribut(String id, String nomTipusAtribut, String valor) {
        try {
            int idItem = Integer.parseInt(id);
            return ctrlDomini.modificarAtribut(idItem, nomTipusAtribut, valor);
        }  
        catch (NumberFormatException excepcion) {return -4;}
    }
    
    public int eliminarAtribut(String id, String nomTipusAtribut) {
        try {
            int idItem = Integer.parseInt(id);
            return ctrlDomini.eliminarAtribut(idItem, nomTipusAtribut);
        }
        catch (NumberFormatException excepcion) {return -3;}
    }            
   

    public int afegirValoracio(String id, double puntuacio) {
        try {
            int idItem = Integer.parseInt(id);
            return ctrlDomini.afegirValoracio(idItem, ctrlDomini.getIdUsuariActiu(), puntuacio);
        }
        catch (NumberFormatException excepcion) {return -5;}
    }

    public int modificarValoracio(String id, double puntuacio) {
        
        try {
            int idItem = Integer.parseInt(id);
            return ctrlDomini.modificarValoracio(idItem, ctrlDomini.getIdUsuariActiu(), puntuacio);
        }
        catch (NumberFormatException excepcion) {return -5;}
    }
    
    public double getValoracio(String id)
    {
        try {
            int idItem = Integer.parseInt(id);
            return ctrlDomini.getValoracio(idItem, ctrlDomini.getIdUsuariActiu());
        }
        catch (NumberFormatException excepcion) {return -1;}
    }

    public int eliminarValoracio(String id) {
        try {
            int idItem = Integer.parseInt(id);
            return ctrlDomini.eliminarValoracio(idItem, ctrlDomini.getIdUsuariActiu());
        }
        catch (NumberFormatException excepcion) {return -2;}
    }

    public int classificaUsuaris()
    {
        return ctrlDomini.classificaUsuaris();
    }

    public String getValor(String id, String nomAtribut) {
        try {
            int idItem = Integer.parseInt(id);
            return ctrlDomini.getValor(idItem, nomAtribut);
        }
        catch (NumberFormatException excepcion) {return "";}
    }
    
    public String getTAtribut(String nomTipusItem, String nomTipusAtribut) {
        return ctrlDomini.getTAtribut(nomTipusItem, nomTipusAtribut);
    }
        
    public List<String> getNomTipusItem() {
        return  ctrlDomini.getNomTipusItems();
    }
    public Item getItem(int idItem) {
        return ctrlDomini.getItem(idItem);
    }
    
    public List<String> getNomsItems(String nomTipusItem) {
        return ctrlDomini.getNomsItems(nomTipusItem);
    }

    public Set<Item> getItemsValorats(int idUsuari, double puntuacioMin) {
        return ctrlDomini.getItemsValorats(idUsuari, puntuacioMin);
    }

    public List<String> getTipusAtributs(String nomTipusItem) {
        return ctrlDomini.getTipusAtributs(nomTipusItem);
    }
    public List<String> getNomsAtributsItem(String idItem) {
        return ctrlDomini.getNomsAtributsItem(Integer.parseInt(idItem));
    }
    
    public HashMap<String, HashMap<String, String>> getItemStrings(String nomTipusItem)
    {
        return ctrlDomini.getItemStrings(nomTipusItem);
    }
    
    public boolean teAtributDefinit(String id, String nomAtribut) 
    {
        try {
            int idItem = Integer.parseInt(id);
            return ctrlDomini.teAtributDefinit(idItem, nomAtribut);
        }
        catch (NumberFormatException excepcion) {return false;}
    }
    
    public int carregaItemsCSV(String nomTipus, String absFile)
    {
        try {
            return ctrlDomini.carregarItemsCSV(nomTipus, absFile);
        } 
        catch(Exception ex) {
            return -5;
        }
    }
    
    public List<Integer> getRecomanacions()
    {
        return ctrlDomini.getKRecomanacionsIds(ctrlDomini.getIdUsuariActiu(), 20);
    }
    
    public int carregarValoracionsCSV(String filename, int min, int max)
    {
        return ctrlDomini.carregarValoracionsCSV(min, max, filename);
    }
    
    public int guardarValoracionsCSV(String filename)
    {
        return ctrlDomini.guardarValoracionsCSV(filename);
    }
    
    public int avaluarRecomanacions(String filename)
    {
        return ctrlDomini.avaluarRecomanacions(filename);
    }
    
    public int carregarRecomanacions(String filename)
    {
        return ctrlDomini.carregarRecomanacions(filename);
    }
    
    public int guardarRecomanacions(String filename)
    {
        return ctrlDomini.guardarRecomanacions(filename);
    }
    
    public HashMap<String, Integer> getUsuaris()
    {
        return ctrlDomini.getUsuaris();
    }
    
    private void setupView(JFrame view)
    {
        view.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ctrlDomini.guardarUsuaris();
                ctrlDomini.guardar();
            }
        });
        
        view.setVisible(true);
    }
}
