package fr.jielos.thetowers.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import fr.jielos.thetowers.components.ScoreboardSign;

public class Data {

	final Game game;
	
	List<Player> players;
	List<Player> playersRespawn;

	java.util.Map<Team, GTeam> teams;
	java.util.Map<Player, ScoreboardSign> boards;
	
	public Data(final Game game) {
		this.game = game;
		
		this.players = new ArrayList<>();
		this.playersRespawn = new ArrayList<>();

		this.teams = new HashMap<>();
		this.boards = new HashMap<>();
	}
	
	public List<Player> getPlayers() {
		return players;
	}

	public List<Player> getPlayersRespawn() {
		return playersRespawn;
	}

	public java.util.Map<Team, GTeam> getTeams() {
		return teams;
	}

	public java.util.Map<Player, ScoreboardSign> getBoards() {
		return boards;
	}
	
}
