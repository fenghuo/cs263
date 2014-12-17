package cs263.cs263;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

enum Type{
	
}

public class Data {

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
