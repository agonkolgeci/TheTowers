package fr.jielos.thetowers.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class PlayerArmorStandManipulate implements Listener {

    @EventHandler
    public void onPlayerArmorStandManipulate(final PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

}
