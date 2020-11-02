package me.Latestion.Totems.VoidTotem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.Totem.Main;

public class DeletePortal {

	private Player player;
	
	private Inventory inv;
	
	public VoidPlayerUtil util;
	
	public DeletePortal(Main plugin, Player player) {
		this.player = player;
		this.util = plugin.voidInv.get(player).util;
		setInv();
	}
	
	public Inventory getInv() {
		return inv;
	}
	
	private void setInv() {
		inv = Bukkit.createInventory(null, 27, ChatColor.RED + "Click to delte a warp");
		for (ItemStack item : util.portals) {
			inv.addItem(item);
		}
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
	
	private void updateItem(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);
	}
	
	public void openInv() {
		player.openInventory(inv);
	}
}
