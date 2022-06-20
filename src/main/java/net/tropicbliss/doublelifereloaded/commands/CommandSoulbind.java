package net.tropicbliss.doublelifereloaded.commands;

import net.tropicbliss.doublelifereloaded.DoubleLifeReloaded;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSoulbind implements CommandExecutor {
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
        String player1Name = args[0];
        String player2Name = args[1];
        if (player1Name.equals(player2Name)) {
            player.sendMessage(ChatColor.RED + "Both players cannot be the same.");
            return true;
        }
        Player player1 = Bukkit.getServer().getPlayerExact(player1Name);
        Player player2 = Bukkit.getServer().getPlayerExact(player2Name);
        if (player1 == null) {
            player.sendMessage(ChatColor.RED + player1Name + " is not on the server.");
            return true;
        }
        if (player2 == null) {
            player.sendMessage(ChatColor.RED + player2Name + " is not on the server.");
            return true;
        }
        if (!player1.hasPermission("doublelife.player")) {
            player.sendMessage(ChatColor.RED + player1Name + " does not have the permission to be soulbound.");
            return true;
        }
        if (!player2.hasPermission("doublelife.player")) {
            player.sendMessage(ChatColor.RED + player2Name + " does not have the permission to be soulbound.");
            return true;
        }
        boolean status = DoubleLifeReloaded.getSoulmateInfo().soulLink(player1, player2);
        if (!status) {
            player.sendMessage(ChatColor.RED + "One of the players is already soulbound.");
            return true;
        }
        player.sendMessage(ChatColor.GREEN + "Successfully soulbound " + player1Name + " and " + player2Name + ".");
        return true;
    }
}
