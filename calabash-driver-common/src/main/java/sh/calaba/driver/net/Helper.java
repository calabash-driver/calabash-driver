package sh.calaba.driver.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class Helper {

	public static JSONObject extractObject(HttpResponse resp)
			throws IOException, JSONException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(resp
				.getEntity().getContent()));
		StringBuilder s = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			s.append(line);
		}
		rd.close();
		String str = s.toString();
		return new JSONObject(str);
	}
}
