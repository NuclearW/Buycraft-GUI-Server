package net.buycraft.gui;

import net.buycraft.Buycraft;

import org.getspout.spoutapi.gui.Color;
import org.getspout.spoutapi.gui.GenericButton;
import org.getspout.spoutapi.gui.GenericGradient;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.player.SpoutPlayer;

public class PackagesPopup extends GenericPopup {
	
	public Buycraft buycraft;
	
	public int screenWidth;
	public int screenHeight;
	
	public PackagesPopup(Buycraft buycraft, SpoutPlayer player) {
		
		this.buycraft = buycraft;
		
		screenWidth = player.getMainScreen().getWidth();
		screenHeight = player.getMainScreen().getHeight();
		
		GenericLabel headerLabel = new GenericLabel();
		GenericButton checkout = new GenericButton();
		GenericButton closeWindow = new GenericButton();
		GenericTexture border = new GenericTexture();
		GenericGradient gradient = new GenericGradient();
		
		PackageTree tree = new PackageTree(buycraft);
		
		checkout.setText("Checkout");
		checkout.setY(195).setX(150);
		checkout.setHeight(20).setWidth(60);
		checkout.setTooltip("Continue to purchase");
		
		closeWindow.setText("Close");
		closeWindow.setY(195).setX(90);
		closeWindow.setHeight(20).setWidth(60);
		closeWindow.setTooltip("Close this interface");
		
		// Header label
		headerLabel.setText("Packages");
		headerLabel.setX((screenWidth / 2) - 25);
		headerLabel.setY(screenHeight / 29 - 25);
		
		// Main HUD
		border.setX(65).setY(20);
		border.setPriority(RenderPriority.High);
		border.setWidth(300).setHeight(200);
		border.setUrl("http://dl.dropbox.com/u/27507830/GuildCraft/Images/HUD/blue.png");
		
		gradient.setTopColor(new Color(0.25F, 0.25F, 0.25F, 1.0F));
		gradient.setBottomColor(new Color(0.35F, 0.35F, 0.35F, 1.0F));
		gradient.setWidth(300).setHeight(200);
		gradient.setX(65).setY(20);
		gradient.setPriority(RenderPriority.Highest);
		
		// List which displays the packages
		tree.setX(90).setY(50);
		tree.setWidth(250).setHeight(125);
		
		this.setTransparent(true);
		this.attachWidget(buycraft, border);
		this.attachWidget(buycraft, gradient);
		this.attachWidget(buycraft, tree);
		this.attachWidget(buycraft, headerLabel);
		this.attachWidget(buycraft, closeWindow);
		this.attachWidget(buycraft, checkout);
	}
}
