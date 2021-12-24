package domain.drivers;

import domain.TipusItem;

import java.util.Scanner;

public class DriverTipusItem {
    public static void main(String[] args) {
        String nomTipusItem;
        String nomTipusAtribut;
        TipusItem tipusItem = null;
        String Tatribut;
        System.out.println("Driver clase TipusItem");
        System.out.println("Escull una opció del testing (1 a 6)\n" +
                "1 -> CrearTipusItem\n" +
                "2 -> AfegirTipusAtribut\n" +
                "3 -> CanviarNomTipusAtribut\n" +
                "4 -> EliminarTipusAtribut\n" +
                "5 -> TAtrbiutTipusAtribut\n" +
                "6 -> Sortir\n");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("6")) {
            if (input.equals("1")) {
                System.out.println("[Creacio d'un tipus d'item]");
                System.out.println("Introduiu el nom del tipus d'item que voleu crear:");
                nomTipusItem = scanner.nextLine();
                tipusItem = new TipusItem(nomTipusItem);
                System.out.println(tipusItem);
                System.out.println();
            } else if (input.equals("2")) {
                System.out.println("[Afegir un tipus atribut]");
                System.out.println("Introduiu el nom del tipus d'atribut:");
                nomTipusAtribut = scanner.nextLine();
                System.out.println("Introduiu el TAtribut del tipus d'atribut, aquest pot ser: categoric, numeric, descriptiu, boolea");
                Tatribut = scanner.nextLine();
                TipusItem.TAtribut a;
                if (Tatribut.equals("categoric")) {
                    a = TipusItem.TAtribut.CATEGORIC;
                    tipusItem.afegirTipusAtribut(nomTipusAtribut, a);
                } else if (Tatribut.equals("numeric")) {
                    a = TipusItem.TAtribut.NUMERIC;
                    tipusItem.afegirTipusAtribut(nomTipusAtribut, a);
                } else if (Tatribut.equals("descriptiu")) {
                    a = TipusItem.TAtribut.DESCRIPTIU;
                    tipusItem.afegirTipusAtribut(nomTipusAtribut, a);
                } else if (Tatribut.equals("boolea")) {
                    a = TipusItem.TAtribut.BOOLEA;
                    tipusItem.afegirTipusAtribut(nomTipusAtribut, a);
                } else System.out.println("TAtribut introduit és incorrecta");
                System.out.println(tipusItem);
            } else if (input.equals("3")) {
                System.out.println("[Canviar el nom del tipus d'atribut]");
                System.out.println("Introduiu el nom actual del tipus d'atribut:");
                nomTipusAtribut = scanner.nextLine();
                System.out.println("Introduiu el nom nou del tipus d'atribut:");
                String nouNomTipusAtribut = scanner.nextLine();
                tipusItem.canviarNomTipusAtribut(nomTipusAtribut, nouNomTipusAtribut);
                System.out.println(tipusItem);
            } else if (input.equals("4")) {
                System.out.println("[Eliminar el nom del tipus d'atribut]");
                System.out.println("Introduiu el nom del tipus d'atribut:");
                nomTipusAtribut = scanner.nextLine();
                tipusItem.eliminarTipusAtribut(nomTipusAtribut);
                System.out.println(tipusItem);
            } else if (input.equals("5")) {
                System.out.println("[Obtenir el TAtribut d'un tipus d'atribut]");
                System.out.println("Introduiu el nom del tipus d'atribut:");
                nomTipusAtribut = scanner.nextLine();
                System.out.println(tipusItem.getTAtribut(nomTipusAtribut));
            }
            System.out.println();
            System.out.println("Escull una opció del testing (1 a 6)\n" +
                    "1 -> CrearTipusItem\n" +
                    "2 -> AfegirTipusAtribut\n" +
                    "3 -> CanviarNomTipusAtribut\n" +
                    "4 -> EliminarTipusAtribut\n" +
                    "5 -> TAtrbiutTipusAtribut\n" +
                    "6 -> Sortir\n");
            input = scanner.nextLine();
        }
        System.out.println("Fi l'execucio driver classe TipusItem");
    }
}