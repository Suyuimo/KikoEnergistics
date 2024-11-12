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

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

      //  TexturePack erzwungen deaktiviert - Kann bei bedarf aktiviert werden
    /*
        event.getPlayer().addResourcePack(UUID.randomUUID(),
                "https://github.com/Suyuimo/KikoEnergistics/raw/refs/heads/master/KikoEnergistics-Texture-Pack.zip",
                DatatypeConverter.parseHexBinary("ca52d4cf52b8680a3bb293b7ec47bfd69a07587d"),
                "This Resspurce Pack must be installed as the “Kiko Energistics” plugin is installed on the server. This resource pack does not change any textures, so it can be combined with the existing texture packs! Only additional textures are added.",
                true);

     */
    }

}
