package de.weinschenk.kikoEnergistics.util;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.util.Vector;

public class BlockUtil {

    public final static String IDENTIFICATOR = "KE-Chest";

    public static boolean isStorage(Sign sign){
        return PlainTextComponentSerializer.plainText().serialize(sign.getSide(Side.FRONT).line(0)).equalsIgnoreCase(IDENTIFICATOR) ||
                PlainTextComponentSerializer.plainText().serialize(sign.getSide(Side.BACK).line(0)).equalsIgnoreCase(IDENTIFICATOR);
    }

    public static Material getFilteringMaterial(Sign sign){
        try{
            String content = PlainTextComponentSerializer.plainText().serialize(sign.getSide(Side.FRONT).line(1));
            return getMaterial(content, true);
        } catch (IllegalArgumentException e) {
            try {
                String content = PlainTextComponentSerializer.plainText().serialize(sign.getSide(Side.BACK).line(1));
                return getMaterial(content, true);
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }
    }

    public static String getHolder(Sign sign){
        try{
            return PlainTextComponentSerializer.plainText().serialize(sign.getSide(Side.FRONT).line(2));
        } catch (IllegalArgumentException e) {
            try {
                return PlainTextComponentSerializer.plainText().serialize(sign.getSide(Side.FRONT).line(2));
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }
    }

    public static Material getMaterial(String material){
        return getMaterial(material, false);
    }

    public static Material getMaterial(String material, boolean deep){
        try {
            return Material.valueOf(material);
        } catch (IllegalArgumentException e) {
            if(deep) {
                for (Material value : Material.values())
                    if (value.toString().equalsIgnoreCase(material) || value.getKey().asString().equalsIgnoreCase(material) || value.toString().replace("_", " ").equalsIgnoreCase(material))
                        return value;
            } else
                for (Material value : Material.values())
                    if(value.toString().equalsIgnoreCase(material) || value.getKey().asString().equalsIgnoreCase(material))
                        return value;
        }
        return null;
    }

    public static final Vector[] POSITIONS = {
            new Vector(1, 0, 0),
            new Vector(-1, 0, 0),
            new Vector(0, 0, 1),
            new Vector(0, 0, -1)
    };

    public static Sign getNearestSign(Block block){
        for (Vector position : POSITIONS) {
            if(block.getLocation().clone().add(position).getBlock().getState() instanceof Sign sign && isStorage(sign))
                return sign;
        }
        return null;
    }

}