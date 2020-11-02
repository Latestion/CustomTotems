package me.Latestion.Totems.VoidTotem;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.Totem.Main;

public class CreatePortalTeleportation {

	public Location teleLoc;
	public Location playerPortalLoc;
	
	public boolean group;
	private Main plugin;
	
	private Player owner;
	
	private ArmorStand spawnAs;
	private ArmorStand teleAs;
	
	public CreatePortalTeleportation(Main plugin, Location teleLoc, Location playerPortalLoc, Player owner, ItemStack item) {
		this.plugin = plugin;
		this.teleLoc = teleLoc;
		this.playerPortalLoc = playerPortalLoc;
		this.owner = owner;
		isGroup(item);
		spawnArmorStandAtPlayerLoc();
		spawnArmorStandAtTeleLoc();
	}

	private void spawnArmorStandAtPlayerLoc() {
		ArmorStand as = (ArmorStand) playerPortalLoc.getWorld().spawnEntity(playerPortalLoc, EntityType.ARMOR_STAND);
		as.setVisible(false);
		as.setGravity(false);
		as.setInvulnerable(true);
		as.addScoreboardTag("portal");
		as.addScoreboardTag("soulfire");
		this.spawnAs = as;
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
          		teleAs.remove();
        		spawnAs.remove();
            }            
        }, plugin.totem.getConfig().getInt("Void.Solo-Portal-Time") * 20);
	}
	
	private void spawnArmorStandAtTeleLoc() {
		Location teleLoc1 = teleLoc.clone();
		teleLoc1.setYaw(teleLoc.clone().getYaw() + 90);
		ArmorStand as = (ArmorStand) teleLoc1.getWorld().spawnEntity(teleLoc1, EntityType.ARMOR_STAND);
		as.setVisible(false);
		as.setGravity(false);
		as.setInvulnerable(true);
		as.addScoreboardTag("portal");
		as.addScoreboardTag("soulfire");
		this.teleAs = as;
	}
	
	private void isGroup(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore.get(0).contains("►")) {
			group = true;
		}
		else {
			group = false;
		}
		if (group) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	            public void run() {
	            	for (Player player : plugin.portalLocation.keySet()) {
	            		plugin.portalLocation.remove(player);
	            		teleAs.remove();
	            		spawnAs.remove();
	            	}
	            }            
	        }, plugin.totem.getConfig().getInt("Void.Group-Portal-Time") * 20);
		}
	}
	
	public void teleport(Player player) {
		Location loc = teleLoc.clone();
		if (player.equals(owner)) {
			loc = getAhead(teleLoc.clone(), 5);
			loc.setYaw(loc.getYaw() + 180);
		}
		player.teleport(loc);
		if (!group) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	            public void run() {         	
            		teleAs.remove();
            		spawnAs.remove();
	            }            
	        }, 2 * 20);
    		plugin.portalLocation.remove(player);
		}
	}
	
	private Location getAhead(Location loc, int i) {
		Location twoBlocksAway = loc.add(loc.getDirection().multiply(i));
		return twoBlocksAway;
	}
	
}
