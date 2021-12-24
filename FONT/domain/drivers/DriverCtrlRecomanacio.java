package domain.drivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import domain.CtrlDomini;
import domain.CtrlRecomanacio;
import domain.Recomanacio;

public class DriverCtrlRecomanacio {

    public static void printGrups(ArrayList<ArrayList<Integer>> usuaris) {
        System.out.println("\r\nCLASSIFICACIO");
        System.out.println("=============");
        int i = 0;
        for (ArrayList<Integer> grup : usuaris) {
            System.out.print("(Grup " + i++ + ")(card=" + grup.size() + ")[ ");
            for (Integer u : grup) {
                System.out.print(u + " ");
            }

            System.out.println("]");
        }
    }

    public static void testGuardar() {
        CtrlRecomanacio ctrlRec = CtrlRecomanacio.getInstance();
        ctrlRec.guardar();

        System.out.println("JSON");
        System.out.println("=====");
        System.out.println("DATA/classificacio.json -> classificacio dels usuaris");
        System.out.println("DATA/recomanacions.json -> recomanacions fins el moment");
    }

    public static void testCarregar() {
        CtrlRecomanacio ctrlRec = CtrlRecomanacio.getInstance();
        ctrlRec.carregar();
        printGrups(ctrlRec.getGrups());

        testRecomanacio(201478, 10);
        testRecomanacio(168738, 10);

        testGuardar();
    }

    public static void testGrups() {
        CtrlRecomanacio ctrlRec = CtrlRecomanacio.getInstance();
        ctrlRec.classifica();

        printGrups(ctrlRec.getGrups());
    }

    public static void testRecomanacio(int idUsuari, int n) {
        System.out.println("\r\n " + n + " recomanacions per " + idUsuari + " (Collaborative)");
        System.out.println("---------------------");
        CtrlRecomanacio ctrlRec = CtrlRecomanacio.getInstance();

        List<Recomanacio> rec = ctrlRec.getRecomanacions(idUsuari, n, true);
        for (Recomanacio re : rec) {
            System.out.println(re.toString());
        }
    }

    public static void testAvaluacio() {
        CtrlRecomanacio ctrlRec = CtrlRecomanacio.getInstance();
        ctrlRec.avaluacioRecomanacions("driver-recomanacio/ratings.test.unknown.csv");

        System.out.println("L'avaluacio està a DATA/avaluacio.csv");
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        CtrlDomini ctrlDomini = CtrlDomini.getInstance();
        ctrlDomini.carregarItemsCSV("movies", "driver-recomanacio/items.csv");
        ctrlDomini.carregarValoracionsCSV(0, 5, "driver-recomanacio/ratings.test.known.csv");

        System.out.println("[Driver del controlador de recomanacions]");
        System.out.println("Classificacio a partir de les dades dels csv");
        testGrups();

        int opt = -1;
        while (opt != 4) {
            System.out.println("Tria una opcio");
            System.out.println("1- Recomanar");
            System.out.println("2- Avaluar recomanacions");
            System.out.println("3- Guardar en json");
            System.out.println("4- Sortir");

            opt = Integer.parseInt(sc.nextLine());
            switch (opt) {
                case 1:
                    System.out.println("Introdueix un identificador d'usuari");
                    int id = Integer.parseInt(sc.nextLine());

                    System.out.println("Introdueix el nombre de recomanacions");
                    int k = Integer.parseInt(sc.nextLine());

                    testRecomanacio(id, k);
                    break;

                case 2:
                    testAvaluacio();
                    break;
                case 3:
                    testGuardar();
                    break;
            }
        }

        sc.close();
    }
}
