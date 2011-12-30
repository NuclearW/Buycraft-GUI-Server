package net.buycraft;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import net.buycraft.api.Api;
import net.buycraft.api.ApiResponse;
import net.buycraft.gui.GuiManager;
import net.buycraft.gui.PackageBindingDelegate;
import net.buycraft.gui.ScreenManager;
import net.buycraft.gui.listeners.ButtonListener;
import net.buycraft.util.Language;
import net.buycraft.util.Settings;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.keyboard.Keyboard;
import org.json.JSONArray;

/**
 * The Buycraft plugin
 * 
 * Main plugin entry point
 * 
 * @author Lmc
 */
public class Buycraft extends JavaPlugin
{
	// -- Start GUI
	public GuiManager guiManager;
	// -- End GUI
	
	public String version = "3.4";
	public String apiUrl = "http://api.buycraft.net/query";
	
	public String configLocation = "plugins" + File.separator + "buycraft" + File.separator + "settings.yml";
	public String langLocation = "plugins" + File.separator + "buycraft" + File.separator + "language.yml";
	
	public long paymentsCheckerInterval;
	public JSONArray packagesForSale;
	public int serverId;
	public String serverSecret;

	public PaymentsChecker paymentsCheckerThread;
	
	public Settings settings;
	public Language lang;
	
	public Logger logger;
	public Api api = new Api(this);
	
	public HashMap<Player, Boolean> disabledChat = new HashMap<Player, Boolean>();

	/**
	 * Called when the plugin is disabled
	 * 
	 * @author Lmc
	 */
	public void onDisable()
	{
		try
		{
			// Send message
			this.logger.info(this.lang.getWithTag("pluginDisabled", this.version));
			
			// Stop the payments checker thread
			this.paymentsCheckerThread.toStop = true;
			this.paymentsCheckerThread = null;
			
			// Empty some variables
			this.packagesForSale = null;
			this.disabledChat = null;
			this.api = null;
			this.settings = null;
			this.lang = null;
			this.serverSecret = null;
			this.configLocation = null;
			this.logger = null;
		}
		catch(Exception e) {} 
	}

	/**
	 * Called when the plugin is enabled
	 * 
	 * @author Lmc
	 */
	public void onEnable() 
	{		
		try
		{
			// -- Start GUI
			SpoutManager.getKeyBindingManager().registerBinding("toggle_gui", Keyboard.KEY_B, "Toggles the GUI", new PackageBindingDelegate(this), this);
			guiManager = new GuiManager(new ScreenManager());
			// -- End GUI
			
			// Load logger
			this.logger = this.getServer().getLogger();

			// Check required files
			checkRequiredFiles();
			
			// Enable settings and language stuff
			this.settings = new Settings(configLocation);
			this.lang = new Language(langLocation);
			
			// Set the server secret
			this.serverSecret = this.settings.getString("secret");
			
			// Perform the API connect call
			ApiResponse apiCheck = this.api.connect();
			
			if(apiCheck == ApiResponse.CONNECT_OLD_VERSION)
			{
				this.logger.severe(this.lang.getWithTag("oldVersion"));
			}
			
			if(apiCheck == ApiResponse.CONNECT_INVALID_SECRET)
			{
				this.logger.severe(this.lang.getWithTag("invalidSecret"));
				
				disablePlugin();
				
				return;
			}
			
			if(apiCheck == ApiResponse.CONNECT_ERROR)
			{
				this.logger.severe(this.lang.getWithTag("authError"));
				
				disablePlugin();
				
				return;
			}
			
			PluginManager manager = getServer().getPluginManager();

			manager.registerEvent(Type.CUSTOM_EVENT, new ButtonListener(this), Priority.Normal, this);
			
			// Start up the payment checker
			this.paymentsCheckerThread = new PaymentsChecker(this);
			this.paymentsCheckerThread.start();
			
		    // Display enabled message
			this.logger.info(this.lang.getWithTag("pluginEnabled", this.version));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			this.disablePlugin();
		}
	}
	
	/**
	 * Disables the plugin
	 * 
	 * @author Lee
	 */
	public void disablePlugin()
	{
		PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.disablePlugin(this);
	}
	
	/**
	 * Creates the required files for the plugin to operate
	 * 
	 * @author Lee
	 */
	private void checkRequiredFiles()
	{	
		try
		{
			File directory = new File("plugins/buycraft");
			
			if(directory.exists() == false)
			{
				directory.mkdir();
			}
			
		    File settingsFile = new File(configLocation);

		    if(settingsFile.exists() == false || settingsFile.length() == 0) // Only create if dosent exist
		    {
			    InputStream inSettings = getClass().getClassLoader().getResourceAsStream("settings.yml");
			    FileWriter outSettings = new FileWriter(settingsFile);
			    
			    int c;
	
			    while ((c = inSettings.read()) != -1)
			    {
			    	outSettings.write(c);
			    }
			    
			    inSettings.close();
				outSettings.close();
		    }
			
		    File langFile = new File(langLocation);
		    
		    if(langFile.exists() == false || langFile.length() == 0) // Only create if dosent exist
		    {
			    InputStream inLang = getClass().getClassLoader().getResourceAsStream("language.yml");
			    FileWriter outLang = new FileWriter(langFile);
			    
			    int cLang;
	
			    while ((cLang = inLang.read()) != -1)
			    {
			    	outLang.write(cLang);
			    }
			    
			    inLang.close();
				outLang.close();
		    }
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
