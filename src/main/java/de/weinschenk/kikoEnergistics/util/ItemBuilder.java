package de.weinschenk.kikoEnergistics.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemBuilder {

    private Material material;
    private int durability;

    private Component displayName;
    private List<Component> lore;

    private int customModelData;
    private ItemFlag[] itemFlags;

    private int maxStackSize;

    public ItemBuilder(Material material){
        this.material = material;
    }

    public ItemBuilder(ItemStack item){
        this.material = item.getType();
        if(!item.hasItemMeta())
            return;

        if(item.getItemMeta() instanceof Damageable damageable)
            durability = damageable.getDamage();
        if(item.getItemMeta().hasDisplayName())
            this.displayName = item.getItemMeta().displayName();
        if(item.getItemMeta().hasLore())
            this.lore = item.getItemMeta().lore();

        if(item.getItemMeta().hasCustomModelData())
            this.customModelData = item.getItemMeta().getCustomModelData();
        this.itemFlags = item.getItemFlags().toArray(new ItemFlag[0]);
        if(item.getItemMeta().hasMaxStackSize())
            this.maxStackSize = item.getItemMeta().getMaxStackSize();
    }

    public ItemStack build(){
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if(customModelData!=0) meta.setCustomModelData(customModelData);
        if(lore!=null && !lore.isEmpty()) meta.lore(lore);
        if(itemFlags!=null&&itemFlags.length>0) meta.addItemFlags(itemFlags);
        if(maxStackSize>0) meta.setMaxStackSize(maxStackSize);
        if(meta instanceof Damageable damageable && durability>0) damageable.setDamage(durability);
        meta.displayName(displayName);

        item.setItemMeta(meta);

        return item;
    }

    public ItemBuilder displayName(Component name){
        this.displayName=name;
        return this;
    }

    public ItemBuilder displayName(String name){
        this.displayName=Component.text(name);
        return this;
    }

    public ItemBuilder maxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }


    public ItemBuilder lore(List<Component> lore){
        this.lore = lore;
        return this;
    }

    public ItemBuilder customModelData(int customModelData){
        this.customModelData = customModelData;
        return this;
    }

    public ItemBuilder hideFlags(ItemFlag[] flags){
        this.itemFlags = flags;
        return this;
    }

    //STATIC ITEMS

    public static ItemStack air(){
        return new ItemStack(Material.AIR);
    }

    public static ItemStack emptyInventoryItem(){
        return ItemBuilder.buildItem(Material.CYAN_STAINED_GLASS_PANE, Component.empty());
    }


    //STATIC BUILDERS

    public static ItemStack buildSkullItem(PlayerProfile profile){
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setPlayerProfile(profile);
        skullMeta.displayName(Component.text(profile.getName()).style(Style.style(TextColor.color(0xFFFF55))));
        skull.lore(List.of(Component.text("Click to unban player!").color(TextColor.color(0x61687A))));
        skull.setItemMeta(skullMeta);

        return skull;
    }

    public static ItemStack buildItem(Material material, Component displayName, List<Component> lore, int customModelData){
        ItemBuilder builder = new ItemBuilder(material);
        builder.displayName(displayName).customModelData(customModelData).lore(lore);
        return builder.build();
    }

    public static ItemStack buildItem(Material material, Component displayName, int customModelData){
        ItemBuilder builder = new ItemBuilder(material);
        builder.displayName(displayName).customModelData(customModelData);
        return builder.build();
    }

    public static ItemStack buildItem(Material material, Component displayName){
        ItemBuilder builder = new ItemBuilder(material);
        builder.displayName(displayName);
        return builder.build();
    }

    public static ItemStack buildGUIItem(Component displayName, int customModelData){
        ItemBuilder builder = new ItemBuilder(Material.ACACIA_SIGN);
        builder.displayName(displayName)
                .maxStackSize(1)
                .customModelData(customModelData);
        return builder.build();
    }

    public static ItemStack ARROW_UP = buildGUIItem(Component.text("Up").decoration(TextDecoration.ITALIC, false), 5001);
    public static ItemStack ARROW_DOWN = buildGUIItem(Component.text("Down").decoration(TextDecoration.ITALIC, false), 5000);
    public static ItemStack SLIDER = buildGUIItem(Component.text("Slider").decoration(TextDecoration.ITALIC, false), 6000);
    public static ItemStack CRAFT = buildGUIItem(Component.text("Open Crafting Table").decoration(TextDecoration.ITALIC, false), 7000);

}
