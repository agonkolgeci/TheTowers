package fr.jielos.thetowers;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.managers.CommandsManager;
import fr.jielos.thetowers.managers.ListenersManager;
import fr.jielos.thetowers.managers.TeamsManager;

public class Main extends JavaPlugin {

	static Main instance;
	public static Main getInstance() {
		return instance;
	}
	
	Game game;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		
		instance = this;
		game = new Game(this);
		
		for(final World world : getServer().getWorlds()) {
			world.setGameRuleValue("doDaylightCycle", String.valueOf(game.getConfig().isDayLightCycle()));
			world.setTime(6000);
			world.setDifficulty(Difficulty.NORMAL);
		}
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		new TeamsManager(this).register();
		new ListenersManager(this).register();
		new CommandsManager(this).register();
	}
	
	@Override
	public void onDisable() {
		getServer().unloadWorld(game.getMap().getWorld(), false);

		if(!game.getConfig().isBungeeCord()) {
			for (final Player player : getServer().getOnlinePlayers()) {
				player.teleport(game.getConfig().getWaitingRoom());
				player.kickPlayer("§cRedémarrage du programme permettant le fonctionnement du jeu, veuillez vous reconnectez.");
			}
		}
	}
	
	public Game getGame() {
		return game;
	}
	
}