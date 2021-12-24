package domain.drivers;

import java.util.ArrayList;
import java.util.Arrays;

import domain.CtrlDomini;
import domain.CtrlItem;
import domain.CtrlUsuari;
import domain.CtrlValoracio;
import domain.Recomanacio;
import domain.TipusItem.TAtribut;
import domain.Usuari;

public class DriverCtrlDomini {

    private static CtrlDomini ctrlDomini;

    public static void testUsuaris() {
        System.out.println("Registre usuari");
        try {
            ctrlDomini.registrarUsuari("test", "contrasenya", "contrasenya", true);
            ctrlDomini.registrarUsuariId(1, "test2", "contrasenya", "contrasenya", true);
            CtrlUsuari.getInstance().printUsuaris();
        } catch (Exception ex) {
            System.out.println("S'ha produit un error registrant l'usuari");
            return;
        }

        try {
            System.out.println("\r\nLogin");
            System.out.println(ctrlDomini.loginUsuari("test1", "contrasenya") == 0 ? "Login test1 OK" : "Login test1 KO");
            System.out.println(ctrlDomini.loginUsuari("test", "contrasenya") == 0 ? "Login test OK" : "Login test KO");

            System.out.println("Logout test and relogin");
            ctrlDomini.logoutUsuari();
            ctrlDomini.loginUsuari("test", "contrasenya");
            System.out.println("Usuari actiu: " + ctrlDomini.getIdUsuariActiu());
        } catch (Exception ex) {
            System.out.println("S'ha produit un error amb el login de l'usuari");
            return;
        }

        try {
            System.out.println("\r\nCanvi de contrasenya");
            System.out.println("test: contrasenya -> password // " + (ctrlDomini.canviarContrasenya("password", "password") == 0 ? "OK" : "KO"));
        } catch (Exception ex) {
            System.out.println("S'ha produit un error amb el canvi de contrasenya");
            return;
        }

        try {
            System.out.println("\r\nCanvi de nom");
            System.out.println("test -> test2 // " + (ctrlDomini.canviarNom("test1") == 0 ? "OK" : "KO"));
        } catch (Exception ex) {
            System.out.println("S'ha produit un error amb el canvi de nom");
            return;
        }

        try {
            System.out.println("\r\nEliminar usuari");
            System.out.println("Eliminar 1: " + (ctrlDomini.eliminarUsuari(1) == 0 ? "OK" : "KO"));
            System.out.println("\r\nUsuaris actuals");
            CtrlUsuari.getInstance().printUsuaris();
        } catch (Exception ex) {
            System.out.println("S'ha produit un error eliminant l'usuari");
            return;
        }
    }

    public static void testItems() {
        System.out.println("Carrega de items");
        try {
            ctrlDomini.carregarItemsCSV("monitor", "items-data-driver.csv");
            CtrlItem.getInstance().printItems();
            CtrlItem.getInstance().printTipusItems();
        } catch (Exception ex) {
            System.out.println("S'ha produit un error carregant els items");
            return;
        }

        System.out.println("Creacio tipus d'item");
        ctrlDomini.crearTipusItem("tipus1");
        ctrlDomini.afegirTipusAtribut("tipus1", "atr1", "Descriptiu");
        CtrlItem.getInstance().printTipusItems();

        System.out.println("Canvi de nom atribut");
        ctrlDomini.canviarNomTipusAtribut("tipus1", "atr1", "atr2");
        CtrlItem.getInstance().printTipusItems();

        System.out.println("Eliminar atribut");
        ctrlDomini.eliminarTipusAtribut("tipus1", "atr2");
        CtrlItem.getInstance().printTipusItems();

        System.out.println("Eliminar item");
        ctrlDomini.eliminarItem(1);
        CtrlItem.getInstance().printItems();

        System.out.println("Crear item");
        ctrlDomini.crearItem(3, "monitor");
        CtrlItem.getInstance().printItems();

        System.out.println("Afegir atribut");
        ctrlDomini.afegirAtribut(3, "nom", "monitor test");
        CtrlItem.getInstance().printItems();

        System.out.println("Modificar atribut");
        ctrlDomini.modificarAtribut(3, "nom", "monitor test mod");
        CtrlItem.getInstance().printItems();

        System.out.println("Eliminar atribut");
        ctrlDomini.eliminarAtribut(3, "nom");
        CtrlItem.getInstance().printItems();
    }

    public static void testValoracions() {
        System.out.println("Afegir valoracio");
        ctrlDomini.afegirValoracio(2, 0, 5);

        try {
            CtrlValoracio.getInstance().printValoracionsUsuari();
        } catch (Exception ex) {
            System.out.println("S'ha produit un error mostrant les valoracions");
        }

        System.out.println("Modificar valoracio");
        ctrlDomini.modificarValoracio(2, 0, 4);

        try {
            CtrlValoracio.getInstance().printValoracionsUsuari();
        } catch (Exception ex) {
            System.out.println("S'ha produit un error mostrant les valoracions");
        }

        System.out.println("Eliminar valoracio");
        ctrlDomini.eliminarValoracio(2, 0);

        try {
            CtrlValoracio.getInstance().printValoracionsUsuari();
        } catch (Exception ex) {
            System.out.println("S'ha produit un error mostrant les valoracions");
        }

        System.out.println("Afegir valoracio");
        ctrlDomini.afegirValoracio(2, 0, 5);

        try {
            CtrlValoracio.getInstance().printValoracionsUsuari();
        } catch (Exception ex) {
            System.out.println("S'ha produit un error mostrant les valoracions");
        }

        System.out.println("Eliminar valoracio per item");
        ctrlDomini.eliminarValoracionsItem(2);

        try {
            CtrlValoracio.getInstance().printValoracionsUsuari();
        } catch (Exception ex) {
            System.out.println("S'ha produit un error mostrant les valoracions");
        }

        System.out.println("Afegir valoracio");
        ctrlDomini.afegirValoracio(2, 0, 5);

        try {
            CtrlValoracio.getInstance().printValoracionsUsuari();
        } catch (Exception ex) {
            System.out.println("S'ha produit un error mostrant les valoracions");
        }

        System.out.println("Eliminar valoracio per usuari");
        ctrlDomini.eliminarValoracionsUsuari(0);

        try {
            CtrlValoracio.getInstance().printValoracionsUsuari();
        } catch (Exception ex) {
            System.out.println("S'ha produit un error mostrant les valoracions");
        }
    }

    public static void testRecomanacions() {
        try {
            ctrlDomini.eliminarUsuari(0);
            ctrlDomini.eliminarItem(2);

            ctrlDomini.crearItem(1, "monitor");
            ctrlDomini.crearItem(2, "monitor");
            ctrlDomini.crearItem(3, "monitor");
            ctrlDomini.crearItem(4, "monitor");
            ctrlDomini.carregarValoracionsCSV(0, 5, "ratings.dummy.csv");
        } catch (Exception ex) {
            System.out.println("Error carregant els ratings");
        }

        System.out.println("\r\nClassificacio");
        ctrlDomini.classificaUsuaris();

        ArrayList<ArrayList<Usuari>> usuaris = ctrlDomini.getClassificacio();
        for (ArrayList<Usuari> g : usuaris) {
            System.out.println(Arrays.toString(g.toArray()));
        }

        for (Recomanacio rec : ctrlDomini.getKRecomanacions(4, 1)) {
            System.out.println(rec);
        }
    }

    public static void main(String[] args) {
        try {
            ctrlDomini = CtrlDomini.getInstance();
        } catch (Exception ex) {
            System.out.println("S'ha produit un error obtenint el controlador de domini");
            return;
        }

        testUsuaris();
        testItems();
        testValoracions();
        testRecomanacions();
    }
}
