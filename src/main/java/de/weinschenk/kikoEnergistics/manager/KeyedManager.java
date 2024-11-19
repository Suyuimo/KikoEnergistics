package de.weinschenk.kikoEnergistics.manager;

import de.weinschenk.kikoEnergistics.KikoEnergistics;

import java.util.HashMap;

public abstract class KeyedManager<K, V> extends AbstractManager {
    protected HashMap<K, V> cache = new HashMap<>();

    public KeyedManager(KikoEnergistics plugin) {
        super(plugin);
    }

    public HashMap<K, V> getAll() {
        return cache;
    }

    public V get(K key) {
        return cache.get(key);
    }

    abstract public V fetch(K key);
}
