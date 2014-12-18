package cs263.cs263;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UserServices {

	public static boolean Add(String input) {
		try {
			Object objs = JSONValue.parse(input);
			JSONObject obj = (JSONObject) (objs);
			return User.Add((String) obj.get("username"),
					(String) obj.get("password"), (String) obj.get("email"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static User Get(String input) {
		System.out.println("User:"+input);
		return User.Get(input);
	}
}
