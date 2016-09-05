package uk.soton.examples.nlp;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class JSONReader {
	
	
	public static void main(String[] args) {
		
		Client client = Client.create();

		WebResource webResource = client
		   .resource("https://gist.githubusercontent.com/hrp/900964/raw/2bbee4c296e6b54877b537144be89f19beff75f4/twitter.json");
		
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
		 

	     JSONObject dico = new JSONObject(tokener);
	     
	  
	     String username = dico.getJSONObject("user").getString("screen_name");  
	     String text = dico.getString("text");
	     System.out.println(username+":"+text);
	     
	     
	     JSONObject entities = dico.getJSONObject("entities");
	     JSONArray values = entities.getJSONArray("hashtags");

	     
	     
	     System.out.println("hashtags:");
	     if (values.length() > 0) {
             
             for (int i = 0; i < values.length(); i++) {
             	System.out.println(values.getString(i));
              }
            
         }
	     
	     
	    

	     

	      
	    

	  
	}

}
