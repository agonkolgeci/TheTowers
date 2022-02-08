package fr.jielos.thetowers.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

	final Game game;
	
	boolean dayLightCycle;
	boolean armorsDrop;
	boolean canOpenAdverseChests;
	boolean naturalRegeneration;
	int maxTime;
	
	int maxPlayersPerTeams;
	int maxPoints;
	boolean autoRun;
	int spawnProtectionXRadius;
	int spawnProtectionYRadius;
	int spawnProtectionZRadius;
	
	int launchTimer;
	int endTimer;
	
	boolean bungeeCord;
	String bungeeCordServer;
	
	String waitingRoomWorld;
	boolean waitingRoomDamages;
	boolean waitingRoomBuild;
	
	String joinTitle;
	String joinSubTitle;
	
	boolean scoreboard;
	String scoreboardTitle;
	String scoreboardFooter;
	
	int menuTeamsItem;
	int menuTeamsItemPosition;
	String menuTeamsItemName;

	boolean menuQuit;
	int menuQuitItem;
	int menuQuitItemPosition;
	String menuQuitItemName;
	
	Location waitingRoom;
	
	int borderXRadius;
	int borderZRadius;
	Location borderCenter;
	
	int spawnerInterval;
	int spawnerRadius;
	Location spawnerIron;
	Location spawnerExperience;
	
	public Config(final Game game) {
		this.game = game;
		
		final FileConfiguration config = game.getInstance().getConfig();
		
		this.dayLightCycle = config.getBoolean("Game.DaylightCycle");
		this.armorsDrop = config.getBoolean("Game.ArmorsDrop");
		this.canOpenAdverseChests = config.getBoolean("Game.CanOpenAdverseChests");
		this.naturalRegeneration = config.getBoolean("Game.NaturalRegeneration");
		this.maxTime = config.getInt("Game.MaxTime");
		
		this.maxPlayersPerTeams = config.getInt("Teams.MaxPlayersPerTeams");
		this.maxPoints = config.getInt("Teams.MaxPoints");
		this.autoRun = config.getBoolean("Teams.AutoRun");
		this.spawnProtectionXRadius = config.getInt("Teams.SpawnProtection.xRadius");
		this.spawnProtectionYRadius = config.getInt("Teams.SpawnProtection.yRadius");
		this.spawnProtectionZRadius = config.getInt("Teams.SpawnProtection.zRadius");
		
		this.launchTimer = config.getInt("Timers.Launch");
		this.endTimer = config.getInt("Timers.End");
		
		this.bungeeCord = config.getBoolean("BungeeCord.Support");
		this.bungeeCordServer = config.getString("BungeeCord.Server");
		
		this.waitingRoomWorld = config.getString("WaitingRoom.World");
		this.waitingRoomDamages = config.getBoolean("WaitingRoom.Damages");
		this.waitingRoomBuild = config.getBoolean("WaitingRoom.Build");
		
		this.joinTitle = config.getString("JoinTitle.Title");
		this.joinSubTitle = config.getString("JoinTitle.SubTitle");
		
		this.scoreboard = config.getBoolean("Scoreboard.Enabled");
		this.scoreboardTitle = config.getString("Scoreboard.Title");
		this.scoreboardFooter = config.getString("Scoreboard.Footer");

		this.menuTeamsItem = config.getInt("Menu.Teams.Item");
		this.menuTeamsItemPosition = config.getInt("Menu.Teams.Position");
		this.menuTeamsItemName = config.getString("Menu.Teams.Name");

		this.menuQuit = config.getBoolean("Menu.Quit.Enabled");
		this.menuQuitItem = config.getInt("Menu.Quit.Item");
		this.menuQuitItemPosition = config.getInt("Menu.Quit.Position");
		this.menuQuitItemName = config.getString("Menu.Quit.Name");

		if(Bukkit.getWorld(waitingRoomWorld) == null) {
			this.waitingRoomWorld = Bukkit.getWorlds().get(0).getName();
			
			game.getInstance().getConfig().set("WaitingRoom.World", waitingRoomWorld);
			game.getInstance().saveConfig();
		}
		
		this.waitingRoom = new Location(Bukkit.getWorld(waitingRoomWorld), config.getDouble("Locations.WaitingRoom.x"), config.getDouble("Locations.WaitingRoom.y"), config.getDouble("Locations.WaitingRoom.z"), (float) config.getDouble("Locations.WaitingRoom.yaw"), (float) config.getDouble("Locations.WaitingRoom.pitch"));
		
		this.borderXRadius = config.getInt("Border.xRadius");
		this.borderZRadius = config.getInt("Border.zRadius");
		this.borderCenter = new Location(game.getMap().getWorld(), config.getDouble("Border.Center.x"), 0, config.getDouble("Border.Center.z"));
		
		this.spawnerInterval = config.getInt("Spawners.Interval");
		this.spawnerRadius = config.getInt("Spawners.Radius");
		this.spawnerIron = new Location(game.getMap().getWorld(), config.getDouble("Spawners.Iron.x"), config.getDouble("Spawners.Iron.y"), config.getDouble("Spawners.Iron.z"));
		this.spawnerExperience = new Location(game.getMap().getWorld(), config.getDouble("Spawners.Experience.x"), config.getDouble("Spawners.Experience.y"), config.getDouble("Spawners.Experience.z"));
	}

	public boolean isDayLightCycle() {
		return dayLightCycle;
	}
	
	public boolean isArmorsDrop() {
		return armorsDrop;
	}
	
	public boolean isCanOpenAdverseChests() {
		return canOpenAdverseChests;
	}
	
	public boolean isNaturalRegeneration() {
		return naturalRegeneration;
	}
	
	public int getMaxTime() {
		return maxTime;
	}
	
	public int getMaxPlayersPerTeams() {
		return maxPlayersPerTeams;
	}
	
	public int getMaxPoints() {
		return maxPoints;
	}
	
	public int getMaxPlayers() {
		return maxPlayersPerTeams*2;
	}
	
	public boolean isAutoRun() {
		return autoRun;
	}
	
	public int getSpawnProtectionXRadius() {
		return spawnProtectionXRadius;
	}
	
	public int getSpawnProtectionYRadius() {
		return spawnProtectionYRadius;
	}
	
	public int getSpawnProtectionZRadius() {
		return spawnProtectionZRadius;
	}
	
	public int getLaunchTimer() {
		return launchTimer;
	}
	
	public int getEndTimer() {
		return endTimer;
	}
	
	public String getWaitingRoomWorld() {
		return waitingRoomWorld;
	}
	
	public boolean isWaitingRoomDamages() {
		return waitingRoomDamages;
	}
	
	public boolean isWaitingRoomBuild() {
		return waitingRoomBuild;
	}
	
	
	public boolean isBungeeCord() {
		return bungeeCord;
	}
	
	public String getBungeeCordServer() {
		return bungeeCordServer;
	}
	
	public String getJoinTitle() {
		return joinTitle;
	}
	
	public String getJoinSubTitle() {
		return joinSubTitle;
	}
	
	public boolean isScoreboard() {
		return scoreboard;
	}
	
	public String getScoreboardTitle() {
		return scoreboardTitle;
	}

	public String getScoreboardFooter() {
		return scoreboardFooter;
	}

	public int getMenuTeamsItem() {
		return menuTeamsItem;
	}
	
	public int getMenuTeamsItemPosition() {
		return menuTeamsItemPosition;
	}
	
	public String getMenuTeamsItemName() {
		return ChatColor.translateAlternateColorCodes('&', menuTeamsItemName);
	}

	public boolean isMenuQuit() {
		return menuQuit;
	}

	public int getMenuQuitItem() {
		return menuQuitItem;
	}
	
	public int getMenuQuitItemPosition() {
		return menuQuitItemPosition;
	}
	
	public String getMenuQuitItemName() {
		return ChatColor.translateAlternateColorCodes('&', menuQuitItemName);
	}
	
	public Location getWaitingRoom() {
		return waitingRoom;
	}
	
	public int getBorderXRadius() {
		return borderXRadius;
	}
	
	public int getBorderZRadius() {
		return borderZRadius;
	}
	
	public Location getBorderCenter() {
		return borderCenter;
	}
	
	public int getSpawnerInterval() {
		return spawnerInterval;
	}
	
	public int getSpawnerRadius() {
		return spawnerRadius;
	}
	
	public Location getSpawnerIron() {
		return spawnerIron;
	}
	
	public Location getSpawnerExperience() {
		return spawnerExperience;
	}

}
