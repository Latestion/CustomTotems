package me.Latestion.Totems.SpiritsTotem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.Latestion.Totem.Main;
import me.Latestion.Totem.Utils.TimeBar;

public class SpiritEvents extends Spirit implements Listener {

	private List<UUID> cache = new ArrayList<>();
	private List<UUID> cache2 = new ArrayList<>();
	
	public SpiritEvents(Main plugin) {
		super(plugin);
	}

	@EventHandler
	public void interat(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		UUID id = player.getUniqueId();
		if (hasTotem(player)) {
			if (getTotem(player) == null) {
				return;
			}
			ItemStack item = getTotem(player);
			if (isSpirit(item)) {
				if (event.getAction() == Action.RIGHT_CLICK_AIR && !player.isSneaking() && !inSpiritWorld(player)) {
					if (plugin.spiritWorlCooldown.contains(id)) return;
					if (plugin.spiritWorld.containsKey(id)) return;
					plugin.spiritWorld.put(id, player.getLocation());
					plugin.spiritWorlCooldown.add(id);
					int sec = plugin.totem.getConfig().getInt("Spirit.Spirit-World-Switch-Cooldown");
					player.setGameMode(GameMode.SPECTATOR);
					player.setAllowFlight(true);
					player.teleport(getSpiritWorld().getSpawnLocation());
					plugin.spiritBar.get(player).setCounter(0);
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			            public void run() {
			            	if (plugin.spiritWorlCooldown.contains(player.getUniqueId())) {
			            		plugin.spiritWorlCooldown.remove(player.getUniqueId());
			            	}
			            }            
			        }, sec * 20);
					return;
				}
				if (event.getAction() == Action.RIGHT_CLICK_AIR && player.isSneaking() 
						&& plugin.spiritWorld.containsKey(id) && inSpiritWorld(player) && !plugin.spiritWorlCooldown.contains(id)) {
					if (inSpiritWorld(player)) {
						plugin.spiritWorlCooldown.add(id);
						player.teleport(plugin.spiritWorld.get(id));
						plugin.spiritWorld.remove(id);
						int sec = plugin.totem.getConfig().getInt("Spirit.Spirit-World-Switch-Cooldown");
						plugin.spiritBar.get(player).setCounter(0);
				        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				            public void run() {
				            	if (plugin.spiritWorlCooldown.contains(player.getUniqueId())) {
				            		plugin.spiritWorlCooldown.remove(player.getUniqueId());
				            	}
				            }            
				        }, sec * 20);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void death(PlayerDeathEvent event) {
		Player player = event.getEntity();
		UUID id = player.getUniqueId();
		if (hasTotem(player)) {
			if (getTotem(player) == null) {
				return;
			}
			ItemStack item = getTotem(player);
			if (isSpirit(item)) {
				if (!plugin.deadSpiritSpec.containsKey(id)) {
					event.setKeepInventory(true);
					event.getDrops().clear();
					event.setKeepLevel(true);
					event.setDroppedExp(0);
					player.updateInventory();
					cache.add(id);
				}
				else if (plugin.deadSpiritSpec.containsKey(id) && inSpiritWorld(player)) {
					player.setAllowFlight(false);
					cache2.add(id);
				}
			}
		}
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		UUID id = player.getUniqueId();
		if (cache.contains(id)) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	            public void run() {
					Location l = getSpiritWorld().getSpawnLocation();
					Location loc = player.getLocation();
					player.teleport(l);
					plugin.deadSpiritSpec.put(id, loc);
	    			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	    	            public void run() {
	    					player.setAllowFlight(true);
	    	            }            
	    	        }, 1);
	            	cache.remove(id);
	            }            
	        }, 1);
		}
		if (cache2.contains(id)) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	            public void run() {
					Location l = getTeleportLocation(player);
					player.teleport(getSpiritWorld().getHighestBlockAt(new Location(getSpiritWorld(), l.getX(), l.getY(), l.getZ()))
							.getLocation());
	            	plugin.deadSpiritSpec.put(id, player.getLocation());
	    			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	    	            public void run() {
	    					player.setAllowFlight(false);
	    	            }            
	    	        }, 1);
	            	cache2.remove(id);
	            }            
	        }, 1);
		}
	}
	
	@EventHandler
	public void walk(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		UUID id = event.getPlayer().getUniqueId();
		if (plugin.deadSpiritSpec.containsKey(id)) {
			if (event.getTo().getBlock().getType().isSolid()) {
				event.getPlayer().setVelocity(new Vector());
				event.setTo(event.getFrom());
			}
			if (inSpiritWorld(player)) {
				Location l = plugin.deadSpiritSpec.get(id);
				Location check = new Location(this.getSpiritWorld(), l.getX(), l.getY(), l.getZ());
				if (player.getLocation().getWorld().getNearbyEntities(check, 30, 30, 30).contains(player)) {
					player.setGameMode(GameMode.SURVIVAL);
					player.teleport(plugin.deadSpiritSpec.get(id));
					player.setAllowFlight(false);
					plugin.deadSpiritSpec.remove(id);
				}	
			}
		}
		if (plugin.spiritWorld.containsKey(id)) {
			if (event.getTo().getBlock().getType().isSolid()) {
				event.getPlayer().setVelocity(new Vector());
				event.setTo(event.getFrom());	
			}
		}
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event) {
		
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String text = plugin.totem.getConfig().getString("Spirit.World-Switch-Bossbar-Text");
		String color = plugin.totem.getConfig().getString("Spirit.World-Switch-Bossbar-Color").toUpperCase();
		BossBar bar1 = createBar(text, color);
		plugin.spiritBar.put(player, new TimeBar(plugin, getTotem(), player, bar1, getSpiritCooldown()));
	}	
	
}
