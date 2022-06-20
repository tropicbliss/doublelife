package net.tropicbliss.doublelifereloaded.listeners;

import net.tropicbliss.doublelifereloaded.DoubleLifeReloaded;
import net.tropicbliss.doublelifereloaded.tasks.SyncHealth;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class GlobalListener implements Listener {
    DoubleLifeReloaded plugin;

    public GlobalListener(DoubleLifeReloaded plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
                return;
            }
            Player player = (Player) event.getEntity();
            Player otherPlayer = DoubleLifeReloaded.getSoulmateInfo().getPairedPlayer(player);
            if (otherPlayer == null) {
                return;
            }
            double damageTaken = event.getDamage();
            otherPlayer.damage(damageTaken);
        }
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.CUSTOM) {
                return;
            }
            Player player = (Player) event.getEntity();
            Player otherPlayer = DoubleLifeReloaded.getSoulmateInfo().getPairedPlayer(player);
            if (otherPlayer == null) {
                return;
            }
            double healAmount = event.getAmount();
            double otherPlayerHealth = player.getHealth();
            double finalHealth = healAmount + otherPlayerHealth;
            if (finalHealth > 20.0) {
                return;
            }
            otherPlayer.setHealth(finalHealth);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (!container.has(DoubleLifeReloaded.getSoulmateInfo().LIFE, PersistentDataType.INTEGER)) {
            container.set(DoubleLifeReloaded.getSoulmateInfo().LIFE, PersistentDataType.INTEGER, 3);
        }
        if (!container.has(DoubleLifeReloaded.getSoulmateInfo().SOULMATE, PersistentDataType.STRING)) {
            container.set(DoubleLifeReloaded.getSoulmateInfo().SOULMATE, PersistentDataType.STRING, "");
        }
        Player pairedPlayer = DoubleLifeReloaded.getSoulmateInfo().getPairedPlayer(player);
        if (pairedPlayer != null) {
            double pairedPlayerHealth = pairedPlayer.getHealth();
            double pairedAbsorption = pairedPlayer.getAbsorptionAmount();
            player.setHealth(pairedPlayerHealth);
            player.setAbsorptionAmount(pairedAbsorption);
            PersistentDataContainer pairedContainer = pairedPlayer.getPersistentDataContainer();
            int pairedPlayerLife = pairedContainer.get(DoubleLifeReloaded.getSoulmateInfo().LIFE, PersistentDataType.INTEGER);
            container.set(DoubleLifeReloaded.getSoulmateInfo().LIFE, PersistentDataType.INTEGER, pairedPlayerLife);
        }
        int life = container.get(DoubleLifeReloaded.getSoulmateInfo().LIFE, PersistentDataType.INTEGER);
        ChatColor chatColor;
        switch (life) {
            case 3:
                chatColor = ChatColor.GREEN;
                break;
            case 2:
                chatColor = ChatColor.YELLOW;
                break;
            case 1:
                chatColor = ChatColor.RED;
                break;
            default:
                chatColor = ChatColor.GRAY;
                player.setGameMode(GameMode.SPECTATOR);
                container.set(DoubleLifeReloaded.getSoulmateInfo().SOULMATE, PersistentDataType.STRING, "");
        }
        player.setDisplayName(chatColor + player.getName());
        player.setPlayerListName(chatColor + player.getName());
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Player pairedPlayer = DoubleLifeReloaded.getSoulmateInfo().getPairedPlayer(player);
        if (pairedPlayer == null) {
            return;
        }
        new SyncHealth(player, pairedPlayer).runTaskLater(plugin, 20L);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (DoubleLifeReloaded.getSoulmateInfo().getPairedPlayer(player) == null) {
            return;
        }
        PersistentDataContainer container = player.getPersistentDataContainer();
        int currentLife = container.get(DoubleLifeReloaded.getSoulmateInfo().LIFE, PersistentDataType.INTEGER);
        if (currentLife > 0) {
            currentLife--;
            container.set(DoubleLifeReloaded.getSoulmateInfo().LIFE, PersistentDataType.INTEGER, currentLife);
        }
        ChatColor chatColor;
        switch (currentLife) {
            case 2:
                chatColor = ChatColor.YELLOW;
                break;
            case 1:
                chatColor = ChatColor.RED;
                break;
            default:
                chatColor = ChatColor.GRAY;
                player.setGameMode(GameMode.SPECTATOR);
                container.set(DoubleLifeReloaded.getSoulmateInfo().SOULMATE, PersistentDataType.STRING, "");
        }
        player.setDisplayName(chatColor + player.getName());
        player.setPlayerListName(chatColor + player.getName());
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        if (event.getCurrentItem().getType() == Material.ENCHANTING_TABLE) {
            event.getWhoClicked().sendMessage(ChatColor.RED + "You cannot craft an enchanting table.");
            event.setCancelled(true);
        }
    }
}
