package net.buycraft.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Language management
 * 
 * @author Lmc
 */
public class Language 
{
	private YamlConfiguration langFile;
	
	/**
	 * Class constructor
	 * 
	 * Loads a new configuration instance
	 * 
	 * @param 	fileName
	 * @author	Lmc
	 */
	public Language(String fileName)
	{
		File langFileLoc = new File(fileName);
		
		this.langFile = new YamlConfiguration();
		
		try
		{
			this.langFile.load(langFileLoc);
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
	 * Returns a string from the language file
	 * 
	 * @param variable
	 * @return
	 */
	public String get(String variable, Object ... args)
	{
		return String.format(this.langFile.getString(variable, ""), args);
	}
	
	/**
	 * Returns a lang with the [Buycraft] tag appended
	 * 
	 * @param variable
	 * @return
	 */
	public String getWithTag(String variable, Object ... args)
	{
		return "[Buycraft GUI] " + String.format(this.langFile.getString(variable, ""), args);
	}
}
