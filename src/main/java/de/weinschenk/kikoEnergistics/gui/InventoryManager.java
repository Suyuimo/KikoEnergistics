package de.weinschenk.kikoEnergistics.gui;

import de.weinschenk.kikoEnergistics.KikoEnergistics;
import de.weinschenk.kikoEnergistics.manager.KeyedManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

public class InventoryManager extends KeyedManager<UUID, GUIInventory> implements Listener {
    public InventoryManager(KikoEnergistics plugin) {
        super(plugin);
    }

    @Override
    public GUIInventory fetch(UUID key) {
        return get(key);
    }

    public void updateAll(String storageHolderName){
        for (Map.Entry<UUID, GUIInventory> entry : getAll().entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if(player == null) {
                getAll().remove(entry.getKey());
                return;
            }
            if(player.getOpenInventory().getTopInventory().getHolder() instanceof StorageInventory inventory &&
                    (storageHolderName == null || storageHolderName.isEmpty() || inventory.getPlayerStorageManager().getPlayerName().equalsIgnoreCase(storageHolderName))){
                inventory.build();
            } else
                getAll().remove(entry.getKey());
        }
    }

    public void updateAll(){
        updateAll(null);
    }

    public void openInventory(Player player, GUIInventory inventory){
        player.openInventory(inventory.getInventory(true));
        getAll().put(player.getUniqueId(), inventory);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        if(event.getInventory().getHolder() instanceof GUIInventory)
            getAll().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onClose(PlayerQuitEvent event){
        getAll().remove(event.getPlayer().getUniqueId());
    }

}
