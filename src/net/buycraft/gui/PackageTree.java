package net.buycraft.gui;

import net.buycraft.Buycraft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.gui.GenericContainer;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericListWidget;
import org.getspout.spoutapi.gui.ListWidgetItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PackageTree extends GenericContainer {
	private Buycraft buycraft;
	private Player player;
	private GenericListWidget list;
	
	public PackageTree(Buycraft instance, Player player) {
		buycraft = instance;
		this.player = player;

		list = new GenericListWidget();
		
		try {
			
			// Get packages for sale
			JSONArray packages = buycraft.packagesForSale;

			// If no packages exist, send prompt
			if(packages.length() == 0) {
				
				GenericLabel none = new GenericLabel(ChatColor.RED + "No packages are being sold");
				none.setWidth(-1).setHeight(-1);
				none.setX(100).setY(100);
				this.addChild(none);
				
			} else {
				
				int startingPoint = 0;
				int finishPoint = packages.length();
				
				// Add an item on the list for each package
				for(int i = startingPoint; i < finishPoint; i++)  {
					
					if(!packages.isNull(i)) {
						JSONObject row = packages.getJSONObject(i);
						list.addItem(new ListWidgetItem(ChatColor.DARK_GREEN + row.get("description").toString(), 
								row.get("price").toString() + " " + row.get("currency")));
					}
				}
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();	
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setWidth(0).setHeight(0);
		this.addChild(list);
	}
	
	public String getPackageUrl(int index) {
		try {
			JSONArray packages = buycraft.packagesForSale;
			JSONObject row = packages.getJSONObject(index);
			String url = "http://buycraft.net/buy/" + this.buycraft.serverId + "/" + row.get("id") + "/" + this.player.getName();
			return url;
		} catch (JSONException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int getSelectedPackageIndex() {
		return list.getSelectedRow();
	}
}