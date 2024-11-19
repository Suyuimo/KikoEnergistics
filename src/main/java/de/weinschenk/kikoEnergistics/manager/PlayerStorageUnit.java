package de.weinschenk.kikoEnergistics.manager;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public record PlayerStorageUnit(String playerName, List<ItemStack> items) {
}
