package domain;

import data.CtrlDades;
import utils.Hash;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jia Long Ji Qiu
 */

public class CtrlUsuari {
    private static CtrlUsuari singletonObject;
    private CtrlDades ctrlDades;
    private CtrlValoracio ctrlValoracio;
    private HashMap<Integer, Usuari> usuaris;
    private HashMap<String, Integer> idNomUsuari;
    private int idUsuariActiu;
    private int last; // Mantindra en tot moment el seguent id disponible

    public static CtrlUsuari getInstance() throws Exception {
        if (singletonObject == null)
            singletonObject = new CtrlUsuari();
        return singletonObject;
    }

    /**
     * Constructora privada del controlador d'usuari
     */
    private CtrlUsuari() {
        this.usuaris = new HashMap<>();
        this.idNomUsuari = new HashMap<>();
        this.idUsuariActiu = -1;
        this.last = 0;
    }

    /**
     * Assigna el controlador de dades al controlador d'usuari
     * @param ctrlDades controlador de dades
     */
    public void setCtrlDades(CtrlDades ctrlDades) {
        this.ctrlDades = ctrlDades;
    }

    /**
     * Assigna el controlador de valoracions al controlador d'usuari
     * @param ctrlValoracio controlador de valoracio
     */
    public void setCtrlValoracio(CtrlValoracio ctrlValoracio) { this.ctrlValoracio = ctrlValoracio; }

    /**
     * Getter dels usuaris carregats al controlador
     * @return usuaris
     */
    public HashMap<Integer, Usuari> getUsuaris() {
        return usuaris;
    }

    /**
     * Carrega els usuaris guardats a la base de dades (fitxer DATA/usuaris.json)
     * @throws Exception
     */
    public void carregarUsuaris() throws Exception {
        List<Usuari> llista = ctrlDades.loadGeneric(Usuari.class, "usuaris.json", "usuaris");
        last = -1;
        for (Usuari u : llista) {
            int id = u.getId();
            if (id > last) last = id;
            usuaris.put(id, u);
            idNomUsuari.put(u.getNom(), id);
        }
        last += 1;
    }

    /**
     * Guarda els usuaris carregats al controlador a la base de dades (fitxer DATA/usuaris.json)
     * @throws Exception
     */
    public void guardarUsuaris() throws Exception {
        List<Usuari> llista = new ArrayList<>();
        llista.addAll(usuaris.values());
        
        ctrlDades.saveGeneric(llista, Usuari.class, "usuaris.json", "usuaris");
    }

    /**
     * Registra un usuari amb nom "nom" i contrasenya "contrasenya1". L'id esta determinat per max{id}+1 i sera admin si admin == true
     * @param nom
     * @param contrasenya1
     * @param contrasenya2
     * @param admin
     * @return 0 si s'ha registrat correctament
     *         -1 si ya existeix un usuari amb el nom especificat
     *         -2 si les contrasenyes no coincideixen
     * @throws Exception
     */
    public int registrarUsuari(String nom, String contrasenya1, String contrasenya2, boolean admin) throws Exception {
        if (idNomUsuari.containsKey(nom)) return -1;
        if (!contrasenya1.equals(contrasenya2)) return -2;

        int idUsuari = last;
        String hash = Hash.getHash(contrasenya1, idUsuari);
        Usuari usuari = new Usuari(idUsuari, nom, hash, admin);
        usuaris.put(idUsuari, usuari);
        idNomUsuari.put(nom, idUsuari);
        ++last;
        return 0;
    }

    /**
     * Registra un usuari amb un id especific
     * @param id id de l'usuari
     * @param nom nom de l'usuari
     * @param contrasenya1 contrasenya de l'usuari
     * @param contrasenya2 confirmacio de la contrasenya
     * @param admin
     * @return 0 si s'ha registrat correctament
     *         -1 si el nom no esta disponible
     *         -2 si les contrasenyes no coincideixen
     *         -3 si l'id no esta disponible
     * @throws Exception
     */
    public int registrarUsuariId(int id, String nom, String contrasenya1, String contrasenya2, boolean admin) throws Exception {
        if (idNomUsuari.containsKey(nom)) return -1;
        if (!contrasenya1.equals(contrasenya2)) return -2;
        if (usuaris.containsKey(id)) return -3;

        String hash = Hash.getHash(contrasenya1, id);
        Usuari usuari = new Usuari(id, nom, hash, admin);
        usuaris.put(id, usuari);
        idNomUsuari.put(nom, id);

        int max = -1;
        for (Integer idUsuari : usuaris.keySet()) {
            if (idUsuari > max) max = idUsuari;
        }
        last = max + 1;

        return 0;
    }

    /**
     * Login usuari (passa a ser l'usuari actiu)
     * @param nom
     * @param contrasenya
     * @return 0 si s'han validat les credencials correctament
     *         -1 si el nom especificat no existeix
     *         -2 si la contrasenya no coincideix
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public int loginUsuari(String nom, String contrasenya) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (!idNomUsuari.containsKey(nom)) return -1;
        int id = idNomUsuari.get(nom);
        String hash = Hash.getHash(contrasenya, id);
        if (!hash.equals(usuaris.get(id).getContrasenya())) return -2;
        idUsuariActiu = id;
        return 0;
    }

    /**
     * Logout de l'usuari actiu
     * @return 0 si s'ha pogut realitzar el logout correctament
     *         -1 si no hi havia usuari actiu
     */
    public int logoutUsuari() {
        if (idUsuariActiu == -1)
            return -1;
        idUsuariActiu = -1;
        return 0;
    }

    /**
     * L'usuari actiu vol canviar la contrasenya
     * @param contrasenya1
     * @param contrasenya2
     * @return 0 si el canvi s'ha pogut dur a terme correctament
     *         -1 si no existeix un usuari actiu
     *         -2 si les contrasenyes no coincideixen
     *         -3 si la nova contrasenya coincideix amb la que ja te
     */
    public int canviarContrasenya(String contrasenya1, String contrasenya2) throws Exception {
        if (idUsuariActiu == -1) return -1;
        if (!contrasenya1.equals(contrasenya2)) return -2;
        if (Hash.getHash(contrasenya1, idUsuariActiu).equals(usuaris.get(idUsuariActiu).getContrasenya())) return -3;
        usuaris.get(idUsuariActiu).setContrasenya(Hash.getHash(contrasenya1, idUsuariActiu));
        return 0;
    }

    /**
     * Canvia el nom de l'usuari actiu
     * @param nom
     * @return 0 si s'ha canviat el nom exitosament
     *         -1 si no existeix un usuari actiu
     *         -2 si l'usuari ja te el nom "nom"
     *         -3 si el nom no esta disponible
     * @throws Exception
     */
    public int canviarNom(String nom) throws Exception {
        if (idUsuariActiu == -1) return -1;
        if (usuaris.get(idUsuariActiu).getNom().equals(nom)) return -2;
        if (idNomUsuari.containsKey(nom)) return -3;
        idNomUsuari.remove(usuaris.get(idUsuariActiu).getNom());
        usuaris.get(idUsuariActiu).setNom(nom);
        idNomUsuari.put(nom, idUsuariActiu);
        return 0;
    }

    /**
     * Elimina un usuari registrat i totes les valoracions que hagi fet
     * @param id
     * @return 0 si s'ha eliminat l'usuari amb id especificat exitosament
     *         -1 si l'usuari actiu no existeix
     *         -2 si l'usuari actiu no es admin
     *         -3 si no s'ha trobat un usuari amb l'id especificat
     *         -4 si l'usuari que es vol eliminar es l'usuari actiu
     */
    public int eliminarUsuari(int id) {
        if (idUsuariActiu == -1) return -1;
        if (!usuaris.get(idUsuariActiu).isAdmin()) return -2;
        if (!usuaris.containsKey(id)) return -3;
        if (id == idUsuariActiu) return -4;
        String nomUsuariEliminat = usuaris.get(id).getNom();

        ctrlValoracio.eliminarValoracionsUsuari(id);

        usuaris.remove(id);

        idNomUsuari.remove(nomUsuariEliminat);
        return 0;
    }

    /**
     * Comprova si existeix un usuari registrat amb id "idUsuari"
     * @param idUsuari id de l'usuari que es vol comprovar
     * @return true si existeix
     *         false si no existeix
     */
    public boolean existsUsuari(int idUsuari) {
        return usuaris.containsKey(idUsuari);
    }

    /**
     * Retorna l'id de l'usuari actiu
     * @return id de l'usuari actiu
     */
    public int getIdUsuariActiu() { return idUsuariActiu; }

    /**
     * Output de tots els usuaris carregats al controlador
     */
    public void printUsuaris() {
        for (Integer id : usuaris.keySet()) {
            System.out.println(usuaris.get(id));
        }
    }
}
