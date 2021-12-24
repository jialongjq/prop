package domain.drivers;

import domain.*;
import domain.algorismes.Distancia;

import java.util.*;

public class DriverDistancia {
    public static void main(String[] args) {
        System.out.println("[Testing de la classe Distancia]");

        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (!input.equals("0")) {
            if (input.equals("1")) {
                System.out.println("[Creacio del tipus d'item del conjunt sobre el qual es calcularan les distancies]");
                System.out.println("- Indica el nom del tipus d'item:");
                input = scanner.nextLine();
                TipusItem tipusItem = new TipusItem(input);
                input = "";
                while (!input.equals("0")) {
                    if (input.equals("1")) {
                        System.out.println("- Indica el nom de l'atribut numeric");
                        input = scanner.nextLine();
                        tipusItem.afegirTipusAtribut(input, TipusItem.TAtribut.NUMERIC);
                    } else if (input.equals("2")) {
                        System.out.println("- Indica el nom de l'atribut boolea");
                        input = scanner.nextLine();
                        tipusItem.afegirTipusAtribut(input, TipusItem.TAtribut.BOOLEA);
                    } else if (input.equals("3")) {
                        System.out.println("- Indica el nom de l'atribut descriptiu");
                        input = scanner.nextLine();
                        tipusItem.afegirTipusAtribut(input, TipusItem.TAtribut.DESCRIPTIU);
                    } else if (input.equals("4")) {
                        System.out.println("- Indica el nom de l'atribut categoric");
                        input = scanner.nextLine();
                        tipusItem.afegirTipusAtribut(input, TipusItem.TAtribut.CATEGORIC);
                    } else if (!input.equals("")) System.out.println("[ERROR: Opcio invalida]");
                    System.out.println("- Indica els atributs del tipus d'item "+tipusItem.getNom()+":\n" +
                            "  1 -> Afegir un tipus d'atribut numeric\n" +
                            "  2 -> Afegir un tipus d'atribut boolea\n" +
                            "  3 -> Afegir un tipus d'atribut descriptiu\n" +
                            "  4 -> Afegir un tipus d'atribut categoric\n" +
                            "  0 -> Continuar amb la creacio d'items");
                    input = scanner.nextLine();
                }
                System.out.println("[Tipus d'item creat correctament]\n" + tipusItem);
                HashMap<Integer, Item> items = new HashMap<>();
                input = "";
                while (!input.equals("0")) {
                    if (input.equals("1")) {
                        System.out.println("- Introdueix l'id de l'item:");
                        input = scanner.nextLine();
                        int id = -1;
                        boolean valid = false;
                        while (!valid) {
                            try {
                                id = Integer.parseUnsignedInt(input);
                                valid = true;
                            } catch (NumberFormatException e) {
                                System.out.println("[ERROR: Identificador invalid]");
                            }
                            if (valid && items.containsKey(id)) {
                                System.out.println("[ERROR: Identificador no disponible]");
                                valid = false;
                            }
                            if (!valid) input = scanner.nextLine();
                        }
                        Item item = new Item(id, tipusItem);

                        input = "";
                        while (!input.equals("0")) {
                            if (input.equals("1")) {
                                System.out.println("- Indica el nom de l'atribut (ha de ser al tipus d'item)");
                                input = scanner.nextLine();
                                while (!tipusItem.getTipusAtributs().containsKey(input)) {
                                    System.out.println("[ERROR: El tipus d'atribut amb el nom especificat no existeix]");
                                    input = scanner.nextLine();
                                }
                                String nomAtribut = input;
                                if (tipusItem.getTipusAtributs().get(nomAtribut) == TipusItem.TAtribut.NUMERIC) {
                                    System.out.println("- Introdueix el valor numeric:");
                                    input = scanner.nextLine();
                                    double n = 0;
                                    valid = false;
                                    while (!valid) {
                                        try {
                                            n = Double.parseDouble(input);
                                            valid = true;
                                        } catch (NumberFormatException e) {
                                            System.out.println("[ERROR: Valor invalid]");
                                        }
                                        if (!valid) input = scanner.nextLine();
                                    }
                                    Atribut a = new AtributNumeric(n);
                                    item.afegirAtribut(nomAtribut, a);
                                }
                                else if (tipusItem.getTipusAtributs().get(nomAtribut) == TipusItem.TAtribut.BOOLEA) {
                                    System.out.println("- Introdueix el valor boolea:");
                                    input = scanner.nextLine();
                                    boolean b = false;
                                    valid = false;
                                    while (!valid) {
                                        if (input.equals("true") || input.equals("false")) {
                                            valid = true;
                                            b = Boolean.parseBoolean(input);
                                        }
                                        else {
                                            System.out.println("[ERROR: Valor invalid]");
                                            input = scanner.nextLine();
                                        }
                                    }
                                    Atribut a = new AtributBoolea(b);
                                    item.afegirAtribut(nomAtribut, a);
                                }
                                else if (tipusItem.getTipusAtributs().get(nomAtribut) == TipusItem.TAtribut.DESCRIPTIU) {
                                    System.out.println("- Introdueix el valor descriptiu:");
                                    input = scanner.nextLine();
                                    Atribut a = new AtributDescriptiu(input);
                                    item.afegirAtribut(nomAtribut, a);
                                }
                                else if (tipusItem.getTipusAtributs().get(nomAtribut) == TipusItem.TAtribut.CATEGORIC) {
                                    System.out.println("- Introdueix els valors categorics (salt de linia per acabar):");
                                    Set<String> s = new HashSet<>();
                                    input = scanner.nextLine();
                                    while (!input.equals("")) {
                                        s.add(input);
                                        input = scanner.nextLine();
                                    }
                                    AtributCategoric aC = new AtributCategoric();
                                    for (String valor : s) aC.afegirValor(valor);
                                    item.afegirAtribut(nomAtribut, aC);
                                }
                            } else if (!input.equals("")) System.out.println("[ERROR: Opcio invalida]");
                            System.out.println("- Indica els atributs del tipus d'item "+tipusItem.getNom()+":\n" +
                                    "  1 -> Afegir un atribut a l'item\n" +
                                    "  0 -> Crear un altre item");
                            input = scanner.nextLine();
                        }
                        items.put(id, item);
                        System.out.println("[Item creat correctament]\n" + item);
                    } else if (!input.equals("")) System.out.println("[ERROR: Opcio invalida]");
                    System.out.println("[Creacio dels items del conjunt]\n" +
                            "  1 -> Crear un item\n" +
                            "  0 -> Continuar amb el calcul de distancies");
                    input = scanner.nextLine();
                    if (input.equals("0") && items.size() == 0) {
                        System.out.println("[ERROR: Items insuficients al conjunt]");
                        input = "";
                    }
                }
                Set<Item> conjunt = new HashSet<>();
                for (Integer id : items.keySet()) conjunt.add(items.get(id));
                HashMap<String, Integer> maximsCategorics = new HashMap<>();
                HashMap<String, Double> maximsNumerics = new HashMap<>();
                double distanciaMaxima = Distancia.calcularDistanciaMaxima(conjunt, maximsCategorics, maximsNumerics);

                input = "";
                while (!input.equals("0")) {
                    if (input.equals("1")) {
                        System.out.println("- Introdueix l'id de l'item A del conjunt");
                        int id = -1;
                        input = scanner.nextLine();
                        boolean valid = false;
                        while (!valid) {
                            try {
                                id = Integer.parseUnsignedInt(input);
                                valid = true;
                            } catch (NumberFormatException e) {
                                System.out.println("[ERROR: Identificador invalid]");
                            }
                            if (valid && !items.containsKey(id)) {
                                System.out.println("[ERROR: No existeix cap item amb l'identificador especificat]");
                                valid = false;
                            }
                            if (!valid) input = scanner.nextLine();
                        }
                        Item itemA = items.get(id);
                        System.out.println("- Introdueix l'id de l'item B del conjunt");
                        input = scanner.nextLine();
                        valid = false;
                        while (!valid) {
                            try {
                                id = Integer.parseUnsignedInt(input);
                                valid = true;
                            } catch (NumberFormatException e) {
                                System.out.println("[ERROR: Identificador invalid]");
                            }
                            if (valid && !items.containsKey(id)) {
                                System.out.println("[ERROR: No existeix cap item amb l'identificador especificat]");
                                valid = false;
                            }
                            if (!valid) input = scanner.nextLine();
                        }
                        Item itemB = items.get(id);
                        double distanciaItems = Distancia.calcularDistancia(itemA, itemB, maximsNumerics, distanciaMaxima);
                        System.out.println("Distancia(Item "+itemA.getId()+", Item "+itemB.getId()+")="+distanciaItems);
                    }
                    else if (!input.equals("")) System.out.println("[ERROR: Opcio invalida]");
                    System.out.println(
                            "[Calcul de distancies d'items del conjunt]\n" +
                                    "1 -> Nou calcul\n" +
                                    "0 -> Finalitza aquesta execucio");
                    input = scanner.nextLine();
                }


            }
            else if (!input.equals("")) System.out.println("[ERROR: Opcio invalida]");
            System.out.println(
                    "[Escull una opcio]\n" +
                            "1 -> Nova execucio\n" +
                            "0 -> Sortir");
            input = scanner.nextLine();
        }
    }
}
