package util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Classe permettant de faire appel a l'API Datagram afin de recuperer
 * les prix d'un produit a partir de son code barre
 */
public class ExternalAPI {

	public static final String X_MASHAPE_KEY = "KReKhv4jhXmshDBVbYlbtdiYGRJdp1ZOAKtjsnfyiGaxZnxQAJ";
	public static final String BASE_URL = "https://datagram-products-v1.p.mashape.com/products/locator/";
	public static final String ZIPCODE = "75005";
	public static final String LOCAL_PRODUCT_KEY = "local_product";
	public static final String PRICE_KEY = "price";
	
	/**
	 * Permet de chercher le prix d'un produit
	 * @param ean la chaine de caracteres correspondant au code bar du produit
	 * @return Double le prix minimum aux alentours pour un produit
	 * @throws UnirestException si l'appel a l'API echoue
	 * @throws JSONException lorsqu'il y a eut une erreur a la creation ou la manipulation de l'objet JSON
	 */
	public static Double searchMinPrice(String ean) throws UnirestException, JSONException {
		String urlRequest = BASE_URL+"?ean="+ean+"&zipcode="+ZIPCODE;
		HttpResponse<String> response = Unirest.get(urlRequest)
				.header("X-Mashape-Key", X_MASHAPE_KEY)
				.header("Accept", "application/json")
				.asString();
		Double minPrice = Double.MAX_VALUE;
		JSONObject results = new JSONObject(response.getBody());
		JSONArray tab = results.getJSONArray(LOCAL_PRODUCT_KEY);
		for (int i = 0; i<tab.length(); i++) {
			JSONObject it = tab.getJSONObject(i);
			if (it.has(PRICE_KEY)) {
				Double res = it.getDouble(PRICE_KEY);
				if(res < minPrice)
					minPrice = res;
			}
		}
		return minPrice;
	}
	
}

