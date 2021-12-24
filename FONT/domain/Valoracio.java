package domain;

/**
 * @author Jia Long Ji Qiu
 */

public class Valoracio {
    int idItem;
    int idUsuari;
    double puntuacio;

    /**
     * Constructora d'una valoracio a partir d'un id d'item, un id d'usuari i una puntuacio
     * @param idItem id de l'item
     * @param idUsuari id de l'usuari
     * @param puntuacio puntuacio
     */
    public Valoracio(int idItem, int idUsuari, double puntuacio) {
        this.idItem = idItem;
        this.idUsuari = idUsuari;
        this.puntuacio = puntuacio;
    }

    /**
     * Getter de l'id de l'item valorat
     * @return id de l'item
     */
    public int getIdItem() { return idItem; }

    /**
     * Getter de l'id de l'usuari que ha fet la valoracio
     * @return id de l'usuari
     */
    public int getIdUsuari() { return idUsuari; }

    /**
     * Getter de la puntuacio d'una valoracio
     * @return puntuacio
     */
    public double getPuntuacio() { return puntuacio; }

    /**
     * Setter de la puntuacio d'una valoracio
     * @param puntuacio nova puntuacio
     */
    public void setPuntuacio(double puntuacio) {
        this.puntuacio = puntuacio;
    }

    @Override
    public String toString() {
        return "Valoracio [idItem=" + idItem + ", idUsuari=" + idUsuari + ", puntuacio=" + puntuacio + "]";
    }
}
