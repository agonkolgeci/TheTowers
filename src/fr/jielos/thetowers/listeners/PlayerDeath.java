package fr.jielos.thetowers.listeners;

import java.util.Iterator;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.GTeam;
import fr.jielos.thetowers.game.Game;
import fr.jielos.thetowers.game.Game.State;

public class PlayerDeath implements Listener {

	@EventHandler
	public void onPlayerDeath(final PlayerDeathEvent event) {
		final Game game = Main.getInstance().getGame();
		final Player player = event.getEntity();
		
		if(game.getState() == State.PLAYING) {
			final LivingEntity killer = player.getKiller();
			
			final Scoreboard scoreboard = game.getInstance().getServer().getScoreboardManager().getMainScoreboard();
			final Iterator<ItemStack> iter = event.getDrops().iterator();
			 
			if(!game.getConfig().isArmorsDrop()) {
				while(iter.hasNext()) {
					final ItemStack stack = iter.next();
					switch (stack.getType()) {
						case LEATHER_HELMET:
							iter.remove();
							break;
							
					    case LEATHER_CHESTPLATE:
					    	iter.remove();
					    	break;
					      
					    case LEATHER_LEGGINGS:
					    	iter.remove();
					    	break;
					      
					    case LEATHER_BOOTS:
					    	iter.remove();
					    	break;
					      
					    default: break;
					}
				}
			}
			
			final Team pTeam = scoreboard.getEntryTeam(player.getName());
			if(pTeam == null) return;
			
			final GTeam playerTeam = game.getData().getTeams().get(pTeam);
			
			if(killer != null) {
				if(killer != player) {
					final Team aTeam = scoreboard.getEntryTeam(killer.getName());
					if(aTeam == null) return;
					
					final GTeam adverseTeam = game.getData().getTeams().get(aTeam);
					event.setDeathMessage(playerTeam.getContent().getChatColor() + player.getName() + " §7a été tué par " + adverseTeam.getContent().getChatColor() + killer.getName() + "§7.");
				} else event.setDeathMessage(playerTeam.getContent().getChatColor() + player.getName() + " §7s'est suicidé.");
			} else event.setDeathMessage(playerTeam.getContent().getChatColor() + player.getName() + " §7est mort.");
		
			player.getLocation().getWorld().strikeLightningEffect(player.getLocation());
			for(final Player gamePlayer : game.getData().getPlayers()) gamePlayer.playSound(gamePlayer.getLocation(), Sound.AMBIENCE_THUNDER, 2F, 1F);
		} else {
			event.getDrops().clear();
			event.setDeathMessage(null);
		}
		
		player.setVelocity(new Vector(0, 0, 0));
		new BukkitRunnable() {
			@Override
			public void run() {
				player.spigot().respawn();
			}
		}.runTaskLater(game.getInstance(), 1);
	}
	
}
