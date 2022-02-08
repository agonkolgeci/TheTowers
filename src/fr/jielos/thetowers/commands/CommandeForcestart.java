package fr.jielos.thetowers.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;

public class CommandeForcestart implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.hasPermission("thetowers.forcestart")) {
			final Game game = Main.getInstance().getGame();
			
			if(game.getState() != State.PLAYING && game.getState() != State.END) {
				if(game.getState() != State.LAUNCHING) {
					if(game.getData().getPlayers().size() >= 2) {
						game.launch();
						sender.sendMessage("§7Vous avez §acorrectement §7forcer le démarrage de la partie.");
					} else sender.sendMessage("§cIl n'y a pas assez de joueurs pour commencer la partie, minimum 2 joueurs.");
				} else sender.sendMessage("§cSoyez patient, la partie va démarrer dans quelques secondes.");
			} else sender.sendMessage("§cMalheureusement, vous ne pouvez pas forcer le démarrage de la partie puisque celle-ci est déjà démarrer/terminée ou en attente de joueurs.");
		}
		return false;
	}

}