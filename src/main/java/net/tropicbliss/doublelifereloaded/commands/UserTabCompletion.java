package net.tropicbliss.doublelifereloaded.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserTabCompletion implements TabCompleter {
    private List<Integer> argIdx;

    public UserTabCompletion(List<Integer> argIdx) {
        this.argIdx = argIdx;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (argIdx.contains(args.length)) {
            return Bukkit.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }
        return null;
    }
}
