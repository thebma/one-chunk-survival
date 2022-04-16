package nl.midnan.onechunksurvival;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OneChunkSurvivalStartEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player invoker;
    private final Location location;

    public OneChunkSurvivalStartEvent(Player invoker, Location location) {
        this.invoker = invoker;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public Player getInvoker() {
        return invoker;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
