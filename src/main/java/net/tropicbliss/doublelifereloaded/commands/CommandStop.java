package net.tropicbliss.doublelifereloaded.commands;

import net.tropicbliss.doublelifereloaded.DoubleLifeReloaded;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStop implements CommandExecutor {
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
        DoubleLifeReloaded.getSoulmateInfo().clear();
        player.sendMessage(ChatColor.GREEN + "Successfully stopped game.");
        return true;
    }
}
