package net.aerulion.shop.task.particles;

import java.util.List;
import net.aerulion.nucleus.api.console.ConsoleUtils;
import net.aerulion.nucleus.api.particle.ParticleUtils;
import net.aerulion.shop.Main;
import net.aerulion.shop.utils.Lang;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class WhiteSpiralTask extends BukkitRunnable {

  private final List<Location> CIRCLE_LOCATIONS;
  private int time;

  public WhiteSpiralTask(Location center) {
    this.CIRCLE_LOCATIONS = ParticleUtils.getCircle(center.clone().add(0D, 2D, 0D), 0.45, 25);
    this.time = 0;
    this.runTaskTimer(Main.plugin, 0L, 2L);
  }

  @Override
  public void run() {
    if (time > (CIRCLE_LOCATIONS.size() - 1)) {
      time = 0;
    }
    World world = CIRCLE_LOCATIONS.get(time).getWorld();
    if (world == null) {
      ConsoleUtils.sendColoredConsoleMessage(
          Lang.ERROR_SPAWNING_PARTICLE + CIRCLE_LOCATIONS.get(time).toString());
      this.cancel();
      return;
    }
    world.spawnParticle(Particle.FIREWORKS_SPARK, CIRCLE_LOCATIONS.get(time), 1, 0, 0, 0, 0);
    this.time++;
  }

  public void stop() {
    this.cancel();
  }
}