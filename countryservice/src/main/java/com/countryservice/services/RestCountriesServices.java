package com.countryservice.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.countryservice.common.BaseCountry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class RestCountriesServices {

	public String getCountryDetailsByName(String countryName) throws URISyntaxException, IOException, InterruptedException {

		String endPoint="https://restcountries.com/v3.1/name/" + countryName +"?fullText=true";
		HttpResponse<String> httpResponse = sendGetRequest(endPoint);

		System.out.println(httpResponse.statusCode());
		return httpResponse.body();

	}

	public String getAllCountriesDetails() throws URISyntaxException, IOException, InterruptedException {

		String endPoint="https://restcountries.com/v3.1/all";
		HttpResponse<String> httpResponse = sendGetRequest(endPoint);

		//System.out.println(httpResponse.statusCode());
		return httpResponse.body();
	}

	public JsonArray filterDetails(String population , String area , String language) throws URISyntaxException, IOException, InterruptedException {

		String jsondata = getAllCountriesDetails();
		JsonArray filtredArray = filterResponseJson(jsondata,population,area,language);
		return sortResult(filtredArray);
		//return new JsonArray();
		//return filterResponseJson(jsondata,population,area,language);
	}

	public HttpResponse<String> sendGetRequest(String endPoint) throws URISyntaxException, IOException, InterruptedException{

		System.out.println("End Point is : "+endPoint);


		HttpRequest request = HttpRequest.newBuilder()
				.uri(new URI(endPoint))
				.version(HttpClient.Version.HTTP_2)
				.timeout(Duration.ofSeconds(10L))
				.GET()
				.build();

		HttpClient client =HttpClient.newBuilder().build();
		HttpResponse<String> httpResponse = client.send(request,
				HttpResponse.BodyHandlers.ofString());

		return httpResponse;

	}

	protected JsonArray filterResponseJson(String jsondata ,String population , String area , String language) {

		JsonArray arr = JsonParser.parseString(jsondata).getAsJsonArray();

		JsonArray Res =new JsonArray();
		int i =1;
		boolean searched = false;
		for (JsonElement jsonObj  : arr) {

			JsonArray tempRes =new JsonArray();
			//        	i++;
			//        	if(i==119) {
			//        		System.out.println(i++);
			//        	}
			if(jsonObj.getAsJsonObject().keySet().contains("languages")&&language!=null && !language.isEmpty()) {

				tempRes = filterByKey("languages",language,jsonObj);
				searched=true;

			}

			if(jsonObj.getAsJsonObject().keySet().contains("area")&&area!=null && !area.isEmpty()) {        		

				//System.out.println(jsonObj.getAsJsonObject().get("area"));
				if(searched==true && tempRes.isEmpty()) {
					searched = false;
					continue;
				}

				tempRes = filterElementByKey("area",area,jsonObj); 
				searched=true;
			}

			if(jsonObj.getAsJsonObject().keySet().contains("population")&&population!=null && !population.isEmpty()) {

				if(searched==true && tempRes.isEmpty()) {
					searched = false;
					continue;
				}
				tempRes = filterElementByKey("population",population,jsonObj);         		
			}

			searched = false;

			//        	String lang = "language not found";
			//        	if(jsonObj.getAsJsonObject().keySet().contains("languages"))
			//        	{
			//        		lang = jsonObj.getAsJsonObject().get("languages").getAsJsonObject().toString();
			//        	}
			if(!tempRes.isEmpty()){
				Res.addAll(tempRes);
				//        		System.out.println("Adding  languages : "  +lang 
				//        				+ " area : " + jsonObj.getAsJsonObject().get("area").toString()
				//        				+ " population : " + jsonObj.getAsJsonObject().get("population").toString());
			}
			//        	else {
			//        		System.out.println("NotAdding  languages : "   +lang 
			//        				+ " area : " + jsonObj.getAsJsonObject().get("area").toString()
			//        				+ " population : " + jsonObj.getAsJsonObject().get("population").toString());
			//        	}

			//        	for(String key : itr.getAsJsonObject().keySet()) {
			//        		
			//        		 
			//        		if(language!=null && !language.isEmpty() && key.equalsIgnoreCase("languages")) {
			//        			JsonElement languageJson = itr.getAsJsonObject().get(key);
			//        			System.out.println(itr.getAsJsonObject().get(key).getAsString().equalsIgnoreCase(language));
			//        			System.out.println(lan);
			//        			res.add(itr);
			//        		}
			//        		
			//        	}
			//System.out.println("\n");
		}


		return Res;
	}

	public JsonArray filterByKey(String jsonKey , String jsonValue , JsonElement jsonObj) {

		JsonArray tempRes =new JsonArray();		
		JsonObject languageJson = jsonObj.getAsJsonObject().get(jsonKey).getAsJsonObject();

		for (String key : languageJson.keySet()) {
			if(languageJson.get(key).getAsString().equalsIgnoreCase(jsonValue)) { 				
				tempRes.add(jsonObj);
				continue;
			}
		}
		return tempRes;

	}

	public JsonArray filterElementByKey(String jsonKey , String jsonValue , JsonElement jsonObj) {	

		JsonArray tempRes =new JsonArray();	
		if(Double.compare(Double.parseDouble(jsonObj.getAsJsonObject().get(jsonKey).toString()), Double.parseDouble(jsonValue))>=0){
			tempRes.add(jsonObj);			
		}

		return tempRes;

	}

	protected JsonArray sortResult(JsonArray arr) throws IOException {

		JsonArray sortedJsonArray = new JsonArray();

		List<JsonObject> jsonValues = new ArrayList<JsonObject>();

		for (JsonElement jsonObj  : arr) {

			jsonValues.add(jsonObj.getAsJsonObject());
		}

		Collections.sort( jsonValues, new Comparator<JsonObject>() {
			@Override
			public int compare(JsonObject a, JsonObject b) {		         		            
				return a.getAsJsonObject().get("name").toString().compareTo(b.getAsJsonObject().get("name").toString());		            
			}
		});

		for (JsonElement jsonObj  : jsonValues) {

			sortedJsonArray.add(jsonObj.getAsJsonObject());
		}

		return sortedJsonArray;
	}

}
