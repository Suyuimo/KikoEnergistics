package de.weinschenk.kikoEnergistics.gui;

import de.weinschenk.kikoEnergistics.manager.StorageManager;
import de.weinschenk.kikoEnergistics.util.FormatUtil;
import de.weinschenk.kikoEnergistics.util.InventoryUtil;
import de.weinschenk.kikoEnergistics.util.ItemBuilder;
import de.weinschenk.kikoEnergistics.util.MathUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StorageInventory extends GUIInventory {
    private int page;

    private final StorageManager storageManager;
    private Material filterMaterial;

    public StorageInventory(StorageManager storageManager, int page){
        this.storageManager = storageManager;
        this.page = page;
    }

    public StorageInventory(StorageManager storageManager, int page, Material material){
        this.storageManager = storageManager;
        this.page = page;
        filterMaterial = material;
    }

    @Override
    public int getHeight() {
        return 6;
    }

    @Override
    public void build(){
        if(inventory == null)
            inventory = Bukkit.createInventory(this,
                    9 * getHeight(),
                    InventoryUtil.generateGUITitle("KikoEnergistics"));
        else
            inventory.clear();
        if(getPage() > getLastPage())
            setPage(getLastPage(), false);

        fillItems();
        placeSlider();

        setItem(-1, -1, ItemBuilder.CRAFT);
        if(page!=0)
            setItem(-1, 1, ItemBuilder.ARROW_UP);
        if(page<getLastPage())
            setItem(-1, -2, ItemBuilder.ARROW_DOWN);
    }

    private void placeSlider(){
        setItem(-1, (int) MathUtil.map(getPage()+1, 1, getLastPage()+1, 2, 4), ItemBuilder.SLIDER);
    }

    public void fillItems(){
        List<ItemStack> filtered = getCurrentPageItems();
        for (int i = 0; i < filtered.size(); i++) {
            ItemStack item = filtered.get(i).asOne();
            getInventory().setItem(i + ((i / 8)), new ItemBuilder(item)
                    .displayName(FormatUtil.formatItemComponent(filtered.get(i)))
                    .build());
        }
    }

    public List<ItemStack> getFilteredItems(){
        return storageManager.getFilteredItems(getFilterMaterial());
    }

    public List<ItemStack> getCurrentPageItems(){
        List<ItemStack> result = getFilteredItems();
        return result.subList(page*8, getPage() == getLastPage() ? result.size() : page*8 + getHeight()*8);
    }

    public Material getFilterMaterial() {
        return filterMaterial;
    }

    public int getLastPage() {
        if(getFilteredItems().size() < 8*getHeight())
            return 0;
        return (int) Math.ceil(getFilteredItems().size()/8.0 - getHeight());
    }

    public ItemStack getItemInSlot(int slot){
        try{
            return getCurrentPageItems().get((slot - (slot+1)/9));
        } catch (Exception e) {
            return null;
        }
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page, boolean rebuild){
        this.page = page;
        if(rebuild)
            build();
    }

    public void updatePage(HumanEntity entity, int page){
        entity.openInventory(new StorageInventory(storageManager, page).getInventory(true));
    }

}
