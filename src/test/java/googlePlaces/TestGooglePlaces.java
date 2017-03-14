package googlePlaces;

import static org.testng.Assert.*;

import java.io.*;
import java.net.*;
import java.util.*;

import org.springframework.http.*;
import org.springframework.web.client.*;
import org.testng.annotations.*;

import com.fasterxml.jackson.databind.*;

public class TestGooglePlaces {

	public static ResponseEntity<String> restGETRequest(String respRestUrl) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> responseRequest = restTemplate.getForEntity(respRestUrl, String.class);
		return responseRequest;
	}

	@Test(enabled = true, groups="2", dependsOnGroups = "1")
	public void postPlace() throws MalformedURLException, IOException {
		From Postman
		https://maps.googleapis.com/maps/api/place/add/json?key=AIzaSyCiQXxmVFpuT7XOGoM79i1UzE8lFFlQh2Q
		{
			  "location": {
			    "lat": 37.7761037000000,
			    "lng": -122.42605870003
			  },
			  "accuracy": 50,
			  "name": "Galina's Salon",
			  "phone_number": "(415) 555-9999",
			  "types": ["beauty_salon"],
			  "website": "http://www.galina.com/",
			  "language": "en-US"
			}
	returns:
	{
		  "id": "30609a17d272ffcdfc2aba18df10c1592452ea2d",
		  "place_id": "qgYvCi0wMDAwMDBkN2UzYjIxZTg4OjgwODU4MGEyNDdiOjQzODYyZWY2MmZmMjhjNmI",
		  "reference": "CkQxAAAAfNIkgBCu9lRLn8BnLifaXClohttSSb-SmyToNqQyDvE2DOu9muZtyOFen6uNc0buch-wBaS-xYmLThPumQ3PZRIQ6yn7dnH5j-dcr09qZtpbpBoUxP9QOkBrYg_qYF2hLukrCaRayIw",
		  "scope": "APP",
		  "status": "OK"
		}
	}

	@Test(enabled = true, groups="4", dependsOnGroups = "3")
	public void deletePlace() throws MalformedURLException, IOException {
		From Postman
		POST https://maps.googleapis.com/maps/api/place/delete/json?key=YOUR_API_KEY HTTP/1.1
			{
			  "place_id": "place ID"
			}
	returns:
	{
		  "status": "OK"
		}
	}

	@Test(enabled = true, groups = "3", dependsOnGroups = "2")
	public void searchPlacesAfter() throws MalformedURLException, IOException {
		String url = "https://maps.googleapis.com";
		String resource = "/maps/api/place/nearbysearch/";
		String outputFormat = "json";
		String charset = java.nio.charset.StandardCharsets.UTF_8.name();
		String location = "37.7749,-122.4194";
		String radius = "500";
		String type = "beauty_salon";
		String keyword = "galina";
		String key = "AIzaSyCiQXxmVFpuT7XOGoM79i1UzE8lFFlQh2Q";
		String query = String.format("location=%s&radius=%s&type=%s&keyword=%s&key=%s", location, radius, type, keyword,
				key);
		String fullUrl = url + resource + outputFormat + "?" + query;
		System.out.println(fullUrl);
		ResponseEntity<String> response = restGETRequest(fullUrl);
		ObjectMapper mapper = new ObjectMapper();

		JsonNode root = mapper.readTree(response.getBody());
		JsonNode status = root.path("status");
		String statusValue = status.asText();
		assertEquals(statusValue, "OK", "Status value is not OK.");

		ArrayList<String> nameResults = new ArrayList();
		JsonNode results = root.path("results");
		// not sure how to get it to go to each index to get the name - what do
		// I put in the path parameter?
		JsonNode result = results.path(0);
		Iterator<JsonNode> elements = result.elements();
		while (elements.hasNext()) {
			JsonNode currentResult = elements.next();
			JsonNode name = currentResult.path("name");
			nameResults.add(name.asText());
		}
		ArrayList<String> expectedNameResults = new ArrayList<>(Arrays.asList("Earthbody", "Galina's Salon"));
		assertEquals(nameResults, expectedNameResults, "Names results do not match expected.");
	}

	@Test(enabled = true, groups = "1")
	public void searchPlacesBefore() throws MalformedURLException, IOException {
		String url = "https://maps.googleapis.com";
		String resource = "/maps/api/place/nearbysearch/";
		String outputFormat = "json";
		String charset = java.nio.charset.StandardCharsets.UTF_8.name();
		String location = "37.7749,-122.4194";
		String radius = "500";
		String type = "beauty_salon";
		String keyword = "galina";
		String key = "AIzaSyCiQXxmVFpuT7XOGoM79i1UzE8lFFlQh2Q";
		String query = String.format("location=%s&radius=%s&type=%s&keyword=%s&key=%s", location, radius, type, keyword,
				key);
		String fullUrl = url + resource + outputFormat + "?" + query;
		System.out.println(fullUrl);
		ResponseEntity<String> response = restGETRequest(fullUrl);
		ObjectMapper mapper = new ObjectMapper();

		JsonNode root = mapper.readTree(response.getBody());
		JsonNode status = root.path("status");
		String statusValue = status.asText();
		assertEquals(statusValue, "OK", "Status value is not OK.");

		ArrayList<String> nameResults = new ArrayList();
		JsonNode results = root.path("results");
		// not sure how to get it to go to each index to get the name - what do
		// I put in the path parameter?
		JsonNode result = results.path(0);
		Iterator<JsonNode> elements = result.elements();
		while (elements.hasNext()) {
			JsonNode currentResult = elements.next();
			JsonNode name = currentResult.path("name");
			nameResults.add(name.asText());
		}
		ArrayList<String> expectedNameResults = new ArrayList<>(Arrays.asList("Earthbody"));
		assertEquals(nameResults, expectedNameResults, "Names results do not match expected.");
	}
}
