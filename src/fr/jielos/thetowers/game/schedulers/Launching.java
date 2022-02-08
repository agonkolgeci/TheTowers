package fr.jielos.thetowers.game.schedulers;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.jielos.thetowers.components.ScoreboardSign;
import fr.jielos.thetowers.game.Game;
import net.md_5.bungee.api.ChatColor;

public class Launching extends BukkitRunnable {

	final Game game;
	int seconds;
	
	public Launching(final Game game) {
		this.game = game;
		this.seconds = game.getConfig().getLaunchTimer();
	}
	
	@Override
	public void run() {
		if(game.getData().getPlayers().size() >= 2) {
			if(seconds <= 0) {
				game.start();
				
				cancel();
				return;
			}
			
			for(final Player player : game.getData().getPlayers()) {
				if(seconds % 5 == 0 || seconds <= 5) {
					player.sendMessage("§eDébut de la partie dans " + (seconds <= 10 ? ChatColor.RED : (seconds <= 20 ? ChatColor.GOLD : ChatColor.GREEN)) + seconds + " §eseconde"+(seconds > 1 ? "s" : "")+" !");
					player.playSound(player.getLocation(), Sound.NOTE_PLING, ((float) (game.getConfig().getLaunchTimer()-seconds)), 0.5F);
				}
			}
			
			for(final ScoreboardSign scoreboardSign : game.getData().getBoards().values()) {
				scoreboardSign.setLine(3, "§fTéléportation §a" + seconds + "s");
			}
			
			seconds--;
		} else {
			game.setState(Game.State.WAITING);

			for(final ScoreboardSign scoreboardSign : game.getData().getBoards().values()) {
				scoreboardSign.setLine(3, "§7En attente ...");
			}

			game.getInstance().getServer().broadcastMessage("§cLancement annulé, visiblement il n'y a plus assez de joueurs pour démarrer la partie.");
			cancel();
		}
	}

}