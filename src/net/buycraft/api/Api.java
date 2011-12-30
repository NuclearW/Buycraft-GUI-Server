package net.buycraft.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;

import net.buycraft.Buycraft;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Performs calls to the BuyCraft API
 * 
 * @author Lmc
 */
public class Api
{
	private Buycraft buycraft;
	
	private long lastCallTime = 0;
	private long apiCoolDownTime = 20000;
	
	public Api(Buycraft instance)
	{
		this.buycraft = instance;
	}
	
	/**
	 * Performs a call to the API with given parameters and returns the JSON response
	 * 
	 * @param ArrayList params
	 * @author Lmc
	 */
	public JSONObject call(HashMap<String, String> params)
	{	
		// Generate the full URL to submit to the API
		String url = this.buycraft.apiUrl + this.generateUrlQueryString(params);

		// Send a HTTP request to the API
		String HTTPResponse = this.HTTPRequest(url);
		
		if(System.currentTimeMillis() > (this.lastCallTime + this.apiCoolDownTime)) // Prevent connection spamming to our API
		{
			try 
			{
				if(HTTPResponse != null)
				{
					this.lastCallTime = System.currentTimeMillis();
					
					// Return the JSON object where we can then receive information from the API
					return new JSONObject(HTTPResponse);
				}
				else
				{
					return null;
				}
			} 
			catch (JSONException e) 
			{
				this.buycraft.logger.severe("[Buycraft] JSON parsing error.");
			}
		}
		else
		{
			this.buycraft.logger.severe("[Buycraft] API call prevented due to " + this.apiCoolDownTime / 1000 + " second cool down period not reached.");
		}

		return null;
	}

	/**
	 * Performs the connect API call
	 * 
	 * @author	Lmc
	 */
	public ApiResponse connect()
	{
		try 
		{
			HashMap<String, String> apiCall = new HashMap<String, String>();
			
			// Set API call details
			apiCall.put("secret", this.buycraft.serverSecret);
			apiCall.put("action", "connect");
			apiCall.put("version", String.valueOf(this.buycraft.version));
			
			// Call the API
			JSONObject apiResponse = this.buycraft.api.call(apiCall);
		
			if(apiResponse != null)
			{
				// First, check if a payload object has been given, if not, something wasnt entered correctly.
				if(apiResponse.isNull("payload") == false)
				{
					JSONObject payload = apiResponse.getJSONObject("payload");
					
					// Set various important data
					this.buycraft.paymentsCheckerInterval = payload.getLong("checker_interval");
					this.buycraft.packagesForSale = payload.getJSONArray("packages");
					this.buycraft.serverId = payload.getInt("server");
					
					// Check if this buycraft version is out of date
					if(Float.valueOf(payload.get("version").toString()) > Float.valueOf(this.buycraft.version))
					{
						return ApiResponse.CONNECT_OLD_VERSION;
					}
				}
				else if(apiResponse.getInt("error") == 1001) // Check if API secret was found
				{
					return ApiResponse.CONNECT_INVALID_SECRET;
				}
				
				return ApiResponse.CONNECT_SUCCESS;
			}
		} 
		catch(JSONException e)
		{
			this.buycraft.logger.severe("[Buycraft] JSON parsing error.");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return ApiResponse.CONNECT_ERROR;
	}
	
	/**
	 * Performs a HTTP Request and returns the result as a string
	 * 
	 * @author	Lmc
	 */
	public String HTTPRequest(String url)
	{	 
		try
		{
			String content = "";
			
			URL conn = new URL(url);
	        URLConnection yc = conn.openConnection();
	        
	        yc.setConnectTimeout(5000);
	        yc.setReadTimeout(5000);
	        
	        BufferedReader in;
			
			in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

	        String inputLine;
	       
			while ((inputLine = in.readLine()) != null) 
			{
				content = content + inputLine;
			}
			
			in.close();
			
			return content;
		}
		catch(ConnectException e)
		{
			this.buycraft.logger.severe("[Buycraft] HTTP request failed due to connection error.");
		}
		catch(SocketTimeoutException e)
		{
			this.buycraft.logger.severe("[Buycraft] HTTP request failed due to timeout error.");
		}
		catch(UnknownHostException e)
		{
			this.buycraft.logger.severe("[Buycraft] HTTP request failed due to hostname not found.");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Generates a URL friendly query string
	 * 
	 * @author	Lmc
	 */
	private String generateUrlQueryString(HashMap<String, String> params)
	{
		int loopCount = 0;
		int hashMapSize = params.size() - 1;
		
		String queryString = "";
		
		// Generate URL params string
		for (String key : params.keySet()) 
		{
			// Check if we need to add the beginning "?"
			if(loopCount == 0)
			{
				queryString = "?";
			}
			
			// Format the query string segment			
			queryString = queryString + key + "=" + params.get(key);
		
			// Check if we need to add a "&"
			if(hashMapSize != loopCount)
			{
				queryString = queryString + "&";
			}
			
			loopCount++;
		}

		return queryString;
	}
}