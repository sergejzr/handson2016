package uk.soton.examples.nlp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class DBPediaReader {
	private final static String API_URL = "http://spotlight.sztaki.hu:2222/";
	private static final double CONFIDENCE = 0.6;
	private static final int SUPPORT = 0;

	public static void main(String[] args) {
		DBPediaReader r = new DBPediaReader();

		String text = "Dubrovnik is a Croatian city on the "
				+ "Adriatic Sea, in the region of Dalmatia. It is one of the most " + "prominent tourist destinations "
				+ "in the Mediterranean Sea, a seaport and the centre of "
				+ "Dubrovnik-Neretva County. Its total population is 42,615 "
				+ "(census 2011). In 1979, the city of Dubrovnik joined the UNESCO " + "list of World Heritage Sites.";

		r.annotate(text);
	}

	public void annotate(String text) {
		try {
			Client client = Client.create();
			
			String requrl = API_URL + "rest/annotate/?" + 
									"confidence=" + CONFIDENCE + 
									"&text="+ URLEncoder.encode(text, "utf-8");

			WebResource webResource = client.resource(requrl);

			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());

			}

			JSONTokener tokener = new JSONTokener(response.getEntityInputStream());

			// in case you need to modify string before parsing:
			// String output = response.getEntity(String.class);
			// System.out.println(output);
			// output=output.replaceAll("a","b");
			// JSONTokener tokener = new JSONTokener(new StringReader(output));

			JSONObject resultJSON = new JSONObject(tokener);
			JSONArray entities = resultJSON.getJSONArray("Resources");
			for (int i = 0; i < entities.length(); i++) {
				try {
					JSONObject entity = entities.getJSONObject(i);
					
					System.out.println(
									"surfaceForm: "	+ entity.getString("@surfaceForm") + " " + 
									"URI: " + entity.getString("@URI") + " " + 
									"support: " + entity.getString("@support") + " " +
									"types: " + entity.getString("@types") + " " + 
									"offset: " + entity.getString("@offset")+ " " + 
									"similarityScore: " + entity.getString("@similarityScore") + " " + 
									"percentageOfSecondRank: " + entity.getString("@percentageOfSecondRank") + "\n");

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
