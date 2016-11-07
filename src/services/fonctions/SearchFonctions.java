package services.fonctions;

import java.util.List;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
		JSONObject results = new JSONObject()
				.put("TotalHits", hits.getTotalHits())
				.put("maxScore", hits.getMaxScore());
		List<JSONObject> list = new ArrayList<>();
		for (SearchHit searchHit : hits) {
			JSONObject hit = new JSONObject()
					.put("_id", searchHit.getId())
					.put("score", searchHit.getScore())
					.put("source", new JSONObject(searchHit.getSourceAsString()));
			list.add(hit);
		}

		client.close();
		JSONArray array = new JSONArray(list);
		results.put("hits", array);		
		return new JSONObject().put("results", results);

	}
}