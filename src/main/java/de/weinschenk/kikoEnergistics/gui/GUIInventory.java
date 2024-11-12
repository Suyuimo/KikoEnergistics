package de.weinschenk.kikoEnergistics.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class GUIInventory implements InventoryHolder {
    protected Inventory inventory;

    abstract int getHeight();
    abstract void build();

    @Override
    public @NotNull Inventory getInventory(){
        return getInventory(false);
    }

    public Inventory getInventory(boolean forceBuild){
        if(forceBuild)
            build();
        return inventory;
    }


    /**
     *
     * @param column (x) starting with 1, if < 0, the position will be calculated from the end
     * @param row (y) starting with 1, if < 0, the position will be calculated from the end
     * @param item
     */
    public void setItem(int column, int row, ItemStack item){
        if(row < 0){
            row = getHeight() + (row) + 1;
        }
        if(column < 0){
            column = 9 + (column + 1);
        }
        getInventory(false).setItem(((row-1) * 9) + (column - 1), item);
    }

    public static int getX(int index){
        return index - getY(index)*9;
    }

    public static int getY(int index){
        return index/9;
    }

    public static boolean isInSquareZone(int x, int y, int startX, int startY, int endX, int endY){
        return (startX <= x && x <= endX) && (startY <= y && y <= endY);
    }

    public static boolean isInSquareZone(int index, int startX, int startY, int endX, int endY){
        return isInSquareZone(getX(index), getY(index), startX, startY, endX, endY);
    }

}
