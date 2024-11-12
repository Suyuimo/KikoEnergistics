package de.weinschenk.kikoEnergistics.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import de.weinschenk.kikoEnergistics.KikoEnergistics;
import de.weinschenk.kikoEnergistics.gui.InventoryManager;
import de.weinschenk.kikoEnergistics.util.ConfigurationSerializableAdapter;
import de.weinschenk.kikoEnergistics.util.FileUtil;
import de.weinschenk.kikoEnergistics.util.ItemStackAdapter;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StorageManager extends CachedManager<ItemStack> {

    private final File storageFile;
    private final File storageBackupFile;

    private final StorageBackupManager backupManager;
    private final InventoryManager inventoryManager;

    public StorageManager(KikoEnergistics plugin) {
        super(plugin);
        storageFile = createStorageFile("storage.json");
        storageBackupFile = new File(getPlugin().getDataFolder() + File.separator + "storage-old.json");

        this.backupManager = new StorageBackupManager(this);
        plugin.getServer().getPluginManager().registerEvents(getBackupManager(), getPlugin());
        this.inventoryManager = new InventoryManager(getPlugin());
    }

    @Override
    public void setAll(List<ItemStack> items) {
        cache = items;
        writeAll();
    }

    public void writeAll() {
        if(cache == null)
            return;
        File file = getStorageFile();
        if(FileUtil.isValid(file))
            FileUtil.copy(file, getStorageBackupFile());

        try(Writer writer = new FileWriter(file)){
//            Files.copy(file.toPath(), getStorageBackupFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
            GSON.toJson(getAll().toArray(), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addItem(ItemStack itemStack){
        for (ItemStack stack : getAll()) {
            if(stack.isSimilar(itemStack)){
                stack.setAmount(stack.getAmount() + itemStack.getAmount());
                getInventoryManager().updateAll();
                return false;
            }
        }
        getAll().add(itemStack);
        getInventoryManager().updateAll();
        return true;
    }

    public void removeItem(ItemStack itemStack, int amount){
        if(amount >= itemStack.getAmount()){
            getAll().remove(itemStack);
        } else
            itemStack.setAmount(itemStack.getAmount() - amount);

        getInventoryManager().updateAll();
    }

    public List<ItemStack> getFilteredItems(Material type){
        return type == null ?
                this.getAll() :
                this.getAll().stream().filter(item->item.getType().equals(type)).toList();
    }

    @Override
    public List<ItemStack> fetchAll() {
        File file = getStorageFile();
        try(Reader reader = new FileReader(file)){
            ItemStack[] result = GSON.fromJson(reader, ItemStack[].class);
            if(result == null)
                cache = new ArrayList<>();
            else
                cache = new ArrayList<>(Arrays.asList(result));
            return cache;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
