package de.weinschenk.kikoEnergistics.listeners;

import de.weinschenk.kikoEnergistics.KikoEnergistics;
import de.weinschenk.kikoEnergistics.gui.GUIInventory;
import de.weinschenk.kikoEnergistics.gui.StorageInventory;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

public class InventoryListener implements Listener{

    private final KikoEnergistics plugin;

    public InventoryListener(KikoEnergistics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getInventory().getHolder() instanceof StorageInventory inventory && event.getWhoClicked() instanceof Player player){
            if(event.getClickedInventory() != null && event.getClickedInventory().getHolder() instanceof StorageInventory) {
                event.setCancelled(true);
                if(event.getSlot() == 8 && inventory.getPage()!=0) {
                    inventory.setPage(inventory.getPage() - 1, true);
                    event.getWhoClicked().playSound(Sound.sound(org.bukkit.Sound.UI_BUTTON_CLICK, Sound.Source.MASTER, 0.6f, 1));
                    return;
                } else if(event.getSlot() == 44 && inventory.getPage()!=inventory.getLastPage()) {
                    inventory.setPage(inventory.getPage() + 1, true);
                    event.getWhoClicked().playSound(Sound.sound(org.bukkit.Sound.UI_BUTTON_CLICK, Sound.Source.MASTER, 0.6f, 1));
                    return;
                } else if(event.getSlot() == 53) {
                    event.getWhoClicked().openWorkbench(event.getWhoClicked().getLocation(), true);
                    return;
                }

                int x = GUIInventory.getX(event.getSlot());
                if(x!=8){
                    ItemStack itemStack = inventory.getItemInSlot(event.getSlot());
                    if(itemStack == null)
                        return;

                    if(!event.getCursor().getType().isAir() && (!event.getCursor().isSimilar(itemStack) || event.getCursor().getAmount() >= event.getCursor().getMaxStackSize()))
                        return;

                    if(event.getClick().isShiftClick()){
                        int toRemove = itemStack.getMaxStackSize();
                        for (Map.Entry<Integer, ItemStack> item : event.getWhoClicked().getInventory().addItem(itemStack.getAmount() <= itemStack.getMaxStackSize() ? itemStack.clone() : itemStack.asQuantity(itemStack.getMaxStackSize())).entrySet())
                            toRemove -= item.getValue().getAmount();
                        plugin.getStorageManager().removeItem(itemStack, toRemove);
                        return;
                    }

                    Bukkit.getScheduler().runTask(plugin, ()->{
                        int toRemove;
                        if(event.getClick().isLeftClick()) {
                            toRemove = Math.min(itemStack.getAmount(), itemStack.getMaxStackSize());
                            if (event.getCursor().getAmount() + itemStack.getAmount() > itemStack.getMaxStackSize()) {
                                toRemove -= event.getCursor().getAmount();
                                event.getWhoClicked().setItemOnCursor(itemStack.asQuantity(itemStack.getMaxStackSize()));
                            } else {
                                event.getWhoClicked().setItemOnCursor(itemStack.clone().add(event.getCursor().getAmount()));
                            }
                        } else if(event.getClick().isRightClick()) {
                            toRemove = 1;
                            event.getWhoClicked().setItemOnCursor(event.getCursor().getType().isAir() ?
                                    itemStack.asOne() :
                                    event.getCursor().add());
                        } else
                            return;

                        plugin.getStorageManager().removeItem(itemStack, toRemove);
                    });
                }
            } else if(event.getClickedInventory() instanceof PlayerInventory){
                if(event.getClick().isShiftClick() && event.getCurrentItem() != null) {
                    event.setCancelled(true);
                    if(plugin.getStorageManager().addItem(event.getCurrentItem().clone()))
                        inventory.setPage(inventory.getLastPage(), false);
                    event.getCurrentItem().setAmount(0);
                    inventory.build();
                }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event){
        if(event.getInventory().getHolder() instanceof StorageInventory)
            event.setCancelled(true);
    }

}
