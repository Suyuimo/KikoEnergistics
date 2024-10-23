package de.weinschenk.kikoEnergistics;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;

/**
 * Custom Event, das ausgelöst wird, wenn ein Spieler in den Suchmodus versetzt wird.
 */
public class PlayerInSearchModeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private boolean isCancelled;

    public PlayerInSearchModeEvent(Player player) {
        this.player = player;
        this.isCancelled = false;
    }

    /**
     * Gibt den Spieler zurück, der in den Suchmodus versetzt wurde.
     *
     * @return Der betroffene Spieler.
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
