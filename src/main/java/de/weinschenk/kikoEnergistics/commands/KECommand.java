package de.weinschenk.kikoEnergistics.commands;

import de.weinschenk.kikoEnergistics.KikoEnergistics;
import de.weinschenk.kikoEnergistics.gui.StorageInventory;
import de.weinschenk.kikoEnergistics.util.BlockUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class KECommand implements CommandExecutor, TabCompleter {

    private final KikoEnergistics plugin;

    public KECommand(KikoEnergistics plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player){
            if(args.length == 0){
                plugin.getStorageManager().getInventoryManager().openInventory(
                        player, new StorageInventory(plugin.getStorageManager().fetch(player.getName()), 0));
            } else {
                plugin.getStorageManager().getInventoryManager().openInventory(
                        player, new StorageInventory(plugin.getStorageManager().fetch(player.getName()), 0, BlockUtil.getMaterial(args[0])));
            }
            return true;
        } else
            sender.sendMessage("Only players can use the command!");
        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1){
            return Arrays.stream(Material.values()).map(material->material.toString().toLowerCase()).toList();
        }
        return List.of();
    }
}
