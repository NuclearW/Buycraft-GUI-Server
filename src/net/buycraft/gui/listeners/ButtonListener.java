package net.buycraft.gui.listeners;

import net.buycraft.Buycraft;
import net.buycraft.gui.GuiManager;
import net.buycraft.gui.PackageTree;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.event.screen.ScreenListener;
import org.getspout.spoutapi.gui.Button;
import org.getspout.spoutapi.player.SpoutPlayer;

public class ButtonListener extends ScreenListener {
	public Buycraft buycraft;
	
	public ButtonListener(Buycraft instance) {
		buycraft = instance;
	}
	
	public void onButtonClick(ButtonClickEvent event) {
		Button button = event.getButton();
		SpoutPlayer player = (SpoutPlayer) event.getPlayer();
		
		if(button.getText().equals("Close")) {
			player.getMainScreen().getActivePopup().close();
		}
		
		if(button.getText().equals("Checkout")) {
			PackageTree tree = GuiManager.getScreenManager().getPackageTree(player);
			String url = tree.getPackageUrl(tree.getSelectedPackageIndex());
			System.out.print(url);
			//player.sendPacket(new PacketOpenBrowser(url));
		}
		
		/*
		 * TODO: This code goes in client portion
		 */
		/*if (Desktop.isDesktopSupported()) 
		{
	        Desktop desktop = Desktop.getDesktop();
	        
	        if (desktop.isSupported(Desktop.Action.BROWSE)) 
	        {
	            try 
	            {
	                desktop.browse(new URI("http://google.com"));
	            }
	            catch(IOException ioe) 
	            {
	                ioe.printStackTrace();
	            }
	            catch(URISyntaxException use) 
	            {
	                use.printStackTrace();
	            }
	        }
	    }*/
	}
}
