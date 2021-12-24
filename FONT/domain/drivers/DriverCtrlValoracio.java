package domain.drivers;

import domain.CtrlDomini;
import domain.CtrlValoracio;
import domain.Item;
import domain.Valoracio;

import java.util.Scanner;
import java.util.Set;

public class DriverCtrlValoracio {
    public static void main(String[] args) throws Exception {
        CtrlDomini ctrlDomini = CtrlDomini.getInstance(); // Necessari per inicialitzar els controladors del domini i dades
        CtrlValoracio ctrlValoracio = CtrlValoracio.getInstance();
        int retVal;
        String input;
        String nomTipusItem;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Driver classe CtrlValoracio");
        System.out.println("Per la creacio d'una valoracio cal primer de tot, crear items i registrar usuaris:");
        System.out.println("[Crear un tipus d'item i crear items (suposem correcte, DriverCtrlItem)]");
        System.out.println("Introduiu el nom del tipus d'item que voleu crear:");
        nomTipusItem = scanner.nextLine();
        ctrlDomini.crearTipusItem(nomTipusItem);
        System.out.println("Si voleu afegir item introduiu 1");
        input = scanner.nextLine();
        while (input.equals("1")) {
            System.out.println("Introduiu l'id de l'item que voleu crear:");
            input = scanner.nextLine();
            ctrlDomini.crearItem(Integer.parseInt(input), nomTipusItem);
            System.out.println("Si voleu afegir item introduiu 1");
            input = scanner.nextLine();
        }

        System.out.println("[Registrar usuaris(suposem correcte, DriverCtrlUsuari)]");
        input = scanner.nextLine();
        System.out.println("Si voleu registrar usuari introduiu 1");
        while (input.equals("1")) {
            System.out.println("Introduiur l'id de l'usuari");
            String id = scanner.nextLine();
            System.out.println(id);
            System.out.println("Introduiu el nom de l'usuari:");
            String nomUsuari = scanner.nextLine();
            System.out.println("Introduiu la contrasenya de l'usuari:");
            String contrasenya = scanner.nextLine();
            System.out.println("Introduiu la confirmacio de la contrasenya de l'usuari:");
            String confirmaContrasenya = scanner.nextLine();
            System.out.println("Indiqueu si l'usuari que voleu registrar es administrador o no: true/false");
            String admin = scanner.nextLine();
            retVal = ctrlDomini.registrarUsuariId(Integer.parseInt(id), nomUsuari, contrasenya, confirmaContrasenya, Boolean.parseBoolean(admin));
            System.out.println("Si voleu registrar usuari introduiu 1");
            input = scanner.nextLine();
        }

        System.out.println("Escull una opció del testing (1 a 7)\n" +
                "1 -> AfegirValoracio\n" +
                "2 -> ModificarValoracio\n" +
                "3 -> EliminarValoracio\n" +
                "4 -> EliminarValoracionsItem\n" +
                "5 -> EliminarValoracionsUsuari\n" +
                "6 -> ObtenirValoracionsItem\n" +
                "7 -> Sortir\n");
        input = scanner.nextLine();
        while (!input.equals("7")) {
            if (input.equals("1")) {
                System.out.println("[Afegir una valoracio]");
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
                retVal = ctrlValoracio.afegirValoracio(Integer.parseInt(idItem), Integer.parseInt(idUsuari), Double.parseDouble(puntuacio));
                if (retVal == 0) System.out.println("Valoracio afegida correctament");
                else if (retVal == -1) System.out.println("No existeix l'item amb l'id especificat");
                else if (retVal == -2) System.out.println("No existeix l'usuari amb l'id especificat");
                else if (retVal == -3) System.out.println("La valoracio ja existeix");
            } else if (input.equals("2")) {
                System.out.println("[Modificar una valoracio]");
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
                retVal = ctrlValoracio.modificarValoracio(Integer.parseInt(idItem), Integer.parseInt(idUsuari), Double.parseDouble(puntuacio));
                if (retVal == 0) System.out.println("Valoracio modificada correctament");
                else if (retVal == -1) System.out.println("No existeix l'item amb l'id especificat");
                else if (retVal == -2) System.out.println("No existeix l'usuari amb l'id especificat");
                else if (retVal == -3) System.out.println("La valoracio no existeix");
            } else if (input.equals("3")) {
                System.out.println("[Eliminar una valoracio]");
                System.out.println("Introduiu l'identificador de l'item: ");
                String idItem = scanner.nextLine();
                System.out.println(idItem);
                System.out.println("Introduiu l'identificador de l'usuari: ");
                String idUsuari = scanner.nextLine();
                System.out.println(idUsuari);
                retVal = ctrlValoracio.eliminarValoracio(Integer.parseInt(idItem), Integer.parseInt(idUsuari));
                if (retVal == 0) System.out.println("Valoracio eliminada correctament");
                else if (retVal == -1) System.out.println("La valoracio no existeix");
            } else if (input.equals("4")) {
                System.out.println("[Eliminar valoracions d'un item]");
                System.out.println("Introduiu l'identificador de l'item: ");
                String idItem = scanner.nextLine();
                System.out.println(idItem);
                retVal = ctrlValoracio.eliminarValoracionsItem(Integer.parseInt(idItem));
                if (retVal == 0) System.out.println("Valoracions eliminades correctament");
                else if (retVal == -1) System.out.println("L'item amb l'id especificat no existeix");
            } else if (input.equals("5")) {
                System.out.println("[Eliminar valoracions d'un usuari]");
                System.out.println("Introduiu l'identificador de l'usuari: ");
                String idUsuari = scanner.nextLine();
                System.out.println(idUsuari);
                retVal = ctrlValoracio.eliminarValoracionsUsuari(Integer.parseInt(idUsuari));
                if (retVal == 0) System.out.println("Valoracions eliminades correctament");
                else if (retVal == -1) System.out.println("L'usuari amb l'id especificat no existeix");
            } else if (input.equals("6")) {
                System.out.println("[Obtenir el conjunt d'items valorats per l'item]");
                System.out.println("Introduiu l'identificador de l'usuari: ");
                String idUsuari = scanner.nextLine();
                System.out.println(idUsuari);
                Set<Item> itemsValorats = ctrlValoracio.getItemsValorats(Integer.parseInt(idUsuari), 0);
                System.out.println("Items valorats:");
                for (Item i : itemsValorats) System.out.println(i);
            }
            System.out.println("[VALORACIONS PER ITEM]");
            ctrlValoracio.printValoracionsItem();
            System.out.println("[VALORACIONS PER USUARI]");
            ctrlValoracio.printValoracionsUsuari();
            System.out.println();

            System.out.println("Escull una opció del testing (1 a 7)\n" +
                    "1 -> AfegirValoracio\n" +
                    "2 -> ModificarValoracio\n" +
                    "3 -> EliminarValoracio\n" +
                    "4 -> EliminarValoracionsItem\n" +
                    "5 -> EliminarValoracionsUsuari\n" +
                    "6 -> ObtenirValoracionsItem\n" +
                    "7 -> Sortir\n");
            input = scanner.nextLine();
        }
        System.out.println("Fi l'execucio driver classe CtrlValoracio");
    }
}
