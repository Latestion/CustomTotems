package me.Latestion.Totems.VoidTotem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.Totem.Main;

public class VoidInv  {

	private Inventory inv;
	public VoidPlayerUtil util;
	
	private Main plugin;
	
	private boolean solo = false;
	
	private Player player;
	
	public VoidInv(Main plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
		getType();
		this.util = new VoidPlayerUtil(plugin, player);
		createInv();
	}

	public Inventory getInventory() {
		return inv;
	}
	
	private void createInv() {
		inv = Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE + "Warp Portals");
		setItems();
		setPortals();
	}

	private void setItems() {
		inv.setItem(18, addNewItem());
		inv.setItem(19, grpItem());
		inv.setItem(20, removeItem());
		inv.setItem(22, closeItem());
	}
	
	private ItemStack closeItem() {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Close");
		item.setItemMeta(meta);
		updateItem(item);
		return item;
	}
	
	private ItemStack addNewItem() {
		ItemStack item = new ItemStack(Material.COMMAND_BLOCK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + "Add New Location");
		item.setItemMeta(meta);
		updateItem(item);
		return item;
	}
	
	private ItemStack removeItem() {
		ItemStack item = new ItemStack(Material.REPEATING_COMMAND_BLOCK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Remove Location");
		item.setItemMeta(meta);
		updateItem(item);
		return item;
	}
	
	private ItemStack grpItem() {
		ItemStack item = new ItemStack(Material.CAMPFIRE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Toggle Group Portal");
		item.setItemMeta(meta);
		updateItem(item);
		setItem(item);
		return item;
	}
	
	public void setItem(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<>();
		if (!solo) {	
			lore.add(ChatColor.GREEN + "Enabled");
			lore.add(ChatColor.BOLD + "►" + ChatColor.RESET + ChatColor.RED + "Disabled");
			setType();
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(19, item);
			return;
		}
		else {
			lore.add(ChatColor.BOLD + "►" + ChatColor.RESET + ChatColor.GREEN + "Enabled");
			lore.add(ChatColor.RED + "Disabled");
			setType();
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(19, item);
			return;
		}
	}
	
	
	public void changeItem(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<>();
		if (solo) {
			solo = false;
			lore.add(ChatColor.GREEN + "Enabled");
			lore.add(ChatColor.BOLD + "►" + ChatColor.RESET + ChatColor.RED + "Disabled");
			setType();
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(19, item);
			return;
		}
		else {
			solo = true;
			lore.add(ChatColor.BOLD + "►" + ChatColor.RESET + ChatColor.GREEN + "Enabled");
			lore.add(ChatColor.RED + "Disabled");
			setType();
			meta.setLore(lore);
			item.setItemMeta(meta);
			inv.setItem(19, item);
			return;
		}
	}
	
	private void getType() {
		solo = plugin.data.getConfig().getBoolean("solo." + player.getUniqueId().toString());
	}
	
	private void setType() {
		plugin.data.getConfig().set("solo." + player.getUniqueId().toString(), solo);
		plugin.data.saveConfig();
	}
	
	private void updateItem(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);
	}
	
	private void setPortals() {
		for (ItemStack item : util.portals) {
			inv.addItem(item);
		}
	}
	
}
