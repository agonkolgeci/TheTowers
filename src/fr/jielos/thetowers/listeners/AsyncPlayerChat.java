package fr.jielos.thetowers.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.GTeam;
import fr.jielos.thetowers.game.Game;

public class AsyncPlayerChat implements Listener {

	@EventHandler
	public void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
		final Game game = Main.getInstance().getGame();
		final Scoreboard scoreboard = game.getInstance().getServer().getScoreboardManager().getMainScoreboard();
		final Player player = event.getPlayer();
		
		final Team team = scoreboard.getEntryTeam(player.getName());
		if(team == null) {
			event.setFormat("§7%1$s: %2$s");
		} else {
			final GTeam playerTeam = game.getData().getTeams().get(team);
			if(!event.getMessage().startsWith("!")) {
				event.setCancelled(true);
				for(final Player gamePlayerTeam : playerTeam.getPlayers()) {
					gamePlayerTeam.sendMessage("§8[§7Équipe§8] " + playerTeam.getContent().getChatColor() + player.getName() + "§7: " + event.getMessage());
				}
			} else {
				event.setMessage(event.getMessage().substring(1));
				event.setFormat("§8[§7Global§8] " + playerTeam.getContent().getChatColor() + "%1$s§7: %2$s");
			}
		}
	}
	
}
