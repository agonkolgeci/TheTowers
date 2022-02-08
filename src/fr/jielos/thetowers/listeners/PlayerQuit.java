package fr.jielos.thetowers.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.components.ScoreboardSign;
import fr.jielos.thetowers.game.GTeam;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;

public class PlayerQuit implements Listener {

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event) { 
		final Game game = Main.getInstance().getGame();
		final Player player = event.getPlayer();
		
		event.setQuitMessage(null);
		
		if(game.getData().getPlayers().contains(player)) {
			if(game.getState() == State.WAITING || game.getState() == State.LAUNCHING) {
				game.getData().getPlayers().remove(player);
				
				if(game.getData().getBoards().containsKey(player)) {
					game.getData().getBoards().get(player).destroy();
					game.getData().getBoards().remove(player);
				}
				
				for(final ScoreboardSign scoreboardSign : game.getData().getBoards().values()) {
					scoreboardSign.setLine(2, "§fJoueurs §e" + game.getData().getPlayers().size());
				}
				
				game.getInstance().getServer().broadcastMessage("§7"+player.getName()+" §evient de quitté la partie ! §e(§c"+game.getData().getPlayers().size()+"§e/§c"+game.getConfig().getMaxPlayers()+"§e)");
			} else if(game.getState() == State.PLAYING) {
				final Scoreboard scoreboard = game.getInstance().getServer().getScoreboardManager().getMainScoreboard();
				final Team team = scoreboard.getEntryTeam(player.getName());
				
				if(team == null) return;
				else team.removeEntry(player.getName());

				final GTeam playerTeam = game.getData().getTeams().get(team);
				if(playerTeam != null) {
					game.getInstance().getServer().broadcastMessage(playerTeam.getContent().getChatColor() + player.getName() + " §7s'est déconnecté.");
					if(playerTeam.getPlayers().size() <= 0) game.end(playerTeam.getAdverseTeam());
				}
			}
		}
	}
	
}