package fr.jielos.thetowers.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.components.ScoreboardSign;
import fr.jielos.thetowers.components.Title;
import fr.jielos.thetowers.game.GTeam;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;
import fr.jielos.thetowers.references.Teams;

public class PlayerMove implements Listener {

	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent event) {
		final Game game = Main.getInstance().getGame();
		
		if(game.getState() == State.PLAYING) {
			final Player player = event.getPlayer();
			final Scoreboard scoreboard = Main.getInstance().getServer().getScoreboardManager().getMainScoreboard();
			
			final Team team = scoreboard.getEntryTeam(player.getName());
			if(team == null) return; 
			
			final GTeam playerTeam = game.getData().getTeams().get(team);
			final GTeam adverseTeam = playerTeam.getAdverseTeam();
			
			if(adverseTeam.getPool().isIn(player)) {
				player.teleport(playerTeam.getSpawn());
				player.setHealth(20F); player.setFoodLevel(20);
				playerTeam.addPoints(1);
				
				if(playerTeam.getPoints() >= game.getConfig().getMaxPoints()) {
					game.getInstance().getServer().broadcastMessage(playerTeam.getContent().getChatColor() + player.getName()+" §6vient de marquer le point de la victoire et fait gagner son équipe !");
					game.end(playerTeam);
				} else {
					GTeam blueTeam = null, redTeam = null;
					if(scoreboard.getTeam(Teams.BLUE.getName()) != null) blueTeam = game.getData().getTeams().get(scoreboard.getTeam(Teams.BLUE.getName()));
					if(scoreboard.getTeam(Teams.RED.getName()) != null) redTeam = game.getData().getTeams().get(scoreboard.getTeam(Teams.RED.getName()));
					
					if(blueTeam != null && redTeam != null) {
						for(final ScoreboardSign scoreboardSign : game.getData().getBoards().values()) {
							scoreboardSign.setLine(2, Teams.BLUE.getChatColor() + Teams.BLUE.getDisplayName() + " §f" + blueTeam.getPoints() + " pts");
							scoreboardSign.setLine(3, Teams.RED.getChatColor() + Teams.RED.getDisplayName() + " §f" + redTeam.getPoints() + " pts");
						}
					}
					
					game.getInstance().getServer().broadcastMessage(playerTeam.getContent().getChatColor() + player.getName()+" §6vient de marquer un point pour son équipe !");
					new Title(playerTeam.getContent().getChatColor() + String.valueOf(playerTeam.getPoints()) + " point" + (playerTeam.getPoints() > 1 ? "s" : "") + " !", playerTeam.getContent().getChatColor() + player.getName() + " §6a marqué un point.").display(game.getData().getPlayers(), 1, 3, 1);;
				}
			}
		}
	}
	
}
