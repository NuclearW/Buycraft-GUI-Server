package net.buycraft.gui;

import org.getspout.spoutapi.io.AddonPacket;
import org.getspout.spoutapi.io.SpoutInputStream;
import org.getspout.spoutapi.io.SpoutOutputStream;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketOpenBrowser extends AddonPacket {
	
	public String url;
	
	public PacketOpenBrowser(String url) {
		this.url = url;
	}

	public void read(SpoutInputStream in) {
		
	}

	public void run(SpoutPlayer player) {

	}

	public void write(SpoutOutputStream out) {

	}
}