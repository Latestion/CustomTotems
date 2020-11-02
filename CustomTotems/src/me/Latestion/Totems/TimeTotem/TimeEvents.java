package me.Latestion.Totems.TimeTotem;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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

public class TimeEvents extends Time implements Listener {
	
	public TimeEvents(Main plugin) {
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
				if (isTime(item)) {
					if (event.getAction() == Action.RIGHT_CLICK_AIR) {
						if (plugin.astralCooldown.contains(player.getUniqueId())) {
							return;
						}		
						event.setCancelled(true);
						player.openInventory(inv);
						plugin.astralCooldown.add(player.getUniqueId());
						int sec = getAstralCooldown();
						plugin.astralBar.get(player).setCounter(0);
				        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				            public void run() {
				            	if (plugin.astralCooldown.contains(player.getUniqueId())) {
				            		plugin.astralCooldown.remove(player.getUniqueId());
				            	}
				            }            
				        }, sec * 20);
					}
					else if (event.getAction() == Action.LEFT_CLICK_AIR && player.isSneaking()) {
						if (plugin.freezeCooldown.contains(player.getUniqueId())) {
							return;
						}
						event.setCancelled(true);
						plugin.freezeCooldown.add(player.getUniqueId());
						int radius = getFreezeRadius();
						int sec = getFreezeDuration();
						int sec2 = getFreezeCooldown();
						Location loc = player.getLocation();
						plugin.freezeBar.get(player).setCounter(0);
						Collection<Entity> ens = loc.getWorld().getNearbyEntities(loc, radius, radius, radius);
						for (Entity en : ens) {
							if (en instanceof LivingEntity && !en.isDead()) {
								if (en instanceof Player) {
									if (((Player)en).equals(player)) {
										continue;
									}
								}
								((LivingEntity)en).setAI(false);
							}
						}
				        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				            public void run() {
				            	for (Entity en : ens) {
				            		if (en instanceof LivingEntity && !en.isDead()) {
										if (en instanceof Player) {
											if (((Player)en).equals(player)) {
												continue;
											}
										}
										((LivingEntity)en).setAI(true);
									}
				            	}
				            }            
				        }, sec * 20);
				        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				            public void run() {
				            	if (plugin.freezeCooldown.contains(player.getUniqueId())) {
				            		plugin.freezeCooldown.remove(player.getUniqueId());
				            	}
				            }            
				        }, sec2 * 20);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void click(InventoryClickEvent event) {
		if (event.getInventory().equals(inv)) {
			if (event.getClickedInventory() instanceof PlayerInventory) {
				return;
			}
			if (event.getWhoClicked() instanceof Player) {
				event.setCancelled(true);
				Player player = (Player) event.getWhoClicked();
				ItemStack item = event.getCurrentItem();
				if (item.getType() != Material.CLOCK) return;
				int hour = Integer.parseInt((item.getItemMeta().getDisplayName().split(": ")[1]));
				player.getLocation().getWorld().setTime(getTick(hour));
				player.closeInventory();
			}
		}
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String text = plugin.totem.getConfig().getString("Time.Astral-Bossbar-Text");
		String color = plugin.totem.getConfig().getString("Time.Astral-Bossbar-Color").toUpperCase();
		String text2 = plugin.totem.getConfig().getString("Time.Freeze-Bossbar-Text");
		String color2 = plugin.totem.getConfig().getString("Time.Freeze-Bossbar-Color").toUpperCase();
		BossBar bar1 = createBar(text, color);
		BossBar bar2 = createBar(text2, color2);
		plugin.astralBar.put(player, new TimeBar(plugin, getTotem(), player, bar1, getAstralCooldown()));
		plugin.freezeBar.put(player, new TimeBar(plugin, getTotem(), player, bar2, getFreezeCooldown()));
	}	
}
