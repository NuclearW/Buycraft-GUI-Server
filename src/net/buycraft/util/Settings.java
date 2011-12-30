package net.buycraft.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Configuration file management
 * 
 * @author Lmc
 */
public class Settings 
{
	private YamlConfiguration configFile;
	
	/**
	 * Class constructor
	 * 
	 * Loads a new configuration instance
	 * 
	 * @param 	fileName
	 * @author	Lmc
	 */
	public Settings(String fileName)
	{
		// Load the desired configuration file
		File pluginSettings = new File(fileName);
		
		this.configFile = new YamlConfiguration();
		
		try
		{
			this.configFile.load(pluginSettings);
		} 
		catch (FileNotFoundException e)
		{
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		} 
		catch (InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a setting from the configuration file as a String
	 * 
	 * @param variable
	 * @return
	 */
	public String getString(String variable)
	{
		return this.configFile.getString(variable, "");
	}
	
	/**
	 * Returns a setting from the configuration file as an Integer
	 * 
	 * @param variable
	 * @return
	 */
	public Integer getInteger(String variable)
	{
		return this.configFile.getInt(variable, 0);
	}
	
	/**
	 * Returns a setting from the configuration file as a Boolean
	 * 
	 * @param variable
	 * @return
	 */
	public boolean getBoolean(String variable)
	{
		return this.configFile.getBoolean(variable, true);
	}

	/**
	 * Returns a setting from the configuration file as a Double
	 * 
	 * @param variable
	 * @return
	 */
	public double getDouble(String variable)
	{
		return this.configFile.getDouble(variable, 1.00);
	}
	
	/**
	 * Returns a setting from the configuration file as a Float
	 * 
	 * @param variable
	 * @return
	 */
	public float getFloat(String variable)
	{
		return Float.valueOf(this.configFile.getString(variable, "0.00"));
	}
}
