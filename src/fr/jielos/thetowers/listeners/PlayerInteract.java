package fr.jielos.thetowers.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.jielos.thetowers.references.Teams;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.components.ItemBuilder;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;
import fr.jielos.thetowers.references.Inventories;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerInteract implements Listener {

	@EventHandler
	public void onPlayerInteract(final PlayerInteractEvent event) {
		final Game game = Main.getInstance().getGame();
		final Player player = event.getPlayer();

		if(game.getState() != State.PLAYING) event.setCancelled(true);
		if(event.getItem() != null && event.getItem().getItemMeta().getDisplayName() != null && (game.getState() == State.WAITING || game.getState() == State.LAUNCHING)) {
			final String currentItemName = event.getItem().getItemMeta().getDisplayName();
			
			if(currentItemName.equals(game.getConfig().getMenuTeamsItemName())) {
				final Inventory inventory = Inventories.TEAMS.getInventory();

				final Scoreboard scoreboard = game.getInstance().getServer().getScoreboardManager().getMainScoreboard();
				final Team blueTeam = scoreboard.getTeam(Teams.BLUE.getName());
				final Team redTeam = scoreboard.getTeam(Teams.RED.getName());

				inventory.clear();

				if(blueTeam != null) {
					List<String> blueContent = blueTeam.getPlayers().stream().map(e -> "§8- §9" + e.getName()).collect(Collectors.toList());

					for(int i = 0; i < game.getConfig().getMaxPlayersPerTeams() - blueTeam.getPlayers().size(); i++) blueContent.add("§8- §7§oPlace disponible");
					inventory.setItem(1, new ItemBuilder(new ItemStack(Material.WOOL, 1, (short) 11)).setName("§9Rejoindre l'équipe Bleu").setLore(blueContent.toArray(new String[0])).toItemStack());
				}

				if(redTeam != null) {
					List<String> redContent = redTeam.getPlayers().stream().map(e ->  "§8- §c" + e.getName()).collect(Collectors.toList());

					for(int i = 0; i < game.getConfig().getMaxPlayersPerTeams() - redTeam.getPlayers().size(); i++) redContent.add("§8- §7§oPlace disponible");
					inventory.setItem(2, new ItemBuilder(new ItemStack(Material.WOOL, 1, (short) 14)).setName("§cRejoindre l'équipe Rouge").setLore(redContent.toArray(new String[0])).toItemStack());
				}

				inventory.setItem(7, new ItemBuilder(new ItemStack(Material.BARRIER)).setName("§8- §f§lAléatoire").toItemStack());
				
				player.openInventory(inventory);
			} else if(currentItemName.equals(game.getConfig().getMenuQuitItemName())) {
				if(game.getConfig().isBungeeCord()) {
					final ByteArrayDataOutput output = ByteStreams.newDataOutput();

					output.writeUTF("Connect");
					output.writeUTF(game.getConfig().getBungeeCordServer());

					player.sendPluginMessage(game.getInstance(), "BungeeCord", output.toByteArray());
				} else {
					player.kickPlayer("§cVous venez de quitter la partie, bon voyage!");
				}
			}
		}
	}
	
}