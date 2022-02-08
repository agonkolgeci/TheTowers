package fr.jielos.thetowers.listeners;

import fr.jielos.thetowers.Main;
import fr.jielos.thetowers.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntity implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        final Game game = Main.getInstance().getGame();

        if(event.getEntity() instanceof Player) {
            final Player victim = (Player) event.getEntity();
            if(event.getDamager() instanceof Player) {
                final Player damager = (Player) event.getDamager();
                if(game.getData().getPlayersRespawn().contains(victim)) {
                    event.setCancelled(true);
                    damager.sendMessage("§c§oLe Spawn-Kill est interdit !");
                }
            }
        }
    }

}
