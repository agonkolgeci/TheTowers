package fr.jielos.thetowers.game.schedulers;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import fr.jielos.thetowers.game.Game;

public class Ending extends BukkitRunnable {

	final Game game;
	public Ending(final Game game) {
		this.game = game;
	}
	
	@Override
	public void run() {
		if(game.getConfig().isBungeeCord()) {
			for(final Player player : game.getInstance().getServer().getOnlinePlayers()) {
				player.teleport(game.getConfig().getWaitingRoom());

				final ByteArrayDataOutput output = ByteStreams.newDataOutput();
				
		        output.writeUTF("Connect");
		        output.writeUTF(game.getConfig().getBungeeCordServer());
		        
		        player.sendPluginMessage(game.getInstance(), "BungeeCord", output.toByteArray());
			}
		} else {
			for(final Player player : game.getInstance().getServer().getOnlinePlayers()) {
				player.kickPlayer("§cMalheureusement, cette partie vient de se finir, rendez-vous pour un prochaine partie !");
			}
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				game.getInstance().getServer().reload();
			}
		}.runTaskLater(game.getInstance(), 1);
	}
	
}
