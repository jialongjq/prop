package domain;

/**
 * @author Jia Long Ji Qiu
 */

public class Usuari {

	private int id;
	private String nom;
	private String contrasenya;
	private boolean admin;

	/**
	 * Constructora d'un usuari a partir d'un id, un nom, una contrasenya i un indicador de si es admin o no
	 * @param id
	 * @param nom
	 * @param contrasenya
	 * @param admin
	 */
	public Usuari(int id, String nom, String contrasenya, boolean admin) {
		this.id = id;
		this.nom = nom;
		this.contrasenya = contrasenya;
		this.admin = admin;
	}

	/**
	 * Getter de l'id de l'usuari
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Getter del nom de l'usuari
	 * @return nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Getter de la contrasenya de l'usuari
	 * @return contrasenya
	 */
	public String getContrasenya() {
		return contrasenya;
	}

	/**
	 * Setter del nom de l'usuari
	 * @param nom nou nom de l'usuari
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * Setter de la contrasenya de l'usuari
	 * @param contrasenya nova contrasenya
	 */
	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}

	/**
	 * Comprova si l'usuari es admin
	 * @return true si l'usuari es admin
	 * 		   false si no
	 */
	public boolean isAdmin() {
		return admin;
	}

	@Override
	public String toString() {
		return "Usuari [id=" + id + ", nom=" + nom + ", contrasenya=" + contrasenya + ", admin=" + admin + "]";
	}
	
}
