package net.tropicbliss.doublelifereloaded;

import net.tropicbliss.doublelifereloaded.commands.*;
import net.tropicbliss.doublelifereloaded.listeners.GlobalListener;
import net.tropicbliss.doublelifereloaded.tasks.SelectRandomSoulmate;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class DoubleLifeReloaded extends JavaPlugin {
    private static SelectRandomSoulmate soulmateInfo;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        soulmateInfo = new SelectRandomSoulmate(this);
        getCommand("doublelifestart").setExecutor(new CommandStart(this));
        getCommand("doublelifestop").setExecutor(new CommandStop());
        getCommand("lifeset").setExecutor(new CommandLifeSet());
        getCommand("lifeset").setTabCompleter(new UserTabCompletion(List.of(1)));
        getCommand("soulbind").setExecutor(new CommandSoulbind());
        getCommand("soulbind").setTabCompleter(new UserTabCompletion(List.of(1, 2)));
        getServer().getPluginManager().registerEvents(new GlobalListener(this), this);
    }

    public static SelectRandomSoulmate getSoulmateInfo() {
        return soulmateInfo;
    }
}
