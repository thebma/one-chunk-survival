package nl.midnan.onechunksurvival;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

public class OneChunkSurvival extends JavaPlugin {
    private FileConfiguration configuration;

    @Override
    public void onEnable() {
        loadOrCreateConfig();
        getLogger().info("One-chunk Survival Plugin Enabled!");
        getCommand("ocs-start").setExecutor(new OneChunkSurvivalStartCommandExecutor(this));
        getCommand("ocs-stop").setExecutor(new OneChunkSurvivalStopCommandExecutor(this));
    }

    public FileConfiguration getConfiguration() {
        return this.configuration;
    }

    private void loadOrCreateConfig()
    {
        File configurationFile = new File(getDataFolder(), "ocs.yml");

        if(!configurationFile.exists()) {
            getLogger().info("Wrote config!");
            configurationFile.getParentFile().mkdirs();

            //Touch file...
            try {
                new FileOutputStream(configurationFile).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        getLogger().info("Config located at " + getDataFolder());

        try {
            configuration = new YamlConfiguration();
            configuration.load(configurationFile);
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().severe("Could not load config...");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("One-chunk Survival Plugin Disabled!");
    }
}
