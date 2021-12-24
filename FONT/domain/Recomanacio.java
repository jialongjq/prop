package domain;

/**
 * @author Roberto Navarro Morales
 */

public class Recomanacio {
    private int idUsuari;
    private int idItem; 
    private double puntuacio;

    public Recomanacio(int usuari,int item,double p) {
        this.idUsuari = usuari; 
        this.idItem = item; 
        this.puntuacio = p;
    }

    public double getPuntuacio() {
        return puntuacio; 
    }
    
    public int getIdUsuari() {
    	return idUsuari; 
    }
    
    public int getIdItem() {
    	return idItem; 
    }
    
    public void setPuntuacio(double p) {
    	puntuacio = p; 
    }
 
    @Override
    public String toString() {
        return "Recomanacio [idItem=" + idItem + ", idUsuari=" + idUsuari + ", puntuacio=" + puntuacio + "]";
    }
}

