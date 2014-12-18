package cs263.cs263;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

@Path("/trip")
public class TripServices {

	@Context
	private HttpServletRequest request;

	@POST
	@Path("/add")
	// @Consumes({ "text/plain", MediaType.APPLICATION_JSON })
	// @Produces({ "application/json", "text/plain" })
	public boolean AddTrip(String input) {
		System.out.println(input);

		String name = null;
		try {
			name = request.getSession().getAttribute("username").toString();
		} catch (Exception e) {
		}
		if (null != name) {

			Object objs = JSONValue.parse(input);
			JSONObject obj = (JSONObject) (objs);
			System.out.println(obj);
			return Trip.Add(name, obj.get("id").toString(), obj.get("trip")
					.toString());
		} else {
			return false;
		}
	}

	@GET
	@Path("/get/{input}")
	// @Produces({ "application/json", "text/plain" })
	public String GetTrip(@PathParam("input") String input) {
		System.out.println(input);

		String name = null;
		try {
			name = request.getSession().getAttribute("username").toString();
		} catch (Exception e) {
		}
		if (null != name) {

			Object objs = JSONValue.parse(input);
			JSONObject obj = (JSONObject) (objs);
			String res = Trip.Get(name, obj.get("id").toString());
			System.out.println(res);
			return res;
		} else {
			return "FOB";
		}
	}

	@GET
	@Path("/getall/{input}")
	// @Produces({ "application/json", "text/plain" })
	public String GetTripIds(@PathParam("input") String input) {
		System.out.println(input);

		String name = null;
		try {
			name = request.getSession().getAttribute("username").toString();
		} catch (Exception e) {
		}
		if (null != name) {

			String res = Trip.GetAllId(name).replace("[,", "[")
					.replace(",]", "]");
			System.out.println(res);
			return res;
		} else {
			return "FOB";
		}
	}

	public Response check() {
		try {
			if (null != request.getAttribute("username")) {
				java.net.URI tt = new java.net.URI("/login.html");
				return Response.seeOther(tt).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
