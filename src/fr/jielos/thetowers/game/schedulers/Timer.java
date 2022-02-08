package fr.jielos.thetowers.game.schedulers;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.scheduler.BukkitRunnable;

import fr.jielos.thetowers.components.ScoreboardSign;
import fr.jielos.thetowers.game.GTeam;
import fr.jielos.thetowers.game.Game;

public class Timer extends BukkitRunnable {

	final Game game;
	
	int elapsed;
	int remaining;
	
	public Timer(final Game game) {
		this.game = game;
		this.elapsed = 0;
		this.remaining = (game.getConfig().getMaxTime()*60);
	}
	
	@Override
	public void run() {
		if(remaining <= 0) {
			final List<GTeam> teams = game.getData().getTeams().entrySet().stream().sorted((k1, k2) -> Integer.valueOf(k1.getValue().getPoints()).compareTo(k2.getValue().getPoints())).map(e -> e.getValue()).collect(Collectors.toList());
			if(teams.get(0).getPoints() == teams.get(1).getPoints()) {
				game.end(null);
			} else game.end(teams.get(0));
			
			return;
		}
		
		for(final ScoreboardSign scoreboardSign : game.getData().getBoards().values()) {
			scoreboardSign.setLine(5, "§7Temps §a"+format(elapsed));
			scoreboardSign.setLine(6, "§7Fin du jeu §b"+format(remaining));
		}
		
		elapsed++;
		remaining--;
	}
	
	public String format(final int remainder) {
		int hours = remainder / 3600;
		int minutes = remainder / 60;
		int seconds = remainder - (minutes * 60);
		
		return ((hours > 0 ? hours+"h" : "") + (minutes > 0 ? minutes+"m" : "") + seconds+"s");
	}
	
}
