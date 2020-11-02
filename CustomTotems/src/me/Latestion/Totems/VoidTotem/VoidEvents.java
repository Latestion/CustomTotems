package me.Latestion.Totems.VoidTotem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.Latestion.Totem.Main;
import me.Latestion.Totem.Utils.TimeBar;

public class VoidEvents extends Voids implements Listener {

	public VoidEvents(Main plugin) {
		super(plugin);
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (hasTotem(player)) {
			if (getTotem(player) == null) {
				return;
			}
			ItemStack item = getTotem(player);
			if (isVoid(item)) {
				if (event.getAction() == Action.RIGHT_CLICK_AIR && !player.isSneaking()) {
					if (!plugin.shadowSneakCooldown.contains(player.getUniqueId())) {
						plugin.shadowSneakCooldown.add(player.getUniqueId());
						player.teleport(getAhead(player.getLocation(), 20));
						int sec = getShadowSneakCooldown();
						plugin.shadowBar.get(player).setCounter(0);
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				            public void run() {
				            	if (plugin.shadowSneakCooldown.contains(player.getUniqueId())) {
				            		plugin.shadowSneakCooldown.remove(player.getUniqueId());
				            	}
				            }            
				        }, sec * 20);
					}
					return;
				}
				if (event.getAction() == Action.LEFT_CLICK_AIR) {
					if (!plugin.teleportCooldown.contains(player.getUniqueId()) ) {
						VoidInv inv = new VoidInv(plugin, player);
						player.openInventory(inv.getInventory());
						plugin.voidInv.put(player, inv);	
					}
				}	
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (!plugin.voidInv.containsKey(player)) {
			return;
		}
		if (event.getClickedInventory() == null) {
			return;
		}
		if (event.getInventory().equals(plugin.voidInv.get(player).getInventory())) {
			if (event.getClickedInventory() instanceof PlayerInventory) {
				return;
			}
			if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
				return;
			}
			event.setCancelled(true);
			if (event.getSlot() < 18) {
				plugin.teleportCooldown.add(player.getUniqueId());
				int sec = getWarpCooldown();
				plugin.warpBar.get(player).setCounter(0);
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		            public void run() {
		            	if (plugin.teleportCooldown.contains(player.getUniqueId())) {
		            		plugin.teleportCooldown.remove(player.getUniqueId());
		            	}
		            }            
		        }, sec * 20);
				player.closeInventory();
				ItemStack item = event.getCurrentItem();
				Location loc = getLocFromItem(item);
				Location spawn = player.getLocation().clone();
				Location spawnLoc = getAhead(spawn, plugin.totem.getConfig().getInt("Void.Warp-Distance"));
				spawnLoc.setYaw(player.getLocation().clone().getYaw() + 90);
				CreatePortalTeleportation tele = 
						new CreatePortalTeleportation(plugin, loc, spawnLoc, player, event.getInventory().getItem(19));
				plugin.portalLocation.put(player, tele);
				if (tele.group) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						plugin.portalLocation.put(p, tele);
					}
				}
			}
			if (event.getSlot() == 18) {
				/*
				 * Create new
				 */
				VoidPlayerUtil util = plugin.voidInv.get(player).util;
				if (util.getTotalPortals() == 18) {
					return;
				}
				else {
					util.blocks.openInventory();
				}
			}
			if (event.getSlot() == 19) {
				/*
				 * Group Portal
				 */
				VoidInv inv = plugin.voidInv.get(player);
				inv.changeItem(inv.getInventory().getItem(19));
				player.updateInventory();
			}
			if (event.getSlot() == 20) {
				/*
				 * Remove
				 */
				DeletePortal portal = new DeletePortal(plugin, player);
				plugin.deletePortal.put(player, portal);
				plugin.cache.add(player);
				player.closeInventory();
				portal.openInv();
			}
			if (event.getSlot() == 22) {
				plugin.cache.add(player);
				player.closeInventory();
			}
		}
		if (plugin.invs.contains(event.getInventory())) {
			if (event.getClickedInventory() instanceof PlayerInventory) {
				return;
			}
			if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
				return;
			}
			event.setCancelled(true);
			if (event.getSlot() < 45) {
				ItemStack item = event.getCurrentItem();
				plugin.itemAddInstance.put(player, item);
				plugin.cache.add(player);
				player.sendMessage(ChatColor.GOLD + "Write the name for the warp!");
				player.closeInventory();
			}
			if (event.getSlot() == 45) {
				if (plugin.invs.get(0) == event.getInventory()) return;
				plugin.cache.add(player);
				player.openInventory(plugin.invs.get(plugin.invs.indexOf(event.getInventory()) - 1));
			}
			if (event.getSlot() == 53) {
				if (plugin.invs.size() == plugin.invs.indexOf(event.getInventory())) return;
				plugin.cache.add(player);
				int i = plugin.invs.indexOf(event.getInventory()) + 1;
				player.openInventory(plugin.invs.get(i));
			}
		}
		if (plugin.deletePortal.containsKey(player)) {
			if (event.getInventory().equals(plugin.deletePortal.get(player).getInv())) {
				if (event.getClickedInventory() instanceof PlayerInventory) {
					return;
				}
				if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
					return;
				}
				DeletePortal portal = plugin.deletePortal.get(player);
				event.setCancelled(true);
				if (event.getSlot() < 18) {
					ItemStack item = event.getCurrentItem();
					portal.util.removePortal(item);
					player.closeInventory();
					return;
				}
				if (event.getSlot() == 22) {
					player.closeInventory();
				}
			}	
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if (!plugin.voidInv.containsKey(player)) {
			return;
		}
		if (plugin.invs.contains(event.getInventory()) && !plugin.deletePortal.containsKey(player)) {
			if (plugin.cache.contains(player)) {
				plugin.cache.remove(player);
				return;
			}
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	            public void run() {
	            	player.openInventory(event.getInventory());
	            }            
	        }, 1);
			return;
		}
		if (plugin.deletePortal.containsKey(player)) {
			if (plugin.deletePortal.get(player).getInv().equals(event.getInventory())) {
				plugin.deletePortal.remove(player);
			}
		}
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (plugin.itemAddInstance.containsKey(player)) {
			/*
			 * Take input
			 * cancle event
			 * take chat 
			 * use the hashmap containing inv shit
			 * create new item in it
			 * add lore to the item
			 * done!
			 */
			String s = event.getMessage();
			event.setCancelled(true);
			ItemStack item = plugin.itemAddInstance.get(player);
			plugin.voidInv.get(player).util.createNewPortal(item, s);
			plugin.itemAddInstance.remove(player);
			player.sendMessage(ChatColor.GOLD + format(s) + " Warp Created!");
		}
	}
	
	@EventHandler
	public void onWalk(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (plugin.portalLocation.containsKey(player)) {
			CreatePortalTeleportation tele = plugin.portalLocation.get(player);
			if (tele.playerPortalLoc.getWorld().getNearbyEntities(tele.playerPortalLoc, 2, 2, 2).contains(player)) {
				tele.teleport(player);
			}
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String text = plugin.totem.getConfig().getString("Void.Shadow-Sneak-Bossbar-Text");
		String color = plugin.totem.getConfig().getString("Void.Shadow-Sneak-Bossbar-Color").toUpperCase();
		String text2 = plugin.totem.getConfig().getString("Void.Warp-Bossbar-Text");
		String color2 = plugin.totem.getConfig().getString("Void.Warp-Bossbar-Color").toUpperCase();
		BossBar bar1 = createBar(text, color);
		BossBar bar2 = createBar(text2, color2);
		plugin.shadowBar.put(player, new TimeBar(plugin, getTotem(), player, bar1, getShadowSneakCooldown()));
		plugin.warpBar.put(player, new TimeBar(plugin, getTotem(), player, bar2, getWarpCooldown()));
	}	
	
		
}
