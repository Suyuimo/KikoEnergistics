package de.weinschenk.kikoEnergistics;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    private final KikoEnergistics plugin;

    public InventoryListener(KikoEnergistics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (event.getView().getTitle().equals("KE Terminal + Crafting")) {
            Player player = (Player) event.getWhoClicked();

            // Wenn der Spieler auf das Suchfeld klickt (Slot 53)
            if (event.getSlot() == 53) {
                event.setCancelled(true); // Verhindert, dass das Item bewegt wird
                player.sendMessage("Gib den Namen des Items im Chat ein.");
                plugin.setPlayerInSearchMode(player); // Setzt den Spieler in den Suchmodus
                return;
            }

            // Crafting-Bereich: Wenn der Spieler im Crafting-Bereich klickt (Slots 54-61)
            if (event.getSlot() >= 54 && event.getSlot() <= 61) {
                updateCraftingResult(player, inventory);
                return;
            }

            // Crafting-Ergebnis: Wenn der Spieler das Crafting-Ergebnis entnimmt (Slot 62)
            if (event.getSlot() == 62) {
                event.setCancelled(true);
                ItemStack result = inventory.getItem(62);
                if (result != null && result.getType() != Material.AIR) {
                    player.getInventory().addItem(result); // Füge das Ergebnis dem Spieler-Inventar hinzu
                    // Entferne die verwendeten Items aus dem Crafting-Bereich
                    for (int i = 54; i <= 61; i++) {
                        inventory.setItem(i, new ItemStack(Material.AIR));
                    }
                    inventory.setItem(62, new ItemStack(Material.AIR)); // Setzt das Crafting-Ergebnis zurück
                }
            }
        }
    }

    // Methode zum Aktualisieren des Crafting-Ergebnisses basierend auf den Items im Crafting-Raster
    private void updateCraftingResult(Player player, Inventory inventory) {
        ItemStack[] craftingGrid = new ItemStack[9];
        for (int i = 54; i <= 61; i++) {
            craftingGrid[i - 54] = inventory.getItem(i); // Kopiere die Items in den Crafting-Bereich
        }

        // Berechne das Crafting-Ergebnis basierend auf dem Grid
        ItemStack result = getCraftingResult(craftingGrid);
        inventory.setItem(62, result); // Zeige das Crafting-Ergebnis im Slot 62 an
    }

    // Simpler Crafting-Algorithmus für ein Beispielrezept (hier: Werkbank aus Holzplanken)
    private ItemStack getCraftingResult(ItemStack[] craftingGrid) {
        // Beispiel: Wenn der Spieler eine Werkbank (Crafting Table) craften möchte (4 Holzblöcke im 2x2-Raster)
        if (craftingGrid[0] != null && craftingGrid[0].getType() == Material.OAK_PLANKS &&
                craftingGrid[1] != null && craftingGrid[1].getType() == Material.OAK_PLANKS &&
                craftingGrid[3] != null && craftingGrid[3].getType() == Material.OAK_PLANKS &&
                craftingGrid[4] != null && craftingGrid[4].getType() == Material.OAK_PLANKS) {
            return new ItemStack(Material.CRAFTING_TABLE); // Ergebnis: Werkbank
        }

        // Wenn kein gültiges Rezept gefunden wurde, wird kein Ergebnis angezeigt
        return new ItemStack(Material.AIR);
    }

    // Speichert das Inventar, wenn der Spieler das KE-Terminal schließt
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().startsWith("KE Terminal")) {
            Player player = (Player) event.getPlayer();
            Inventory inventory = event.getInventory();
            plugin.saveInventory(player, inventory); // Speichert das aktuelle Inventar des Spielers
        }
    }
}
