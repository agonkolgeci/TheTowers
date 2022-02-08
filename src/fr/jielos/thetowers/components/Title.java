package fr.jielos.thetowers.components;

import java.util.Collection;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;

public class Title {

    private String title;
    private String subtitle;

    public Title(String title) {
        this.title = title;
        this.subtitle = "";
    }

    public Title(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public void display(Player player, int fadeIn, int stay, int fadeOut) {
        PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
        PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn * 20, stay * 20, fadeOut * 20);
        playerConnection.sendPacket(packetPlayOutTimes);

        if(this.subtitle != null) {
            IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + this.subtitle + "\"}");
            PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
            playerConnection.sendPacket(packetPlayOutSubTitle);
        }

        if(this.title != null) {
            IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + this.title + "\"}");
            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
            playerConnection.sendPacket(packetPlayOutTitle);
        }
    }

    public void display(Collection<? extends Player> players, int fadeIn, int stay, int fadeOut){
        players.forEach(player -> display(player, fadeIn, stay, fadeOut));
    }
}
