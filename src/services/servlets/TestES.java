package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * Servlet implementation class Test
 */
public class TestES extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TestES() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		try {
			Settings settings = Settings.builder()
			        .put("client.transport.sniff", true).build();
			TransportClient client = new PreBuiltTransportClient(settings);
			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"),9300));
//			QueryBuilder qb = queryStringQuery("Chafik");

			SearchResponse res = client.prepareSearch()
					.setIndices("cheapmeal")
					.setTypes("ingredients")
					.setSize(50)
					.setQuery(QueryBuilders.matchAllQuery())
					.execute()
					.actionGet();
			
//			SearchResponse response = client.prepareSearch().execute().actionGet();

			pw.println("total Hits = "+res.getHits().getTotalHits()+" ;; ");
			pw.println("Max score = "+res.getHits().getMaxScore()+" ;; ");
			int i = 0;
			for (SearchHit hit : res.getHits()) {
				i++;
				pw.print("id = "+hit.getId()+" ;; ");
				pw.print("score = "+hit.getScore()+ " ;; ");
				pw.println(hit.getSourceAsString());
			}
			pw.println("nombre de hits recupere = "+i );
			
			client.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
