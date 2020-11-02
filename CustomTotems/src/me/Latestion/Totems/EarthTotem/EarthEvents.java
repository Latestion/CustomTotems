package me.Latestion.Totems.EarthTotem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import me.Latestion.Totem.Main;
import me.Latestion.Totem.Utils.TimeBar;

public class EarthEvents extends Earth implements Listener {

	public EarthEvents(Main plugin) {
		super(plugin);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (hasTotem(player)) {
			if (getTotem(player) == null) {
				return;
			}
			else {
				ItemStack item = getTotem(player);
				if (isEarth(item)) {
					if (event.getAction() == Action.RIGHT_CLICK_AIR && !player.isSneaking()) {
						if (!plugin.wallCooldown.contains(player.getUniqueId())) {
							plugin.wallCooldown.add(player.getUniqueId());
							Material material = Material.valueOf(plugin.totem.getConfig().getString(
									"Earth.Wall.Material"));
							long duration = plugin.totem.getConfig().getLong(
									"Earth.Wall.Duration");
							int x = 0;
							int z = 0;
							int w = plugin.totem.getConfig().getInt("Earth.Wall.Width");
							if (w % 2 == 0) w += 1;
							int h = plugin.totem.getConfig().getInt("Earth.Wall.Height");
							String dir = getCardinalDirection(player);
							if (!(dir == null)) {
								if (dir == "N") z = -1;
								else if (dir == "S") z = 1;
								else if (dir == "E") x = 1;
								else if (dir == "W") x = -1;
							}		
							player.playSound(player.getLocation(), Sound.BLOCK_GRASS_PLACE, 1000f, 1f);
							spawnWall(player.getLocation().getBlock().getLocation(), material, duration, x, z, w, h);
							plugin.wallBar.get(player).setCounter(0);
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					            public void run() {
					            	if (plugin.wallCooldown.contains(player.getUniqueId())) {
					            		plugin.wallCooldown.remove(player.getUniqueId());
					            	}
					            }            
					        }, getWallCooldown() * 20);
						}
					}
					if (event.getAction() == Action.RIGHT_CLICK_AIR && player.isSneaking()) {
						if (!plugin.domeCooldown.contains(player.getUniqueId())) {
							plugin.domeCooldown.add(player.getUniqueId());
							Material material = Material.valueOf(plugin.totem.getConfig().getString(
									"Earth.Dome.Material"));
							long duration = plugin.totem.getConfig().getLong(
									"Earth.Dome.Duration");
							int radius = plugin.totem.getConfig()
									.getInt("Earth.Dome.Radius");
							player.playSound(player.getLocation(), Sound.BLOCK_GRASS_PLACE, 1000f, 1f);
							spawnSphere(player.getLocation(), material, duration, radius);	
							plugin.domeBar.get(player).setCounter(0);
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					            public void run() {
					            	if (plugin.domeCooldown.contains(player.getUniqueId())) {
					            		plugin.domeCooldown.remove(player.getUniqueId());
					            	}
					            }            
					        }, getDomeCooldown() * 20);
						}
					}
				}
			}
		}
	}
	
	
 	@EventHandler
 	public void join(PlayerJoinEvent event) {
 		Player player = event.getPlayer();
 		String text = plugin.totem.getConfig().getString("Earth.Wall.Bar-Text");
 		String color = plugin.totem.getConfig().getString("Earth.Wall.Bar-Color").toUpperCase();
 		String text2 = plugin.totem.getConfig().getString("Earth.Wall.Bar-Text");
 		String color2 = plugin.totem.getConfig().getString("Earth.Dome.Bar-Color").toUpperCase();
 		BossBar bar1 = createBar(text, color);
 		BossBar bar2 = createBar(text2, color2);
 		plugin.wallBar.put(player, new TimeBar(plugin, getTotem(), player, bar1, getWallCooldown()));
 		plugin.domeBar.put(player, new TimeBar(plugin, getTotem(), player, bar2, getDomeCooldown()));
 	}	
	 
}
