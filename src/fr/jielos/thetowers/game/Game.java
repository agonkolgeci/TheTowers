package fr.jielos.thetowers.game;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.jielos.thetowers.components.ItemBuilder;
import fr.jielos.thetowers.components.ScoreboardSign;
import fr.jielos.thetowers.components.Title;
import fr.jielos.thetowers.game.schedulers.Ending;
import fr.jielos.thetowers.game.schedulers.Launching;
import fr.jielos.thetowers.game.schedulers.Spawners;
import fr.jielos.thetowers.game.schedulers.Timer;
import fr.jielos.thetowers.references.Teams;

public class Game {

	final JavaPlugin instance;
	
	State state;
	
	Map map;
	Data data;
	Config config;
	
	Spawners spawners;
	Launching launching;
	Timer timer;
	
	public Game(final JavaPlugin instance) {
		this.instance = instance;
		
		this.state = State.WAITING;
		
		this.map = new Map(this);
		this.data = new Data(this);
		this.config = new Config(this);
	}
	
	public void launch() {
		this.state = State.LAUNCHING;
		
		this.launching = new Launching(this);
		this.launching.runTaskTimer(instance, 0, 20);
	}
	
	public void start() {
		this.state = State.PLAYING;
		this.instance.getServer().broadcastMessage("§eDémarrage de la partie, bonne chance à tous !");

		final Scoreboard scoreboard = instance.getServer().getScoreboardManager().getMainScoreboard();
		for(final Player player : data.getPlayers()) {
			Team team = scoreboard.getEntryTeam(player.getName());

			boolean assignTeam = false;
			if(team == null) {
				assignTeam = true;
			} else {
				if(scoreboard.getEntryTeam(player.getName()).getEntries().size() >= data.getPlayers().size()) {
					assignTeam = true;
				}
			}

			if(assignTeam) {
				final List<Team> availableTeams = scoreboard.getTeams().stream().filter(e -> e.getEntries().size() < (data.getPlayers().size()-1) && e.getEntries().size() < config.getMaxPlayersPerTeams()).collect(Collectors.toList());

				team = availableTeams.get(new Random().nextInt(availableTeams.size()));
				team.addEntry(player.getName());
			}
			
			final GTeam playerTeam = data.getTeams().get(team);
			
			player.teleport(playerTeam.getSpawn());
			player.setGameMode(GameMode.SURVIVAL);
			player.setHealth(20); player.setFoodLevel(20);
			player.setLevel(0); player.setExp(0);
			player.getInventory().clear(); 
			player.playSound(player.getLocation(), Sound.CAT_MEOW, 2F, 1F);
			player.getInventory().addItem(new ItemStack(Material.BAKED_POTATO, 8));
			
			player.getInventory().setArmorContents(new ItemStack[]{
				new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS)).setLeatherArmorColor(playerTeam.getContent().getDyeColor().getColor()).toItemStack(),
				new ItemBuilder(new ItemStack(Material.LEATHER_LEGGINGS)).setLeatherArmorColor(playerTeam.getContent().getDyeColor().getColor()).toItemStack(),
				new ItemBuilder(new ItemStack(Material.LEATHER_CHESTPLATE)).setLeatherArmorColor(playerTeam.getContent().getDyeColor().getColor()).toItemStack(),
				new ItemBuilder(new ItemStack(Material.LEATHER_HELMET)).setLeatherArmorColor(playerTeam.getContent().getDyeColor().getColor()).toItemStack()
			});
			
			for(PotionEffect potionEffect : player.getActivePotionEffects()) player.removePotionEffect(potionEffect.getType());
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15*20, 1));
			player.updateInventory();
		}
		
		for(final ScoreboardSign scoreboardSign : data.getBoards().values()) {
			scoreboardSign.setLine(1, " ");
			scoreboardSign.setLine(2, Teams.BLUE.getChatColor() + Teams.BLUE.getDisplayName() + " §f0 pts");
			scoreboardSign.setLine(3, Teams.RED.getChatColor() + Teams.RED.getDisplayName() + " §f0 pts");
			scoreboardSign.setLine(4, "  ");
			scoreboardSign.setLine(5, "§7Temps §a0s");
			scoreboardSign.setLine(6, "§7Fin du jeu §b"+config.getMaxTime()+"m");
			scoreboardSign.setLine(7, "   ");
			scoreboardSign.setLine(8, config.getScoreboardFooter());
		}
		
		this.spawners = new Spawners(this);
		this.spawners.runTaskTimer(instance, 0, config.getSpawnerInterval()*20);
		
		this.timer = new Timer(this);
		this.timer.runTaskTimer(instance, 0, 20);
	}
	
	public void end(final GTeam playerTeam) {
		this.state = State.END;
		this.timer.cancel();
		
		if(playerTeam != null) {
			this.instance.getServer().broadcastMessage("§b§lL'équipe " + playerTeam.getContent().getChatColor() + playerTeam.getContent().getDisplayName() + " §b§lvient de remporter la partie !");
			
			for(final Player winner : playerTeam.getPlayers()) {
				winner.setGameMode(GameMode.CREATIVE);
				winner.playSound(winner.getLocation(), Sound.ENDERDRAGON_DEATH, 10F, 20F);
				new Title("§aVous avez gagné !", "§7Félicitations à vous!").display(winner, 1, 3, 1);
			}

			for(final Player looser : playerTeam.getAdverseTeam().getPlayers()) {
				looser.setGameMode(GameMode.SPECTATOR);
				looser.playSound(looser.getLocation(), Sound.WITHER_DEATH, 10F, 20F);
				new Title("§cVous avez perdu :(").display(looser, 1, 3, 1);
			}
			
			for(final ScoreboardSign scoreboardSign : data.getBoards().values()) {
				scoreboardSign.setLine(1, " ");
				scoreboardSign.setLine(2, "§cPartie terminée !");
				scoreboardSign.setLine(3, "§fVainqueurs " + playerTeam.getContent().getChatColor() + playerTeam.getContent().getDisplayName());
				scoreboardSign.setLine(4, "  ");
				scoreboardSign.setLine(5, "§7§oFélicitations à l'équipe");
				scoreboardSign.setLine(6, "§7§odes gagnants !");
				scoreboardSign.setLine(7, "   ");
				scoreboardSign.setLine(8, config.getScoreboardFooter());
			}
		} else {
			this.instance.getServer().broadcastMessage("§c§lMalheureusement, la partie est terminée avec une égalité pour les deux équipes !");
			
			for(final Player gamePlayer : data.getPlayers()) {
				gamePlayer.setGameMode(GameMode.SPECTATOR);
				gamePlayer.playSound(gamePlayer.getLocation(), Sound.WITHER_DEATH, 10F, 20F);
				new Title("§cÉgalité").display(gamePlayer, 1, 3, 1);
			}
			
			for(final ScoreboardSign scoreboardSign : data.getBoards().values()) {
				scoreboardSign.setLine(1, " ");
				scoreboardSign.setLine(2, "§cTemps écoulée !");
				scoreboardSign.setLine(3, "§fÉgalité");
				scoreboardSign.setLine(4, "  ");
				scoreboardSign.setLine(5, "§7§oAucune équipe ne");
				scoreboardSign.setLine(6, "§7§osort vainqueur !");
				scoreboardSign.setLine(7, "   ");
				scoreboardSign.setLine(8, config.getScoreboardFooter());
			}
		}
		
		
		new Ending(this).runTaskLater(instance, config.getEndTimer()*20);
	}
	
	
	public JavaPlugin getInstance() {
		return instance;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}
	
	public Map getMap() {
		return map;
	}
	
	public Data getData() {
		return data;
	}
	
	public Config getConfig() {
		return config;
	}
	
	public Launching getLaunching() {
		return launching;
	}
	
	public enum State {
		WAITING,
		LAUNCHING,
		PLAYING,
		END
	}
	
}
