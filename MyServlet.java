package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;

import javax.print.attribute.standard.DateTimeAtCompleted;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		String apiKey="2c62e60a76a0b6fd8e8c0ab22583759a\n"
				+ "";
		
		String city=request.getParameter("city");
		
		 
		
		
		//create the url for openweather API requestion
	   
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + apiKey;
		//API Integeration
		URL url=new URL(apiUrl);
		HttpURLConnection connection=(HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		
		
		//Reading data from network
		InputStream inputStream=connection.getInputStream(); //connection se input milega
		InputStreamReader reader=new InputStreamReader(inputStream); //ab us input ko read krna hai
		
		//want to store in string
		Scanner scanner=new Scanner(reader);
		StringBuilder responseContent=new StringBuilder();
		
		while(scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
		}
		
		scanner.close();
		
		//type casting(parsing data into json changing repsonseContent data(String we get data in string format) into json format data
		Gson  gson=new Gson();
		JsonObject jsonObject =gson.fromJson(responseContent.toString(),JsonObject.class);
		
		
		//date & time
		long dateTimeStamp=jsonObject.get("dt").getAsLong()*1000;
		Date date =new Date(dateTimeStamp);
		
		
		
		//temperature
		double temperatureKeliv=jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelius=(int) (temperatureKeliv-273.15);
		
		//humidity
		int humidity=jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		
		//windspeed
		 double windSpeed=jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		 
		 //weather condtion
		 String weatherCondition=jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		 
		 //set the data as request attributes(for sending to the jsp page)
		 request.setAttribute("date", date);
		 request.setAttribute("city", city);
		 request.setAttribute("temperature", temperatureCelius);
		 request.setAttribute("weatherConditon", weatherCondition);
		 request.setAttribute("humidity", humidity);
		 request.setAttribute("windspeed", windSpeed);
		 request.setAttribute("weatherData", responseContent.toString());
		 
		 
		 
		 connection.disconnect();
		 
		 request.getRequestDispatcher("index.jsp").forward(request, response);



	}

}
