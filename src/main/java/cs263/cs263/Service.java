package cs263.cs263;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

@Path("/User")
public class Service {

	@Context
	private HttpServletRequest request;

	@POST
	@Path("/add")
	// @Consumes({ "text/plain", MediaType.APPLICATION_JSON })
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
		return String.valueOf(UserServices.Get(input));
	}

	@GET
	@Path("/session/{input}")
	// @Produces({ "application/json", "text/plain" })
	public String GetSession(@PathParam("input") String input) {
		System.out.println(input);
		return String.valueOf(request.getSession().getAttribute(input));
	}

	@GET
	@Path("/set/{input}")
	// @Produces({ "application/json", "text/plain" })
	public void SetSession(@PathParam("input") String input) {
		System.out.println(input);
		request.getSession().setAttribute("username", input);
	}

	@GET
	@Path("/login/{input}")
	// @Produces({ "application/json", "text/plain" })
	public boolean Login(@PathParam("input") String input) {
		System.out.println(input);
		Object objs = JSONValue.parse(input);
		JSONObject obj = (JSONObject) (objs);
		if (Login.passwordMatch((String) obj.get("username"),
				(String) obj.get("password"))) {
			request.getSession().setAttribute("username",
					(String) obj.get("username"));
			return true;
		} else
			return false;
	}

}
