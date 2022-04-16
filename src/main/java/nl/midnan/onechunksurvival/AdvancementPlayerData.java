package nl.midnan.onechunksurvival;

import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdvancementPlayerData {
    public Player player;

    private final List<String> completedAdvancements;

    public AdvancementPlayerData(JavaPlugin plugin, Player player, List<Advancement> allAdvancements) {
        this.player = player;
        plugin.getLogger().info("Created profile for " + player.getDisplayName());

        this.completedAdvancements = new ArrayList<String>();

        for(Advancement adv : allAdvancements) {

            boolean completed = completedAdvancement(adv);
            if(completed) {
                String achievementName = adv.getKey().toString().toLowerCase(Locale.ROOT);
                plugin.getLogger().info("User " + player.getDisplayName() + " initially completed " + achievementName);
                this.completedAdvancements.add(adv.getKey().toString().toLowerCase(Locale.ROOT));
            }
        }
    }

    public boolean completedAdvancement(Advancement advancement) {
        String advKey = advancement.getKey().toString().toLowerCase(Locale.ROOT);

        if(this.completedAdvancements.contains(advKey)) {
            return false;
        }

        AdvancementProgress advancementProgress = player.getAdvancementProgress(advancement);
        boolean isDone = advancementProgress.isDone();

        if(isDone) {
            this.completedAdvancements.add(advKey);
        }

        return isDone;
    }
}
