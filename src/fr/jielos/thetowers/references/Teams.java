package fr.jielos.thetowers.references;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public enum Teams {

	BLUE("Blue", "Bleue", ChatColor.BLUE, DyeColor.BLUE),
	RED("Red", "Rouge", ChatColor.RED, DyeColor.RED);

	final String name;
	final String displayName;
	final ChatColor chatColor;
	final DyeColor dyeColor;
	
	Teams(final String name, final String displayName, final ChatColor chatColor, final DyeColor dyeColor) {
		this.name = name;
		this.displayName = displayName;
		this.chatColor = chatColor;
		this.dyeColor = dyeColor;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public ChatColor getChatColor() {
		return chatColor;
	}
	
	public DyeColor getDyeColor() {
		return dyeColor;
	}
	
}