package de.weinschenk.kikoEnergistics.listeners;

import de.weinschenk.kikoEnergistics.KikoEnergistics;
import de.weinschenk.kikoEnergistics.gui.StorageInventory;
import de.weinschenk.kikoEnergistics.manager.PlayerStorageManager;
import de.weinschenk.kikoEnergistics.util.BlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.HopperInventorySearchEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HopperListener implements Listener {

    private final KikoEnergistics plugin;

    public HopperListener(KikoEnergistics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(HopperInventorySearchEvent event){
        if(event.getBlock().getState() instanceof Hopper hopper && event.getContainerType().equals(HopperInventorySearchEvent.ContainerType.SOURCE)){
//            if(hopper.getTransferCooldown() > 0)
//                return;

            Sign sign = BlockUtil.getNearestSign(event.getSearchBlock());
            if(sign == null)
                return;
            Material material = BlockUtil.getFilteringMaterial(sign);

            String holder = BlockUtil.getHolder(sign);
            PlayerStorageManager manager = plugin.getStorageManager().fetch(holder);
            if(manager == null)
                return;
            List<ItemStack> filtered = manager.getFilteredItems(material);
            if(filtered.isEmpty())
                return;

            ItemStack targetItem = getTargetItem(hopper.getInventory());
            if(targetItem == null)
                return;
            ItemStack targetStorageItem = targetItem.isEmpty() ? targetItem : getSimilar(filtered, targetItem);
            if(targetStorageItem == null)
                return;

            Bukkit.getScheduler().runTask(plugin, ()->{
                if(targetStorageItem.isEmpty()){
                    hopper.getInventory().addItem(filtered.getFirst().asOne());
                    manager.removeItem(filtered.getFirst(), 1);
                }
                hopper.getInventory().addItem(targetStorageItem.asOne());
                manager.removeItem(targetStorageItem, 1);
            });
            hopper.setTransferCooldown(8);
            hopper.update();
        }
    }

    private ItemStack getSimilar(List<ItemStack> list, ItemStack similar){
        for (ItemStack itemStack : list) {
            if(itemStack.isSimilar(similar))
                return itemStack;
        }
        return null;
    }

    private ItemStack getTargetItem(Inventory inventory){
        for (ItemStack itemStack : inventory) {
            if(itemStack == null || itemStack.isEmpty())
                return new ItemStack(Material.AIR);
        }
        for (ItemStack itemStack : inventory) {
            if(itemStack.isEmpty() || itemStack.getAmount() < itemStack.getMaxStackSize())
                return itemStack;
        }
        return null;
    }

    @EventHandler
    public void onTransfer(InventoryMoveItemEvent event){
        if(event.getSource().getHolder() instanceof Hopper hopper)
            if(event.getDestination().getHolder() instanceof Chest chest)
                if(BlockUtil.getNearestSign(chest.getBlock()) != null)
                    event.setCancelled(true);

        if(event.getDestination().getHolder() instanceof Hopper hopper)
            if(event.getSource().getHolder() instanceof Chest chest)
                if(BlockUtil.getNearestSign(chest.getBlock()) != null)
                    event.setCancelled(true);
    }

}