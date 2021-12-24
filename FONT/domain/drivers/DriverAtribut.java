package domain.drivers;

import domain.*;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DriverAtribut {
    public static void main(String[] args) {
        Atribut a;
        System.out.println("[Testing de la classe domain.Atribut]");
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals("0")) {
            if (input.equals("1")) {
                System.out.println("[Creacio d'un atribut numeric]\n" +
                        "- Introdueix el valor numeric:");
                input = scanner.nextLine();
                double n = 0;
                boolean valid = false;
                while (!valid) {
                    try {
                        n = Double.parseDouble(input);
                        valid = true;
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR: Valor invalid]");
                    }
                    if (!valid) input = scanner.nextLine();
                }
                a = new AtributNumeric(n);
                System.out.println("[Atribut numeric creat satisfactoriament]\n" +
                        "Valor=" + a);
            }
            else if (input.equals("2")) {
                System.out.println("[Creacio d'un atribut boolea]\n" +
                        "- Introdueix el valor boolea (true/false):");
                input = scanner.nextLine();
                boolean b = false;
                boolean valid = false;
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
                a = new AtributBoolea(b);
                System.out.println("[Atribut boolea creat satisfactoriament]\n" +
                        "Valor=" + a);
            }
            else if (input.equals("3")) {
                System.out.println("[Creacio d'un atribut descriptiu]\n" +
                        "- Introdueix el valor descriptiu:");
                input = scanner.nextLine();
                a = new AtributDescriptiu(input);
                System.out.println("[Atribut descriptiu creat satisfactoriament]\n" +
                        "Valor=" + a);
            }
            else if (input.equals("4")) {
                System.out.println("[Creacio d'un atribut categoric]\n" +
                        "- Introdueix els valors categorics (salt de linia per acabar):");
                Set<String> s = new HashSet<>();
                input = scanner.nextLine();
                while (!input.equals("")) {
                    s.add(input);
                    input = scanner.nextLine();
                }
                AtributCategoric aC = new AtributCategoric();
                for (String valor : s) aC.afegirValor(valor);
                a = aC;
                System.out.println("[Atribut categoric creat satisfactoriament]\n" +
                        "Valor=" + a);
            }
            else if (!input.equals("")) System.out.println("[ERROR: Opcio invalida]");
            System.out.println("[Escull el tipus de l'atribut a ser creat]\n" +
                    "1 -> Atribut numeric\n" +
                    "2 -> Atribut boolea\n" +
                    "3 -> Atribut descriptiu\n" +
                    "4 -> Atribut categoric\n" +
                    "0 -> Sortir");
            input = scanner.nextLine();
        }
    }
}

