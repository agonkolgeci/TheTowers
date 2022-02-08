package fr.jielos.thetowers.listeners;

import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.GTeam;
import fr.jielos.thetowers.game.Game;

public class InventoryOpen implements Listener {

	@EventHandler
	public void onInventoryOpen(final InventoryOpenEvent event) {
		final Game game = Main.getInstance().getGame();
		
		if(event.getPlayer() instanceof Player && (event.getInventory().getHolder() instanceof Chest || event.getInventory().getHolder() instanceof DoubleChest)) {
			final Player player = (Player) event.getPlayer();
			final Scoreboard scoreboard = game.getInstance().getServer().getScoreboardManager().getMainScoreboard();
			
			final Location location = (event.getInventory().getHolder() instanceof Chest ? ((Chest) event.getInventory().getHolder()).getLocation() : ((DoubleChest) event.getInventory().getHolder()).getLocation());
			
			final Team team = scoreboard.getEntryTeam(player.getName());
			if(team == null) return; 
			
			final GTeam adverseTeam = game.getData().getTeams().entrySet().stream().filter(e -> !e.getKey().getName().equals(team.getName())).map(e -> e.getValue()).collect(Collectors.toList()).get(0);
			if(adverseTeam.getRegion().isIn(location)) event.setCancelled(true);
		}
	}
	
}