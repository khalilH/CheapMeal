package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import util.bdTools.DBStatic;
import util.bdTools.MongoFactory;

/**
 * Servlet implementation class Test
 */
public class TestMongoDB extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestMongoDB() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String login = request.getParameter("login");
//		String name = request.getParameter("name");
//		String surname = request.getParameter("surname");
		try {
//			MongoDatabase database = DBStatic.getMongoConnection();
//			MongoCollection<BasicDBObject> col = database.getCollection("users", BasicDBObject.class);
//			BasicDBObject document = new BasicDBObject("login", "pet");
//			document.append("name", "pat").append("surname", "patate");
//			col.insertOne(document);
//			
			DBStatic.closeMongoDBConnection();
			ArrayList<String> ingredients = new ArrayList<String>();
			ingredients.add("pommes");
			ingredients.add("pate feuilletee");
			ArrayList<String> preparation = new ArrayList<String>();
			preparation.add("cuir les pommes");
			MongoFactory.ajouterRecette("tarte aux pomme", 5, "patou", ingredients, preparation);
		}
		catch (Exception e) {
			response.setContentType("text/html");
			PrintWriter pw = response.getWriter();
			pw.println(e.getMessage());
		}
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		pw.println("OK");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
