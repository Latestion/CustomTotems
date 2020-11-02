package me.Latestion.Totems.EarthTotem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.Latestion.Totem.Main;
import me.Latestion.Totems.Totems;

public class Earth extends Totems {

	public Main plugin;
	
	public Earth(Main plugin) {
		this.plugin = plugin;
	}
	

	public ItemStack getTotem() {
		return totem("Earth", plugin.totem);
	}
	
	public boolean isEarth(ItemStack item) {
		if (item.isSimilar(getTotem())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void spawnWall(Location location, Material material, long duration, int x, int z, int w, int h) {
		Location loc = location.clone();
		loc.setX(loc.getX() + (z * 2));
		loc.setZ(loc.getZ() + (x * 2));
		double pos = 0;
		if (x != 0) pos = loc.getX();
		else if (z != 0) pos = loc.getZ();
		List<Location> locs = new ArrayList<Location>();
		List<Block> originals = new ArrayList<Block>();
		for (int i = (int) (pos - ((w - 1) / 2)); i < (int) (pos + ((w - 1) / 2) + 1); i++) {
			for (int j = (int) Math.floor(loc.getY()); j < (int) (Math.floor(loc.getY()) + h); j++) {
				Location bLoc = null;
				if (x != 0) bLoc = new Location(loc.getWorld(), i, j, loc.getZ());
				else if (z != 0) bLoc = new Location(loc.getWorld(), loc.getX(), j, i);
				originals.add(bLoc.getBlock());
				locs.add(bLoc);
			}
		}
		List<BlockState> blocks = new ArrayList<BlockState>();
		for (Block b : originals) {
			blocks.add(b.getState());
		}
		for (Block b : originals) {
			b.setType(material);
		}
		new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				for (Location loc : locs) {
					loc.getBlock().setType(blocks.get(i).getType());
					loc.getBlock().setBlockData(blocks.get(i).getBlockData());
					i++;
				}
			}
		}.runTaskLater(plugin, duration * 20);
	}
	
	public void spawnSphere(Location loc, Material material, long duration, int radius) {
		List<Location> locs = new ArrayList<Location>();
		List<Block> originals = new ArrayList<Block>();
		int X = loc.getBlockX();
		int Y = loc.getBlockY();
		int Z = loc.getBlockZ();
		for (int x = X - radius; x <= X + radius; x++) {
			for (int y = Y - radius; y <= Y + radius; y++) {
				for (int z = Z - radius; z <= Z + radius; z++) {
					if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) <= (radius * radius)) {
						if ((X - x) * (X - x) + (Y - y) * (Y - y) + (Z - z) * (Z - z) > (radius - 1) * (radius - 1)) {
							Location bLoc = new Location(loc.getWorld(), x, y, z);
							originals.add(bLoc.getBlock());
							locs.add(bLoc);
						}
					}
				}
			}
		}
		List<BlockState> blocks = new ArrayList<BlockState>();
		for (Block b : originals) {
			blocks.add(b.getState());
		}
		for (Block b : originals) {
			b.setType(material);
		}
		new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				for (Location loc : locs) {
					loc.getBlock().setType(blocks.get(i).getType());
					loc.getBlock().setBlockData(blocks.get(i).getBlockData());
					i++;
				}
			}
		}.runTaskLater(plugin, duration * 20);
	}

	public String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw()) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
         if (0 <= rotation && rotation < 45.0 || 315.0 <= rotation) {
        	 return "E";
         } else if (45.0 <= rotation && rotation < 135.0) {
        	 return "N";
         } else if (135.0 <= rotation && rotation < 225.0) {
        	 return "W";
         } else if (225.0 <= rotation && rotation < 315.0) {
        	 return "S";
         }
         return null;
	}
	
	public Location getAhead(Location loc, int i) {
		Location twoBlocksAway = loc.add(loc.getDirection().multiply(i));
		return twoBlocksAway;
	}
	
	public int getWallCooldown() {
		return plugin.totem.getConfig().getInt("Earth.Wall.Cooldown");
	}
	
	public int getDomeCooldown() {
		return plugin.totem.getConfig().getInt("Earth.Dome.Cooldown");
	}
	
}
