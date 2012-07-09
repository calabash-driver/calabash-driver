package sh.calaba.driver.server.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import sh.calaba.driver.net.FailedWebDriverLikeResponse;
import sh.calaba.driver.net.WebDriverLikeCommand;
import sh.calaba.driver.net.WebDriverLikeRequest;
import sh.calaba.driver.net.WebDriverLikeResponse;
import sh.calaba.driver.server.Handler;
import sh.calaba.driver.server.command.CommandMapping;

public class CalabashServlet extends CalabashProxyBasedServlet {

	private static final long serialVersionUID = -8544875030463578977L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			process(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			process(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			process(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void process(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		WebDriverLikeRequest req = getRequest(request);
		WebDriverLikeResponse resp = getResponse(req);

		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		// TODO implement the json protocol properly.
		if (req.getGenericCommand() == WebDriverLikeCommand.NEW_SESSION) {
			String session = resp.getSessionId();
			System.out.println("CalabashServlet: new session: " + session);
			// TODO DDARY protocol, host & port need to be written there, values
			// seems not to be very important
			response.setHeader("Location", "http://localhost:2345/session/"
					+ session);
			response.setStatus(301);
			writeResultToResponse(response, resp);
		} else {
			response.setStatus(200);
			writeResultToResponse(response, resp);
		}

	}

	private void writeResultToResponse(HttpServletResponse response,
			WebDriverLikeResponse resp) throws Exception {
		//System.out.println("Response CS: " + resp.getValue());
		String responseTXT = resp.stringify();

		response.getWriter().print(responseTXT);
		response.getWriter().close();
	}

	private WebDriverLikeRequest getRequest(HttpServletRequest request)
			throws Exception {

		String method = request.getMethod();
		String path = request.getPathInfo();
		String json = null;
		if (request.getInputStream() != null) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			StringBuilder s = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				s.append(line);
			}
			rd.close();
			json = s.toString();
		}
		JSONObject o = new JSONObject();
		if (json != null && !json.isEmpty()) {
			o = new JSONObject(json);
		}

		WebDriverLikeRequest orig = new WebDriverLikeRequest(method, path, o);
		return orig;

	}

	private WebDriverLikeResponse getResponse(WebDriverLikeRequest request) {
		WebDriverLikeCommand wdlc = request.getGenericCommand();
		try {

			Handler h = CommandMapping.get(wdlc).createHandler(
					getCalabashProxy(), request);
			return h.handle();
		} catch (Exception e) {
			System.out.println("Error occured: " + request.getPath()
					+ " Error: " + e);
			System.out.println(request.toString());
			// TODO ddary find somehting better
			return new FailedWebDriverLikeResponse(
					request.getVariableValue(":sessionId"), e);
		}

	}
}
