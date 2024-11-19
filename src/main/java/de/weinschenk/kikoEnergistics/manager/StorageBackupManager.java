package de.weinschenk.kikoEnergistics.manager;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class StorageBackupManager implements Listener {

    private final GlobalStorageManager globalStorageManager;

    public StorageBackupManager(GlobalStorageManager globalStorageManager) {
        this.globalStorageManager = globalStorageManager;
    }

    @EventHandler
    public void onSave(WorldSaveEvent event){
        if(!event.getWorld().equals(Bukkit.getWorlds().getFirst()))
            return;
        globalStorageManager.getPlugin().getLogger().info("The world is saving. Rewriting cache");
        globalStorageManager.writeAll();
    }

}
