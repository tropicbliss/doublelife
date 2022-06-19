package net.tropicbliss.doublelifereloaded.commands;

import net.tropicbliss.doublelifereloaded.DoubleLifeReloaded;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLifeSet implements CommandExecutor {
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
        if (args.length != 2) {
            return false;
        }
        String targetPlayerArgs = args[0];
        Player targetPlayer = Bukkit.getServer().getPlayerExact(targetPlayerArgs);
        if (targetPlayer == null) {
            player.sendMessage(ChatColor.RED + targetPlayerArgs + " is not on the server.");
            return true;
        }
        int life;
        try {
            life = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        boolean status = DoubleLifeReloaded.getSoulmateInfo().updatePlayerLife(targetPlayer, life);
        if (life < 0 || life > 3) {
            return false;
        }
        if (!status) {
            player.sendMessage(ChatColor.RED + targetPlayerArgs + " does not have a soulmate.");
            return true;
        }
        player.sendMessage(ChatColor.GREEN + "Successfully set the life of " + targetPlayerArgs + ".");
        return true;
    }
}
