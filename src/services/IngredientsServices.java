package services;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;

import services.fonctions.IngredientsFonctions;
import util.ServiceTools;

public class IngredientsServices {

	public static JSONObject getListeIngredients(String query) throws JSONException {
		try {
			List<BasicDBObject> ingredients = IngredientsFonctions.getListeIngredients(query);
			JSONArray suggestions = new JSONArray(ingredients);
			JSONObject res = new JSONObject()
					.put("suggestions", suggestions);
			return res;
		} catch (MongoClientException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (UnknownHostException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (NullPointerException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		}
	}
	
	public static JSONObject putListeIngredients(String fileName) throws JSONException {
		try {
			List<String> ingredients = IngredientsFonctions.putListeIngredients(fileName);
			JSONArray array = new JSONArray(ingredients);
			return new JSONObject().append("ingredients", array);
		} catch (MongoClientException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (UnknownHostException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		} catch (FileNotFoundException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		}
	}
}
