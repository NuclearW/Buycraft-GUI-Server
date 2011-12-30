package net.buycraft.gui;

import java.util.HashMap;

import org.getspout.spoutapi.player.SpoutPlayer;

public class ScreenManager {
	
	private HashMap<String, PackageTree> tree = new HashMap<String, PackageTree>();
	
	public PackageTree getPackageTree(SpoutPlayer splayer) {
		return tree.get(splayer.getName());
	}
	
	public void setPackageTree(SpoutPlayer splayer, PackageTree packageTree) {
		tree.put(splayer.getName(), packageTree);
	}
}