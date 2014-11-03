package cs263.cs263;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.appengine.api.datastore.EntityNotFoundException;

@Path("/jerseyws")
public class MyTest {

    @GET
    @Path("/my")
    public String testMethod() throws EntityNotFoundException {
    	System.out.println("test");
    	return DataStore.Test();
    }
}
