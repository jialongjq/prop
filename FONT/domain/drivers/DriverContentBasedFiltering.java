package domain.drivers;

import domain.*;
import domain.algorismes.ContentBasedFiltering;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

public class DriverContentBasedFiltering {
    public static void main(String[] args) throws Exception {
        System.out.println("[Testing de l'algorisme ContentBasedFiltering]");
        CtrlDomini ctrlDomini = CtrlDomini.getInstance();

        System.out.println("- Carrega del conjunt d'items del fitxer DATA/items-driver.csv="+ctrlDomini.carregarItemsCSV("movies", "items-driver.csv"));
        System.out.println("- Carrega del conjunt de valoracions del fitxer DATA/ratings.db-driver.csv="+(ctrlDomini.carregarValoracionsCSV(0, 5, "ratings.db-driver.csv")==0));

        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (!input.equals("0")) {
            if (input.equals("1")) {
                System.out.println("[Introdueix l'id de l'usuari al qual es vol aplicar l'algorisme]");
                input = scanner.nextLine();
                int id = -1;
                boolean valid = false;
                while (!valid) {
                    try {
                        id = Integer.parseInt(input);
                        valid = true;
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR: Identificador invalid]");
                    }
                    if (valid && !ctrlDomini.existsUsuari(id)) {
                        System.out.println("[ERROR: L'usuari amb id "+id+" no existeix]");
                        valid = false;
                    }
                    if (!valid) input = scanner.nextLine();
                }
                System.out.println("[Introdueix el valor de k]");
                input = scanner.nextLine();
                int k = -1;
                valid = false;
                while (!valid) {
                    try {
                        k = Integer.parseUnsignedInt(input);
                        valid = true;
                    } catch (NumberFormatException e) {
                        System.out.println("[ERROR: Valor invalid]");
                    }
                    if (!valid) input = scanner.nextLine();
                }
                System.out.println("[Aplicacio de l'algorisme ContentBasedFiltering a l'usuari amb id="+id+" i k="+k+"]\n" +
                        "- Conjunt d'items valorats: aquells items valorats per l'usuari amb una puntuacio superior a 3.5\n" +
                        "- Conjunt d'items: tots els items carregats");
                Set<Item> itemsValorats = ctrlDomini.getItemsValorats(id, 3.5);
                Set<Item> conjuntItems = ctrlDomini.getItems("movies");

                ContentBasedFiltering content = new ContentBasedFiltering(k, itemsValorats, conjuntItems);
                content.resol();
                ArrayList<Item> recomanacions = content.getResult();
                System.out.println("[Items recomanats (ordenats per rellevancia)]");
                for (Item i : recomanacions) System.out.print(i.getId() + " ");
                System.out.println();
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
