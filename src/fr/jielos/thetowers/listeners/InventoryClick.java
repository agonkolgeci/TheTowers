package fr.jielos.thetowers.listeners;

import fr.jielos.thetowers.components.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;
import fr.jielos.thetowers.references.Inventories;
import fr.jielos.thetowers.references.Teams;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryClick implements Listener {

	@EventHandler
	public void onInventoryClick(final InventoryClickEvent event) {
		final Game game = Main.getInstance().getGame();

		if(event.getWhoClicked() instanceof Player) {
			final Player player = (Player) event.getWhoClicked();
			final Inventory inventory = event.getInventory();

			if (game.getState() != State.PLAYING) event.setCancelled(true);

			if (inventory.getName().equals(Inventories.TEAMS.getInventory().getName()) && (game.getState() == State.WAITING || game.getState() == State.LAUNCHING)) {
				final ItemStack currentItem = event.getCurrentItem();
				if (currentItem == null || currentItem.getType() == Material.AIR) return;

				final Scoreboard scoreboard = game.getInstance().getServer().getScoreboardManager().getMainScoreboard();

				event.setCancelled(true);
				if (event.getRawSlot() == 1 || event.getRawSlot() == 2) {
					final Teams team = (event.getRawSlot() == 1 ? Teams.BLUE : Teams.RED);

					final Team scoreboardTeam = scoreboard.getTeam(team.getName());
					if (scoreboardTeam != null && !scoreboardTeam.hasEntry(player.getName())) {
						if (scoreboardTeam.getEntries().size() < game.getConfig().getMaxPlayersPerTeams()) {
							scoreboardTeam.addEntry(player.getName());
							player.sendMessage("§7§oVous venez de rejoindre l'équipe " + team.getChatColor() + team.getDisplayName() + "§7§o.");

							for (final HumanEntity humanEntity : inventory.getViewers()) {
								if (humanEntity instanceof Player) {
									final Player viewer = (Player) humanEntity;

									final Team blueTeam = scoreboard.getTeam(Teams.BLUE.getName());
									final Team redTeam = scoreboard.getTeam(Teams.RED.getName());

									if (blueTeam != null) {
										List<String> blueContent = blueTeam.getPlayers().stream().map(e -> "§8- §9" + e.getName()).collect(Collectors.toList());

										for (int i = 0; i < game.getConfig().getMaxPlayersPerTeams() - blueTeam.getPlayers().size(); i++)
											blueContent.add("§8- §7§oPlace disponible");
										viewer.getOpenInventory().setItem(1, new ItemBuilder(new ItemStack(Material.WOOL, 1, (short) 11)).setName("§9Rejoindre l'équipe Bleu").setLore(blueContent.toArray(new String[0])).toItemStack());
									}

									if (redTeam != null) {
										List<String> redContent = redTeam.getPlayers().stream().map(e -> "§8- §c" + e.getName()).collect(Collectors.toList());

										for (int i = 0; i < game.getConfig().getMaxPlayersPerTeams() - redTeam.getPlayers().size(); i++)
											redContent.add("§8- §7§oPlace disponible");
										viewer.getOpenInventory().setItem(2, new ItemBuilder(new ItemStack(Material.WOOL, 1, (short) 14)).setName("§cRejoindre l'équipe Rouge").setLore(redContent.toArray(new String[0])).toItemStack());
									}
								}
							}
						} else player.sendMessage("§c§oImpossible de rejoindre cette équipe, celle-ci est complète.");
					}
				} else if (event.getRawSlot() == 7) {
					if (scoreboard.getEntryTeam(player.getName()) != null) {
						scoreboard.getEntryTeam(player.getName()).removeEntry(player.getName());
						player.sendMessage("§7§oVous venez de quitter votre équipe, elle sera asignée automatiquement au démarrage.");
					}
				}
			}
		}
	}
}
