package de.weinschenk.kikoEnergistics.manager;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class StorageBackupManager implements Listener {

    private final StorageManager storageManager;

    public StorageBackupManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @EventHandler
    public void onSave(WorldSaveEvent event){
        if(!event.getWorld().equals(Bukkit.getWorlds().getFirst()))
            return;
        storageManager.getPlugin().getLogger().info("The world is saving. Rewriting cache");
        storageManager.writeAll();
    }

}
