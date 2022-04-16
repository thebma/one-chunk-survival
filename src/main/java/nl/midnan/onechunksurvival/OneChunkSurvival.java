package nl.midnan.onechunksurvival;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class OneChunkSurvival extends JavaPlugin {
    private File configurationFile;
    private FileConfiguration configuration;

    @Override
    public void onEnable() {
        getLogger().info("One-chunk Survival Plugin Enabled!");
        getCommand("ocs-start").setExecutor(new OneChunkSurvivalStartCommandExecutor(this));
        getCommand("ocs-stop").setExecutor(new OneChunkSurvivalStopCommandExecutor(this));
    }

    public FileConfiguration getConfiguration() {
        return this.configuration;
    }

    private void loadOrCreateConfig()
    {
        configurationFile = new File(getDataFolder(), "ocs.yml");

        if(!configurationFile.exists()) {
            configurationFile.getParentFile().mkdirs();
            saveResource("ocs.yml", false);
        }

        configuration = new YamlConfiguration();
        try {
            configuration.load(configurationFile);
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().log(Level.ALL, "Could not load config...");
        }
    }


    @Override
    public void onDisable() {
        getLogger().info("One-chunk Survival Plugin Disabled!");
    }
}
