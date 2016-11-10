package util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * 
 * @author khalil
 *
 */
public class ExternalAPI {

	public static final String X_MASHAPE_KEY = "KReKhv4jhXmshDBVbYlbtdiYGRJdp1ZOAKtjsnfyiGaxZnxQAJ";
	public static final String BASE_URL = "https://datagram-products-v1.p.mashape.com/products/locator/";
	public static final String ZIPCODE = "75005";
	public static final String LOCAL_PRODUCT_KEY = "local_product";
	public static final String PRICE_KEY = "price";
	
	/**
	 * 
	 * @param ean
	 * @return
	 * @throws UnirestException
	 * @throws JSONException
	 */
	public static ArrayList<Double> searchPrices(String ean) throws UnirestException, JSONException {
		String urlRequest = BASE_URL+"?ean="+ean+"&zipcode="+ZIPCODE;
		HttpResponse<String> response = Unirest.get(urlRequest)
				.header("X-Mashape-Key", X_MASHAPE_KEY)
				.header("Accept", "application/json")
				.asString();
		
		JSONObject results = new JSONObject(response.getBody());
		JSONArray tab = results.getJSONArray(LOCAL_PRODUCT_KEY);
		ArrayList<Double> prices = new ArrayList<>();
		for (int i = 0; i<tab.length(); i++) {
			JSONObject it = tab.getJSONObject(i);
			if (it.has(PRICE_KEY)) {
				prices.add(it.getDouble(PRICE_KEY));
			}
		}
		return prices;
	}
	

}

