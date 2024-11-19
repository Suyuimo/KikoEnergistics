package de.weinschenk.kikoEnergistics.manager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerStorageManager extends AbstractManager {
    private final GlobalStorageManager globalStorageManager;
    private final String playerName;
    private List<ItemStack> items;

    public PlayerStorageManager(GlobalStorageManager manager, String playerName, List<ItemStack> items) {
        super(manager.getPlugin());
        this.globalStorageManager = manager;
        this.playerName = playerName;
        this.items = items;
    }

    public boolean addItem(ItemStack itemStack){
        for (ItemStack stack : getItems()) {
            if(stack.isSimilar(itemStack)){
                stack.setAmount(stack.getAmount() + itemStack.getAmount());
                getGlobalStorageManager().getInventoryManager().updateAll(getPlayerName());
                return false;
            }
        }
        getItems().add(itemStack);
        getGlobalStorageManager().getInventoryManager().updateAll(getPlayerName());
        return true;
    }

    public void removeItem(ItemStack itemStack, int amount){
        if(amount >= itemStack.getAmount()){
            getItems().remove(itemStack);
        } else
            itemStack.setAmount(itemStack.getAmount() - amount);

        getGlobalStorageManager().getInventoryManager().updateAll(getPlayerName());
    }

    public List<ItemStack> getFilteredItems(Material type){
        return type == null ?
                this.getItems() :
                this.getItems().stream().filter(item->item.getType().equals(type)).toList();
    }

    public GlobalStorageManager getGlobalStorageManager() {
        return globalStorageManager;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayerStorageUnit toStorageUnit(){
        return new PlayerStorageUnit(playerName, items);
    }
}
