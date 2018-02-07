package practice;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;

public class Mongo {

	public static void main(String args[]) {

		MongoClient mongo = new MongoClient();
		MongoCredential credential = MongoCredential.createCredential("kask", "practice", "kask4all".toCharArray());//(username,db name,password)
		List<String> dbs = mongo.getDatabaseNames();
		System.out.println(dbs);

		MongoDatabase database = mongo.getDatabase("practice");
		for (String name : database.listCollectionNames()) {
			System.out.println(name);
		}
		MongoCollection<Document> collection = database.getCollection("akask");
	
/*		List<Document> list=new ArrayList<Document>();
		// to insert
		Document document1 = new Document("name", "narsimha").append("addr", "nzb").append("age", 26);
		//collection.insertOne(document1);
		
		Document document2 = new Document("name", "anvesh").append("addr", "bswd").append("age", 23);
		//collection.insertOne(document2);

		Document document3 = new Document("name", "saketh").append("addr", "hyd").append("age", 25);
		//collection.insertOne(document3);

		Document document4 = new Document("name", "vishu").append("addr", "vem").append("age", 25);
		//collection.insertOne(document4);

		list.add(document4);
		list.add(document3);
		list.add(document2);
		list.add(document1);
		
		collection.insertMany(list);*/
		
		//to store multiple values in a field like in below quiz and 99 are stored inside score field
		Document score = new Document().append("type", "quiz").append("score",99);
		collection.updateMany(Filters.eq("id", 1),Updates.set("scores", score));
		
		//update first way
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", new BasicDBObject().append("id", 1));
		
		BasicDBObject searchQuery = new BasicDBObject().append("name", "saketh");

		collection.updateMany(searchQuery, newDocument);
		
		//update second way 
		collection.updateMany(Filters.eq("name", "anvesh"), Updates.set("id", 2)); // updateOne will update
																					// first instance
		FindIterable<Document> iterDoc = collection.find();
		Iterator it = iterDoc.iterator();
		while (it.hasNext()) {
			System.out.println(it.next());
		}
		collection.deleteMany(Filters.eq("name", "narsimha"));
		// collection.drop();
	}
}