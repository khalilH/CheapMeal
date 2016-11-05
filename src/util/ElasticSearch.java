package util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ElasticSearch {

	public final static String REFRESH_URL = "http://localhost:9200/cheapmeal/_refresh";
	

	public static String refresh() throws UnirestException {
		
		HttpResponse<String> response = Unirest.post(REFRESH_URL)
				.asString();
		
		return response.getBody();
	}
	
}
