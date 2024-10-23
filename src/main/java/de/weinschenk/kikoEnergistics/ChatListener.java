package de.weinschenk.kikoEnergistics;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ChatListener implements Listener {

    private final KikoEnergistics plugin;
    private Set<Player> playersInSearchMode = new HashSet<>();

    public ChatListener(KikoEnergistics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();

        // Wenn sich der Spieler im Suchmodus befindet
        if (playersInSearchMode.contains(player)) {
            event.setCancelled(true); // Verhindert, dass die Nachricht im Chat angezeigt wird

            // Starte die Suche nach dem Item
            plugin.searchItem(player, message);

            // Entferne den Spieler aus dem Suchmodus
            playersInSearchMode.remove(player);
        }
    }

    // Setzt den Spieler in den Suchmodus
    public void setPlayerInSearchMode(Player player) {
        playersInSearchMode.add(player);
    }
}
