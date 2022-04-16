package nl.midnan.onechunksurvival;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class OneChunkSurvivalListener implements Listener {
    private final JavaPlugin plugin;

    private boolean isRunning = false;
    private Player invoker = null;
    private Map<Advancement, Boolean> advancementMapping;
    private List<Advancement> allAchievementsCached;
    private final List<AdvancementPlayerData> playerAdvancementData = new ArrayList<>();

    public OneChunkSurvivalListener(JavaPlugin plugin) {
        this.plugin = plugin;

        getAllAdvancements();

        BukkitScheduler sched = this.plugin.getServer().getScheduler();
        sched.scheduleSyncRepeatingTask(plugin, this::doAdvancementCheckLogic, 0, 40);
    }

    @EventHandler
    public void onOneChunkSurvivalStartEvent(OneChunkSurvivalStartEvent startEvent) {
        this.invoker = startEvent.getInvoker();
        Location location = startEvent.getLocation();

        String locationStr = location.getX() + " " + location.getZ();
        this.invoker.performCommand("worldborder center " + locationStr);

        int borderStart = plugin.getConfig().getInt("border.start");
        this.invoker.performCommand("worldborder set " + borderStart);

        teleportAllPlayers(location);

        this.isRunning = true;
    }

    @EventHandler
    public void onOneChunkSurvivalStopEvent(OneChunkSurvivalStopEvent stopEvent) {
        this.invoker = stopEvent.getInvoker();
        this.invoker.performCommand("worldborder set 9999999");
        this.invoker.performCommand("worldborder center 0 0");
        this.isRunning = false;
    }

    private void teleportAllPlayers(Location location) {
        plugin.getLogger().info("Teleporting all players.");
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            player.teleport(location);
        }
    }

    private void getAllAdvancements() {
        List<Advancement> allAchievements = new ArrayList<>();
        Iterator<Advancement> advancementIterator = plugin.getServer().advancementIterator();
        while(advancementIterator.hasNext()) {
            Advancement advancement = advancementIterator.next();
            allAchievements.add(advancement);
        }

        plugin.getLogger().info("Registered " + allAchievements.size() + " achievement(s)");
        allAchievementsCached = allAchievements;
    }

    private void doAdvancementCheckLogic() {
        if(!this.isRunning) return;

        for(Player player : plugin.getServer().getOnlinePlayers()) {
            Iterator<Advancement> advancementIterator = plugin.getServer().advancementIterator();

            while(advancementIterator.hasNext()) {
                Advancement advancementInQuestion = advancementIterator.next();
                AdvancementPlayerData data = getPlayerAdvancementData(player);

                if(data.completedAdvancement(advancementInQuestion)) {
                    this.plugin.getLogger().info("Advancement " + advancementInQuestion.getKey() + " completed by " + data.player.getDisplayName());
                }
            }
        }
    }

    private AdvancementPlayerData getPlayerAdvancementData(Player player) {
        String playerDisplayName = player.getDisplayName().toLowerCase(Locale.ROOT);
        for(AdvancementPlayerData data : playerAdvancementData) {
            String playerDataUsername = data.player.getDisplayName().toLowerCase(Locale.ROOT);

            if(playerDisplayName.equalsIgnoreCase(playerDataUsername)) {
                return data;
            }
        }

        AdvancementPlayerData advancementPlayerData = new AdvancementPlayerData(
                this.plugin, player, allAchievementsCached
        );

        playerAdvancementData.add(advancementPlayerData);
        return advancementPlayerData;
    }

    private void newAchievementCompleted(Player completer, Advancement advancement) {
        advancementMapping.put(advancement, true);
        plugin.getServer().broadcastMessage(ChatColor.DARK_RED + "Advancement " + advancement.getKey() + " was completed by " + completer.getDisplayName());
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "worldborder add 16 1");
    }
}