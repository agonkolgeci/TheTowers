package fr.jielos.thetowers.managers;

import fr.jielos.thetowers.listeners.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenersManager {

	final JavaPlugin instance;
	public ListenersManager(final JavaPlugin instance) {
		this.instance = instance;
	}
	
	public void register() {
		final PluginManager pluginManager = instance.getServer().getPluginManager();
		
		pluginManager.registerEvents(new PlayerLogin(), instance);
		pluginManager.registerEvents(new PlayerJoin(), instance);
		pluginManager.registerEvents(new PlayerQuit(), instance);
		pluginManager.registerEvents(new PlayerMove(), instance);
		pluginManager.registerEvents(new PlayerDeath(), instance);
		pluginManager.registerEvents(new PlayerDrop(), instance);
		pluginManager.registerEvents(new PlayerRespawn(), instance);
		pluginManager.registerEvents(new PlayerInteract(), instance);
		pluginManager.registerEvents(new PlayerArmorStandManipulate(), instance);
		pluginManager.registerEvents(new AsyncPlayerChat(), instance);
		
		pluginManager.registerEvents(new BlockPlace(), instance);
		pluginManager.registerEvents(new BlockBreak(), instance);
		pluginManager.registerEvents(new EntityDamage(), instance);
		pluginManager.registerEvents(new EntityDamageByEntity(), instance);
		pluginManager.registerEvents(new EntityExplode(), instance);
		
		pluginManager.registerEvents(new FoodLevelChange(), instance);
		pluginManager.registerEvents(new WeatherChange(), instance);
		pluginManager.registerEvents(new CreatureSpawn(), instance);
		pluginManager.registerEvents(new InventoryClick(), instance);
		pluginManager.registerEvents(new InventoryOpen(), instance);
	}
	
}