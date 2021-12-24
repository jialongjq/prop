package domain.drivers;

import domain.CtrlDomini;
import domain.CtrlUsuari;
import domain.Usuari;

import java.util.Scanner;

public class DriverCtrlUsuari {
    public static void main(String[] args) throws Exception {
        CtrlDomini ctrlDomini = CtrlDomini.getInstance(); // Necessari per inicialitzar els controladors del domini i dades
        CtrlUsuari ctrlUsuari = CtrlUsuari.getInstance();
        int retVal;
        String nomUsuari;
        String id;
        String contrasenya;
        String confirmaContrasenya;

        System.out.println();

        System.out.println("[Carrega d'usuaris del fitxer DATA/usuaris.json]");
        ctrlUsuari.carregarUsuaris();
        System.out.println("Usuaris carregats correctament");
        ctrlUsuari.printUsuaris();
        ctrlUsuari.guardarUsuaris();

        System.out.println();

        System.out.println("Escull una opció del testing (1 a 8)\n" +
                "1 -> RegistrarUsuari\n" +
                "2 -> RegistrarUsuariIdEspecific\n" +
                "3 -> LoginUsuari\n" +
                "4 -> LogoutUsuariActiu\n" +
                "5 -> CanviarContrasenya\n" +
                "6 -> CanviarNom\n" +
                "7 -> ExisteixUsuari\n" +
                "8 -> Sortir");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        while (!input.equals("8")) {
            if (input.equals("1")) {
                System.out.println("[Registrar un usuari nou]");
                System.out.println("Introduiu el nom de l'usuari:");
                nomUsuari = scanner.nextLine();
                System.out.println("Introduiu la contrasenya de l'usuari:");
                contrasenya = scanner.nextLine();
                System.out.println("Introduiu la confirmacio de la contrasenya de l'usuari:");
                confirmaContrasenya = scanner.nextLine();
                System.out.println("Indiqueu si l'usuari que voleu registrar es administrador o no: true/false");
                String admin = scanner.nextLine();
                retVal = ctrlUsuari.registrarUsuari(nomUsuari, contrasenya, confirmaContrasenya, Boolean.parseBoolean(admin));
                if (retVal == 0) System.out.println("Usuari registrat correctament");
                else if (retVal == -1) System.out.println("El nom especificat no esta disponible");
                else if (retVal == -2) System.out.println("Les contrasenyes no coincideixen");
                ctrlUsuari.printUsuaris();
            } else if (input.equals("2")) {
                System.out.println("[Registrar un usuari amb un id especific]");
                System.out.println("Introduiu l'id de l'usuari:");
                id = scanner.nextLine();
                System.out.println("Introduiu el nom de l'usuari:");
                nomUsuari = scanner.nextLine();
                System.out.println("Introduiu la contrasenya de l'usuari:");
                contrasenya = scanner.nextLine();
                System.out.println("Introduiu la confirmacio de la contrasenya de l'usuari:");
                confirmaContrasenya = scanner.nextLine();
                System.out.println("Indiqueu si l'usuari que voleu registrar es administrador o no: true/false");
                String admin = scanner.nextLine();
                retVal = ctrlUsuari.registrarUsuariId(Integer.parseInt(id), nomUsuari, contrasenya, confirmaContrasenya, Boolean.parseBoolean(admin));
                if (retVal == 0) System.out.println("Usuari registrat correctament");
                else if (retVal == -1) System.out.println("El nom especificat no esta disponible");
                else if (retVal == -2) System.out.println("Les contrasenyes no coincideixen");
                else if (retVal == -3) System.out.println("L'id no esta disponible");
            } else if (input.equals("3")) {
                System.out.println("[Login de l'usuari]");
                System.out.println("Introduiu el nom de l'usuari:");
                nomUsuari = scanner.nextLine();
                System.out.println("Introduiu la contrasenya de l'usuari:");
                contrasenya = scanner.nextLine();
                retVal = ctrlUsuari.loginUsuari(nomUsuari, contrasenya);
                if (retVal == 0) System.out.println("Usuari loguejat correctament");
                else if (retVal == -1) System.out.println("El nom especificat no esta registrat");
                else if (retVal == -2) System.out.println("La contrasenya es incorrecta");
                System.out.println("idUsuariActiu=" + ctrlUsuari.getIdUsuariActiu());
            } else if (input.equals("4")) {
                System.out.println("[Logout de l'usuari actiu]");
                retVal = ctrlUsuari.logoutUsuari();
                if (retVal == 0) System.out.println("Sessio tancada correctament");
                else if (retVal == -1) System.out.println("No hi ha un usuari actiu");
                System.out.println("idUsuariActiu=" + ctrlUsuari.getIdUsuariActiu());
            } else if (input.equals("5")) {
                System.out.println("[Canviar la contrasenya de l'usuari]");
                System.out.println("Introduiu la nova contrasenya");
                contrasenya = scanner.nextLine();
                System.out.println("Introduiu la confirmacio de la nova contrasenya");
                confirmaContrasenya = scanner.nextLine();
                retVal = ctrlUsuari.canviarContrasenya(contrasenya, confirmaContrasenya);
                if (retVal == 0) System.out.println("Canvi de contrasenya efectuada correctament");
                else if (retVal == -1) System.out.println("No hi ha un usuari actiu");
                else if (retVal == -2) System.out.println("Les contrasenyes no coincideixen");
                else if (retVal == -3) System.out.println("La nova contrasenya no pot ser igual que la anterior");
            } else if (input.equals("6")) {
                System.out.println("[Canviar el nom de l'usuari]");
                System.out.println("Introduiu el nou nom:");
                nomUsuari = scanner.nextLine();
                retVal = ctrlUsuari.canviarNom(nomUsuari);
                if (retVal == 0) System.out.println("Canvi de nom efectuat correctament");
                else if (retVal == -1) System.out.println("No hi ha un usuari actiu");
                else if (retVal == -2) System.out.println("L'usuari ja te el nom especificat");
                else if (retVal == -3) System.out.println("El nom no esta disponible");
                System.out.println("idUsuariActiu=" + ctrlUsuari.getIdUsuariActiu());
            } else if (input.equals("7")) {
                System.out.println("[Comprova si existeix l'usuari]");
                System.out.println("Introduiu id de l'usuari:");
                id = scanner.nextLine();
                System.out.println(ctrlUsuari.existsUsuari(Integer.parseInt(id)));
            }
            System.out.println();
            System.out.println("Escull una opció del testing (1 a 8)\n" +
                    "1 -> RegistrarUsuari\n" +
                    "2 -> RegistrarUsuariIdEspecific\n" +
                    "3 -> LoginUsuari\n" +
                    "4 -> LogoutUsuariActiu\n" +
                    "5 -> CanviarContrasenya\n" +
                    "6 -> CanviarNom\n" +
                    "7 -> ExisteixUsuari\n" +
                    "8 -> Sortir");
            input = scanner.nextLine();
        }
        System.out.println("\n[Guarda la llista d'usuaris carregats al controlador al fitxer DATA/usuaris.json (comprovar el fitxer)]");
        ctrlUsuari.guardarUsuaris();
        System.out.println("\nFi l'execucio driver classe CtrlUsuari");
    }
}
