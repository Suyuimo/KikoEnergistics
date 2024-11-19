package de.weinschenk.kikoEnergistics.manager;

import com.google.gson.annotations.Expose;
import de.weinschenk.kikoEnergistics.KikoEnergistics;

public abstract class AbstractManager {

    private final KikoEnergistics plugin;

    public AbstractManager(KikoEnergistics plugin) {
        this.plugin = plugin;
    }

    public KikoEnergistics getPlugin() {
        return plugin;
    }

}
