package de.weinschenk.kikoEnergistics.listeners;

import de.weinschenk.kikoEnergistics.KikoEnergistics;
import de.weinschenk.kikoEnergistics.gui.StorageInventory;
import de.weinschenk.kikoEnergistics.manager.PlayerStorageManager;
import de.weinschenk.kikoEnergistics.util.BlockUtil;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

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
            String holder = BlockUtil.getHolder(sign);
            PlayerStorageManager manager = plugin.getStorageManager().fetch(holder);
            if(manager == null)
                manager = plugin.getStorageManager().fetch(event.getPlayer().getName());
            plugin.getStorageManager().getInventoryManager().openInventory(
                    event.getPlayer(), new StorageInventory(manager, 0, material));
        }
    }

}
