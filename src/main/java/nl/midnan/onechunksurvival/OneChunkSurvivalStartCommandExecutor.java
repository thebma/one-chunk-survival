package nl.midnan.onechunksurvival;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Locale;

public class OneChunkSurvivalStartCommandExecutor implements CommandExecutor {
    private final JavaPlugin plugin;
    private List<String> gamemasters;

    public OneChunkSurvivalStartCommandExecutor(JavaPlugin thePlugin) {
        this.plugin = thePlugin;
        this.gamemasters = this.plugin.getConfig().getStringList("gamemasters");
        plugin.getLogger().info("Registered " + this.gamemasters.size() + " gamemasters...");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        String playerName = player.getDisplayName().toLowerCase(Locale.ROOT);

        if(!this.gamemasters.contains(playerName)) {
            player.sendMessage("You're not a gamemaster, repent heretic!");
            return false;
        }

        OneChunkSurvivalStartEvent start = new OneChunkSurvivalStartEvent(player, player.getLocation());
        plugin.getServer().getPluginManager().callEvent(start);

        return true;
    }
}
