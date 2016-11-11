package util;

import java.util.concurrent.Callable;

public class MyPriceCallable implements Callable<Double> {
	String ean;
	Double quantiteRef;
	Double quantiteProduit;

	/**
	 * Construit un callable avec l'ean d'un produit
	 * @param ean
	 */
	public MyPriceCallable(String ean,Double quantiteRef, Double quantiteProduit) {
		this.ean = ean;
		this.quantiteRef = quantiteRef;
		this.quantiteProduit = quantiteProduit;
	}
	
	@Override
	public Double call() throws Exception {
		Double price =  ExternalAPI.searchMinPrice(ean);
		Double result = (quantiteProduit * price )/ quantiteRef;
		return result;
	}
}
