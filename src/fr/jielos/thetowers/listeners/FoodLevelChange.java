package fr.jielos.thetowers.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;

public class FoodLevelChange implements Listener {

	@EventHandler
	public void onFoodLevelChange(final FoodLevelChangeEvent event) {
		final Game game = Main.getInstance().getGame();
		
		if(game.getState() != State.PLAYING) {
			event.setCancelled(true);
		}
	}
	
}
