package util;

import java.util.concurrent.Callable;

public class MyPriceCallable implements Callable<Double> {
	String ean;
	Double quantiteRef;
	Double quantiteProduit;

	/**
	 * Construit un callable avec l'ean d'un produit
	 * @param ean la chaine de caractere correspondant au code barre du produit
	 * @param quantiteRef quantite du produit associe a au code barre ean
	 * @param quantiteProduit quantite du produit dans la recette
	 */
	public MyPriceCallable(String ean,Double quantiteRef, Double quantiteProduit) {
		this.ean = ean;
		this.quantiteRef = quantiteRef;
		this.quantiteProduit = quantiteProduit;
	}
	
	@Override
	/**
	 * Fonction d'appel a la fonction de recherche dans l'API externe
	 */
	public Double call() throws Exception {
		Double price =  ExternalAPI.searchMinPrice(ean);
		Double result = (quantiteProduit * price )/ quantiteRef;
		return result;
	}
}
