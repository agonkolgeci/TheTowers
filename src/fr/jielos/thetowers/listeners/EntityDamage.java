package fr.jielos.thetowers.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;

public class EntityDamage implements Listener {

	@EventHandler
	public void onEntityDamage(final EntityDamageEvent event) {
		final Game game = Main.getInstance().getGame();
		
		if(event.getEntity() instanceof Player) {
			if(game.getState() != State.PLAYING && !game.getConfig().isWaitingRoomDamages()) {
				event.setCancelled(true);
			}
		}
	}
	
}
