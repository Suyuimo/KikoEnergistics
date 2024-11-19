package de.weinschenk.kikoEnergistics;

import de.weinschenk.kikoEnergistics.commands.KECommand;
import de.weinschenk.kikoEnergistics.listeners.ChestInteractListener;
import de.weinschenk.kikoEnergistics.listeners.HopperListener;
import de.weinschenk.kikoEnergistics.listeners.InventoryListener;
import de.weinschenk.kikoEnergistics.listeners.PlayerJoinListener;
import de.weinschenk.kikoEnergistics.manager.GlobalStorageManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class KikoEnergistics extends JavaPlugin {

    private static KikoEnergistics instance;
    private GlobalStorageManager globalStorageManager;

    @Override
    public void onEnable() {
        instance = this;
        globalStorageManager = new GlobalStorageManager(this);

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

    public GlobalStorageManager getStorageManager() {
        return globalStorageManager;
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