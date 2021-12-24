package domain.drivers;

import domain.Usuari;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class DriverUsuari {
    static int idUsuari;
    static String nomUsuari;
    static String contrasenya;
    static boolean esAdmin;
    static Usuari usuari;

    public static void  testConstructor() throws IOException {
        usuari = new Usuari(idUsuari, nomUsuari, contrasenya, esAdmin);
        if (usuari.getId() == idUsuari && usuari.getNom().equals(nomUsuari)
                && usuari.getContrasenya().equals(contrasenya) && usuari.isAdmin() == esAdmin) {
            System.out.println("Exit en la constructura.");
            System.out.println("S'ha creat l'usuari amb l'id: " + usuari.getId() + ", nom: " + usuari.getNom() + ", contrasenya: " + usuari.getContrasenya() + ", Administrador?: " + usuari.isAdmin());
        }
        else System.out.println("Error en la constructura.");
    }

    public static void GetIdUsuari() throws IOException {
        System.out.println("Id usuari esperat:" + idUsuari);
        System.out.println("Id usuari obtingut: " + usuari.getId());
    }

    public static void GetNomUsuari() throws IOException {
        System.out.println("Nom usuari esperat:" + nomUsuari);
        System.out.println("Nom usuari obtingut: " + usuari.getNom());
    }

    public static void GetContrasenyaUsuari() throws IOException {
        System.out.println("Contrasenya esperada de l'usuari:" + contrasenya);
        System.out.println("Contrasenya obtinguda de l'usuari: " + usuari.getContrasenya());
    }

    public static void GetEsAdmin() throws IOException {
        System.out.println("Valor esperat:" + esAdmin);
        System.out.println("Valor obtingut: " + usuari.isAdmin());
    }

    public static void SetNomUsuari(String nouNom) throws IOException {
        System.out.println("Usuari amb l'id: " + usuari.getId() + ", nom: " + usuari.getNom() + ", contrasenya: " + usuari.getContrasenya() + ", Administrador?: " + usuari.isAdmin());
        usuari.setNom(nouNom);
        System.out.println("Canvi del nom realitzat");
        System.out.println("Usuari actual: id: " + usuari.getId() + ", nom: " + usuari.getNom() + ", contrasenya: " + usuari.getContrasenya() + ", Administrador?: " + usuari.isAdmin());
    }

    public static void SetContrasenyaUsuari(String novaContrasenya) throws IOException {
        System.out.println("Usuari amb l'id: " + usuari.getId() + ", nom: " + usuari.getNom() + ", contrasenya: " + usuari.getContrasenya() + ", Administrador?: " + usuari.isAdmin());
        usuari.setContrasenya(novaContrasenya);
        System.out.println("Canvi de la contrasenya realitzada");
        System.out.println("Usuari actual: id: " + usuari.getId() + ", nom: " + usuari.getNom() + ", contrasenya: " + usuari.getContrasenya() + ", Administrador?: " + usuari.isAdmin());
    }

        public static void main(String[] args) throws IOException {
        System.out.println("Driver clase Usuari");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduiu l'id de l'usuari");
        idUsuari = Integer.parseInt(scanner.nextLine());
        System.out.println("Introduiu el nom de l'usuari");
        nomUsuari = scanner.nextLine();
        System.out.println("Introduiu la contrasenya de l'usuari");
        contrasenya = scanner.nextLine();
        System.out.println("Indiqueu si l'usuari creat es administrador: true/false");
        esAdmin = Boolean.parseBoolean(scanner.nextLine());
        usuari = new Usuari(idUsuari, nomUsuari, contrasenya, esAdmin);

        System.out.println("Escull una opció del testing (1 a 4)\n" +
                            "1 -> Constructores\n" +
                            "2 -> Getters\n" +
                            "3 -> Setters\n" +
                            "4 -> Sortir\n");
        String input = scanner.nextLine();
        while (!input.equals("4")) {
            switch (input) {
                case "1":
                    DriverUsuari.testConstructor();
                    break;
                case "2":
                    System.out.println("Has entrat el testeig dels Getters, escull una de les següents opcions(1 a 4):\n" +
                            "1 -> GetIdUsuari()\n" +
                            "2 -> GetNomUsuari()\n" +
                            "3 -> GetContrasenyaUsuari()\n" +
                            "4 -> GetEsAdmin()\n");
                    String opc = scanner.nextLine();
                    switch (opc) {
                        case "1":
                            DriverUsuari.GetIdUsuari();
                            break;
                        case "2":
                            DriverUsuari.GetNomUsuari();
                            break;
                        case "3":
                            DriverUsuari.GetContrasenyaUsuari();
                            break;
                        case "4":
                            DriverUsuari.GetEsAdmin();
                            break;
                    }
                    break;
                case "3":
                    System.out.println("Has entrat el testeig dels Setters, escull una de les següents opcions(1 a 2):\n" +
                            "1 -> SetNomUsuari()\n" +
                            "2 -> SetContrasenyaUsuari()\n" );
                    String opcio = scanner.nextLine();
                    switch (opcio) {
                        case "1":
                            System.out.println("Introduiu el nou nom de l'usuari");
                            DriverUsuari.SetNomUsuari(scanner.nextLine());
                            break;
                        case "2":
                            System.out.println("Introduiu la nova contrasenya de l'usuari");
                            DriverUsuari.SetContrasenyaUsuari(scanner.nextLine());
                            break;
                    }
                    break;
                default:
                    break;
            }
            System.out.println("Escull una opció del testing (1 a 4)\n" +
                    "1 -> Constructores\n" +
                    "2 -> Getters\n" +
                    "3 -> Setters\n" +
                    "4 -> Sortir\n");
            input = scanner.nextLine();
        }
            System.out.println("Fi l'execucio driver classe Usuari");
    }
}
