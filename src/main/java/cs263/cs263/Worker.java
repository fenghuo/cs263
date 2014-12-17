package cs263.cs263;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class Worker extends HttpServlet {
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String key = request.getParameter("key");
		// Do something with key.

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Entity employee = new Entity("Employee", "names");
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers
				.getConsistentLogAndContinue(Level.INFO));
		List<String> value = (List<String>) syncCache.get("names"); // read from
		value.add(0, key);
		syncCache.put("names", value);

		Entity ret = null;
		try {
			ret = datastore.get(employee.getKey());
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(ret == null){
			ret=new  Entity("Employee", "names");
			ret.setProperty("names", Arrays.<String>asList("test Name"));
		}
		List<String> list = (List<String>)ret.getProperty("names");
		if(list==null)
			list=new ArrayList<String>();

		list.add(0, key);
		employee.setProperty("names", list);
		
		datastore.put(employee);
	}
}
