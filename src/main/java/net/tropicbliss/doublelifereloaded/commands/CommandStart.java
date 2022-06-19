package net.tropicbliss.doublelifereloaded.commands;

import net.tropicbliss.doublelifereloaded.DoubleLifeReloaded;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStart implements CommandExecutor {
    DoubleLifeReloaded plugin;

    public CommandStart(DoubleLifeReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("The console cannot issue this command.");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("doublelife.admin")) {
            player.sendMessage(ChatColor.RED + "You do not have permissions to run this command.");
            return true;
        }
        if (DoubleLifeReloaded.getSoulmateInfo().isSettingUp()) {
            player.sendMessage(ChatColor.YELLOW + "The game is currently in the process of setting up.");
            return true;
        }
        DoubleLifeReloaded.getSoulmateInfo().setSettingUp();
        int delay = plugin.getConfig().getInt("shuffleStartDelay");
        DoubleLifeReloaded.getSoulmateInfo().runTaskLater(plugin, delay * 20L);
        player.sendMessage(ChatColor.GREEN + "Player shuffle will run " + delay + " seconds later.");
        return true;
    }
}
