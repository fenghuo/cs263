package cs263.cs263;

import java.util.HashMap;
import java.util.Map;

public class Json {
	public static String toJson(HashMap<String, String> map) {
		String res = "{";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			res += entry.getKey() + ":" + entry.getValue() + ",";
		}
		return res + "}";
	}

	public static HashMap<String, String> fromJson(String input) {
		String res = input.substring(1, input.length() - 1);
		HashMap<String, String> map = new HashMap<String, String>();
		String[] pair = res.split(",");
		for (String p : pair)
			map.put(p.split(":")[0], p.split(":")[1]);
		return map;
	}

	public static HashMap<String, String> fromPost(String input) {
		HashMap<String, String> map = new HashMap<String, String>();
		String[] pair = input.split("&");
		for (String p : pair)
			map.put(p.split("=")[0], p.split("=")[1]);
		return map;
	}
}
