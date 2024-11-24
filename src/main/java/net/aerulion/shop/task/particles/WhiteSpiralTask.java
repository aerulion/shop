package net.aerulion.shop.task.particles;

import java.util.List;
import net.aerulion.erenos.utils.particle.ParticleUtils;
import net.aerulion.shop.Main;
import net.aerulion.shop.utils.Lang;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class WhiteSpiralTask extends BukkitRunnable {

  private final @NotNull List<Location> circleLocations;
  private int time;

  public WhiteSpiralTask(final @NotNull Location center) {
    this.circleLocations = ParticleUtils.getCircle(center.clone().add(0D, 2D, 0D), 0.45, 25);
    this.time = 0;
    this.runTaskTimer(Main.plugin, 0L, 2L);
  }

  @Override
  public void run() {
    if (time > (circleLocations.size() - 1)) {
      time = 0;
    }
    final World world = circleLocations.get(time).getWorld();
    if (world == null) {
      Main.plugin.getComponentLogger().warn(LegacyComponentSerializer.legacySection()
          .deserialize(Lang.ERROR_SPAWNING_PARTICLE + circleLocations.get(time).toString()));
      this.cancel();
      return;
    }
    world.spawnParticle(Particle.FIREWORKS_SPARK, circleLocations.get(time), 1, 0, 0, 0, 0);
    this.time++;
  }

  public void stop() {
    this.cancel();
  }

}