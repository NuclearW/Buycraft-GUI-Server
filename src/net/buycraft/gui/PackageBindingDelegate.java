package net.buycraft.gui;

import net.buycraft.Buycraft;

import org.getspout.spoutapi.event.input.KeyBindingEvent;
import org.getspout.spoutapi.keyboard.BindingExecutionDelegate;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PackageBindingDelegate implements BindingExecutionDelegate {
	
	public Buycraft buycraft;
	
	public PackageBindingDelegate(Buycraft buycraft) {
		this.buycraft = buycraft;
	}
	
	public void keyPressed(KeyBindingEvent event) {
		SpoutPlayer splayer = event.getPlayer();
		if(splayer.getMainScreen().getActivePopup() instanceof PackagesPopup) {
			splayer.getMainScreen().getActivePopup().close();
		} else {
			splayer.getMainScreen().attachPopupScreen(new PackagesPopup(buycraft, splayer));
		}
	}

	public void keyReleased(KeyBindingEvent event) {
		// Do nothing
	}
}