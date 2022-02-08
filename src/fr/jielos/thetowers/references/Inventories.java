package fr.jielos.thetowers.references;

import org.bukkit.Bukkit;

import org.bukkit.inventory.Inventory;

public enum Inventories {

	TEAMS("§8Équipes", 9);
	
	final Inventory inventory;
	Inventories(final String name, final int size) {
		this.inventory = Bukkit.createInventory(null, size, name);
	}

	public Inventory getInventory() {
		return inventory;
	}
	
}
