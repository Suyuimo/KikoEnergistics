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
        event.getPlayer().addResourcePack(UUID.randomUUID(),
                "https://www.dropbox.com/scl/fi/is4o9dv7uofs3yb5nto98/KikoEnergistics.zip?rlkey=p7vrdkb5n25xcypljnlxg3z6c&st=erspbyti&dl=1",
                DatatypeConverter.parseHexBinary("ca52d4cf52b8680a3bb293b7ec47bfd69a07587d"),
                "Please, allow resource packs for better experience!",
                true);
    }

}
