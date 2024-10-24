package de.weinschenk.kikoEnergistics;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryListener implements Listener {

    private final KikoEnergistics plugin;

    public InventoryListener(KikoEnergistics plugin) {
        this.plugin = plugin;
    }

    // Verarbeitet Klicks im Inventar, einschließlich Crafting und Ergebnis
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (event.getView().getTitle().startsWith("KE Terminal")) { // Überprüfe, ob es sich um das KE-Terminal handelt
            Player player = (Player) event.getWhoClicked();

            // Erlaube das Platzieren von Items im Crafting-Grid (Slots 19-21, 28-30, 37-39)
            if (event.getSlot() >= 19 && event.getSlot() <= 39) {
                // Shift-Klick und manuelles Platzieren sind erlaubt
                return; // Blockiere nichts, erlaube das Platzieren
            }

            // Blockiere das Crafting-Ergebnis (Slot 24) für das Platzieren von Items
            if (event.getSlot() == 24) {
                event.setCancelled(true); // Verhindere das Platzieren von Items im Crafting-Ergebnis-Slot
                ItemStack result = inventory.getItem(24);
                if (result != null && result.getType() != Material.AIR) {
                    player.getInventory().addItem(result); // Füge das Ergebnis dem Spieler-Inventar hinzu
                    // Leere das Crafting-Grid nach Entnahme des Ergebnisses
                    for (int i : new int[]{19, 20, 21, 28, 29, 30, 37, 38, 39}) {
                        inventory.setItem(i, new ItemStack(Material.AIR)); // Leere die Crafting-Slots
                    }
                    inventory.setItem(24, new ItemStack(Material.AIR)); // Setze das Crafting-Ergebnis zurück
                }
            }
        }
    }

    // Speichert das Inventar eines Spielers beim Schließen des KE-Terminals
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().startsWith("KE Terminal")) {
            Player player = (Player) event.getPlayer();
            Inventory inventory = event.getInventory();

            // Liste der Items, die gespeichert werden sollen
            List<ItemStack> itemsToSave = new ArrayList<>();

            // Gehe durch alle Slots des Inventars (einschließlich Crafting-Grid)
            for (int i = 0; i < 54; i++) {
                ItemStack item = inventory.getItem(i);
                if (item != null && item.getType() != Material.AIR) {
                    itemsToSave.add(item); // Alle Items zum Speichern hinzufügen
                }
            }

            // Speichert das Inventar im Netzwerk (einschließlich der Crafting-Grid-Items)
            plugin.saveInventory(player, itemsToSave); // Diese Methode speichert die Liste ins Netzwerk
        }
    }
}
