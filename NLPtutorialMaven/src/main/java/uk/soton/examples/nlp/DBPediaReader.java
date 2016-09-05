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
	private static final double CONFIDENCE = 0.0;
	private static final int SUPPORT = 0;
	
	public static void main(String[] args) {
		DBPediaReader r=new DBPediaReader();
		r.annotate(" Barack Obama. He is a president of the United States. Next year there will be another president. This Friday I am free. Anyone around know where a good place to get hair colored at?  Mod Salon!  Kate and Co! Get Kelsey to do it What are the price ranges?  My place  Walmart you can do it yourself unless you want to pay something crazy Depends on how long your hair is and what you want done, if you look on Mods website it will give you an idea  Capri! Ask for an advanced student. Good prices with wonderful results  College hill barbers, ask for jenny  Posh on university-Cedar Loo area");
	}
	public void annotate(String text)
	{
		try {
			Client client = Client.create();
			String requrl = API_URL + "rest/annotate/?" +
					"confidence=" + CONFIDENCE
					+ "&support=" + SUPPORT
					+ "&text=" + URLEncoder.encode(text, "utf-8");
			
			WebResource webResource = client
					   .resource(requrl);
					
					ClientResponse response = webResource.accept("application/json")
			                .get(ClientResponse.class);

					if (response.getStatus() != 200) {
					   throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
					  
					}

					

					 JSONTokener tokener = new JSONTokener(response.getEntityInputStream());
					 
					 //in case you need to modify string before parsing:
					//String output = response.getEntity(String.class);
					 //output=output.replaceAll("a","b");
					//JSONTokener tokener = new JSONTokener(new StringReader(output));
					 

				     JSONObject resultJSON = new JSONObject(tokener);
				     JSONArray entities = resultJSON.getJSONArray("Resources");
				     
				     System.out.println(resultJSON);
				     for(int i = 0; i < entities.length(); i++) {
							try {
								JSONObject entity = entities.getJSONObject(i);
								System.out.println(entity.getString("@surfaceForm")+" "+
										entity.getString("@URI")+" "+
												entity.getString("@support")+"("+entity.getString("@similarityScore")+")  "+entity.getString("@types")+"\n");

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
