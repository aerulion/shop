package net.aerulion.shop.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Base64Utils {

    public static List<String> EncodeItems(List<ItemStack> input) {
        List<String> output = new ArrayList<>();
        for (ItemStack itemstack : input)
            output.add(ItemStackToBase64(itemstack));
        return output;
    }

    public static List<ItemStack> DecodeItems(List<String> input) {
        List<ItemStack> output = new ArrayList<>();
        for (String string : input)
            output.add(ItemStackFromBase64(string));
        return output;
    }

    public static String ItemStackToBase64(ItemStack item) {
        String base64 = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitout = new BukkitObjectOutputStream(out);
            bukkitout.writeObject(item);
            bukkitout.close();
            base64 = Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }

    public static ItemStack ItemStackFromBase64(String base64) {
        ItemStack itemstack = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
            BukkitObjectInputStream bukkitin = new BukkitObjectInputStream(in);
            itemstack = (ItemStack) bukkitin.readObject();
            bukkitin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemstack;
    }
}