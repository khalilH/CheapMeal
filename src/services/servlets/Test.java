package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import Util.BDTools.DBStatic;

/**
 * Servlet implementation class Test
 */
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Test() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		try {
		MongoDatabase database = DBStatic.getMongoConnection();
		MongoCollection<BasicDBObject> col = database.getCollection("Users", BasicDBObject.class);
		BasicDBObject document = new BasicDBObject("login", login);
		document.append("name", name).append("surname", surname);
		col.insertOne(document);
		DBStatic.closeMongoDBConnection();
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
