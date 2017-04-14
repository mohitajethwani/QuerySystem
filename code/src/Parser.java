import java.io.*;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.simple.*;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.DBCursor;

public class Parser {

	static JSONArray array = new JSONArray();
	static int linecounter = 0;

	public static void main(String arg[]) throws IOException {

		
		try {
			
			MongoClient mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB("test");
			System.out.println("Connect to database successfully");
			//DBCollection coll = db.createCollection("apiData", null);
			//System.out.println("Collection api created successfully");
			DBCollection collection = db.getCollection("apiData");
			System.out.println("Collection selected");

			BufferedReader br = new BufferedReader(new FileReader("api.txt"));
			System.out.println("1");
			String line = br.readLine();
			System.out.println(line);
			while (line != null) {
				System.out.println("in while");
				BasicDBObject object = splitAndParse(line);
				collection.insert(object);
				line = br.readLine();

			}
			System.out.println("2");
			//DBCollection coll1 = db.createCollection("mashupData", null);
			//System.out.println("Collection mashup created successfully");
			DBCollection collection1 = db.getCollection("mashupData");
			System.out.println("Collection selected");
			br = new BufferedReader(new FileReader("mashup.txt"));
			line = br.readLine();
			while (line != null) {
				BasicDBObject object = splitAndParseMashup(line);
				collection1.insert(object);
				line = br.readLine();
			}

		}

		catch (Exception e) {

		}

	}

	public static BasicDBObject splitAndParseMashup(String line) throws ParseException {
		String tokens[] = line.split("\\$" + "\\#" + "\\$");
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].equals("")) {
				tokens[i] = "(null)";
			}
			tokens[i] = tokens[i].trim();

		}

		BasicDBObject mashupObject = new BasicDBObject();
		mashupObject.put("id", tokens[0]);
		mashupObject.put("title", tokens[1]);
		mashupObject.put("summary", tokens[2]);
		if(!tokens[3].equals("(null)")){
			double rating=Double.parseDouble(tokens[3]);
			mashupObject.put("rating", rating);
		}
		
		else {
			String rating="(null)";
			mashupObject.put("rating", rating);
		}
		mashupObject.put("name", tokens[4]);
		mashupObject.put("label", tokens[5]);
		mashupObject.put("author", tokens[6]);
		mashupObject.put("description", tokens[7]);
		mashupObject.put("type", tokens[8]);
		if(!tokens[9].equals("(null)")){
			int downloads=Integer.parseInt(tokens[9]);
			mashupObject.put("downloads", downloads);
		}
		
		else {
			String downloads="(null)";
			mashupObject.put("downloads", downloads);
		}
	
		System.out.println(tokens[10]);
		if(!tokens[10].equals("(null)")){
			int useCount=Integer.parseInt(tokens[10]);
			mashupObject.put("useCount", useCount);
		}
		
		else {
			String useCount="(null)";
			mashupObject.put("useCount", useCount);
		}
		mashupObject.put("sampleUrl", tokens[11]);
		System.out.println("ok");
		
		if(!tokens[12].equals("(null)")) {
			SimpleDateFormat simpleDateFormat1= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date date1=(Date) simpleDateFormat1.parse(tokens[12]);
			mashupObject.put("dateModified", date1);
			//System.out.println("date added");
		}
		else {
			mashupObject.put("dateModified", tokens[12]);
		}
		
		if(!tokens[13].equals("(null)")){
			int numComments=Integer.parseInt(tokens[13]);
			mashupObject.put("numComments", numComments);
		}
		
		else {
			mashupObject.put("numComments", tokens[13]);
		}
		
		mashupObject.put("commentsUrl", tokens[14]);

		JSONArray tagsArray = new JSONArray();
		String parts[] = {};
		if (tokens[15].contains("###")) {
			parts = tokens[15].split("\\#" + "\\#" + "\\#");
		}
		for (int i = 0; i < parts.length; i++) {
			tagsArray.add(parts[i]);
		}
		mashupObject.put("tags", tagsArray);

		JSONArray apisArray = new JSONArray();
		String parts1[] = tokens[16].split("\\#" + "\\#" + "\\#");

		for (int i = 0; i < parts1.length; i++) {
			if (parts1[i].contains("$$$")) {
				BasicDBObject partObject = new BasicDBObject();
				String parts2[] = parts1[i].split("\\$" + "\\$" + "\\$");
				partObject.put("apiName", parts2[0]);
				partObject.put("apiUrl", parts2[1]);
				apisArray.add(partObject);
			}
		}
		mashupObject.put("APIs", apisArray);

		if(!tokens[17].equals("(null)")) {
			SimpleDateFormat simpleDateFormat1= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date date1=(Date) simpleDateFormat1.parse(tokens[17]);
			mashupObject.put("updated", date1);
			//System.out.println("date added");
		}
		else {
			mashupObject.put("updated", tokens[17]);
		}
		
		
		return mashupObject;

	}

	public static BasicDBObject splitAndParse(String line) throws ParseException {
		int count=0;
		System.out.println("parsing");
		String tokens[] = line.split("\\$" + "\\#" + "\\$");
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].equals("")) {
				tokens[i] = "(null)";
			}
			tokens[i] = tokens[i].trim();
		}

		BasicDBObject apiObject = new BasicDBObject();
		System.out.println("obj created");
		apiObject.put("id", tokens[0]);
		System.out.println("Added: "+(count++));
		apiObject.put("title", tokens[1]);
		System.out.println("Added: "+(count++));
		apiObject.put("summary", tokens[2]);
		System.out.println("Added: "+(count++));
		System.out.println("sup");
		if(!tokens[3].equals("(null)")){
			double rating=Double.parseDouble(tokens[3]);
			apiObject.put("rating", rating);
		}
		
		else {
			String rating="(null)";
			apiObject.put("rating", rating);
		}
		System.out.println("Added: "+(count++));
		System.out.println("supp");
		apiObject.put("name", tokens[4]);
		System.out.println("Added: "+(count++));
		apiObject.put("label", tokens[5]);
		System.out.println("Added: "+(count++));
		apiObject.put("author", tokens[6]);
		System.out.println("Added: "+(count++));
		apiObject.put("description", tokens[7]);
		System.out.println("Added: "+(count++));
		apiObject.put("type", tokens[8]);
		System.out.println("Added: "+(count++));
		System.out.println(tokens[9]);
		if(!tokens[9].equals("(null)")){
			int downloads=Integer.parseInt(tokens[9]);
			apiObject.put("downloads", downloads);
		}
		
		else {
			String downloads="(null)";
			apiObject.put("downloads", downloads);
		}
	
		System.out.println("Added: "+(count++));
		System.out.println(tokens[10]);
		if(!tokens[10].equals("(null)")){
			int useCount=Integer.parseInt(tokens[10]);
			apiObject.put("useCount", useCount);
		}
		
		else {
			String useCount="(null)";
			apiObject.put("useCount", useCount);
		}
		System.out.println("Added: "+(count++));
		apiObject.put("sampleUrl", tokens[11]);
		System.out.println("Added: "+(count++));
		apiObject.put("downloadUrl", tokens[12]);
		System.out.println("Added: "+(count++));
		System.out.println(tokens[13]);
	
		apiObject.put("dateModified", tokens[13]);
			
		
		System.out.println("Added: "+(count++));
		apiObject.put("remoteFeed", tokens[14]);
		System.out.println("Added: "+(count++));
		if(!tokens[15].equals("(null)")){
			int numComments=Integer.parseInt(tokens[15]);
			apiObject.put("numComments", numComments);
		}
		
		else {
			apiObject.put("numComments", tokens[15]);
		}
		System.out.println("Added: "+(count++));
		apiObject.put("commentsUrl", tokens[16]);
		System.out.println("Added: "+(count++));
		JSONArray tagsArray = new JSONArray();
		String parts[] = {};
		if (tokens[17].contains("###")) {
			parts = tokens[17].split("\\#" + "\\#" + "\\#");
		}
		for (int i = 0; i < parts.length; i++) {
			tagsArray.add(parts[i]);
		}
		apiObject.put("tags", tagsArray);

		System.out.println("Added: "+(count++));
		apiObject.put("category", tokens[18]);
		System.out.println("Added: "+(count++));
		apiObject.put("protocols", tokens[19]);
		System.out.println("Added: "+(count++));
		apiObject.put("serviceEndpoint", tokens[20]);
		System.out.println("Added: "+(count++));
		apiObject.put("version", tokens[21]);
		System.out.println("Added: "+(count++));
		apiObject.put("wsdl", tokens[22]);
		System.out.println("Added: "+(count++));
		apiObject.put("dataFormats", tokens[23]);
		System.out.println("Added: "+(count++));
		apiObject.put("groups", tokens[24]);
		System.out.println("Added: "+(count++));

		JSONArray examplesArray = new JSONArray();
		String parts1[] = {};
		if (tokens[25].contains("###")) {
			parts1 = tokens[25].split("\\#" + "\\#" + "\\#");
		}
		for (int i = 0; i < parts1.length; i++) {
			examplesArray.add(parts1[i]);
		}
		apiObject.put("examples", examplesArray);

		System.out.println("Added: "+(count++));
		apiObject.put("clientInstall", tokens[26]);
		System.out.println("Added: "+(count++));
		apiObject.put("authentication", tokens[27]);
		System.out.println("Added: "+(count++));
		apiObject.put("ssl", tokens[28]);
		System.out.println("Added: "+(count++));
		apiObject.put("readonly", tokens[29]);
		System.out.println("Added: "+(count++));
		apiObject.put("VendorApiKits", tokens[30]);
		System.out.println("Added: "+(count++));
		apiObject.put("CommunityApiKits", tokens[31]);
		System.out.println("Added: "+(count++));
		apiObject.put("blog", tokens[32]);
		System.out.println("Added: "+(count++));
		apiObject.put("forum", tokens[33]);
		System.out.println("Added: "+(count++));
		apiObject.put("support", tokens[34]);
		System.out.println("Added: "+(count++));
		apiObject.put("accountReq", tokens[35]);
		System.out.println("Added: "+(count++));
		apiObject.put("commercial", tokens[36]);
		System.out.println("Added: "+(count++));
		apiObject.put("provider", tokens[37]);
		System.out.println("Added: "+(count++));
		apiObject.put("managedBy", tokens[38]);
		System.out.println("Added: "+(count++));
		apiObject.put("nonCommercial", tokens[39]);
		System.out.println("Added: "+(count++));
		apiObject.put("dataLicensing", tokens[40]);
		System.out.println("Added: "+(count++));
		apiObject.put("fees", tokens[41]);
		System.out.println("Added: "+(count++));
		apiObject.put("limits", tokens[42]);
		System.out.println("Added: "+(count++));
		apiObject.put("terms", tokens[43]);
		System.out.println("Added: "+(count++));
		apiObject.put("company", tokens[44]);
		System.out.println("Added: "+(count++));
		if(!tokens[45].equals("(null)")) {
			System.out.println("in if");
			String iso="yyyy-MM-dd'T'HH:mm:ss";
			SimpleDateFormat simpleDateFormat= new SimpleDateFormat(iso);
			Date date=(Date) simpleDateFormat.parse(tokens[45]);
			apiObject.put("updated", date);
			//System.out.println("date added");
		}
		else {
			apiObject.put("updated", tokens[45]);
		}

		System.out.println("Added: "+(count++));
		return apiObject;
	}
}
