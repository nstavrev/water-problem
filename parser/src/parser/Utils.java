package parser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Utils {
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	public static String encodeURIComponent(String s) {
        String result = null;

        try
        {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");

        // This exception should never occur.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }
	
	public static void sendRequest(String targetUrl) {
		URL url;
		HttpURLConnection connection;
		try {
			url = new URL(targetUrl);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			System.out.println("Response " + connection.getResponseCode());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
