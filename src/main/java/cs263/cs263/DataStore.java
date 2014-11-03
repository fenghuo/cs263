package cs263.cs263;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.log.*;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.apphosting.datastore.EntityV4.Key;

public class DataStore {

	public static List<String> Test() {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Entity employee = new Entity("Employee", "names");

		employee.setProperty("names", Arrays.<String> asList("test Name"));
		// datastore.put(employee);

		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers
				.getConsistentLogAndContinue(Level.INFO));
		List<String> value = (List<String>) syncCache.get("names"); // read from
																	// cache
		if (value == null) {

			Entity ret;
			try {
				ret = datastore.get(employee.getKey());
			} catch (EntityNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Arrays.asList("Not Found!");
			}

			List<String> list = (List<String>) ret.getProperty("names");
			value = list;
			syncCache.put("names", value); // populate cache

		}

		return value;
	}

	public static void Add(String name, String school) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Entity employee = new Entity("Employee", name);
		employee.setProperty("name", name);
		employee.setProperty("school", school);
		datastore.put(employee);
	}

	public static void remove(String name) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Entity employee = new Entity("Employee", name);
		datastore.delete(employee.getKey());
	}

	public static String read(String name) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Entity employee = new Entity("Employee", name);
		Entity re = null;

		String key = name;
		String value;

		// Using the synchronous cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers
				.getConsistentLogAndContinue(Level.INFO));
		value = (String) syncCache.get(key); // read from cache
		if (value == null) {
			// get value from other source
			// ........

			try {
				re = datastore.get(employee.getKey());
			} catch (EntityNotFoundException e) {
				return "Not Found!";
			}

			String res = "";
			for (Map.Entry<String, Object> entry : re.getProperties()
					.entrySet()) {
				res += entry.getKey() + " : " + entry.getValue() + "    ";
			}

			value = res;
			syncCache.put(key, value); // populate cache
		}
		return value.toString();
	}

}
