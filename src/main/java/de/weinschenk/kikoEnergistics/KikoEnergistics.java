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
    private void openKEGui(Player player, int page) {
        UUID playerId = player.getUniqueId();
        List<ItemStack> items = keStorage.getOrDefault(playerId, new ArrayList<>());
        Inventory keGui = Bukkit.createInventory(null, 54, "KE Terminal + Crafting"); // 54 Slots (maximale Größe)

        // Items der aktuellen Seite anzeigen
        int startIndex = page * ITEMS_PER_PAGE;
        for (int i = 0; i < ITEMS_PER_PAGE; i++) {
            if (startIndex + i < items.size()) {
                keGui.setItem(i, items.get(startIndex + i));
            }
        }

        // Suchfeld (beispielsweise Slot 45, wenn du die untere Reihe für spezielle Slots nutzen willst)
        ItemStack searchItem = new ItemStack(Material.PAPER);
        searchItem.getItemMeta().setDisplayName(ChatColor.YELLOW + "Suchfeld");
        keGui.setItem(45, searchItem); // Lege das Suchfeld in Slot 45

        // Crafting-Bereich (in der letzten Reihe ab Slot 46 bis 53)
        for (int i = 46; i <= 53; i++) {
            keGui.setItem(i, new ItemStack(Material.AIR)); // Leerer Crafting-Bereich
        }

        // Crafting-Ergebnis (falls benötigt, Slot 44 oder anderen Platz im Bereich anpassen)
        keGui.setItem(44, new ItemStack(Material.AIR)); // Setze das Crafting-Ergebnis in einen Slot deiner Wahl

        player.openInventory(keGui);
        playerPages.put(playerId, page); // Speichert die aktuelle Seite des Spielers
    }


    // Seite wechseln
    private void changePage(Player player, int direction) {
        UUID playerId = player.getUniqueId();
        int currentPage = playerPages.getOrDefault(playerId, 0);
        int newPage = currentPage + direction;

        // Maximal zulässige Seitenzahl
        int maxPage = (int) Math.ceil(keStorage.getOrDefault(playerId, new ArrayList<>()).size() / (double) ITEMS_PER_PAGE) - 1;
        if (newPage < 0) newPage = 0;
        if (newPage > maxPage) newPage = maxPage;

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

        int currentPage = playerPages.getOrDefault(playerId, 0);
        int startIndex = currentPage * ITEMS_PER_PAGE;

        for (int i = 0; i < ITEMS_PER_PAGE; i++) {
            if (startIndex + i < items.size()) {
                items.set(startIndex + i, inventory.getItem(i));
            } else {
                items.add(inventory.getItem(i));
            }
        }

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