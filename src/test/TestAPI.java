package test;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONException;

import com.mashape.unirest.http.exceptions.UnirestException;
import antlr.StringUtils;
import util.ExternalAPI;

public class TestAPI {

	public static void main(String[] args) {
		try {
			Double prix = ExternalAPI.searchMinPrice("3451790941610");
//			System.out.println("TAILLE = "+prix);
			String cle = "<script src='zeazeae'>";
			System.out.println(cle);
			cle = StringEscapeUtils.escapeHtml4(cle);
			System.out.println(cle);
		} catch (UnirestException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
