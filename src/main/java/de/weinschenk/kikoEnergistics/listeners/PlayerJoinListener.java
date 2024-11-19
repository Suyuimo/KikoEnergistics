package de.weinschenk.kikoEnergistics.listeners;

import jakarta.xml.bind.DatatypeConverter;
import de.weinschenk.kikoEnergistics.KikoEnergistics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final KikoEnergistics plugin;

    public PlayerJoinListener(KikoEnergistics plugin) {
        this.plugin = plugin;
    }

    // Texture Pack erzwingen

    //Deakivert

    /*

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.getPlayer().addResourcePack(UUID.randomUUID(),
                "https://github.com/Suyuimo/KikoEnergistics/raw/refs/heads/master/KikoEnergistics-Texture-Pack.zip",
                DatatypeConverter.parseHexBinary("ca52d4cf52b8680a3bb293b7ec47bfd69a07587d"),
                "Please, allow resource packs for better experience!",
                true);
    }



     */

}
