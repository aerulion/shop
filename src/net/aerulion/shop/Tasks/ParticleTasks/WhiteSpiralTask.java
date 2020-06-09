package net.aerulion.shop.Tasks.ParticleTasks;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import net.aerulion.shop.Main;
import net.aerulion.shop.Utils.ParticleUtils;

public class WhiteSpiralTask extends BukkitRunnable {

    private int time;
    private ArrayList<Location> circleloc;

    public WhiteSpiralTask(Location center) {
        this.circleloc = ParticleUtils.getCircle(center.clone().add(0D, 2D, 0D), 0.45, 25);
        this.time = 0;
        this.runTaskTimer(Main.plugin, 0L, 2L);
    }

    @Override
    public void run() {
        if (time > (circleloc.size() - 1))
            time = 0;
        circleloc.get(time).getWorld().spawnParticle(Particle.FIREWORKS_SPARK, circleloc.get(time), 1, 0, 0, 0, 0);
        this.time++;
    }

    public void stop() {
        this.cancel();
    }
}
