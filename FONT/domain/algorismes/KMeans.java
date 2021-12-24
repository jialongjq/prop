package domain.algorismes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

/**
 * Algorisme KMeans
 * 
 * @author Roberto Navarro Morales
 *
 */
public class KMeans implements Algorisme {
	/**
	 * Parametre principal, el nombre de baricentres
	 */
	private final int k;
	
	/**
	 * La dimensio dels vectors proporcionats
	 */
	private final int d;
	
	/**
	 * Limit d'iteracions
	 */
	private final int lim;
	
	/**
	 * La iteracio actual
	 */
	private int iter;
	
	/**
	 * Baricentres de la iteracio i
	 */
	private ArrayList<ArrayList<Double>> baricentres;
	
	/**
	 * Baricentres de la iteracio i - 1
	 */
	private ArrayList<ArrayList<Double>> prevBaricentres;
	
	/**
	 * n vectors de d dimensions que defineixen el conjunt de punts
	 */
	private ArrayList<ArrayList<Double>> data;
	
	
	/**
	 * [0, k) clusters, cada cluster te una llista on cada
	 * valor es correspon a data[i]
	 */
	private ArrayList<ArrayList<Integer>> clusters;
	
	/**
	 * Nivell del log, si esta a true s'escriura informacio per la sortida estandard
	 */
	boolean verbose;
	
	/**
	 * Constuctor
	 * @param k
	 * @param data n vectors de d dimensions
	 * @param baricentres centres imposats inicialment (si = null es trien random)
	 * @throws Exception
	 */
	public KMeans(int k, ArrayList<ArrayList<Double>> data, ArrayList<ArrayList<Double>> baricentres) throws Exception
	{
		this.k = k;
		
		if (data.isEmpty())
			throw new Exception("KMeans: no s'ha rebut cap punt");
		
		if (baricentres != null && k != baricentres.size())
			throw new Exception("KMeans: els baricentres proposats i la k no coincideixen");
		
		this.lim = 30;
		this.iter = 0;
		this.d = data.get(0).size();
		
		// Validar que la dimensio de tots els punts es correcta
		for (int i = 1; i < data.size(); i++) {
			if (data.get(i).size() != this.d)
				throw new Exception("KMeans: rebut un punt amb una dimensio incorrecta");
		}
	
		this.data = data;
		
		this.clusters = new ArrayList<ArrayList<Integer>>();
		
		if (baricentres == null) {
			this.baricentres = new ArrayList<ArrayList<Double>>();
			this.prevBaricentres = new ArrayList<ArrayList<Double>>();
			
			initBaricentres();
		}
		else {
			this.baricentres = new ArrayList<ArrayList<Double>>(baricentres);
			this.prevBaricentres = new ArrayList<ArrayList<Double>>(baricentres);
		}
		
		initClusters();
	}
	
	/**
	 * Inicialitzacio dels baricentres de manera aleatoria
	 */
	private void initBaricentres()
	{
		// Punts definits com a baricentres, per a evitar tenir 
		// dos baricentres inicialment solapats
		HashSet<Integer> defined = new HashSet<Integer>();
		
		Random rnd = new Random();
		rnd.setSeed(0);
		
		// Definir k punts de manera aleatoria com a baricentres inicials
		for (int i = 0; i < this.k; i++) {
			int p = 0;
			
			while(true) {
				int n = this.data.size();
				
				p = rnd.nextInt(n);
				
				if (defined.add(p))
					break;
			}
			
			this.baricentres.add(new ArrayList<Double>(this.data.get(p)));
			this.prevBaricentres.add(new ArrayList<Double>(this.data.get(p)));
		}
	}
	
	/**
	 * Es creen els k clusters
	 */
	private void initClusters()
	{
		for (int i = 0; i < this.k; i++) 
			this.clusters.add(new ArrayList<Integer>());
	}
	
	/**
	 * Es verifica si l'algorisme s'ha d'aturar
	 * @return true si s'ha d'aturar, false altrament
	 */
	private boolean stop()
	{
		// Verificar si algun dels baricentres ha variat
		boolean equal = true;
		
		for (int i = 0; i < this.baricentres.size() && equal; i++) {
			ArrayList<Double> point = this.baricentres.get(i);
			ArrayList<Double> point_prev = this.prevBaricentres.get(i);
			
			for (int j = 0; j < point.size() && equal; j++) {
				double p_curr = point.get(j);
				double p_prev = point_prev.get(j);
				
				if (Math.abs(p_curr - p_prev) > 1e-2) 
					equal = false;
			}
		}
		
		if (equal || this.iter + 1 == this.lim)
			return true;
		
		return false;
	}
	
	/**
	 * Calcul de la mitjana d'un cluster
	 * @param cluster
	 * @return la mitjana del cluster
	 */
	private ArrayList<Double> mitjanaCluster(ArrayList<Integer> cluster)
	{
		ArrayList<Double> tot = new ArrayList<Double>(Collections.nCopies(this.d, 0.0));
		
		for (Integer i : cluster) {
			ArrayList<Double> p = this.data.get(i);
			
			for (int j = 0; j < this.d; j++) {  
				 Double val = tot.get(j);
				 tot.set(j, val + p.get(j) / cluster.size());
			}
		}
		
		return tot;
	}
	
	/**
	 * Calcul de la distancia entre dos vectors
	 * 
	 * @param u
	 * @param v
	 * @return la distancia
	 */
	private double distancia(ArrayList<Double> u, ArrayList<Double> v) 
	{
		double dis = 0;
		
		for (int i = 0; i < this.d; i++) 
			dis += Math.pow(u.get(i) - v.get(i), 2);
		
		return Math.sqrt(dis);
	}
	
	/**
	 * Resolucio de l'algorisme
	 */
	public void resol() 
	{
		while (true) {
			
			// Netejar les assignacions anteriors
			for (ArrayList<Integer> c : clusters)
				c.clear();
			
			if (verbose)
				logBaricentres();
			
			// Assignar cada punt al baricentre mes proper
			for (int i = 0; i < this.data.size(); i++) {
				
				double min_dis = Double.MAX_VALUE;
				int min_cent = 0;
				
				ArrayList<Double> punt = this.data.get(i);
				
				for (int j = 0; j < this.baricentres.size(); j++) {
					ArrayList<Double> baricentre = this.baricentres.get(j);
					
					double dist = this.distancia(baricentre, punt);
					
					if (dist < min_dis) {
						min_dis = dist;
						min_cent = j;
					}
				}
				
				// Afegir al cluster
				clusters.get(min_cent).add(i);
			}
			
			if (verbose)
				logClusters();
			
			// Recalcular els baricentres
			for (int i = 0; i < this.clusters.size(); i++) {
				ArrayList<Integer> cluster = this.clusters.get(i);
				
				if (cluster.size() > 0) {
					this.prevBaricentres.set(i, new ArrayList<Double>(this.baricentres.get(i)));
					this.baricentres.set(i, mitjanaCluster(cluster));
				}
			}
			
			if (stop())
				break;
			
			this.iter++;
		}
	}
	
	public ArrayList<ArrayList<Integer>> getClusters()
	{
		return clusters;
	}
	
	public ArrayList<ArrayList<Double>> getBaricentres()
	{
		return baricentres;
	}
	
	public void setVerbose()
	{
		this.verbose = true;
	}
	
	private void logBaricentres()
	{
		System.out.println();
		System.out.println("Baricentres i=" + iter);
		
		int i = 0;
		for (ArrayList<Double> bc : baricentres) {
			System.out.print(i++ + ": ");
			for (Double d : bc) {				
				System.out.printf("%.2f ", d);
			}
			System.out.println();
		}
	}
	
	private void logClusters()
	{
		System.out.println();
		System.out.println("Clusters i=" + iter);
		
		int i = 0;
		for (ArrayList<Integer> cluster : clusters) {
			System.out.print(i++ + "(" +  cluster.size() + "): ");
			for (Integer c : cluster) {
				System.out.print(c + " ");
			}
			System.out.println();
		}
	}
	
}