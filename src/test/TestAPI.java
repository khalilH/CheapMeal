package test;

import org.json.JSONException;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TestAPI {

	public static void main(String[] args) {
		try {
			HttpResponse<String> response = Unirest.get("https://datagram-products-v1.p.mashape.com/chains/")
					.header("X-Mashape-Key", "KReKhv4jhXmshDBVbYlbtdiYGRJdp1ZOAKtjsnfyiGaxZnxQAJ")
					.header("Accept", "application/json")
					.asString();
			
			JSONObject json = new JSONObject(response.getBody());
			System.out.println(json.toString(1));
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
