package me.Latestion.Totems;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Latestion.Totem.Main;

public class TotemCheck extends Totems {
	
	private Main plugin;
	
	public TotemCheck(Main plugin) {
		this.plugin = plugin;
	}
	
	public ItemStack belt() {
		String mat = plugin.totem.getConfig().getString("Belt.Material");
		int model = plugin.totem.getConfig().getInt("Belt.Model");
		List<String> lore = plugin.totem.getConfig().getStringList("Belt.Lore");
		String display = plugin.totem.getConfig().getString("Belt.Displayname");
		return totemBelt(mat, model, lore, display);
	}
	
	public boolean hasBelt(Player player) {
		if (player.getInventory().contains(belt())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void cast() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (getAllPlayerTotems(player, plugin.allTotems).size() > 1) {
						if (hasBelt(player)) {
							// Do nothing
						}
						else {
							player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 4, false, false, false));	
						}
					}
				}
			}
		}, 0, 5L);
	}
	
	
}
