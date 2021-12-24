package domain.drivers;

import domain.*;

import java.util.Scanner;
import java.util.Set;

public class DriverItem {
    static String nomTipusAtribut;
    static String nomTipusItem;
    static String id;
    static String Tatribut;
    static String valor;

    public static void main(String[] args) {
        System.out.println("NOTA: En aquest driver es suposa que es compleixen les precondicions necessaries (ho asseguren els controladors), per tant no es comprovaran els errors");

        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.println("[Creacio d'un tipus d'item: necessari per la creacio d'un item]");
        System.out.println("Introduiu el nom del tipus d'item que voleu crear:");
        nomTipusItem = scanner.nextLine();
        TipusItem tipusItem = new TipusItem(nomTipusItem);

        System.out.println("1 -> Creacio d'un tipus d'atribut al tipus d'item creat");
        while (scanner.nextLine().equals("1")) {
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
            System.out.println("1 -> Creacio d'un tipus d'atribut al tipus d'item creat\n");
        }
        System.out.println(tipusItem);
        System.out.println();

        System.out.println("[Creacio d'un item de tipus d'item amb nom |" + nomTipusItem + "| ]");
        System.out.println("Introduiu l'id de l'item que voleu crear:");
        id = scanner.nextLine();
        Item item = new Item(Integer.parseInt(id), tipusItem);
        System.out.println(item);
        System.out.println();

        System.out.println("Escull una opció del testing (1 a 5)\n" +
                "1 -> AfegirAtribut\n" +
                "2 -> ModificarAtribut\n" +
                "3 -> EliminarAtribut\n" +
                "4 -> ExisteixAtribut\n" +
                "5 -> Sortir\n");
        String input = scanner.nextLine();
        while (!input.equals("5")) {
            if (input.equals("1")) {
                System.out.println("[Afegir valor a un atribut]");
                System.out.println("Introduiu el nom del tipus d'atribut:");
                nomTipusAtribut = scanner.nextLine();
                System.out.println("Introduiu el valor que voleu afegir:");
                valor = scanner.nextLine();
                System.out.println("Nom del tipus d'atribut: " + nomTipusAtribut + ", valor: " + valor);
                TipusItem.TAtribut t = tipusItem.getTAtribut(nomTipusAtribut);
                if (t.equals(TipusItem.TAtribut.CATEGORIC)) {
                    AtributCategoric aC = new AtributCategoric();
                    aC.afegirValor(valor);
                    item.afegirAtribut(nomTipusAtribut, aC);
                } else if (t.equals(TipusItem.TAtribut.BOOLEA))
                    item.afegirAtribut(nomTipusAtribut, new AtributBoolea(Boolean.parseBoolean(valor)));
                else if (t.equals(TipusItem.TAtribut.DESCRIPTIU))
                    item.afegirAtribut(nomTipusAtribut, new AtributDescriptiu(valor));
                else if (t.equals(TipusItem.TAtribut.NUMERIC))
                    item.afegirAtribut(nomTipusAtribut, new AtributNumeric(Integer.parseInt(valor)));
                System.out.println(item);
                System.out.println();
            } else if (input.equals("2")) {
                System.out.println("[Modificar un atribut]");
                System.out.println("Introduiu el nom de l'atribut que voleu modificar:");
                nomTipusAtribut = scanner.nextLine();
                System.out.println("Introduiu nova valor de l'atribut:");
                valor = scanner.nextLine();
                System.out.println("Nom del tipus d'atribut: " + nomTipusAtribut + ", nova valor: " + valor);
                TipusItem.TAtribut t = tipusItem.getTAtribut(nomTipusAtribut);
                if (t.equals(TipusItem.TAtribut.CATEGORIC)) {
                    AtributCategoric aC = new AtributCategoric();
                    aC.afegirValor(valor);
                    item.modificarAtribut(nomTipusAtribut, aC);
                } else if (t.equals(TipusItem.TAtribut.BOOLEA))
                    item.modificarAtribut(nomTipusAtribut, new AtributBoolea(Boolean.parseBoolean(valor)));
                else if (t.equals(TipusItem.TAtribut.DESCRIPTIU))
                    item.modificarAtribut(nomTipusAtribut, new AtributDescriptiu(valor));
                else if (t.equals(TipusItem.TAtribut.NUMERIC))
                    item.modificarAtribut(nomTipusAtribut, new AtributNumeric(Integer.parseInt(valor)));
                System.out.println(item);
                System.out.println();
            } else if (input.equals("3")) {
                System.out.println("[Eliminar un atribut]");
                System.out.println("Introduiu el nom del tipus d'atribut que voleu eliminar:");
                nomTipusAtribut = scanner.nextLine();
                System.out.println("Nom del tipus d'atribut: " + nomTipusAtribut);
                item.eliminarAtribut(nomTipusAtribut);
                System.out.println(item);
                System.out.println();
            } else if (input.equals("4")) {
                System.out.println("[Comprovar si existeix atribut]");
                System.out.println("Introduiu el nom d'atribut que voleu comprovar");
                nomTipusAtribut = scanner.nextLine();
                System.out.println("Nom del tipus d'atribut: " + nomTipusAtribut);
                System.out.println("Existeix l'atribut indicat: " + item.existsAtribut(nomTipusAtribut));
            }
            input = scanner.nextLine();
            System.out.println("Escull una opció del testing (1 a 5)\n" +
                    "1 -> AfegirAtribut\n" +
                    "2 -> ModificarAtribut\n" +
                    "3 -> EliminarAtribut\n" +
                    "4 -> ExisteixAtribut\n" +
                    "5 -> Sortir\n");
        }
        System.out.println("Fi l'execucio driver classe Item");
    }
}
