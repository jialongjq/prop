package domain;

import java.util.HashMap;

import data.JsonAdd;
import data.JsonIgnore;
import data.JsonKeyValue;

/**
 * @author Jia Long Ji Qiu
 */

public class TipusItem {
	public enum TAtribut
	{
		NUMERIC,
		BOOLEA,
		CATEGORIC,
		DESCRIPTIU
	}

	private String nom;
	
	@JsonAdd("afegirTipusAtribut")
	@JsonKeyValue()
	private HashMap<String, TAtribut> tipusAtributs;

	/**
	 * Constructora d'un tipus d'item a partir d'un nom
	 * @param nom
	 */
	public TipusItem(String nom) {
		this.nom = nom;
		this.tipusAtributs = new HashMap<>();
	}

	/**
	 * Getter de l'atribut nom
	 * @return nom del tipus d'item
	 */
	public String getNom() 
	{
		return nom;
	}

	/**
	 * Getter de tots els tipus d'atributs
	 * @return tipus d'atributs
	 */
	public HashMap<String, TAtribut> getTipusAtributs() { return tipusAtributs; }

	/**
	 * Getter d'un TAtribut donat un nom d'atribut
	 * @param nom nom de l'atribut
	 * @return el TAtribut d'aquell atribut
	 */
	public TAtribut getTAtribut(String nom) {
		return tipusAtributs.get(nom);
	}

	/**
	 * Setter dels tipus d'atributs (util per a la carrega de fitxers CSV d'items)
	 * @param tipus tipus d'atributs
	 */
	public void setTipusAtributs(HashMap<String, TAtribut> tipus) {
		tipusAtributs = tipus;
	}

	/**
	 * Afegeix un atribut donat un nom i el seu TAtribut
	 * @param nom nom de l'atribut
	 * @param tipus TAtribut
	 */
	public void afegirTipusAtribut(String nom, TAtribut tipus)
	{
		tipusAtributs.put(nom, tipus);
	}

	/**
	 * Canvia el nom d'atribut
	 * @param nom1 nom de l'atribut del qual es vol canviar el nom
	 * @param nom2 nou nom
	 */
	public void canviarNomTipusAtribut(String nom1, String nom2) {
		// Hauria de ser precondicio pero per si de cas
		if (tipusAtributs.containsKey(nom1)) tipusAtributs.put(nom2, tipusAtributs.remove(nom1));
	}

	/**
	 * Elimina un atribut del tipus d'item donat un nom
	 * @param nom nom de l'atribut el qual es vol eliminar
	 */
	public void eliminarTipusAtribut(String nom) {
		tipusAtributs.remove(nom);
	}

	@Override
	public String toString() {
		String ret = "TipusItem [nom=" + nom + "]";
		for (String nomAtribut : tipusAtributs.keySet()) {
			ret+="\nnomAtribut="+nomAtribut+", tipusAtribut="+tipusAtributs.get(nomAtribut);
		}
		return ret;
	}
}
