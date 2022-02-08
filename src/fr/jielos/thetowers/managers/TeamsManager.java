package fr.jielos.thetowers.managers;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.GTeam;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.references.Teams;

public class TeamsManager {

	final JavaPlugin instance;
	public TeamsManager(final JavaPlugin instance) {
		this.instance = instance;
	}
	
	public void register() {
		final Game game = Main.getInstance().getGame();
		final Scoreboard scoreboard = instance.getServer().getScoreboardManager().getMainScoreboard();
		
		for(final Team team : scoreboard.getTeams()) team.unregister();
		for(final Teams team : Teams.values()) {
			if(scoreboard.getTeam(team.getName()) == null) {
				final Team scoreboardTeam = scoreboard.registerNewTeam(team.getName());
				scoreboardTeam.setDisplayName(team.getDisplayName());
				scoreboardTeam.setPrefix(team.getChatColor().toString());
				scoreboardTeam.setAllowFriendlyFire(false);
				
				game.getData().getTeams().put(scoreboardTeam, new GTeam(game, scoreboardTeam, team));
			}
		}
	}
	
}
