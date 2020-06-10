package net.aerulion.shop.utils;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

public class ParticleUtils {

    public static ArrayList<Location> getCircle(Location center, double radius, int amount) {
        World world = center.getWorld();
        double increment = 6.283185307179586D / amount;
        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double x = center.getX() + radius * Math.cos(angle);
            double z = center.getZ() + radius * Math.sin(angle);
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }
}