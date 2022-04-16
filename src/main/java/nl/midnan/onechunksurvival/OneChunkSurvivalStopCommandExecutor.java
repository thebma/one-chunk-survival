package nl.midnan.onechunksurvival;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class OneChunkSurvivalStopCommandExecutor implements CommandExecutor {
    private final JavaPlugin plugin;

    public OneChunkSurvivalStopCommandExecutor(JavaPlugin thePlugin) {
        this.plugin = thePlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if(player.getDisplayName().compareToIgnoreCase("midnan") != 0) {
            player.sendMessage("pmiD");
            return false;
        }

        OneChunkSurvivalStartEvent start = new OneChunkSurvivalStartEvent(player, player.getLocation());
        plugin.getServer().getPluginManager().callEvent(start);
        return true;
    }
}
