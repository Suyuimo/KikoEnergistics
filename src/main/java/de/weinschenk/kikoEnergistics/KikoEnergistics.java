package de.weinschenk.kikoEnergistics;

import de.weinschenk.kikoEnergistics.commands.KECommand;
import de.weinschenk.kikoEnergistics.listeners.ChestInteractListener;
import de.weinschenk.kikoEnergistics.listeners.HopperListener;
import de.weinschenk.kikoEnergistics.listeners.InventoryListener;
import de.weinschenk.kikoEnergistics.listeners.PlayerJoinListener;
import de.weinschenk.kikoEnergistics.manager.StorageManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class KikoEnergistics extends JavaPlugin {

    private static KikoEnergistics instance;
    private StorageManager storageManager;

    @Override
    public void onEnable() {
        instance = this;
        storageManager = new StorageManager(this);

        registerEvents();
        registerCommands();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new HopperListener(this), this);
        getServer().getPluginManager().registerEvents(new ChestInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    private void registerCommands() {
        KECommand keCommand = new KECommand(this);
        getCommand("kikoenergistics").setTabCompleter(keCommand);
        getCommand("kikoenergistics").setExecutor(keCommand);
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public static KikoEnergistics getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getStorageManager().writeAll();
    }
}
