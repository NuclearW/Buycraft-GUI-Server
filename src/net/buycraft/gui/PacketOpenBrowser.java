package net.buycraft.gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.getspout.spoutapi.packet.PacketType;
import org.getspout.spoutapi.packet.SpoutPacket;

public class PacketOpenBrowser implements SpoutPacket {
	
	String url;
	int playerId;
	
	public PacketOpenBrowser(String url) {
		this.url = url;
	}

	public void failure(int id) {

	}

	public int getNumBytes() {
		return 0;
	}

	public PacketType getPacketType() {
		return null;
	}

	public int getVersion() {
		return 0;
	}

	public void readData(DataInputStream in) throws IOException {
		
	}

	public void run(int PlayerId) {
		
	}

	public void writeData(DataOutputStream out) throws IOException {
		
	}

}
