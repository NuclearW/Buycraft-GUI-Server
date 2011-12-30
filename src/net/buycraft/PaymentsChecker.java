package net.buycraft;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Checks if any users should have their package removed from them,
 * and also if any users should have thier packages given to them.
 * Run on an interval defined at plugin startup.
 * 
 * @author Lmc
 */
public class PaymentsChecker extends Thread
{
	private Buycraft buycraft;
	public boolean toStop = false;
	public boolean firstLoop = true;
	
	public int errorCount = 0;
	
	public PaymentsChecker(Buycraft instance) 
	{
		this.buycraft = instance;
	}

	public void run()
	{
		 while (true && this.toStop == false) 
		 {	
			try 
			{
				if(this.firstLoop == false) // Do not call upon first loop.
				{
					if(this.buycraft.getServer().getOnlinePlayers().length > 0) // Only perform check if players are online
					{
						HashMap<String, String> apiCall = new HashMap<String, String>();
							
						// Set API call details
						apiCall.put("secret", this.buycraft.serverSecret);
						apiCall.put("action", "checker");
						apiCall.put("do", "get");
						
						// Call the API to get the expired packages
						JSONObject apiResponse = this.buycraft.api.call(apiCall);
			            
						if(apiResponse != null)
						{
							JSONObject apiPayload = apiResponse.getJSONObject("payload");
							JSONArray expirysPayload = apiPayload.getJSONArray("expirys");
							JSONArray claimablesPayload = apiPayload.getJSONArray("claimables");
							
							ArrayList<String> executedExpirys = new ArrayList<String>();
							ArrayList<String> executedClaimables = new ArrayList<String>();
							
							// Check if we have been given any expires to process
						    if(expirysPayload.length() > 0)
							{
								// Loop through each expire
								for (int i = 0; i < expirysPayload.length(); i++) 
								{
									JSONObject row = expirysPayload.getJSONObject(i);
									
									String username = row.getString("user");
									Player currentPlayer = this.buycraft.getServer().getPlayer(username);
									
									if(currentPlayer != null && currentPlayer.isOnline()) // Only execute if player is online
									{
										// Get the commands
									    JSONArray commands = row.getJSONArray("commands");
									    
										// Loop through each command and execute
										for (int c = 0; c < commands.length(); c++) 
										{
											// Execute the command in the console
										    this.buycraft.getServer().dispatchCommand(this.buycraft.getServer().getConsoleSender(), commands.getString(c));
										}
										
										if(executedExpirys.contains(username) == false) 
										{
											executedExpirys.add(username);
										}
										
										currentPlayer.sendMessage(ChatColor.WHITE + "----------------------" + ChatColor.LIGHT_PURPLE + " BUYCRAFT " + ChatColor.WHITE + "---------------------");
										currentPlayer.sendMessage("");
										currentPlayer.sendMessage(ChatColor.GREEN + this.buycraft.lang.get("purchasedPackageExpired"));
										currentPlayer.sendMessage("");
										currentPlayer.sendMessage(ChatColor.WHITE + "-----------------------------------------------------");
									}
								}
							}
						    
						    // Check if any users have claimables awaiting for them
						    if(claimablesPayload.length() > 0)
							{
								// Loop through each claimable
								for (int i = 0; i < claimablesPayload.length(); i++) 
								{
									JSONObject row = claimablesPayload.getJSONObject(i);
							    
									String username = row.getString("user");
									Player currentPlayer = this.buycraft.getServer().getPlayer(username);
									
									if(currentPlayer != null && currentPlayer.isOnline()) // Only execute if player is online
									{
										// Get the commands
									    JSONArray commands = row.getJSONArray("commands");
									    
										// Loop through each command and execute
										for (int c = 0; c < commands.length(); c++) 
										{
											// Execute the command in the console
										    this.buycraft.getServer().dispatchCommand(this.buycraft.getServer().getConsoleSender(), commands.getString(c));
										}
										
										if(executedClaimables.contains(username) == false)
										{
											executedClaimables.add(username);
										}
										
										currentPlayer.sendMessage(ChatColor.WHITE + "----------------------" + ChatColor.LIGHT_PURPLE + " BUYCRAFT " + ChatColor.WHITE + "---------------------");
										currentPlayer.sendMessage("");
										currentPlayer.sendMessage(ChatColor.GREEN + this.buycraft.lang.get("purchasedPackageClaimed"));
										currentPlayer.sendMessage("");
										currentPlayer.sendMessage(ChatColor.WHITE + "-----------------------------------------------------");
									}
								}
							}
						    
						    if(executedExpirys.size() > 0 || executedClaimables.size() > 0) // Do we need to bother calling the API to delete expirys/claimables?
						    {
							    HashMap<String, String> finalApiCall = new HashMap<String, String>();
								
								// Set API call details
								finalApiCall.put("secret", this.buycraft.serverSecret);
								finalApiCall.put("action", "checker");
								finalApiCall.put("expirys", new JSONArray(executedExpirys.toArray()).toString());
								finalApiCall.put("claimables", new JSONArray(executedClaimables.toArray()).toString());
								finalApiCall.put("do", "delete");
								
								// Call the API to delete the expired packages
								this.buycraft.api.call(finalApiCall);
						    }
						    
						    // Since we have successfully connected, reset error counter
						    this.errorCount = 0;
						}
						else
						{
							this.errorCount++;
						}
					}
				}
				else
				{
				    this.firstLoop = false;
				}
			} 
			catch (Exception e) 
			{ 
				e.printStackTrace(); 
				
				this.errorCount++;
			}
			finally
			{
				if(this.buycraft.paymentsCheckerInterval < 300000) // Protection if interval checker is set too low. 
				{
					this.buycraft.paymentsCheckerInterval = 300000;
				}
				
				// Set how long to sleep depending on current error count (Increase if error count is higher)
				long howLongToSleep = this.buycraft.paymentsCheckerInterval + (300000 * this.errorCount);
				
				try 
				{
					// Make the thread sleep for the set interval
					sleep(howLongToSleep);
				} 
				catch (InterruptedException e) 
				{
					// If this has been thrown, it will go into an infinite loop calling our web server ALOT of times - we dont want that!
					this.buycraft.disablePlugin();
					
					return;
				}
			}
        }
	}
}
