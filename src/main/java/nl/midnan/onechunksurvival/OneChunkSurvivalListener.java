package nl.midnan.onechunksurvival;

import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OneChunkSurvivalListener implements Listener {
    private final JavaPlugin plugin;

    private boolean isRunning = false;
    private Player invoker = null;
    private Location location = null;
    private Map<Advancement, Boolean> advancementMapping;

    public OneChunkSurvivalListener(JavaPlugin plugin) {
        this.plugin = plugin;

        BukkitScheduler sched = this.plugin.getServer().getScheduler();
        sched.scheduleSyncRepeatingTask(plugin, this::doAdvancementCheckLogic, 0, 120);
    }

    @EventHandler
    public void onOneChunkSurvivalStartEvent(OneChunkSurvivalStartEvent startEvent) {
        initializeAchievements();
        this.invoker = startEvent.getInvoker();
        this.location = startEvent.getLocation();

        this.invoker.performCommand("worldborder center ~ ~ ~");
        this.invoker.performCommand("worldborder set 16 0");
        teleportAllPlayers(this.location);

        this.isRunning = true;
    }

    @EventHandler
    public void onOneChunkSurvivalStopEvent(OneChunkSurvivalStopEvent stopEvent) {
        if(this.invoker == stopEvent.getInvoker()) {
            this.invoker.performCommand("worldborder set 30000000 0");
            this.isRunning = false;
        }
    }

    private void teleportAllPlayers(Location location) {
        plugin.getLogger().info("Teleporting all players.");
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            player.teleport(location);
        }
    }

    private void initializeAchievements() {
        advancementMapping = new HashMap<>();
        Iterator<Advancement> advancementIterator = plugin.getServer().advancementIterator();
        while(advancementIterator.hasNext()) {
            Advancement adv = advancementIterator.next();
            advancementMapping.put(adv, false);
        }
    }

    private void doAdvancementCheckLogic() {
        if(!this.isRunning) return;

        plugin.getLogger().info("Checking for new advancements.");
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            Iterator<Advancement> advancementIterator = plugin.getServer().advancementIterator();

            while(advancementIterator.hasNext()) {
                Advancement advancementInQuestion = advancementIterator.next();
                boolean playerCompletedAdvancement = checkAdvancementOnPlayer(player, advancementInQuestion);

                if(playerCompletedAdvancement) {
                    boolean wasCompleted = advancementMapping.get(advancementInQuestion);

                    if(!wasCompleted) {
                        newAchievementCompleted(player, advancementInQuestion);
                    }
                }
            }
        }
    }

    private boolean checkAdvancementOnPlayer(Player player, Advancement advancement) {
        AdvancementProgress advancementProgress = player.getAdvancementProgress(advancement);
        return advancementProgress.isDone();
    }

    private void newAchievementCompleted(Player completer, Advancement advancement) {
        advancementMapping.put(advancement, true);
        plugin.getServer().broadcastMessage("Advancement " + advancement.getKey() + " was completed by " + completer.getDisplayName());
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "worldborder add 16 5");
    }
}