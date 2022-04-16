package nl.midnan.onechunksurvival;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.bukkit.plugin.java.JavaPlugin;

public class OneChunkSurvival extends JavaPlugin {

    final Logger logger = LoggerFactory.getLogger(OneChunkSurvival.class);

    @Override
    public void onEnable() {
        logger.debug("One-chunk Survival Plugin Enabled!");
        getCommand("ocs-start").setExecutor(new OneChunkSurvivalStartCommandExecutor(this));
        getCommand("ocs-stop").setExecutor(new OneChunkSurvivalStopCommandExecutor(this));
    }

    @Override
    public void onDisable() {
        logger.debug("One-chunk Survival Plugin Disabled!");
    }
}
