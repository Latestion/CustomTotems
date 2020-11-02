package me.Latestion.Totem.Utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TotemUtils {
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
}
