package fr.jielos.thetowers.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.components.Cuboid;
import fr.jielos.thetowers.game.GTeam;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;

public class BlockPlace implements Listener {

	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent event) {
		final Game game = Main.getInstance().getGame();
		
		if(game.getState() != State.PLAYING && !game.getConfig().isWaitingRoomBuild() && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		} else if(game.getState() == State.PLAYING && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			final Cuboid map = new Cuboid(game.getConfig().getBorderCenter().clone().add(game.getConfig().getBorderXRadius(), 255, game.getConfig().getBorderZRadius()), game.getConfig().getBorderCenter().clone().subtract(game.getConfig().getBorderXRadius(), 0, game.getConfig().getBorderZRadius()));
			if(!map.isIn(event.getBlock().getLocation())) {
				event.setCancelled(true);
			} else {
				final Player player = event.getPlayer();
				final Scoreboard scoreboard = game.getInstance().getServer().getScoreboardManager().getMainScoreboard();
				
				final Team team = scoreboard.getEntryTeam(player.getName());
				if(team == null) return; 
				
				for(final GTeam gameTeam : game.getData().getTeams().values()) {
					if(gameTeam.getSpawnProtection().isIn(event.getBlock().getLocation())) {
						event.setCancelled(true);
						break;
					}
				}
			}
		}
	}
	
}