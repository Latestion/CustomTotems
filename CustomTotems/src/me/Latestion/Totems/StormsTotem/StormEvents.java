package me.Latestion.Totems.StormsTotem;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.Latestion.Totem.Main;
import me.Latestion.Totem.Utils.TimeBar;

public class StormEvents extends Storm implements Listener {

	public StormEvents(Main plugin) {
		super(plugin);
	}

	@EventHandler
	public void interact(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (hasTotem(player)) {
			if (getTotem(player) == null) {
				return;
			}
			else {
				ItemStack item = getTotem(player);
				if (isStorm(item)) {
					if (event.getAction() == Action.RIGHT_CLICK_AIR) {
						if (plugin.weatherCooldown.contains(player.getUniqueId())) {
							// Do nothing
						}
						else {
							plugin.weatherCooldown.add(player.getUniqueId());
							int secs = plugin.totem.getConfig().getInt("Storms.Weather-Change-Cooldown");
							player.openInventory(inv);
							plugin.weatherBar.get(player).setCounter(0);
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					            public void run() {
					            	if (plugin.weatherCooldown.contains(player.getUniqueId())) {		
					            		plugin.weatherCooldown.remove(player.getUniqueId());
					            	}
					            } 
					        }, secs * 20);
						}
					}
					else {
						if (event.getAction() == Action.LEFT_CLICK_AIR) {
							if (plugin.strikeCooldown.contains(player.getUniqueId())) {
								// Do nothing
							}
							else {
								int range = plugin.totem.getConfig().getInt("Storms.Lightning-Strike-Max-Distance");
								int secs = plugin.totem.getConfig().getInt("Storms.Lightning-Strike-Cooldown");
								Entity en = getEntityLookingAtEntity(player, range);
								if (en == null) {
									return;
								}
								plugin.strikeCooldown.add(player.getUniqueId());
								en.getLocation().getWorld().strikeLightning(en.getLocation());
								plugin.strikeBar.get(player).setCounter(0);
								Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						            public void run() {
						            	if (plugin.strikeCooldown.contains(player.getUniqueId())) {		
						            		plugin.strikeCooldown.remove(player.getUniqueId());
						            	}
						            } 
						        }, secs * 20);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (event.getInventory() instanceof PlayerInventory) {
			return;
		}
		if (event.getClickedInventory() == null) {
			return;
		}
		if (event.getClickedInventory().equals(inv)) {
			Player player = (Player) event.getWhoClicked();
			World world = player.getLocation().getWorld();
			int secs = plugin.totem.getConfig().getInt("Storms.Weather-Change-Duration");
			event.setCancelled(true);
			player.closeInventory();
			if (event.getSlot() == 2) {
				world.setStorm(true);
		        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		            public void run() {
		            	if (world.hasStorm()) {
		            		world.setStorm(false);
		            	}
		            	else {
		            		// Do nothing
		            	}
		            }            
		        }, secs * 20);
			}
			else if (event.getSlot() == 4) {
				world.setStorm(false);
				world.setThundering(false);
			}
			else if (event.getSlot() == 6) {
				world.setThunderDuration(secs);
				world.setStorm(true);
		        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		            public void run() {
		            	if (world.hasStorm()) {
		            		world.setStorm(false);
		            	}
		            	else {
		            		// Do nothing
		            	}
		            }            
		        }, secs * 20);
			}
		}
	}
	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String text = plugin.totem.getConfig().getString("Storms.Weather-Bossbar-Text");
		String color = plugin.totem.getConfig().getString("Storms.Weather-Bossbar-Color").toUpperCase();
		String text2 = plugin.totem.getConfig().getString("Storms.Strike-Bossbar-Text");
		String color2 = plugin.totem.getConfig().getString("Storms.Strike-Bossbar-Color").toUpperCase();
		BossBar bar1 = createBar(text, color);
		BossBar bar2 = createBar(text2, color2);
		bar1.addPlayer(player);
		bar2.addPlayer(player);
		plugin.weatherBar.put(player, new TimeBar(plugin, getTotem(), player, bar1, getWeatherChangeCooldown()));
		plugin.strikeBar.put(player, new TimeBar(plugin, getTotem(), player, bar2, getLightningStrikeCooldown()));
	}	
}
