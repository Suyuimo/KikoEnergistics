package de.weinschenk.kikoEnergistics.manager;

import de.weinschenk.kikoEnergistics.KikoEnergistics;

import java.util.List;

public abstract class CachedManager<T> extends AbstractManager {

    protected List<T> cache;

    protected CachedManager(KikoEnergistics plugin) {
        super(plugin);
    }

    public List<T> getAll(){
        if(cache != null)
            return cache;
        return fetchAll();
    }

    abstract void setAll(List<T> toSet);

    abstract List<T> fetchAll();

}
