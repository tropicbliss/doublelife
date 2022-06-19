package net.tropicbliss.doublelifereloaded.tasks;

import net.tropicbliss.doublelifereloaded.DoubleLifeReloaded;
import org.bukkit.Bukkit;
import java.util.function.Consumer;

public class CountdownTimer implements Runnable {
    private final DoubleLifeReloaded plugin;
    private Integer assignedTaskId;
    private int secondsLeft;
    private final Consumer<CountdownTimer> everySecond;

    public CountdownTimer(DoubleLifeReloaded plugin, int seconds, Consumer<CountdownTimer> everySecond) {
        this.plugin = plugin;
        this.secondsLeft = seconds;
        this.everySecond = everySecond;
    }

    @Override
    public void run() {
        if (secondsLeft < 1) {
            if (assignedTaskId != null) {
                Bukkit.getScheduler().cancelTask(assignedTaskId);
                return;
            }
        }
        everySecond.accept(this);
        secondsLeft--;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public void scheduleTimer() {
        this.assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 40L);
    }
}
