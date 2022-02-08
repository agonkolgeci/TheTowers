package fr.jielos.thetowers.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.components.ItemBuilder;
import fr.jielos.thetowers.components.ScoreboardSign;
import fr.jielos.thetowers.components.Title;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;

public class PlayerJoin implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Game game = Main.getInstance().getGame();
		final Player player = event.getPlayer();
		
		event.setJoinMessage(null);
		player.setHealth(20); player.setFoodLevel(20);
		player.setLevel(0); player.setExp(0);
		player.getInventory().clear(); player.getInventory().setArmorContents(null);
		player.spigot().respawn();
		for(PotionEffect potionEffect : player.getActivePotionEffects()) player.removePotionEffect(potionEffect.getType()); 
		
		if(game.getState() == State.WAITING || game.getState() == State.LAUNCHING) {
			game.getData().getPlayers().add(player);
			
			new Title(ChatColor.translateAlternateColorCodes('&', game.getConfig().getJoinTitle()), ChatColor.translateAlternateColorCodes('&', game.getConfig().getJoinSubTitle().replace("{player_name}", player.getName()))).display(player, 1, 3, 1);;
			
			if(!game.getData().getBoards().containsKey(player)) {
				final ScoreboardSign scoreboardSign = new ScoreboardSign(player, ChatColor.translateAlternateColorCodes('&', game.getConfig().getScoreboardTitle()));
				scoreboardSign.create();
				
				scoreboardSign.setLine(1, " ");
				scoreboardSign.setLine(2, "§fJoueurs §e" + game.getData().getPlayers().size());
				scoreboardSign.setLine(3, "§7En attente ...");
				scoreboardSign.setLine(4, "  ");
				scoreboardSign.setLine(5, game.getConfig().getScoreboardFooter());
				
				game.getData().getBoards().put(player, scoreboardSign);
			}
			
			for(final ScoreboardSign scoreboardSign : game.getData().getBoards().values()) {
				scoreboardSign.setLine(2, "§fJoueurs §e" + game.getData().getPlayers().size());
			}
			
			player.teleport(game.getConfig().getWaitingRoom());
			player.setGameMode(GameMode.ADVENTURE);

			player.getInventory().setItem(game.getConfig().getMenuTeamsItemPosition(), new ItemBuilder(new ItemStack(Material.getMaterial(game.getConfig().getMenuTeamsItem()))).setName(game.getConfig().getMenuTeamsItemName()).toItemStack());
			player.getInventory().setHeldItemSlot(game.getConfig().getMenuTeamsItemPosition());

			if(game.getConfig().isMenuQuit()) {
				player.getInventory().setItem(game.getConfig().getMenuQuitItemPosition(), new ItemBuilder(new ItemStack(Material.getMaterial(game.getConfig().getMenuQuitItem()))).setName(game.getConfig().getMenuQuitItemName()).toItemStack());
			}

			player.updateInventory();
			
			game.getInstance().getServer().broadcastMessage("§7"+player.getName()+" §evient de rejoindre la partie ! §e(§b"+game.getData().getPlayers().size()+"§e/§b"+game.getConfig().getMaxPlayers()+"§e)");
			if(game.getData().getPlayers().size() >= game.getConfig().getMaxPlayers()) game.launch();
		}
	}
	
}
