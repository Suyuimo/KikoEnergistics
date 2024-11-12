package de.weinschenk.kikoEnergistics.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public class FormatUtil {

    public static Component formatItemComponent(ItemStack itemStack){
        return getDisplayName(itemStack)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                .append(Component.text(" x" + itemStack.getAmount(),
                        Style.style(TextColor.color(Color.GRAY.asRGB())).decoration(TextDecoration.ITALIC, false)));
    }

    private static Component getDisplayName(ItemStack itemStack){
        return itemStack.getItemMeta().hasDisplayName() ? itemStack.getItemMeta().displayName().colorIfAbsent(TextColor.color(0xffffff)) :
                Component.translatable(itemStack.translationKey()).colorIfAbsent(TextColor.color(0xffffff));
    }

}
