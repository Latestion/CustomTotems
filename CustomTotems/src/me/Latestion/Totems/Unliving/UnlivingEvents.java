package me.Latestion.Totems.Unliving;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.Latestion.Totem.Main;
import me.Latestion.Totem.Utils.TimeBar;

public class UnlivingEvents extends Unliving implements Listener {

	public UnlivingEvents(Main plugin) {
		super(plugin);
	}

	@EventHandler
	public void onTarget(EntityTargetEvent event) {
		if (event.getTarget() instanceof Player) {
			Player player = (Player) event.getTarget();
			if (hasTotem(player)) {
				if (getTotem(player) == null) {
					return;
				}
				ItemStack item = getTotem(player);
				if (isUnliving(item)) {
					event.setCancelled(true);
				}
			}
			if (plugin.ownerUndead.containsKey(player.getUniqueId())) {
				if (event.getEntity() == plugin.ownerUndead.get(player.getUniqueId())) {
					event.setCancelled(true);
				}
			}
		}
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
				if (isUnliving(item)) {
					if (event.getAction() == Action.RIGHT_CLICK_AIR) {
						// Summon Undead
						plugin.undeadSpawnCooldown.add(event.getPlayer().getUniqueId());
						Location loc = event.getPlayer().getLocation();
						LivingEntity en = (LivingEntity) loc.getWorld().spawnEntity(loc, getRandomMob());
						plugin.ownerUndead.put(player.getUniqueId(), en);
						en.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
						en.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 0));
						plugin.undeadBar.get(player).setCounter(0);
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				            public void run() {
				            	if (!en.isDead()) {
				            		plugin.ownerUndead.remove(player.getUniqueId());
				            		en.remove();
				            	}
				            }
				        }, 120 * 20);
						Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				            public void run() {
				            	if (plugin.undeadSpawnCooldown.contains(player.getUniqueId())) {
				            		plugin.undeadSpawnCooldown.remove(player.getUniqueId());
				            	}
				            }
				        }, getUndeadSpawnCooldown() * 20);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEnDeath(EntityDeathEvent event) {
		if (plugin.ownerUndead.containsValue(event.getEntity())) {
			plugin.ownerUndead.remove(getUndeadOwner(event.getEntity()));
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String text = plugin.totem.getConfig().getString("Unliving.Bossbar-Text");
		String color = plugin.totem.getConfig().getString("Unliving.Bossbar-Color").toUpperCase();	
		BossBar bar1 = createBar(text, color);
		plugin.undeadBar.put(player, new TimeBar(plugin, getTotem(), player, bar1, getUndeadSpawnCooldown()));
	}
}
