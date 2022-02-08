package fr.jielos.thetowers.listeners;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDrop implements Listener {

	@EventHandler
	public void onPlayerInteract(final PlayerDropItemEvent event) {
		final Game game = Main.getInstance().getGame();

		if(game.getState() == State.WAITING || game.getState() == State.LAUNCHING) {
			event.setCancelled(true);
		}
	}
	
}