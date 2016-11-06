package services.servlets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import util.bdTools.DBStatic;
import util.bdTools.MongoFactory;

public class InsererIngredients extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		try {
			MongoDatabase database = DBStatic.getMongoConnection();
			MongoCollection<BasicDBObject> col = database.getCollection("ingredients", BasicDBObject.class);
			col.deleteMany(new BasicDBObject());
			File f = new File("/home/ladi/ingredients.csv");
			Scanner scanner = new Scanner(f);
			scanner.nextLine(); // Pour tej la premiere ligne
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] tab = line.split(",");
				BasicDBObject document = new BasicDBObject();
				document.put(MongoFactory.NOM_INGREDIENT, tab[0]);
				document.put(MongoFactory.EAN, tab[1]);
				pw.println(document.toJson());
				col.insertOne(document);
			}
			scanner.close();
			DBStatic.closeMongoDBConnection();
//			pw.println(ElasticSearch.refresh());
		} catch (FileNotFoundException e) {
			pw.println(e.getMessage());
		} catch (MongoClientException e) {
			pw.println(e.getMessage());
		} catch (UnknownHostException e) {
			pw.println(e.getMessage());
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public InsererIngredients() {
		// TODO Auto-generated constructor stub
	}

}
