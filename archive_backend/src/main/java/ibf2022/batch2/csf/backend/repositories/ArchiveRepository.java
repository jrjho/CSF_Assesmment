package ibf2022.batch2.csf.backend.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf2022.batch2.csf.backend.models.Archives;
import ibf2022.batch2.csf.exception.Exceptions;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Repository
public class ArchiveRepository {

	private static final String COLLECTION_NAME = "archives";

	@Autowired
	MongoTemplate mongoTemplate;

	// TODO: Task 4
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	//
	// public Archives recordBundle(String name, String title, String comments,
	// ArrayList<String> urlArray) {
	public JsonObject recordBundle(String bundleId, Document toInsert) {

		try{
		Document success = mongoTemplate.insert(toInsert, COLLECTION_NAME);
		System.out.printf(">>> Review inserted: %s", success.toString());
		JsonObject bundleIdJson = Json.createObjectBuilder()
				.add("bundleId", bundleId).build();
		return bundleIdJson;

		}catch(Exception e){
			throw new Exceptions("Upload to Mongo failed!");
		}


	}

	// TODO: Task 5
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	//
	public List<Archives> getBundleByBundleId(String bundleId) {
		try{
			Criteria criteria = Criteria.where("bundleId").is(bundleId);
			Query query = Query.query(criteria);

			List<Archives> bundle = mongoTemplate.find(query, Archives.class, COLLECTION_NAME);

			System.out.println(">>> Get Bundle from Mongo: " + bundle.toString());

			return bundle;
		}catch(Exception e){
			throw new Exceptions("Get Bundle from Mongo failed!" + e.getMessage());
		}
		
	}	

	// TODO: Task 6
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	//
	public Object getBundles(/* any number of parameters here */) {
		return null;
	}

}
