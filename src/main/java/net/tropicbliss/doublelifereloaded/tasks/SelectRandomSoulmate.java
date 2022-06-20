package net.tropicbliss.doublelifereloaded.tasks;

import net.tropicbliss.doublelifereloaded.DoubleLifeReloaded;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class SelectRandomSoulmate extends BukkitRunnable {
    private final DoubleLifeReloaded plugin;
    private boolean settingUp;
    public final NamespacedKey LIFE;
    public final NamespacedKey SOULMATE;

    public SelectRandomSoulmate(DoubleLifeReloaded plugin) {
        this.plugin = plugin;

        settingUp = false;
        LIFE = new NamespacedKey(plugin, "life");
        SOULMATE = new NamespacedKey(plugin, "soulmate");
    }

    @Override
    public void run() {
        LinkedList<Player> players = plugin.getServer().getOnlinePlayers().stream().filter((p) -> p.hasPermission("doublelife.player")).collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(players);
        CountdownTimer timer = new CountdownTimer(plugin, 5, (t) -> {
            int secondsLeft = t.getSecondsLeft() - 2;
            if (secondsLeft < 1) {
                int taskId = t.getSecondsLeft();
                switch (taskId) {
                    case 2:
                        sendTitleToAllPlayers(ChatColor.GREEN + "Your soulmate is...");
                        break;
                    case 1:
                        for (Player player : plugin.getServer().getOnlinePlayers()) {
                            player.setHealth(20.0);
                        }
                        while (players.size() > 1) {
                            Player player1 = players.pollLast();
                            Player player2 = players.pollLast();
                            PersistentDataContainer container1 = player1.getPersistentDataContainer();
                            PersistentDataContainer container2 = player2.getPersistentDataContainer();
                            container1.set(SOULMATE, PersistentDataType.STRING, player2.getUniqueId().toString());
                            container2.set(SOULMATE, PersistentDataType.STRING, player1.getUniqueId().toString());
                        }
                        sendTitleToAllPlayers(ChatColor.GREEN + "????");
                        settingUp = false;
                }
            } else {
                sendTitleToAllPlayers(ChatColor.GREEN + String.valueOf(secondsLeft));
            }
        });
        timer.scheduleTimer();
    }

    public Player getPairedPlayer(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        String rawPairedUUID = container.get(SOULMATE, PersistentDataType.STRING);
        if (rawPairedUUID.equals("")) {
            return null;
        }
        UUID pairedUUID = UUID.fromString(rawPairedUUID);
        return Bukkit.getPlayer(pairedUUID);
    }

    public void setSettingUp() {
        settingUp = true;
    }

    public boolean isSettingUp() {
        return settingUp;
    }

    public boolean updatePlayerLife(Player player, int life) {
        if (life < 0 || life > 3) {
            return false;
        }
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (container.get(SOULMATE, PersistentDataType.STRING).equals("")) {
            return false;
        }
        container.set(LIFE, PersistentDataType.INTEGER, life);
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
                container.set(SOULMATE, PersistentDataType.STRING, "");
        }
        player.setDisplayName(chatColor + player.getName());
        player.setPlayerListName(chatColor + player.getName());
        Player soulmate = getPairedPlayer(player);
        if (soulmate != null) {
            PersistentDataContainer soulmateContainer = soulmate.getPersistentDataContainer();
            soulmateContainer.set(LIFE, PersistentDataType.INTEGER, life);
            soulmate.setDisplayName(chatColor + soulmate.getName());
            soulmate.setPlayerListName(chatColor + soulmate.getName());
            if (life == 0) {
                soulmate.setGameMode(GameMode.SPECTATOR);
                soulmateContainer.set(SOULMATE, PersistentDataType.STRING, "");
            }
            soulmate.sendMessage("Your life has been indirectly set to " + life + " by an administrator.");
        }
        player.sendMessage("Your life has been set to " + life + " by an administrator.");
        return true;
    }

    public boolean soulLink(Player player1, Player player2) {
        if (player1.equals(player2)) {
            return false;
        }
        PersistentDataContainer container1 = player1.getPersistentDataContainer();
        PersistentDataContainer container2 = player2.getPersistentDataContainer();
        if (!container1.get(new NamespacedKey(plugin, "soulmate"), PersistentDataType.STRING).equals("")) {
            return false;
        }
        if (!container2.get(new NamespacedKey(plugin, "soulmate"), PersistentDataType.STRING).equals("")) {
            return false;
        }
        container1.set(new NamespacedKey(plugin, "soulmate"), PersistentDataType.STRING, player2.getUniqueId().toString());
        container2.set(new NamespacedKey(plugin, "soulmate"), PersistentDataType.STRING, player1.getUniqueId().toString());
        return true;
    }

    private void sendTitleToAllPlayers(String title) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.sendTitle(title, "", 10, 70, 20);
        }
    }
}
