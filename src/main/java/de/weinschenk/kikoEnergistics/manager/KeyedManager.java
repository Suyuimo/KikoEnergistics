package de.weinschenk.kikoEnergistics.manager;

import de.weinschenk.kikoEnergistics.KikoEnergistics;

import java.util.HashMap;

public abstract class KeyedManager<K, V> extends AbstractManager {
    protected final HashMap<K, V> values = new HashMap<>();

    public KeyedManager(KikoEnergistics plugin) {
        super(plugin);
    }

    public HashMap<K, V> getAll() {
        return values;
    }

    public V get(K key) {
        return values.get(key);
    }
}
