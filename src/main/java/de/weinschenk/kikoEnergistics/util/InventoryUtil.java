package de.weinschenk.kikoEnergistics.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Color;

public class InventoryUtil {

    public static Component generateGUITitle(String title){
        return Component.text("\uEE00"+"\uEC01\uEE01", TextColor.color(0xffffff))
                .append(Component.text(title, TextColor.color(0x555555)));
    }

}
