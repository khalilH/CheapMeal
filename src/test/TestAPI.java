package test;

import java.util.ArrayList;

import org.json.JSONException;

import com.mashape.unirest.http.exceptions.UnirestException;

import util.ExternalAPI;

public class TestAPI {

	public static void main(String[] args) {
		try {
			ArrayList<Double> prix = ExternalAPI.searchPrices("3451790941610");
			System.out.println("TAILLE = "+prix.size());
			for (Double d : prix) {
				System.out.println(d.toString());
			}
		} catch (UnirestException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
