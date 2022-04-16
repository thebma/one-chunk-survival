package nl.midnan.onechunksurvival;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OneChunkSurvivalStopEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player invoker;

    public OneChunkSurvivalStopEvent(Player invoker) {
        this.invoker = invoker;
    }

    public Player getInvoker() {
        return invoker;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
