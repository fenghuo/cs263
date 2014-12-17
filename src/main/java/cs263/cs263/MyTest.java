package cs263.cs263;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.appengine.api.datastore.EntityNotFoundException;

// http://wtp2.appspot.com/AppEngineMapDemo.htm
@Path("/jerseyws")
public class MyTest {

	@GET
	@Path("/test")
	public String testMethod() {
		return "this is a test";
	}
}
