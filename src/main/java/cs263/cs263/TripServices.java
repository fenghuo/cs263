package cs263.cs263;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

@Path("/trip")
public class TripServices {

	@POST
	@Path("/add")
	// @Consumes({ "text/plain", MediaType.APPLICATION_JSON })
	// @Produces({ "application/json", "text/plain" })
	public boolean AddTrip(String input) {
		System.out.println(input);
		Object objs = JSONValue.parse(input);
		JSONObject obj = (JSONObject) (objs);
		System.out.println(obj);
		return Trip.Add(obj.get("username").toString(), obj.get("id")
				.toString(), obj.get("trip").toString());
	}

	@GET
	@Path("/get/{input}")
	// @Produces({ "application/json", "text/plain" })
	public String GetTrip(@PathParam("input") String input) {
		System.out.println(input);
		Object objs = JSONValue.parse(input);
		JSONObject obj = (JSONObject) (objs);
		String res = Trip.Get(obj.get("username").toString(), obj.get("id")
				.toString());
		System.out.println(res);
		return res;
	}

	@GET
	@Path("/getall/{input}")
	// @Produces({ "application/json", "text/plain" })
	public String GetTripIds(@PathParam("input") String input) {
		System.out.println(input);
		String res = Trip.GetAllId(input).replace("[,", "[").replace(",]", "]");
		System.out.println(res);
		return res;
	}
}
