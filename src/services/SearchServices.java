package services;

import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import services.fonctions.SearchFonctions;
import util.ServiceTools;

public class SearchServices {

	public static JSONObject search(String query) throws JSONException {

		try {
			return SearchFonctions.search(query);
		} catch (UnknownHostException e) {
			return ServiceTools.serviceRefused(e.getMessage(), -1);
		}

	}

}
