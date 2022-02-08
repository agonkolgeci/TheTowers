package fr.jielos.thetowers.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;

public class PlayerLogin implements Listener {

	@EventHandler
	public void onPlayerLogin(final PlayerLoginEvent event) {
		final Game game = Main.getInstance().getGame();
		
		if(game.getState() == State.PLAYING || game.getState() == State.END) {
			event.disallow(Result.KICK_OTHER, "§cImpossible de rejoindre cette partie, celle-ci est en jeu.");
		} else if(game.getState() == State.WAITING || game.getState() == State.LAUNCHING) {
			if(game.getData().getPlayers().size() >= game.getConfig().getMaxPlayers()) {
				event.disallow(Result.KICK_OTHER, "§cImpossible de rejoindre cette partie, celle-ci est pleine.");
			}
		}
	}
	
}
