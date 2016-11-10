package services.fonctions;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchFonctions {

	/**
	 * Recherche les Recettes satisfaisant la recherche
	 * @param query requete de la recherche, optionel
	 * @return un JSONObject contenant les recettes satisfaisant la requete
	 * @throws JSONException
	 * @throws UnknownHostException si elasticSearch ne repond pas
	 */
	public static JSONObject search(String query) throws JSONException, UnknownHostException {

		Settings settings = Settings.builder()
				.put("client.transport.sniff", true).build();
		TransportClient client = new PreBuiltTransportClient(settings);
		client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));

		SearchResponse res;

		if (query == null || query.equals("")) {
			res = client.prepareSearch()
					.setIndices("cheapmeal")
					.setTypes("Recettes")
					.setSize(50)
					.setQuery(QueryBuilders.matchAllQuery())
					.execute()
					.actionGet();
		}
		else {
			res = client.prepareSearch()
					.setIndices("cheapmeal")
					.setTypes("Recettes")
					.setSize(50)
					.setQuery(QueryBuilders.queryStringQuery(query))
					.execute()
					.actionGet();
		}

		SearchHits hits = res.getHits();
		JSONObject results = new JSONObject();
  		List<JSONObject> list = new ArrayList<>();
		for (SearchHit searchHit : hits) {
			JSONObject hit = new JSONObject()
					.put("score", searchHit.getScore())
					.put("source", new JSONObject(searchHit.getSourceAsString()).put("_id", searchHit.getId()));
			list.add(hit);
		}

		client.close();
		JSONArray array = new JSONArray(list);
		results.put("recettesBest", array);		
		return  results;

	}
	

}