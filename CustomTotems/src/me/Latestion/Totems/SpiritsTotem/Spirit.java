package me.Latestion.Totems.SpiritsTotem;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Latestion.Totem.Main;
import me.Latestion.Totems.Totems;

public class Spirit extends Totems {

	public Main plugin;
	Inventory inv;
	
	public Spirit(Main plugin) {
		this.plugin = plugin;
		
		if (!plugin.allTotems.contains(getTotem())) plugin.allTotems.add(getTotem());
	}
	
	
	public ItemStack getTotem() {
		return totem("Spirit", plugin.totem);
	}
	
	public boolean isSpirit(ItemStack item) {
		if (item.isSimilar(getTotem())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public World getSpiritWorld() {
		return Bukkit.getWorld(plugin.totem.getConfig().getString("Spirit.Spirit-World-Name"));
	}
	
	public boolean inSpiritWorld(Player player) {
		return (getSpiritWorld() == player.getLocation().getWorld()) ? true : false;
	}
	
	public Location getTeleportLocation(Player player) {
		boolean has = (player.getBedSpawnLocation() == null) ? false : true;
		if (!has) {
			return player.getLocation().getWorld().getSpawnLocation();
		}
		else {
			return player.getBedSpawnLocation();
		}
	}
	
	public void createWorld() {
		if (Bukkit.getWorlds().contains(getSpiritWorld())) {
			return;
		}
		Bukkit.createWorld(new WorldCreator(plugin.totem.getConfig().getString("Spirit.Spirit-World-Name")));
	}
	
	public Location getChangedWorld(Location loc) {
		Location retu = new Location(getSpiritWorld(), loc.getX(), loc.getY(), loc.getZ());
		return retu;
	}
	
	public int getSpiritCooldown() {
		return plugin.totem.getConfig().getInt("Spirit.Spirit-World-Switch-Cooldown");
	}
	
}
