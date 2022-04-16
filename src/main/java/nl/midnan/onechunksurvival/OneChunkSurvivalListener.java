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
    private Map<String, Boolean> advancementMapping;
    private List<Advancement> allAdvancements;
    private final List<AdvancementPlayerData> playerAdvancementData = new ArrayList<>();

    public OneChunkSurvivalListener(JavaPlugin plugin) {
        this.plugin = plugin;

        BukkitScheduler sched = this.plugin.getServer().getScheduler();
        sched.scheduleSyncRepeatingTask(plugin, this::doAdvancementCheckLogic, 0, 40);
    }

    private void getAllAdvancements() {
        advancementMapping = new HashMap<>();
        List<String> eligibleNamespace = this.plugin.getConfig().getStringList("advancement_namespaces");
        List<Advancement> eligibleAdvancements = new ArrayList<>();
        Iterator<Advancement> advancementIterator = plugin.getServer().advancementIterator();
        while(advancementIterator.hasNext()) {
            Advancement advancement = advancementIterator.next();

            for(String namespace : eligibleNamespace) {
                String advancementKey = advancement.getKey().toString().toLowerCase(Locale.ROOT);
                if(advancementKey.contains(namespace)) {
                    eligibleAdvancements.add(advancement);
                    advancementMapping.put(advancementKey, false);
                    break;
                }
            }
        }

        plugin.getLogger().info("Registered " + eligibleAdvancements.size() + " advancements(s)");
        allAdvancements = eligibleAdvancements;
    }


    @EventHandler
    public void onOneChunkSurvivalStartEvent(OneChunkSurvivalStartEvent startEvent) {
        getAllAdvancements();

        Player invoker = startEvent.getInvoker();
        Location location = startEvent.getLocation();

        String locationStr = location.getX() + " " + location.getZ();
        invoker.performCommand("worldborder center " + locationStr);

        int borderStart = plugin.getConfig().getInt("border.start");
        invoker.performCommand("worldborder set " + borderStart);

        plugin.getLogger().info("Teleporting all players.");
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            player.teleport(location);
        }
        this.isRunning = true;
    }

    @EventHandler
    public void onOneChunkSurvivalStopEvent(OneChunkSurvivalStopEvent stopEvent) {
        Player invoker = stopEvent.getInvoker();
        invoker.performCommand("worldborder set 9999999");
        invoker.performCommand("worldborder center 0 0");
        this.isRunning = false;
    }

    private void doAdvancementCheckLogic() {
        if(!this.isRunning) return;

        for(Player player : plugin.getServer().getOnlinePlayers()) {

            for(Advancement advancementInQuestion : allAdvancements) {

                String advancementKey = advancementInQuestion.getKey().toString().toLowerCase(Locale.ROOT);
                AdvancementPlayerData data = getPlayerAdvancementData(player);
                if(data.completedAdvancement(advancementInQuestion)) {

                    boolean didSharedComplete = advancementMapping.get(advancementKey);
                    if(!didSharedComplete) {
                        onSharedAdvancementComplete(player, advancementInQuestion);
                        this.plugin.getLogger().info("Advancement " + advancementKey + " completed by " + data.player.getDisplayName());
                    }
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
                this.plugin, player, allAdvancements
        );

        playerAdvancementData.add(advancementPlayerData);
        return advancementPlayerData;
    }

    private void onSharedAdvancementComplete(Player completer, Advancement advancement) {
        plugin.getServer().broadcastMessage(
                "Advancement " + ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + advancement.getKey() + ChatColor.DARK_GREEN + "]" + ChatColor.WHITE +
                " was completed by " + ChatColor.DARK_RED + "[" + ChatColor.GREEN +completer.getDisplayName() + ChatColor.DARK_RED + "]" + ChatColor.WHITE
        );

        int borderGrowRate = this.plugin.getConfig().getInt("border.grow");
        int borderSpeed = this.plugin.getConfig().getInt("border.speed");
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "worldborder add " + borderGrowRate + " " + borderSpeed);
    }
}