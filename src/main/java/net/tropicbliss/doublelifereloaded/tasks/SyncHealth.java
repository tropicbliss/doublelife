package net.tropicbliss.doublelifereloaded.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SyncHealth extends BukkitRunnable {
    private final Player from;
    private final Player to;

    public SyncHealth(Player from, Player to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void run() {
        double health = from.getHealth();
        double absorption = from.getAbsorptionAmount();
        to.setHealth(health);
        to.setAbsorptionAmount(absorption);
    }
}
