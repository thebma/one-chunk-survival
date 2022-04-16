package nl.midnan.onechunksurvival;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class OneChunkSurvival extends JavaPlugin {
    private FileConfiguration configuration;

    @Override
    public void onEnable() {
        loadOrCreateConfig();

        getLogger().info("One-chunk Survival Plugin Enabled!");

        getCommand("ocs-start").setExecutor(new OneChunkSurvivalStartCommandExecutor(this));
        getCommand("ocs-stop").setExecutor(new OneChunkSurvivalStopCommandExecutor(this));

        getServer().getPluginManager().registerEvents(new OneChunkSurvivalListener(this), this);
    }

    public FileConfiguration getConfig() {
        return this.configuration;
    }

    private void loadOrCreateConfig()
    {
        File configurationFile = new File(getDataFolder(), "config.yml");

        this.saveDefaultConfig();

        if (!configurationFile.exists()) {
            configurationFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
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
