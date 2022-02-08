package fr.jielos.thetowers.game;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.jielos.thetowers.components.Cuboid;
import fr.jielos.thetowers.references.Teams;

public class GTeam {

	final Game game;
	final Team team;
	final Teams content;
	
	Location spawn;
	Cuboid pool;
	Cuboid region;
	Cuboid spawnProtection;
	int points;
	
	public GTeam(final Game game, final Team team, final Teams content) {
		this.game = game;
		this.team = team;
		this.content = content;
		
		final FileConfiguration config = game.getInstance().getConfig();
		this.spawn = new Location(game.getMap().getWorld(), config.getDouble("Locations.Teams."+content.getName()+".Spawn.x"), config.getDouble("Locations.Teams."+content.getName()+".Spawn.y"), config.getDouble("Locations.Teams."+content.getName()+".Spawn.z"), (float) config.getDouble("Locations.Teams."+content.getName()+".Spawn.yaw"), (float) config.getDouble("Locations.Teams."+content.getName()+".Spawn.pitch"));
		this.pool = new Cuboid(new Location(game.getMap().getWorld(), config.getDouble("Locations.Teams."+content.getName()+".Pool.1.x"), config.getDouble("Locations.Teams."+content.getName()+".Pool.1.y"), config.getDouble("Locations.Teams."+content.getName()+".Pool.1.z")), new Location(game.getMap().getWorld(), config.getDouble("Locations.Teams."+content.getName()+".Pool.2.x"), config.getDouble("Locations.Teams."+content.getName()+".Pool.2.y"), config.getDouble("Locations.Teams."+content.getName()+".Pool.2.z")));
		this.region = new Cuboid(new Location(game.getMap().getWorld(), config.getDouble("Locations.Teams."+content.getName()+".Region.1.x"), config.getDouble("Locations.Teams."+content.getName()+".Region.1.y"), config.getDouble("Locations.Teams."+content.getName()+".Region.1.z")), new Location(game.getMap().getWorld(), config.getDouble("Locations.Teams."+content.getName()+".Region.2.x"), config.getDouble("Locations.Teams."+content.getName()+".Region.2.y"), config.getDouble("Locations.Teams."+content.getName()+".Region.2.z")));
		this.spawnProtection = new Cuboid(spawn.clone().add(game.getConfig().getSpawnProtectionXRadius(), game.getConfig().getSpawnProtectionYRadius(), game.getConfig().getSpawnProtectionZRadius()), spawn.clone().subtract(game.getConfig().getSpawnProtectionXRadius(), game.getConfig().getSpawnProtectionYRadius(), game.getConfig().getSpawnProtectionZRadius()));
	}
	
	public Team getTeam() {
		return team;
	}
	
	public Teams getContent() {
		return content;
	}
	
	public Location getSpawn() {
		return spawn;
	}

	public Cuboid getPool() {
		return pool;
	}
	
	public Cuboid getRegion() {
		return region;
	}
	
	public Cuboid getSpawnProtection() {
		return spawnProtection;
	}
	
	public void addPoints(int value) {
		this.points += value;
	}
	
	public int getPoints() {
		return points;
	}
	
	public List<Player> getPlayers() {
		final Scoreboard scoreboard = game.getInstance().getServer().getScoreboardManager().getMainScoreboard();
		return game.getData().getPlayers().stream().filter(e -> scoreboard.getEntryTeam(e.getName()) != null && scoreboard.getEntryTeam(e.getName()).getName().equals(content.getName())).collect(Collectors.toList());
	}
	
	public GTeam getAdverseTeam() {
		return game.getData().getTeams().entrySet().stream().filter(e -> !e.getKey().getName().equals(content.getName())).map(e -> e.getValue()).collect(Collectors.toList()).get(0);
	}

}
