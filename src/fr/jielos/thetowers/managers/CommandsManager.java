package fr.jielos.thetowers.managers;

import org.bukkit.plugin.java.JavaPlugin;

import fr.jielos.thetowers.commands.CommandeForcestart;

public class CommandsManager {

	final JavaPlugin instance;
	public CommandsManager(final JavaPlugin instance) {
		this.instance = instance;
	}
	
	public void register() {
		instance.getCommand("forcestart").setExecutor(new CommandeForcestart());
	}
	
}
