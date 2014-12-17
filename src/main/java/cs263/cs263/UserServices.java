package cs263.cs263;

import java.util.Map;

public class UserServices {

	public static boolean Add(String input) {

		Map<String, String> map = Json.fromPost(input);

		return User.Add(map.get("username"), map.get("password"),
				map.get("email"));
	}

	public static String Get(String input) {
		return String.valueOf(User.Get(input));
	}
}
