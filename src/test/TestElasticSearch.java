package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class TestElasticSearch {

	/**
	 * WON'T WORK HERE, JUST AN EXAMPLE, SEE TestES servlet 
	 * 
	 */
	
	public static void main(String[] args) {
		try {
			Settings settings = Settings.builder()
			        .put("client.transport.sniff", true).build();
			TransportClient client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
//			QueryBuilder qb = queryStringQuery("Chafik");

			SearchResponse res = client.prepareSearch()
					.setIndices("cheapmeal")
					.setQuery(QueryBuilders.queryStringQuery("pommes"))
					.execute()
					.actionGet();
			
//			SearchResponse response = client.prepareSearch().execute().actionGet();

			System.out.println("total Hits = "+res.getHits().getTotalHits()+" ;; ");
			System.out.println("Max score = "+res.getHits().getMaxScore()+" ;; ");
			
			for (SearchHit hit : res.getHits()) {
				
				System.out.print("id = "+hit.getId()+" ;; ");
				System.out.print("score = "+hit.getScore()+ " ;; ");
				System.out.println(hit.getSourceAsString());
			}
			
			
			client.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
