package cs263.cs263;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class User implements Cloneable {

	public final static String NAME = "User";
	public String username = "";
	public String password = "";
	public String email = "";

	public User() {

	}

	public User(String a, String b, String c) {
		username = a;
		password = b;
		email = c;
	}

	public static boolean Add(String username, String password, String email) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Entity user = new Entity(NAME, username);
		try {
			if (datastore.get(user.getKey()) != null)
				return false;
		} catch (EntityNotFoundException e) {
		}
		user.setProperty("username", username);
		user.setProperty("password", password);
		user.setProperty("email", email);
		datastore.put(user);
		return true;
	}

	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public static User Get(String username) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Entity user = new Entity(NAME, username);

		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers
				.getConsistentLogAndContinue(Level.INFO));
		User res = (User) syncCache.get(username); // read from cache
		if (res != null)
			return res;

		try {
			if (datastore.get(user.getKey()) == null)
				return null;
			user = datastore.get(user.getKey());
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		res = new User((String) (user.getProperty("username")),
				(String) (user.getProperty("password")),
				(String) (user.getProperty("email")));
		Object o = null;
		try {
			o = res.clone();
			System.out.println(res.username + " -- " + res.password);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
}
