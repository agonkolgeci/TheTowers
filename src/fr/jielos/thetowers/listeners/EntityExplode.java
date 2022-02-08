package fr.jielos.thetowers.listeners;

import java.util.Iterator;

import fr.jielos.thetowers.game.GTeam;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.Game;

public class EntityExplode implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityExplode(final EntityExplodeEvent event) {
		final Game game = Main.getInstance().getGame();
		final EntityType entityType = event.getEntityType();
		
		if(entityType == EntityType.PRIMED_TNT) {
			event.blockList().removeIf(block -> block.getType() == Material.CHEST);
			event.blockList().removeIf(block -> {
				for(final GTeam gameTeam : game.getData().getTeams().values()) {
					if(gameTeam.getSpawnProtection().isIn(block.getLocation())) {
						return true;
					}
				}

				return false;
			});
		}
	}
	
}
