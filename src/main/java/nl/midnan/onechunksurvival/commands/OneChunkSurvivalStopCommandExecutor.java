package nl.midnan.onechunksurvival.commands;

import nl.midnan.onechunksurvival.events.OneChunkSurvivalStopEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Locale;

public class OneChunkSurvivalStopCommandExecutor implements CommandExecutor {
    private final JavaPlugin plugin;
    private List<String> gamemasters;

    public OneChunkSurvivalStopCommandExecutor(JavaPlugin thePlugin) {
        this.plugin = thePlugin;
        this.gamemasters = this.plugin.getConfig().getStringList("gamemasters");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player)sender;
        String playerName = player.getDisplayName().toLowerCase(Locale.ROOT);

        if(!this.gamemasters.contains(playerName)) {
            player.sendMessage("You're not a gamemaster, repent heretic!");
            return false;
        }

        OneChunkSurvivalStopEvent start = new OneChunkSurvivalStopEvent(player);
        plugin.getServer().getPluginManager().callEvent(start);

        return true;
    }
}
