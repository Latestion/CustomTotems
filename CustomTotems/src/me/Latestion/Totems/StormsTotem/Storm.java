package me.Latestion.Totems.StormsTotem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.Totem.Main;
import me.Latestion.Totems.Totems;

public class Storm extends Totems {

	public Main plugin;
	Inventory inv;
	
	public Storm(Main plugin) {
		this.plugin = plugin;
		createInv();
		if (!plugin.allTotems.contains(getTotem())) plugin.allTotems.add(getTotem());
	}
	
	public ItemStack getTotem() {
		return totem("Storms", plugin.totem);
	}
	
	public boolean isStorm(ItemStack item) {
		if (item.isSimilar(getTotem())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	private void createInv() {
		inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Change Weather");
		createInvGlass();
		createInvItems();
	}
	 
	private void createInvGlass() {
		ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		inv.setItem(0, item);
		inv.setItem(1, item);
		inv.setItem(3, item);
		inv.setItem(5, item);
		inv.setItem(7, item);
		inv.setItem(8, item);
	}
	
	private void createInvItems() {
		ItemStack item = new ItemStack(Material.WATER_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName(ChatColor.BLUE + "Rain");
		item.setItemMeta(meta);
		inv.addItem(item);
		
		ItemStack item2 = new ItemStack(Material.BUCKET);
		ItemMeta meta2 = item2.getItemMeta();
		meta2.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta2.setDisplayName(ChatColor.BLUE + "Clear");
		item2.setItemMeta(meta2);
		inv.addItem(item2);
		
		ItemStack item3 = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta3 = item3.getItemMeta();
		meta3.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta3.setDisplayName(ChatColor.BLUE + "Thunder Storn");
		item3.setItemMeta(meta3);
		inv.addItem(item3);
	}
	
	public int getWeatherChangeCooldown() {
		return plugin.totem.getConfig().getInt("Storms.Weather-Change-Cooldown");
	}
	
	public int getLightningStrikeCooldown() {
		return plugin.totem.getConfig().getInt("Storms.Lightning-Strike-Cooldown");
	}
	
}
