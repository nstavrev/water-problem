package com.water;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.math3.util.Pair;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import parser.model.Measurement;

import com.google.gson.Gson;

/**
 * Servlet implementation class RunAlgorithm
 */
@WebServlet("/run")
public class RunAlgorithm extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RunAlgorithm() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().print("Hello");
		
		Algorithm alg;
		
		// 
		
		String algName = request.getParameter("algName");
		Date startDate = convertToDate(request.getParameter("startDate"));
		Date endDate = convertToDate(request.getParameter("endDate"));
		switch (algName) {
			case "linearRegression": {
				alg = new SimpleRegression();
				try{
				Map<Pair<Double, Double>, Map<Date, Double>> prediction = runAlgorithm(alg, endDate);
				returnResponse(response, prediction); // [ 34.56  , 3435.5656 ]
				} catch(Exception ex) {
					response.getWriter().print(ex.getMessage());
					response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
				}
			}
		}
	}

	private void returnResponse(HttpServletResponse response,
			Map<Pair<Double, Double>, Map<Date, Double>> prediction) throws IOException {
		PrintWriter writer;
			writer = response.getWriter();
			
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON);
			String json = convertResponse(prediction); // convert jsonObjectToByteArr
			writer.print(json );
	}

	private String convertResponse(Map<Pair<Double, Double>, Map<Date, Double>> prediction) {
		Gson gson = new Gson();
		return gson.toJson(prediction);
	}

	private Map<Pair<Double, Double>, Map<Date, Double>> runAlgorithm(Algorithm alg, Date endDate) throws ClientProtocolException, IOException, URISyntaxException {
		
		DataSet dSet = getData(endDate);
		alg.setData(dSet);
		return alg.predict(endDate);
	}

	
	private DataSet getData(Date endDate) throws ClientProtocolException, IOException, URISyntaxException{
		
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet();
		httpGet.setURI(new URI("http://192.168.1.7:1337/service/getLatestMeasurements"));
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		Calendar c = Calendar.getInstance();
		c.setTime(endDate);
		basicHttpParams.setParameter("date", c.get(Calendar.DATE) + "/" + 
				   (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR));
		httpGet.setParams(basicHttpParams);
		HttpResponse response = client.execute(httpGet);
		InputStreamReader content = new InputStreamReader(response.getEntity().getContent());
		
		return createDataSet(content.toString());
	}

	private DataSet createDataSet(String content) {
	
		content = content.substring(1,  content.length() - 1);
		String[] splitByBracket = content.split("]");
		List<Measurement> result = new ArrayList<Measurement>();
		for(int i = 0; i < splitByBracket.length; i++) {
			splitByBracket[i] = splitByBracket[i].substring(1, splitByBracket[i].length() - 1);
			String[] splitByComma = splitByBracket[i].split(",");
			for(int j = 0; j < splitByComma.length; i++) {
				
				splitByComma[0] = splitByComma[0].substring(1, splitByComma[0].length() - 1);
				Date date = convertToDate(splitByComma[0]);
				double waterQuality = Double.parseDouble(splitByComma[1]);
				double latitude = Double.parseDouble(splitByComma[2]);
				double longtitude = Double.parseDouble(splitByComma[3]);
				result.add(new Measurement(date, waterQuality, latitude, longtitude));
			}
		}
		return new DataSet(result);
	}

	private Date convertToDate(String date) {
		
		String[] splitedDate = date.split("/");
		return new Date(Integer.parseInt(splitedDate[0]),
				Integer.parseInt(splitedDate[1]), 
				Integer.parseInt(splitedDate[2]));
	}
}
