package com.water;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

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
		Date startDate = new Date(request.getParameter("startDate"));
		Date endDate = new Date(request.getParameter("endDate"));
		switch (algName) {
			case "linearRegression": {
				alg = new SimpleRegression();
				double[] prediction = runAlgorithm(alg, endDate);
				returnResponse(response, prediction); // [ 34.56  , 3435.5656 ]
			}
			default : {
				response.getWriter().print("No Such Algorithm");
				response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
			}
		}
	}

	private void returnResponse(HttpServletResponse response,
			double[] prediction) {
		PrintWriter writer;
		try {
			writer = response.getWriter();
			
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON);
			String json = convertResponse(prediction); // convert jsonObjectToByteArr
			writer.print(json );
		} catch (IOException e) {
			e.printStackTrace();
			/// 
		}
	}

	private String convertResponse(double[] prediction) {
		Gson gson = new Gson();
		return gson.toJson(prediction);
	}

	private double[] runAlgorithm(Algorithm alg, Date endDate) {
		// TODO impl
		alg.setData(null);
		//double[] response = alg.predict(endDate);
		return null;
	}

}
