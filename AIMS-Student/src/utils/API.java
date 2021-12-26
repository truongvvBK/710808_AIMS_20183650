package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;

/**
 * Class provides methods to send request to server and receive data
 * @author dattq.180042
 * @version 1.0
 */
public class API {

	/**
	 * Thuộc tính giúp format ngày tháng theo định dạng
	 */
	public static DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	/**
	 * Thuộc tính giúp log thông tin ra console
	 */
	private static final Logger LOGGER = Utils.getLogger(Utils.class.getName());

	/**
	 * Thiết lập connection từ server
	 * @param url : Đường dẫn tới server cần request
	 * @param method : Giao thức tới API
	 * @param token : Đoạn code cần cung cấp để xác thực người dùng
	 * @return connection
	 */
	private static HttpURLConnection setupConnection(String url, String method, String token) throws IOException{
		LOGGER.info("Request URL: " + url + "\n");
		URL line_api_url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) line_api_url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		return conn;
	}

	/**
	 * Đọc dữ liệu trả về từ server
	 * @param conn: connection to server
	 * @return response : Phẩn hồi tới server
	 */
	private static String readResponse(HttpURLConnection conn) throws IOException {
		BufferedReader in;
		String inputLine;
		if (conn.getResponseCode() / 100 == 2) {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		LOGGER.info("Response Info: " + response);
		return response.toString();
	}

	/**
	 * Phương thức giúp gọi các API đang GET
	 * @param url : Đường dẫn tới server cần request
	 * @param token : Đoạn code xác thực user
	 * @return response: Phản hồi từ server
	 */
	public static String get(String url, String token) throws Exception {
		HttpURLConnection conn = setupConnection(url, "GET", token);
		return readResponse(conn);
	}

	/**
	 * Phương thức gọi các API đang POST (thanh toán, ...)
	 * @param url : Đường dẫn tới server
	 * @param data : Dữ liệu cần xử lý
	 * @param token : Mã xác thực
	 * @return respone: Phản hồi từ server
	 */
	public static String post(String url, String data, String token) throws IOException {

		allowMethods("PATCH");

		HttpURLConnection conn = setupConnection(url, "PATCH", token);

		Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		writer.write(data);
		writer.close();

		return readResponse(conn);
	}

	private static void allowMethods(String... methods) {
		try {
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
			methodsField.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

			String[] oldMethods = (String[]) methodsField.get(null);
			Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
			methodsSet.addAll(Arrays.asList(methods));
			String[] newMethods = methodsSet.toArray(new String[0]);

			methodsField.set(null/* static field */, newMethods);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

}
