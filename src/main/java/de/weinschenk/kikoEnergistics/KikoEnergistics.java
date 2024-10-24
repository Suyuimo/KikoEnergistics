package de.weinschenk.kikoEnergistics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class KikoEnergistics extends JavaPlugin implements Listener {

    // Speichert pro Spieler die Items im KE-Netzwerk
    private HashMap<UUID, List<ItemStack>> keStorage = new HashMap<>();
    // Speichert, auf welcher Seite der Spieler aktuell ist
    private HashMap<UUID, Integer> playerPages = new HashMap<>();

    private final int ITEMS_PER_PAGE = 54;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "KikoEnergistics Plugin aktiviert!");
        this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "KikoEnergistics Plugin deaktiviert!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerId = player.getUniqueId();

            switch (label.toLowerCase()) {
                case "ke":
                    openKEGui(player, playerPages.getOrDefault(playerId, 0));
                    return true;
                case "kesearch":
                    if (args.length > 0) {
                        String searchQuery = args[0].toLowerCase();
                        searchItem(player, searchQuery);
                    } else {
                        player.sendMessage(ChatColor.RED + "Bitte gib ein Item ein, nach dem gesucht werden soll.");
                    }
                    return true;
                case "keprev":
                    changePage(player, -1);
                    return true;
                case "kenext":
                    changePage(player, 1);
                    return true;
            }
        }
        return false;
    }

    // KE-Terminal mit Seiten öffnen
    public void openKEGui(Player player, int page) {
        UUID playerId = player.getUniqueId();
        List<ItemStack> items = keStorage.getOrDefault(playerId, new ArrayList<>());

        // Berechne die maximale Seitenanzahl
        int maxPage = (items.size() > 0) ? (int) Math.ceil(items.size() / (double) ITEMS_PER_PAGE) - 1 : 0;

        // Sicherstellen, dass die Seitenzahl gültig ist
        if (page < 0) {
            page = 0; // Verhindert negative Seitenzahlen
        }
        if (page > maxPage) {
            page = maxPage; // Verhindert das Blättern über die maximale Seitenanzahl hinaus
        }

        // KE-Terminal GUI mit 54 Slots erstellen
        Inventory keGui = Bukkit.createInventory(null, 54, "KE Terminal - Seite " + (page + 1) + " / " + (maxPage + 1));

        // Berechne den Startindex der Items basierend auf der Seite
        int startIndex = page * ITEMS_PER_PAGE;

        // Fülle die GUI mit Items der aktuellen Seite oder leeren Slots (AIR)
        for (int i = 0; i < 45; i++) { // Bis Slot 44, da 45 bis 53 Crafting und spezielle Slots sind
            if (startIndex + i < items.size()) {
                keGui.setItem(i, items.get(startIndex + i)); // Setze das Item in den Slot
            } else {
                keGui.setItem(i, new ItemStack(Material.AIR)); // Fülle leere Slots mit AIR
            }
        }

        // Crafting-Grid (Gelb markiert): Slots 19, 20, 21, 28, 29, 30, 37, 38, 39
        for (int i : new int[]{19, 20, 21, 28, 29, 30, 37, 38, 39}) {
            keGui.setItem(i, new ItemStack(Material.AIR)); // Leere Slots für das Crafting-Grid
        }

        // Crafting-Ergebnis (Rot markiert): Slot 24
        keGui.setItem(24, new ItemStack(Material.AIR)); // Leerer Slot für das Crafting-Ergebnis

        // Öffne das Inventar für den Spieler
        player.openInventory(keGui);

        // Speichere die aktuelle Seite des Spielers
        playerPages.put(playerId, page);
    }









    // Seite wechseln
    private void changePage(Player player, int direction) {
        UUID playerId = player.getUniqueId();
        int currentPage = playerPages.getOrDefault(playerId, 0); // Aktuelle Seite des Spielers
        int newPage = currentPage + direction; // Neue Seite berechnen

        // Berechne die maximale Seitenanzahl basierend auf der Anzahl der Items
        List<ItemStack> items = keStorage.getOrDefault(playerId, new ArrayList<>());
        int maxPage = (int) Math.ceil(items.size() / (double) ITEMS_PER_PAGE) - 1;

        // Verhindere, dass der Spieler auf ungültige Seiten wechselt
        if (newPage < 0) {
            newPage = 0; // Verhindert negative Seitenzahlen
        }
        if (newPage > maxPage) {
            newPage = maxPage; // Verhindert das Blättern über die maximale Seitenanzahl hinaus
        }

        // Öffne die neue Seite
        openKEGui(player, newPage);
    }

    // Suche nach einem Item im KE-Terminal
    public void searchItem(Player player, String query) {
        UUID playerId = player.getUniqueId();
        List<ItemStack> items = keStorage.getOrDefault(playerId, new ArrayList<>());

        Inventory searchInventory = Bukkit.createInventory(null, 54, "Suchergebnisse: " + query);
        int index = 0;

        // Suche nach dem Itemnamen
        for (ItemStack item : items) {
            if (item != null && item.getType() != Material.AIR && item.getType().name().toLowerCase().contains(query)) {
                if (index < 54) {
                    searchInventory.setItem(index, item);
                    index++;
                } else {
                    break; // Maximal 54 Suchergebnisse anzeigen
                }
            }
        }

        player.openInventory(searchInventory);
    }

    // Speichert das Inventar nach Schließen
    public void saveInventory(Player player, Inventory inventory) {
        UUID playerId = player.getUniqueId();
        List<ItemStack> items = keStorage.getOrDefault(playerId, new ArrayList<>());

        // Aktuelle Seite des Spielers abrufen
        int currentPage = playerPages.getOrDefault(playerId, 0);
        int startIndex = currentPage * ITEMS_PER_PAGE;

        // Überprüfen, ob der Startindex negativ ist oder außerhalb der Grenzen liegt
        if (startIndex < 0) {
            startIndex = 0; // Verhindere, dass der Index negativ wird
        }

        // Liste erweitern, falls sie zu klein ist, um die aktuelle Seite zu speichern
        while (items.size() < startIndex + ITEMS_PER_PAGE) {
            items.add(null); // Leere Slots hinzufügen
        }

        // Speichere alle relevanten Slots (die ersten 45 Slots)
        for (int i = 0; i < 45; i++) {
            ItemStack item = inventory.getItem(i); // Hol das Item aus dem Inventar
            if (startIndex + i < items.size()) {
                items.set(startIndex + i, item); // Setze das Item in die Liste
            }
        }

        // Optional: Speichere auch die Crafting-Grid-Slots (19-21, 28-30, 37-39)
        int[] craftingSlots = {19, 20, 21, 28, 29, 30, 37, 38, 39};
        for (int slot : craftingSlots) {
            ItemStack item = inventory.getItem(slot);
            if (item != null && item.getType() != Material.AIR) {
                items.add(item); // Füge das Crafting-Item der Liste hinzu
            }
        }

        // Optional: Speichere das Crafting-Ergebnis (Slot 24), wenn vorhanden
        ItemStack result = inventory.getItem(24);
        if (result != null && result.getType() != Material.AIR) {
            items.add(result); // Füge das Crafting-Ergebnis hinzu
        }

        // Speichere die aktualisierte Liste in der HashMap
        keStorage.put(playerId, items);
    }



    // Setzt Spieler in den Suchmodus
    public void setPlayerInSearchMode(Player player) {
        // Neues PlayerInSearchModeEvent auslösen
        PlayerInSearchModeEvent event = new PlayerInSearchModeEvent(player);
        getServer().getPluginManager().callEvent(event);

        // Falls das Event nicht abgebrochen wurde, setze den Spieler in den Suchmodus
        if (!event.isCancelled()) {
            getServer().getPluginManager().getPlugin("KikoEnergistics").getServer().getPluginManager().callEvent(event);
        }
    }
}