package me.Latestion.Totems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.Latestion.Totem.Files.TotemManager;

public class Totems {

	public boolean hasTotem(Player player) {
		if (player.getInventory().getItemInMainHand() != null) {
			if (player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING) {
				return true;
			}
		}
		else if (player.getInventory().getItemInOffHand() != null) {
			if (player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
				return true;
			}
		}
		else {
			return false;
		}
		return false;
	}
	
	public ItemStack getTotem(Player player) {
		if (player.getInventory().getItemInMainHand() != null) {
			if (player.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING) {
				return player.getInventory().getItemInMainHand();
			}
		}
		else if (player.getInventory().getItemInOffHand() != null) {
			if (player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
				return player.getInventory().getItemInOffHand();
			}
		}
		return null;
	}
	
	public String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public List<String> lore(List<String> set) {
		List<String> lore = new ArrayList<String>();
		for (String s : set) {
			lore.add(format(s));
		}
		return lore;
	}
	
	public ItemStack totem(String s, TotemManager totemData) {
		ItemStack item = new ItemStack(Material.TOTEM_OF_UNDYING);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.setDisplayName(format(totemData.getConfig().getString(s + ".Displayname")));
		meta.setLore(lore(totemData.getConfig().getStringList(s + ".Lore")));
		meta.setCustomModelData(totemData.getConfig().getInt(s + ".Model"));
		item.setItemMeta(meta);
		return item;
	}
	
	public BossBar createBar(String text, String color) {
		BossBar bar = Bukkit.createBossBar(format(text), BarColor.valueOf(color), BarStyle.SOLID);
		bar.setVisible(false);
		return bar;
	}
	
	public Entity getEntityLookingAtEntity(Player p, int range){
	
		Vector playerLookDir = p.getEyeLocation().getDirection();	
		Vector playerEyeLocation = p.getEyeLocation().toVector();
		Entity bestEntity = null;
		float bestAngle = 0.4f;
		for(Entity e : p.getNearbyEntities(range, range, range)){
			if(!p.hasLineOfSight(e)) continue;
			Vector entityLoc = e.getLocation().toVector();
			Vector playerToEntity = entityLoc.subtract(playerEyeLocation);
			if(playerLookDir.angle(playerToEntity) < bestAngle) {
				bestAngle = playerLookDir.angle(playerToEntity);
				bestEntity = e;
			}
		}
		return bestEntity;
	}
	
	public List<ItemStack> getAllPlayerTotems(Player player, List<ItemStack> allTotems) {
		List<ItemStack> hisTotems = new ArrayList<ItemStack>();
		for (ItemStack item : player.getInventory()) {
			if (allTotems.contains(item)) {
				hisTotems.add(item);
			}
		}
		return hisTotems;
	}
	
	public ItemStack totemBelt(String mat, int model, List<String> lorre, String displayname) {
		ItemStack item = new ItemStack(Material.matchMaterial(mat));
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(model);
		meta.setLore(lore(lorre));
		meta.setDisplayName(format(displayname));
		item.setItemMeta(meta);
		return item;
	}
	
	public String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw()) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }  
        if (0 <= rotation && rotation < 45.0 || 315.0 <= rotation) {
        	 return "S";
        }
        else if (45.0 <= rotation && rotation < 135.0) {
        	 return "W";
        }
        else if (135.0 <= rotation && rotation < 225.0) {
        	 return "N";
        } 
        else if (225.0 <= rotation && rotation < 315.0) {
        	return "E";
        }
        return null;
	}

}
