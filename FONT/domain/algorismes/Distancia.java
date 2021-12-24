package domain.algorismes;
import domain.*;
import java.util.*;
import java.math.*;

/**
 * @author Jia Long Ji Qiu
 */

public final class Distancia {
    /**
     * Compara dos atributs categorics
     * @param a
     * @param b
     * @return el numero de coincidencies entre els valors dels atributs categorics
     *
     */
    public static double compararCategorics(Set<String> a, Set<String> b) {
        double ret = 0;
        for (String valor : a) {
            if (b.contains(valor)) ret += 1;
        }
        return ret;
    }
    /**
     * Compara dos atributs numerics, donat un maxim que pot assolir l'atribut corresponent.
     * @param a
     * @param b
     * @return un valor entre [0, 1]
     *
     */
    public static double compararNumerics(double a, double b, double max) {
        if (max == 0) return 1;
        return 1 - Math.abs(a/max - b/max);
    }
    /**
     * Compara dos atributs booleans
     * @param a
     * @param b
     * @return 1 si els atributs coincideixen
     *         0 si son diferents
     */
    public static double compararBooleans(boolean a, boolean b) {
        if (a == b) return 1;
        return 0;
    }
    /**
     * Compara dos atributs descriptius
     * @param a
     * @param b
     * @return 1 si els atributs coincideixen
     *         0 si son diferents
     */
    public static double compararDescriptius(String a, String b) {
        if (a.equals(b) && !a.equals("")) return 1;
        return 0;
    }

    /**
     * Retorna la distancia maxima que es pot assolir dins un conjunt d'items. Guarda els maxims categorics i numerics corresponents.
     * @param conjuntItems conjunt d'items del qual es vol calcular la distancia maxima
     * @param maximsCategorics guarda la quantitat maxima de categories per cada atribut
     * @param maximsNumerics guarda els valors maxims per cada atribut
     * @return distancia maxima dins d'un conjunt
     */
    public static double calcularDistanciaMaxima(Set<Item> conjuntItems, HashMap<String, Integer> maximsCategorics, HashMap<String, Double> maximsNumerics) {
        double distanciaMaxima = 0;
        HashMap<String, TipusItem.TAtribut> tipusAtributs = conjuntItems.iterator().next().getTipusItem().getTipusAtributs();
        for (String nomAtribut : tipusAtributs.keySet()) {
            TipusItem.TAtribut t = tipusAtributs.get(nomAtribut);
            if (t == TipusItem.TAtribut.CATEGORIC || t == TipusItem.TAtribut.NUMERIC) {
                if (t == TipusItem.TAtribut.NUMERIC) ++distanciaMaxima;
                for (Item item : conjuntItems) {
                    if (item.existsAtribut(nomAtribut)) {
                        Atribut atribut = item.getAtribut(nomAtribut);
                        if (t == TipusItem.TAtribut.CATEGORIC) {
                            int sizeCategoric = ((Set<String>) atribut.getValor()).size();
                            if (!maximsCategorics.containsKey(nomAtribut)) {
                                maximsCategorics.put(nomAtribut, sizeCategoric);
                            } else {
                                if (maximsCategorics.get(nomAtribut) < sizeCategoric) {
                                    maximsCategorics.put(nomAtribut, sizeCategoric);
                                }
                            }
                        } else {
                            double valorNumeric = Math.abs((double) atribut.getValor());
                            if (!maximsNumerics.containsKey(nomAtribut)) {
                                maximsNumerics.put(nomAtribut, valorNumeric);
                            } else {
                                if (maximsNumerics.get(nomAtribut) < valorNumeric)
                                    maximsNumerics.put(nomAtribut, valorNumeric);
                            }
                        }
                    }
                }
            }
            else {
                ++distanciaMaxima;
            }
        }
        for (String s : maximsCategorics.keySet()) distanciaMaxima += maximsCategorics.get(s);
        //System.out.println(distanciaMaxima);
        return distanciaMaxima;
    }

    /**
     * Calcula la distancia entre dos items. Es el resultat de [numero total de coincidencies possibles] - [numero de coincidencies] entre cada atribut dels dos items.
     * @param a item1
     * @param b item2
     * @param maximsNumerics indica quin es el valor maxim de cada tipus d'atribut numeric entre els atributs corresponents d'un conjunt d'items. Util per a la normalitzacio de valors numerics.
     * @param distanciaMaxima definit com el numero total d'atributs. Per a un cert conjunt d'items, els atributs numerics, booleans i descriptius valen 1. En el cas dels categorics, cadascun val el maxim del numero de categories entre els atributs corresponents del conjunt d'items
     * @return distancia entre dos items
     */
    public static double calcularDistancia(Item a, Item b, HashMap<String, Double> maximsNumerics, double distanciaMaxima) {
        TipusItem tipusItem = a.getTipusItem();
        double puntuacio = 0;
        for (String nomAtribut : tipusItem.getTipusAtributs().keySet()) {
            if (!nomAtribut.equals("id") && a.existsAtribut(nomAtribut) && b.existsAtribut(nomAtribut)) {
                TipusItem.TAtribut tipus = tipusItem.getTipusAtributs().get(nomAtribut);
                if (tipus == TipusItem.TAtribut.NUMERIC) {
                    double valorA = (double)a.getAtribut(nomAtribut).getValor();
                    double valorB = (double)b.getAtribut(nomAtribut).getValor();
                    puntuacio += compararNumerics(valorA, valorB, maximsNumerics.get(nomAtribut));
                }
                else if (tipus == TipusItem.TAtribut.CATEGORIC) {
                    Set<String> valorA = (Set<String>)a.getAtribut(nomAtribut).getValor();
                    Set<String> valorB = (Set<String>)b.getAtribut(nomAtribut).getValor();
                    puntuacio += compararCategorics(valorA, valorB);
                }
                else if (tipus == TipusItem.TAtribut.BOOLEA) {
                    boolean valorA = (boolean)a.getAtribut(nomAtribut).getValor();
                    boolean valorB = (boolean)b.getAtribut(nomAtribut).getValor();
                    puntuacio += compararBooleans(valorA, valorB);
                }
                else if (tipus == TipusItem.TAtribut.DESCRIPTIU) {
                    String valorA = (String)a.getAtribut(nomAtribut).getValor();
                    String valorB = (String)b.getAtribut(nomAtribut).getValor();
                    puntuacio += compararDescriptius(valorA, valorB);
                }
            }
        }
        return distanciaMaxima - puntuacio;
    }
}
