package me.Latestion.Totems.VoidTotem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.Totem.Main;

public class VoidPlayerUtil {

	private Main plugin;
	private Player player;
	
	public List<ItemStack> portals = new ArrayList<>();
	
	public PortalCreateBlocks blocks;
	
	public VoidPlayerUtil(Main plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
		getPlayerPortals();
		this.blocks = new PortalCreateBlocks(plugin, player);
	}
	
	private void getPlayerPortals() {
		List<ItemStack> send = new ArrayList<ItemStack>();
		try {
			plugin.data.getConfig().getConfigurationSection("data." + player.getUniqueId().toString()).getKeys(false).forEach(num -> {
				ItemStack add = plugin.data.getConfig().getItemStack("data." + player.getUniqueId().toString() + "." + num);
				send.add(add);
			});
		} catch (Exception e) {
			
		}
		portals = send;
	}

	public void setPlayerPortals(List<ItemStack> items) {
		int i = 0;
		String name = player.getUniqueId().toString();
		plugin.data.getConfig().set("data." + name, null);
		plugin.data.saveConfig();
		for (ItemStack item : items) {
			plugin.data.getConfig().set("data." + name + "." + i, item);
			plugin.data.saveConfig();
			i++;
		}
		getPlayerPortals();
	}
	
	public void createNewPortal(ItemStack item, String display) {
		Location loc = player.getLocation();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(format(display));
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.GOLD + "World: " + ChatColor.WHITE + loc.getWorld().getName());
		lore.add(ChatColor.GOLD + "X: " + ChatColor.WHITE  + loc.getBlockX());
		lore.add(ChatColor.GOLD + "Y: " + ChatColor.WHITE  + loc.getBlockY());
		lore.add(ChatColor.GOLD + "Z: " + ChatColor.WHITE  + loc.getBlockZ());
		lore.add(ChatColor.GOLD + "Yaw: " + ChatColor.WHITE  + loc.getYaw());
		lore.add(ChatColor.GOLD + "Pitch: " + ChatColor.WHITE  + loc.getPitch());
		meta.setLore(lore);
		item.setItemMeta(meta);
		portals.add(item);
		setPlayerPortals(portals);
	}
	
	public void removePortal(ItemStack item) {
		portals.remove(item);
		setPlayerPortals(portals);
		player.sendMessage(ChatColor.RED + item.getItemMeta().getDisplayName() + " Warp Deleted!");
	}
	
	public int getTotalPortals() {
		return portals.size();
	}
	
	public String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
}
