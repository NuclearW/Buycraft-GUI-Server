package net.buycraft.gui;

public class GuiManager {
	
	private static ScreenManager screenManager;
	
	public GuiManager(ScreenManager screenManager) {
		GuiManager.screenManager = screenManager;
	}
	
	public static ScreenManager getScreenManager() {
		return screenManager;
	}
}