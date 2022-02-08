package fr.jielos.thetowers.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.components.ItemBuilder;
import fr.jielos.thetowers.game.GTeam;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;

public class PlayerRespawn implements Listener {

	@EventHandler
	public void onPlayerRespawn(final PlayerRespawnEvent event) {
		final Game game = Main.getInstance().getGame();
		final Player player = event.getPlayer();
		
		if(game.getState() != State.PLAYING) {
			event.setRespawnLocation(game.getConfig().getWaitingRoom());
		} else if(game.getState() == State.PLAYING) {
			final Scoreboard scoreboard = game.getInstance().getServer().getScoreboardManager().getMainScoreboard();

			final Team team = scoreboard.getEntryTeam(player.getName());
			if(team == null) return; 
			
			final GTeam playerTeam = game.getData().getTeams().get(team);

			event.setRespawnLocation(playerTeam.getSpawn());

			game.getData().getPlayersRespawn().add(player);
			new BukkitRunnable() {
				@Override
				public void run() {
					game.getData().getPlayersRespawn().removeIf(e -> game.getData().getPlayersRespawn().contains(e));
				}
			}.runTaskLater(game.getInstance(), 5*20);

			new BukkitRunnable() {
				public void run() {
					player.teleport(playerTeam.getSpawn());
					player.setGameMode(GameMode.SURVIVAL);
					player.setHealth(20); player.setFoodLevel(20);
					player.setLevel(0); player.setExp(0);
					player.playSound(player.getLocation(), Sound.CAT_MEOW, 2F, 1F);

					player.getInventory().addItem(new ItemStack(Material.BAKED_POTATO, 8));
					player.getInventory().setArmorContents(new ItemStack[]{
						new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS)).setLeatherArmorColor(playerTeam.getContent().getDyeColor().getColor()).toItemStack(),
						new ItemBuilder(new ItemStack(Material.LEATHER_LEGGINGS)).setLeatherArmorColor(playerTeam.getContent().getDyeColor().getColor()).toItemStack(),
						new ItemBuilder(new ItemStack(Material.LEATHER_CHESTPLATE)).setLeatherArmorColor(playerTeam.getContent().getDyeColor().getColor()).toItemStack(),
						new ItemBuilder(new ItemStack(Material.LEATHER_HELMET)).setLeatherArmorColor(playerTeam.getContent().getDyeColor().getColor()).toItemStack()
					});
					
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 15*20, 1));
					player.updateInventory();
				}
			}.runTaskLater(game.getInstance(), 1);
		}
	}
	
}
