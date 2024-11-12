package de.weinschenk.kikoEnergistics.util;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;

import java.io.*;
import java.nio.channels.FileChannel;

public class FileUtil {

    public static void copy(File from, File to){
        try(FileChannel src = new FileInputStream(from).getChannel();
            FileChannel destination = new FileOutputStream(to).getChannel();) {
            destination.transferFrom(src, 0, src.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isValid(File file) {
        return file.length() > 0 && isValidJson(file);
    }

    public static boolean isValidJson(File file) {
        try (FileReader reader = new FileReader(file)) {
            JsonParser.parseReader(reader);
            return true;
        } catch (IOException | JsonParseException e) {
            return false;
        }
    }
}
