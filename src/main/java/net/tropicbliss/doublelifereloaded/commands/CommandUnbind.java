package net.tropicbliss.doublelifereloaded.commands;

import net.tropicbliss.doublelifereloaded.DoubleLifeReloaded;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUnbind implements CommandExecutor {
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
        if (args.length != 1) {
            return false;
        }
        String targetedPlayerName = args[0];
        Player targetedPlayer = Bukkit.getServer().getPlayerExact(targetedPlayerName);
        if (targetedPlayer == null) {
            player.sendMessage(ChatColor.RED + targetedPlayerName + " is not on the server.");
            return true;
        }
        boolean status = DoubleLifeReloaded.getSoulmateInfo().unbind(targetedPlayer);
        if (!status) {
            player.sendMessage(ChatColor.RED + "The player you are targeting is either already unbounded or that its soulmate is currently not on the server.");
            return true;
        }
        player.sendMessage(ChatColor.GREEN + "Successfully unbounded " + targetedPlayerName + ".");
        return true;
    }
}
