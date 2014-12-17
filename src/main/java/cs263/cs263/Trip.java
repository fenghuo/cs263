package cs263.cs263;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class Trip {

	public static String NAME = "TRIP";

	public static boolean Add(String username, String id, String trip) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers
				.getConsistentLogAndContinue(Level.INFO));
		Entity user = new Entity(NAME, username);
		try {
			user = datastore.get(user.getKey());
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		Text text=new Text(trip);
		user.setProperty(id, text);
		datastore.put(user);
		syncCache.put(username + id, text);
		try {
			user = datastore.get(user.getKey());
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		String res = "[";
		for (Entry<String, Object> entry : user.getProperties().entrySet()) {
			res += entry.getKey() + ",";
		}
		res += "]";
		res = res.replace(",]", "]");
		res = res.replace("[,", "[").replace(",,", ",");
		syncCache.put(username + "ids", res);
		System.out.println("Updated:"
				+ (String) syncCache.get(username + "ids"));
		return true;
	}

	public static String Get(String username, String id) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Entity user = new Entity(NAME, username);

		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers
				.getConsistentLogAndContinue(Level.INFO));
		Text res = (Text) syncCache.get(username + id); // read from cache
		if (res != null)
			return res.getValue();

		try {
			user = datastore.get(user.getKey());
		} catch (EntityNotFoundException e) {
		}
		res = (Text) user.getProperty(id);
		syncCache.put(username + id, res);

		return res.getValue();
	}

	public static String GetAllId(String username) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Entity user = new Entity(NAME, username);

		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers
				.getConsistentLogAndContinue(Level.INFO));
		String res = (String) syncCache.get(username + "ids"); // read from
																// cache
		if (res != null)
			return res.toString();

		try {
			user = datastore.get(user.getKey());
		} catch (EntityNotFoundException e) {
		}
		String ress = "[";
		for (Entry<String, Object> entry : user.getProperties().entrySet()) {
			ress += entry.getKey() + ",";
		}
		ress += "]";
		ress = ress.replace(",]", "]");
		syncCache.put(username + "ids", ress);
		return ress;
	}
}
