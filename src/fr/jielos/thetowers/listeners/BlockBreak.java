package fr.jielos.thetowers.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.GTeam;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;

public class BlockBreak implements Listener {

	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event) {
		final Game game = Main.getInstance().getGame();
		
		if(game.getState() != State.PLAYING && !game.getConfig().isWaitingRoomBuild() && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		} else if(game.getState() == State.PLAYING && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			final Player player = event.getPlayer();
			final Scoreboard scoreboard = game.getInstance().getServer().getScoreboardManager().getMainScoreboard();
			
			final Team team = scoreboard.getEntryTeam(player.getName());
			if(team == null) return; 
			
			if(event.getBlock().getType() == Material.CHEST) {
				final GTeam playerTeam = game.getData().getTeams().get(team);
				final GTeam adverseTeam = playerTeam.getAdverseTeam();
				if(adverseTeam.getRegion().isIn(event.getBlock().getLocation())) {
					event.setCancelled(true);
				}
			}
			
			for(final GTeam gameTeam : game.getData().getTeams().values()) {
				if(gameTeam.getSpawnProtection().isIn(event.getBlock().getLocation())) {
					event.setCancelled(true);
					break;
				}
			}
		}
	}
	
}
