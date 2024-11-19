package de.weinschenk.kikoEnergistics.util;

public class MathUtil {

    public static long map(long x, long in_min, long in_max, long out_min, long out_max) {
        if(in_max - in_min == 0)
            return out_min;
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

}