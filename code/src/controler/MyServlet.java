package controler;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public MongoClient mongoClient = null;
	public DB db;
	public DBCollection collection = null;
	public DBCollection collection1 = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyServlet() {
		super();

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			System.out.println("in try");
			if (mongoClient == null) {
				mongoClient = new MongoClient("localhost", 27017);
				System.out.println("Mongo client: " + mongoClient);
				System.out.println("client created");
			}

			db = mongoClient.getDB("test");

			System.out.println("Connected");
		}

		catch (Exception e) {

		}
		PrintWriter pw = response.getWriter();
		response.setContentType("text/html");

		String apiOrMashup = request.getParameter("apiOrMashup");
		String criteria = request.getParameter("criteria");
		String values = request.getParameter("value");
		// String values[]=valueString.split(",");

		/*
		 * for(int i=0; i<values.length; i++) { values[i]=values[i].trim();
		 * System.out.println(values[i]); }
		 */

		if (criteria.equals("keywords")) {
			ArrayList<String> result=getByKeywords(apiOrMashup, values);
			PrintWriter out = response.getWriter();
			for (int i = 0; i < result.size(); i++) {

				out.print(result.get(i) + "\n\n");
				response.setContentType("text/html");

			}
			out.close();
		}

		else if (criteria.equals("year")) {
			try {
				ArrayList<String> result = getByYear(apiOrMashup, values);
				PrintWriter out = response.getWriter();
				for (int i = 0; i < result.size(); i++) {

					out.print(result.get(i) + "\n\n");
					response.setContentType("text/html");

				}
				out.close();

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else if (criteria.equals("protocols")) {
			ArrayList<String> result = getByProtocol(apiOrMashup, values);
			PrintWriter out = response.getWriter();
			for (int i = 0; i < result.size(); i++) {

				out.print(result.get(i) + "\n");
				response.setContentType("text/html");

			}
			out.close();
		}

		else if (criteria.equals("category")) {
			ArrayList<String> result = getByCategory(apiOrMashup, values);
			PrintWriter out = response.getWriter();
			for (int i = 0; i < result.size(); i++) {

				out.print(result.get(i) + "\n");
				response.setContentType("text/html");

			}
			out.close();
		}

		else if (criteria.equals("tags")) {
			ArrayList<String> result = getByTags(apiOrMashup, values);
			PrintWriter out = response.getWriter();
			for (int i = 0; i < result.size(); i++) {

				out.print(result.get(i) + "\n\n");
				response.setContentType("text/html");

			}
			out.close();
		}

		else if (criteria.equals("usedApis")) {
			ArrayList<String> result=getByUsedApis(apiOrMashup, values);
			PrintWriter out = response.getWriter();
			for (int i = 0; i < result.size(); i++) {

				out.print(result.get(i) + "\n\n");
				response.setContentType("text/html");

			}
			out.close();
		}

		else {
			// rating
			String option = request.getParameter("ratingOptions");
			ArrayList<String> result = getByRating(apiOrMashup, values, option);
			PrintWriter out = response.getWriter();
			for (int i = 0; i < result.size(); i++) {

				out.print(result.get(i) + "\n\n");
				response.setContentType("text/html");

			}
			out.close();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public ArrayList<String> getByKeywords(String apiOrMashup, String values) {

		ArrayList<String> result=new ArrayList<String>();
		String val[]=values.split(",");
		String searchString="";
		for(int i=0; i<val.length; i++) {
			val[i]=val[i].trim();
			val[i]="\""+val[i]+"\"";
			searchString+=val[i];
			
		}
		
		
		if (apiOrMashup.equals("api")) {

			DBCollection collection = db.getCollection("apiData");
			BasicDBObject query=new BasicDBObject("$text",new BasicDBObject("$search",searchString));
			DBCursor cursor = collection.find(query);

			while (cursor.hasNext()) {

				System.out.println(result.add((String) cursor.next().get("id")));
			}

		} else {

			DBCollection collection = db.getCollection("mashupData");
			BasicDBObject query=new BasicDBObject("$text",new BasicDBObject("$search",searchString));
			DBCursor cursor = collection.find(query);

			while (cursor.hasNext()) {

				System.out.println(result.add((String) cursor.next().get("id")));
			}
			
			
		}

		System.out.println(result.size());
		return result;
		
	}

	public ArrayList<String> getByYear(String apiOrMashup, String values) throws ParseException {

		ArrayList<String> result = new ArrayList<String>();
		if (apiOrMashup.equals("api")) {

			String startDate = values + "-01-01T00:00:00Z";
			String endDate = values + "-12-31T23:59:59Z";
			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			System.out.println("lets parse");
			Date date1 = (Date) simpleDateFormat1.parse(startDate);
			System.out.println("one parsed");
			Date date2 = (Date) simpleDateFormat1.parse(endDate);
			BasicDBObject field = new BasicDBObject("updated", new BasicDBObject("$gte", date1).append("$lt", date2));

			DBCollection collection = db.getCollection("apiData");
			DBCursor cursor = collection.find(field);

			while (cursor.hasNext()) {

				System.out.println(result.add((String) cursor.next().get("id")));
			}

		} else {

			String startDate = values + "-01-01T00:00:00Z";
			System.out.println(startDate);
			String endDate = values + "-12-31T23:59:59Z";
			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date date1 = (Date) simpleDateFormat1.parse(startDate);
			Date date2 = (Date) simpleDateFormat1.parse(endDate);
			BasicDBObject field = new BasicDBObject("updated", new BasicDBObject("$gte", date1).append("$lt", date2));

			DBCollection collection = db.getCollection("mashupData");
			DBCursor cursor = collection.find(field);

			while (cursor.hasNext()) {

				System.out.println(result.add((String) cursor.next().get("id")));
			}
		}

		System.out.println(result.size());
		return result;
	}

	public ArrayList<String> getByProtocol(String apiOrMashup, String values) {
		ArrayList<String> result = new ArrayList<String>();
		if (apiOrMashup.equals("api")) {

			BasicDBObject field = new BasicDBObject();
			field.put("protocols", java.util.regex.Pattern.compile(values, Pattern.CASE_INSENSITIVE));

			DBCollection collection = db.getCollection("apiData");
			DBCursor cursor = collection.find(field);

			while (cursor.hasNext()) {

				System.out.println(result.add((String) cursor.next().get("id")));
			}

		} else {
			result.add("There were no mashups retrieved");
		}

		System.out.println(result.size());
		return result;

	}

	public ArrayList<String> getByCategory(String apiOrMashup, String values) {

		ArrayList<String> result = new ArrayList<String>();
		if (apiOrMashup.equals("api")) {
			
			BasicDBObject field = new BasicDBObject();
			field.put("category", java.util.regex.Pattern.compile(values, Pattern.CASE_INSENSITIVE));

			DBCollection collection = db.getCollection("apiData");
			DBCursor cursor = collection.find(field);

			while (cursor.hasNext()) {

				System.out.println(result.add((String) cursor.next().get("id")));
			}

		} else {
			result.add("There were no mashups retrieved");
		}

		return result;
	}

	public ArrayList<String> getByTags(String apiOrMashup, String values) {

		ArrayList<String> result = new ArrayList<String>();
		if (apiOrMashup.equals("api")) {

			BasicDBObject field = new BasicDBObject();
			List<String> list = new ArrayList<String>();
			list.add(values);

			field.put("tags", new BasicDBObject("$in", list));
			DBCollection collection = db.getCollection("apiData");
			DBCursor cursor = collection.find(field);

			while (cursor.hasNext()) {

				System.out.println(result.add((String) cursor.next().get("id")));
			}

		} else {
			BasicDBObject field = new BasicDBObject();
			List<String> list = new ArrayList<String>();
			list.add(values);

			field.put("tags", new BasicDBObject("$in", list));
			DBCollection collection1 = db.getCollection("mashupData");
			DBCursor cursor = collection1.find(field);

			while (cursor.hasNext()) {

				System.out.println(result.add((String) cursor.next().get("id")));
			}
		}

		return result;
	}

	public ArrayList<String> getByUsedApis(String apiOrMashup, String values) {

		
		ArrayList<String> result = new ArrayList<String>();
		BasicDBObject field = new BasicDBObject();
		
		if (apiOrMashup.equals("api")) {

			result.add("There were no apis retrieved");
			

		} else {
			field = new BasicDBObject("APIs.apiName",values);
			
			DBCollection collection1 = db.getCollection("mashupData");
			DBCursor cursor = collection1.find(field);

			while (cursor.hasNext()) {

				System.out.println(result.add((String) cursor.next().get("id")));
			}
		}

		return result;
		
	}

	public ArrayList<String> getByRating(String apiOrMashup, String values, String option) {

		Double rating = Double.parseDouble(values);
		ArrayList<String> result = new ArrayList<String>();
		BasicDBObject field = new BasicDBObject();
		if (apiOrMashup.equals("api")) {
			if (option.equals("equalTo")) {

				field.put("rating", rating);
				DBCollection collection = db.getCollection("apiData");
				DBCursor cursor = collection.find(field);

				while (cursor.hasNext()) {

					System.out.println(result.add((String) cursor.next().get("id")));
				}
			}

			else if (option.equals("lessThan")) {
				BasicDBObject query = new BasicDBObject();
				query.put("rating", new BasicDBObject("$lt", rating));
				DBCollection collection = db.getCollection("apiData");
				DBCursor cursor = collection.find(query);

				while (cursor.hasNext()) {

					System.out.println(result.add((String) cursor.next().get("id")));
				}
			}

			else {
				// greaterThan
				BasicDBObject query = new BasicDBObject();
				query.put("rating", new BasicDBObject("$gt", rating));
				DBCollection collection = db.getCollection("apiData");
				DBCursor cursor = collection.find(query);

				while (cursor.hasNext()) {

					System.out.println(result.add((String) cursor.next().get("id")));
				}

			}

		} else {

			// mashup
			if (option.equals("equalTo")) {

				field.put("rating", rating);
				DBCollection collection1 = db.getCollection("mashupData");
				DBCursor cursor1 = collection1.find(field);

				while (cursor1.hasNext()) {

					System.out.println(result.add((String) cursor1.next().get("id")));
				}
			}

			else if (option.equals("lessThan")) {
				BasicDBObject query = new BasicDBObject();
				query.put("rating", new BasicDBObject("$lt", rating));
				DBCollection collection = db.getCollection("mashupData");
				DBCursor cursor = collection.find(query);

				while (cursor.hasNext()) {

					System.out.println(result.add((String) cursor.next().get("id")));
				}
			}

			else {
				// greaterThan
				BasicDBObject query = new BasicDBObject();
				query.put("rating", new BasicDBObject("$gt", rating));
				DBCollection collection = db.getCollection("mashupData");
				DBCursor cursor = collection.find(query);

				while (cursor.hasNext()) {

					System.out.println(result.add((String) cursor.next().get("id")));
				}
			}
		}

		return result;
	}
}
