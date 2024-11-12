package de.weinschenk.kikoEnergistics.listeners;

import de.weinschenk.kikoEnergistics.KikoEnergistics;
import de.weinschenk.kikoEnergistics.gui.StorageInventory;
import de.weinschenk.kikoEnergistics.util.BlockUtil;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestInteractListener implements Listener {

    private final KikoEnergistics plugin;

    public ChestInteractListener(KikoEnergistics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event){
        if(event.getAction().isRightClick() &&
                !event.getPlayer().isSneaking() &&
                event.getClickedBlock() != null &&
                event.getClickedBlock().getType().equals(Material.CHEST)){
            Sign sign = BlockUtil.getNearestSign(event.getClickedBlock());
            if(sign == null)
                return;

            event.setCancelled(true);
            Material material = BlockUtil.getFilteringMaterial(sign);
            plugin.getStorageManager().getInventoryManager().openInventory(
                    event.getPlayer(), new StorageInventory(plugin.getStorageManager(), 0, material));
        }
    }

}
