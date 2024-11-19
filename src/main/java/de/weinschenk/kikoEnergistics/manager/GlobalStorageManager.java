package de.weinschenk.kikoEnergistics.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import de.weinschenk.kikoEnergistics.KikoEnergistics;
import de.weinschenk.kikoEnergistics.gui.InventoryManager;
import de.weinschenk.kikoEnergistics.util.ConfigurationSerializableAdapter;
import de.weinschenk.kikoEnergistics.util.FileUtil;
import de.weinschenk.kikoEnergistics.util.ItemStackAdapter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class GlobalStorageManager extends KeyedManager<String, PlayerStorageManager> {
    private final File storageFile;
    private final File storageBackupFile;

    private final StorageBackupManager backupManager;
    private final InventoryManager inventoryManager;

    public GlobalStorageManager(KikoEnergistics plugin) {
        super(plugin);
        storageFile = createStorageFile("storage.json");
        storageBackupFile = new File(getPlugin().getDataFolder() + File.separator + "storage-old.json");

        this.backupManager = new StorageBackupManager(this);
        plugin.getServer().getPluginManager().registerEvents(getBackupManager(), getPlugin());
        this.inventoryManager = new InventoryManager(getPlugin());
        fetchAll();
    }

    public void writeAll() {
        if(cache == null)
            return;
        File file = getStorageFile();
        if(FileUtil.isValid(file))
            FileUtil.copy(file, getStorageBackupFile());

        try(Writer writer = new FileWriter(file)){
            Bukkit.getLogger().info(Arrays.toString(cache.values().toArray()));
            GSON.toJson(cache.values().stream().map(PlayerStorageManager::toStorageUnit).toArray(), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, PlayerStorageManager> fetchAll() {
        File file = getStorageFile();
        try(Reader reader = new FileReader(file)){
            PlayerStorageUnit[] result = GSON.fromJson(reader, PlayerStorageUnit[].class);
            if(result == null)
                cache = new HashMap<>();
            else {
                cache = new HashMap<>();
                for (PlayerStorageUnit playerStorageUnit : result) {
                    cache.put(playerStorageUnit.playerName(), new PlayerStorageManager(this, playerStorageUnit.playerName(), playerStorageUnit.items()));
                }
            }
            return cache;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerStorageManager fetch(String key) {
        if(cache.containsKey(key))
            return cache.get(key);
        if(Bukkit.getPlayerExact(key) != null) {
            PlayerStorageManager result = new PlayerStorageManager(this, key, new ArrayList<>());
            cache.put(key, result);
            return result;
        }
        return null;
    }

    public File getStorageFile(){
        return storageFile;
    }

    public File getStorageBackupFile(){
        return storageBackupFile;
    }

    public File createStorageFile(String name){
        File file = new File(getPlugin().getDataFolder() + File.separator + name);
        try {
            if(!file.exists()) {
                Files.createDirectories(file.getParentFile().toPath());
                file.createNewFile();
            }
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public StorageBackupManager getBackupManager() {
        return backupManager;
    }

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .setObjectToNumberStrategy(JsonReader::nextInt)
            .registerTypeHierarchyAdapter(ConfigurationSerializable.class, new ConfigurationSerializableAdapter())
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
            .create();

}