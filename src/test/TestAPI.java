package test;

import org.json.JSONException;

import com.mashape.unirest.http.exceptions.UnirestException;

import util.ExternalAPI;

public class TestAPI {

	public static void main(String[] args) {
		try {
			Double prix = ExternalAPI.searchMinPrice("3451790941610");
			System.out.println("TAILLE = "+prix);
		} catch (UnirestException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
