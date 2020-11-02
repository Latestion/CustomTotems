package me.Latestion.Totems.VoidTotem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.Totem.Main;
import me.Latestion.Totems.Totems;

public class Voids extends Totems {
	
	public Main plugin;
	Inventory inv;
	
	List<UUID> cooldown1 = new ArrayList<UUID>();
	List<UUID> cooldown2 = new ArrayList<UUID>();
	
	public Voids(Main plugin) {
		this.plugin = plugin;
		if (!plugin.allTotems.contains(getTotem())) plugin.allTotems.add(getTotem());
	}
	
	public ItemStack getTotem() {
		return totem("Void", plugin.totem);
	}
	
	public boolean isVoid(ItemStack item) {
		if (item.isSimilar(getTotem())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public int getShadowSneakCooldown() {
		return plugin.totem.getConfig().getInt("Void.Shadow-Sneak-Cooldown");
	}
	
	public int getWarpCooldown() {
		return plugin.totem.getConfig().getInt("Void.Warp-Cooldown");
	}
	
	public Location get5Ahead(Location loc) {
		Location twoBlocksAway = loc.add(loc.getDirection().multiply(5));
		return twoBlocksAway;
	}
	
	public Location getAhead(Location loc, int i) {
		Location twoBlocksAway = loc.add(loc.getDirection().multiply(i));
		return twoBlocksAway;
	}
	
	public Location getLocFromItem(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		World world = Bukkit.getWorld(stripColor(lore.get(0)).split(": ")[1]);
		int x = Integer.parseInt(stripColor(lore.get(1)).split(": ")[1]);
		int y = Integer.parseInt(stripColor(lore.get(2)).split(": ")[1]);
		int z = Integer.parseInt(stripColor(lore.get(3)).split(": ")[1]);
		float yaw = Float.parseFloat(stripColor(lore.get(4)).split(": ")[1]);
		float pitch = Float.parseFloat(stripColor(lore.get(5)).split(": ")[1]);
		return new Location(world, x, y, z, yaw, pitch);
	}
	
	private String stripColor(String s) {
		return ChatColor.stripColor(s);
	}
	
}
