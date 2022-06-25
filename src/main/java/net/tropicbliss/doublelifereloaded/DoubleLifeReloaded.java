package net.tropicbliss.doublelifereloaded;

import net.tropicbliss.doublelifereloaded.commands.*;
import net.tropicbliss.doublelifereloaded.listeners.GlobalListener;
import net.tropicbliss.doublelifereloaded.tasks.SelectRandomSoulmate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
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
        getCommand("lifeset").setExecutor(new CommandLifeSet());
        getCommand("lifeset").setTabCompleter(new UserTabCompletion(List.of(1)));
        getCommand("soulbind").setExecutor(new CommandSoulbind());
        getCommand("soulbind").setTabCompleter(new UserTabCompletion(List.of(1, 2)));
        getCommand("unbind").setExecutor(new CommandUnbind());
        getCommand("unbind").setTabCompleter(new UserTabCompletion(List.of(1)));
        ItemStack tnt = new ItemStack(Material.TNT);
        NamespacedKey easyTnt = new NamespacedKey(this, "easy_tnt");
        ShapedRecipe recipe = new ShapedRecipe(easyTnt, tnt);
        recipe.shape("PSP", "SGS", "PSP");
        recipe.setIngredient('P', Material.PAPER);
        recipe.setIngredient('S', Material.SAND);
        recipe.setIngredient('G', Material.GUNPOWDER);
        Bukkit.addRecipe(recipe);
        getServer().getPluginManager().registerEvents(new GlobalListener(this), this);
    }

    public static SelectRandomSoulmate getSoulmateInfo() {
        return soulmateInfo;
    }
}
