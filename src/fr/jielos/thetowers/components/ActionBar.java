package fr.jielos.thetowers.components;

import java.util.Collection;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.jielos.thetowers.Main;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class ActionBar {

    private String message;
    private String[] messages;

    public ActionBar(String message) {
        this.message = message;
    }

    public ActionBar(String... messages) {
        this.messages = messages;
    }

    public void send(Player player, int fadeIn, int stay, int fadeOut) {
        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
        PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn * 20, stay * 20, fadeOut * 20);
        playerConnection.sendPacket(packetPlayOutTimes);

        if(this.message != null) {
            IChatBaseComponent actionBar = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + this.message + "\"}");
            PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(actionBar, (byte) 2);
            playerConnection.sendPacket(packetPlayOutChat);
        }
    }

    public void send(Collection<? extends Player> players, int fadeIn, int stay, int fadeOut) {
        players.forEach(player -> send(player, fadeIn, stay, fadeOut));
    }

    public void play(Player player, int fadeIn, int stay, int fadeOut, int interval) {
        new BukkitRunnable() {
            int messagesCount = 0;
            @Override
            public void run() {
                if(messages != null) {
                    ActionBar actionBar = new ActionBar(messages[messagesCount]);
                    actionBar.send(player, fadeIn, stay, fadeOut);

                    if((messagesCount + 1) == messages.length) { messagesCount = 0; }
                    else { messagesCount++; }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, (fadeIn * 20) + (stay * 20) + (fadeOut * 20) + (interval * 20) );
    }

    public void playAll(Collection<? extends Player> players, int fadeIn, int stay, int fadeOut, int interval) {
        new BukkitRunnable() {
            int messagesCount = 0;
            @Override
            public void run() {
                if(messages != null) {
                    ActionBar actionBar = new ActionBar(messages[messagesCount]);
                    players.forEach(player -> actionBar.send(player, fadeIn, stay, fadeOut));

                    if((messagesCount + 1) == messages.length) { messagesCount = 0; }
                    else { messagesCount++; }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, (fadeIn * 20) + (stay * 20) + (fadeOut * 20) + (interval * 20) );
    }

}
