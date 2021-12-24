package domain.drivers;

import domain.*;

import java.util.Scanner;

public class DriverCtrlItem {
    CtrlDomini ctrlDomini = CtrlDomini.getInstance(); //Inicialitzar les instancies del domini i dades
    static CtrlItem ctrlItem = CtrlItem.getInstance();
    static int retVal;
    static boolean Val;

    public DriverCtrlItem() throws Exception {
    }

    public static void imprimir() {
        System.out.println("[TIPUS D'ITEM(S)]");
        ctrlItem.printTipusItems();
        System.out.println("[ITEM(S)]");
        ctrlItem.printItems();

    }
    public static void DriverCrearTipusItem(String nomTipusItem) throws Exception {
        System.out.println("Crear tipus d'item: " + nomTipusItem);
        retVal = ctrlItem.crearTipusItem(nomTipusItem);
        if (retVal == 0) System.out.println("El tipus d'item s'ha creat correctament.");
        else if (retVal == -1) System.out.println("ERROR: Ja existeix un tipus d'item amb el nom especificat.");
    }

    public static void DriverEliminarTipusItem(String nomTipusItem) throws Exception {
        System.out.println("Eliminar el tipus d'item: " + nomTipusItem);
        retVal = ctrlItem.eliminarTipusItem(nomTipusItem);
        if (retVal == 0) System.out.println("El tipus d'item, s'ha eliminat correctament");
        else if (retVal == -1) System.out.println("ERROR: No existeix el tipus d'item amb el nom indicat");
        else if (retVal == -2) System.out.println("ERROR: Existeix algun item que pertany el tipus d'item indicat");
    }

    public static void DriverAfegirTipusAtribut(String nomTipusItem, String nomTipusAtribut, String Tatribut) {
        System.out.println("Afegir nou tipus d'atribut: " + nomTipusAtribut + " de tipus " + Tatribut + " en el Tipus d'item " + nomTipusItem);
        TipusItem.TAtribut t;
        if (Tatribut.equals("numeric")) t = TipusItem.TAtribut.NUMERIC;
        else if (Tatribut.equals("categoric")) t = TipusItem.TAtribut.CATEGORIC;
        else if (Tatribut.equals("descriptiu")) t = TipusItem.TAtribut.DESCRIPTIU;
        else t = TipusItem.TAtribut.BOOLEA;
        retVal = ctrlItem.afegirTipusAtribut(nomTipusItem, nomTipusAtribut, t);
        if (retVal == 0)
            System.out.println("El tipus d'atribut s'ha afegit correctament en el tipus d'item especificat");
        else if (retVal == -1) System.out.println("ERROR: No existeix el tipus d'item amb el nom especificat");
        else if (retVal == -2) System.out.println("ERROR: Ja existeix el tipus d'atribut amb el nom proporcionat");
    }

    public static void DriverCanviarNomTipusAtribut(String nomTipusItem, String nomTipusAtribut, String nouNomTipusAtribut) {
        System.out.println("Canviar el nom del tipus d'atribut: " + nomTipusAtribut + " pel " + nouNomTipusAtribut + " en el tipus d'item " + nomTipusItem);
        retVal = ctrlItem.canviarNomTipusAtribut(nomTipusItem, nomTipusAtribut, nouNomTipusAtribut);
        if (retVal == 0) System.out.println("Tipus d'atribut eliminat correctament");
        else if (retVal == -1) System.out.println("ERROR: No existeix el tipus d'item especificat");
        else if (retVal == -2) System.out.println("ERROR: No existeix el tipus d'atribut especificat");
    }

    public static void DriverEliminarTipusAtribut(String nomTipusItem, String nomTipusAtribut) {
        System.out.println("Eliminar tipus d'atribut: " + nomTipusAtribut + " del tipus d'item " + nomTipusItem);
        retVal = ctrlItem.eliminarTipusAtribut(nomTipusItem, nomTipusAtribut);
        if (retVal == 0) System.out.println("Canvi nom del tipus d'atribut realitzat correctament");
        else if (retVal == -1) System.out.println("ERROR: No existeix el tipus d'item especificat");
        else if (retVal == -2) System.out.println("ERROR: No existeix el tipus d'atribut especificat");
        else if (retVal == -3) System.out.println("ERROR: Existeix item/s que pertany el tipus d'item especificat");
    }

    public static void DriverCrearItem(String id, String nomTipusItem) {
        Integer idItem = Integer.parseInt(id);
        System.out.println("Crear item amb l'id " + id + " en el tipus d'item: " + nomTipusItem);
        retVal = ctrlItem.crearItem(idItem, nomTipusItem);
        if (retVal == 0) System.out.println("Item s'ha creat correctament.");
        else if (retVal == -1)  System.out.println("ERROR: Ja existeix un item amb l'id especificat.");
        else if (retVal == -2) System.out.println("ERROR: No existeix el tipus d'item especificat");
    }

    public static void DriverEliminarItem(String id) {
        System.out.println("Eliminar l'item amb l'id " + id);
        Integer idItem = Integer.parseInt(id);
        retVal = ctrlItem.eliminarItem(idItem);
        if (retVal == 0) System.out.println("Item amb l'id especificat s'ha eliminat correctament");
        else System.out.println("ERROR: No existeix l'item especificat");
    }

    public static void DriverAfegirAtribut(String id, String nomTipusAtribut, String Tatribut, String valor) {
        System.out.println("Afegir el valor " + valor + " de tipus " + Tatribut + " en el tipus d'atribut " + nomTipusAtribut + " del item " + id);
        Integer idItem = Integer.parseInt(id);
        Atribut a;
        if (Tatribut.equals("numeric")) a = new AtributNumeric(Double.parseDouble(valor));
        else if (Tatribut.equals("categoric")) {
            String[] splitted = valor.split(";");
            a = new AtributCategoric();
            for (String s : splitted) ((AtributCategoric) a).afegirValor(s);
        }
        else if (Tatribut.equals("descriptiu")) a = new AtributDescriptiu(valor);
        else a = new AtributBoolea(Boolean.parseBoolean(valor));

        retVal = ctrlItem.afegirAtribut(idItem, nomTipusAtribut, a);
        if (retVal == 0) System.out.println("Atribut s'ha afegit correctament en l'item especificat");
        else if (retVal == -1) System.out.println("ERROR: No existeix l'item amb l'id especificat");
        else if (retVal == -2) System.out.println("ERROR: Ja existeix l'atribut especificat");
        else if (retVal == -3) System.out.println("ERROR: No existeix el tipus d'atribut especificat al tipus d'item corresponent");
        else if (retVal == -4) System.out.println("ERROR: No coincideix el tipus d'atribut especificat amb el tipus d'atribut indicat");
    }

    public static void DriverModificarAtribut(String id, String nomTipusAtribut, String Tatribut, String valor) {
        System.out.println("Modificar el valor del tipus d'atribut " + nomTipusAtribut + " pel " + valor + " de tipus " + Tatribut + " del item " + id);
        Integer idItem = Integer.parseInt(id);
        Atribut a;
        if (Tatribut.equals("numeric")) a = new AtributNumeric(Double.parseDouble(valor));
        else if (Tatribut.equals("categoric")) {
            String[] splitted = valor.split(";");
            a = new AtributCategoric();
            for (String s : splitted) ((AtributCategoric) a).afegirValor(s);
        } else if (Tatribut.equals("descriptiu")) a = new AtributDescriptiu(valor);
        else a = new AtributBoolea(Boolean.parseBoolean(valor));
        retVal = ctrlItem.modificarAtribut(idItem, nomTipusAtribut, a);
        if (retVal == 0) System.out.println("Atribut s'ha modificat correctament");
        else if (retVal == -1) System.out.println("ERROR: No existeix item amb l'id especificat");
        else if (retVal == -2) System.out.println("ERROR: No existeix el tipus d'atribut especificat");
        else if (retVal == -3)
            System.out.println("ERROR: No coincideix el tipus d'atribut especificat amb el tipus d'atribut indicat");
    }


    public static void DriverEliminarAtribut(String id, String nomTipusAtribut) {
        System.out.println("Eliminar Atribut "+ nomTipusAtribut + "de l'item " + id);
        Integer idItem = Integer.parseInt(id);
        retVal = ctrlItem.eliminarAtribut(idItem, nomTipusAtribut);
        if (retVal == 0) System.out.println("Atribut s'ha eliminat correctament");
        else if (retVal == -1) System.out.println("ERROR: No existeix item amb l'id especificat");
        else if (retVal == -2) System.out.println("ERROR: No existeix el tipus d'atribut especificat");
    }

    public static void DriverExisteixItem(String id) {
        System.out.println("Comprovar si existeix item amb id " + id);
        Integer idItem = Integer.parseInt(id);
        Val = ctrlItem.existsItem(idItem);
        if (Val) System.out.println("Existeix item amb id especificat");
        else System.out.println("No existeix item amb id especificat");
    }
    public static void main(String[] args) throws Exception {
        String nomTipusItem;
        String nomTipusAtribut;
        String nouNomTipusAtribut;
        String tAtribut;
        String id;
        String valor;
        System.out.println("Driver classe CtrlItem.");
        System.out.println("Escull una opció del testing (0 a 12)\n" +
                "0 ->  Exit\n" +
                "1 ->  CrearTipusItem\n" +
                "2 ->  EliminarTipusItem\n" +
                "3 ->  AfegirTipusAtribut\n" +
                "4 ->  CanviarNomTipusAtribut\n" +
                "5 ->  EliminarTipusAtribut\n" +
                "6 ->  CrearItem\n" +
                "7 ->  EliminarItem\n" +
                "8 ->  AfegirAtribut\n" +
                "9 ->  ModificarAtribut\n" +
                "10 -> EliminarAtribut\n" +
                "11 -> ExisteixItem\n" +
                "12 -> Mostrar els tipus d'items i items\n");

        Scanner scanner = new Scanner(System.in);
        String input;
        boolean sortir = false;
        while (!sortir) {
            input = scanner.nextLine();
             if (input.equals("0")) {
                 sortir = true;
             }
             else if (input.equals("1")) {
                 System.out.println("[Crear un tipus d'item]\n" +
                         "Introdueix el nom del tipus d'item");
                 nomTipusItem = scanner.nextLine();
                 DriverCrearTipusItem(nomTipusItem);
             }
             else if (input.equals("2")) {
                 System.out.println("[Eliminar un tipus d'item]\n" +
                         "Introdueix el nom del tipus d'item");
                 nomTipusItem = scanner.nextLine();
                 DriverEliminarTipusItem(nomTipusItem);
             }
             else if (input.equals("3")) {
                 System.out.println("[Afegir un tipus d'atribut al tipus d'item especificat]\n" +
                         "Introdueix els parametres:\n" +
                         "Nom del tipus d'item");
                 nomTipusItem = scanner.nextLine();
                 System.out.println("Nom del tipus d'atribut");
                 nomTipusAtribut = scanner.nextLine();
                 System.out.println("TAtribut\n" +
                         "TAtribut pot ser: numeric, categoric, descriptiu o boolea");
                 tAtribut = scanner.nextLine();
                 DriverAfegirTipusAtribut(nomTipusItem, nomTipusAtribut, tAtribut);
             }
             else if (input.equals("4")) {
                System.out.println("[Canviar el nom d'un tipus d'atribut]\n" +
                    "Introdueix els parametres:\n" +
                    "Nom del tipus d'item");
                nomTipusItem = scanner.nextLine();
                System.out.println("Nom del tipus d'atribut");
                nomTipusAtribut = scanner.nextLine();
                System.out.println("Nom nou del tipus d'atribut");
                nouNomTipusAtribut = scanner.nextLine();
                DriverCanviarNomTipusAtribut(nomTipusItem, nomTipusAtribut, nouNomTipusAtribut);
            }
            else if (input.equals("5")) {
                System.out.println("[Eliminar un tipus d'atribut]\n" +
                        "Introdueix els parametres:\n" +
                        "Nom del tipus d'item");
                nomTipusItem = scanner.nextLine();
                System.out.println("Nom del tipus d'atribut");
                nomTipusAtribut = scanner.nextLine();
                DriverEliminarTipusAtribut(nomTipusItem, nomTipusAtribut);
            }
            else if (input.equals("6")) {
                System.out.println("[Crear un item nou]\n" +
                        "Introdueix els parametres:\n" +
                        "Id de l'item");
                id = scanner.nextLine();
                System.out.println("Nom del tipus d'item");
                nomTipusItem = scanner.nextLine();
                DriverCrearItem(id, nomTipusItem);
            }
            else if (input.equals("7")) {
                System.out.println("[Eliminar un item]\n" +
                        "Introdueix l'id de l'item");
                id = scanner.nextLine();
                DriverEliminarItem(id);
            }
            else if (input.equals("8")) {
                System.out.println("[Afegir un atribut]\n" +
                        "Introdueix els parametres:\n" +
                        "Id de l'item");
                id = scanner.nextLine();
                System.out.println("Nom del tipus d'atribut");
                nomTipusAtribut = scanner.nextLine();
                System.out.println("TAtribut\n" +
                        "TAtribut pot ser: numeric, categoric, descriptiu o boolea");
                tAtribut = scanner.nextLine();
                System.out.println("Valor de l'atribut");
                valor = scanner.nextLine();
                DriverAfegirAtribut(id, nomTipusAtribut, tAtribut, valor);
            }
            else if (input.equals("9")) {
                System.out.println("[Modificar atribut d'un item determinat]\n" +
                        "Introdueix els parametres:\n" +
                        "Id de l'item");
                id = scanner.nextLine();
                System.out.println("Nom del tipus d'atribut");
                nomTipusAtribut = scanner.nextLine();
                System.out.println("TAtribut\n" +
                        "TAtribut pot ser: numeric, categoric, descriptiu o boolea");
                tAtribut = scanner.nextLine();
                System.out.println("Valor de l'atribut");
                valor = scanner.nextLine();
                DriverModificarAtribut(id, nomTipusAtribut, tAtribut, valor);
            }
            else if (input.equals("10")) {
                System.out.println("[Eliminar atribut d'un item determinat]\n" +
                        "Introdueix els parametres:\n" +
                        "Id de l'item");
                id = scanner.nextLine();
                System.out.println("Nom del tipus d'atribut");
                nomTipusAtribut = scanner.nextLine();
                DriverEliminarAtribut(id, nomTipusAtribut);
            }
            else if (input.equals("11")) {
                System.out.println("[Comprova si l'item especificat existeix]\n" +
                        "Parametre: idItem");
                id = scanner.nextLine();
                DriverExisteixItem(id);
            }
            else if (input.equals("12")){
                System.out.println("[Impressio: Tipus d'items i Items]");
                imprimir();
            }
            System.out.println();
        }
        System.out.println("Fi l'execucio driver classe CtrlItem");
    }
}