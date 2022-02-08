package fr.jielos.thetowers.game.schedulers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.jielos.thetowers.components.Cuboid;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;

public class Spawners extends BukkitRunnable {

	final Game game;
	
	Cuboid ironCuboid;
	Cuboid experienceCuboid;
	
	public Spawners(final Game game) {
		this.game = game;
		
		int radius = game.getConfig().getSpawnerRadius();
		this.ironCuboid = new Cuboid(game.getConfig().getSpawnerIron().clone().add(radius, radius, radius), game.getConfig().getSpawnerIron().clone().subtract(radius, radius, radius));
		this.experienceCuboid = new Cuboid(game.getConfig().getSpawnerExperience().clone().add(radius, radius, radius), game.getConfig().getSpawnerExperience().clone().subtract(radius, radius, radius));
	}
	
	@Override
	public void run() {
		if(game.getState() == State.PLAYING) {
			boolean hasPlayerSpawnerIron = false;
			boolean hasPlayerSpawnerExperience = false;
			for(final Player player : game.getData().getPlayers()) {
				if(ironCuboid.isIn(player)) hasPlayerSpawnerIron = true;
				if(experienceCuboid.isIn(player)) hasPlayerSpawnerExperience = true;
			}
			
			if(hasPlayerSpawnerIron) game.getMap().getWorld().dropItem(game.getConfig().getSpawnerIron(), new ItemStack(Material.IRON_INGOT, 1));
			if(hasPlayerSpawnerExperience) game.getMap().getWorld().dropItem(game.getConfig().getSpawnerExperience(), new ItemStack(Material.EXP_BOTTLE, 1));
		}
	}
	
}
