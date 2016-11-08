package util;

public class ElasticSearch {

	
	public final static String REFRESH_URL = "http://localhost:9200/cheapmeal/_refresh";
	public final static String SEARCH_RECETTES_URL = "http://localhost:9200/cheapmeal/Recettes/_search";
	
	//TODO a supprimer si vraiment pas besoin
//	public static String refresh() throws UnirestException {
//		
//		HttpResponse<String> response = Unirest.post(REFRESH_URL)
//				.asString();
//		
//		return response.getBody();
//	}
	
}
