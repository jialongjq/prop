package domain.drivers;

import domain.Valoracio;

import java.util.Scanner;

public class DriverValoracio {
    static Valoracio v;
    public static void main(String[] args) {
        System.out.println("Driver classe Valoracio");
        System.out.println("Escull una opció del testing (1 a 3)\n" +
                "1 -> CrearValoracio\n" +
                "2 -> ModificarValoracio\n" +
                "3 -> Sortir\n");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("3")) {
            if (input.equals("1")) {
                System.out.println("[Creacio d'una valoracio]");
                System.out.println("Introduiu l'identificador de l'item: ");
                String idItem = scanner.nextLine();
                System.out.println(idItem);
                System.out.println("Introduiu l'identificador de l'usuari: ");
                String idUsuari = scanner.nextLine();
                System.out.println(idUsuari);
                System.out.println("Introduiu la puntacio:");
                String puntuacio = scanner.nextLine();
                System.out.println(puntuacio);
                System.out.println();
                v = new Valoracio(Integer.parseInt(idItem), Integer.parseInt(idUsuari), Double.parseDouble(puntuacio));
                System.out.println(v);
            } else if (input.equals("2")) {
                System.out.println("[Modificacio de la puntuacio d'una valoracio]");
                System.out.println("Introduiu la nova puntacio:");
                String puntuacio = scanner.nextLine();
                System.out.println(puntuacio);
                v.setPuntuacio(Double.parseDouble(puntuacio));
                System.out.println(v);
            }
            System.out.println();
            System.out.println("Escull una opció del testing (1 a 3)\n" +
                    "1 -> CrearValoracio\n" +
                    "2 -> ModificarValoracio\n" +
                    "3 -> Sortir\n");
            input = scanner.nextLine();
        }
        System.out.println("Fi l'execucio driver classe Valoracio");
    }
}
