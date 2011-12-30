package net.buycraft.gui;

import org.getspout.spoutapi.io.AddonPacket;
import org.getspout.spoutapi.io.SpoutInputStream;
import org.getspout.spoutapi.io.SpoutOutputStream;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PacketOpenBrowser extends AddonPacket {
	private String url;
	
	public PacketOpenBrowser(String url) {
		this.url = url;
	}

	@Override
	public void read(SpoutInputStream in) {
		// Do nothing with data
	}

	@Override
	public void run(SpoutPlayer player) {
		// When a user sends us a packet, we're going to do something about it.
		
		// TODO: Put them in a list with player.getName();
	}

	@Override
	public void write(SpoutOutputStream out) {
		if(this.url == null) {
			this.url = "null";
		}
		ouputStream.writeString(this.url);
	}
}