package cs263.cs263;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/User")
public class Service {

	@POST
	@Path("/add")
	//@Consumes({ "text/plain", MediaType.APPLICATION_JSON })
	// @Produces({ "application/json", "text/plain" })
	public boolean AddUser(String input) {
		System.out.println(input);
		return UserServices.Add(input);
	}

	@GET
	@Path("/get/{input}")
	// @Produces({ "application/json", "text/plain" })
	public String GetUser(@PathParam("input") String input) {
		System.out.println(input);
		return UserServices.Get(input);
	}
	@GET
	@Path("/login/{input}")
	// @Produces({ "application/json", "text/plain" })
	public String Login(@PathParam("input") String input) {
		System.out.println(input);
		return Login.toJson();
	}
}
