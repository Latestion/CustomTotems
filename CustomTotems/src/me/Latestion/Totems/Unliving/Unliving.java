package me.Latestion.Totems.Unliving;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import me.Latestion.Totem.Main;
import me.Latestion.Totems.Totems;

public class Unliving extends Totems {

	public Main plugin;
	
	List<EntityType> ens = new ArrayList<EntityType>();
	
	public Unliving(Main plugin) {
		this.plugin = plugin;
		if (!plugin.allTotems.contains(getTotem())) plugin.allTotems.add(getTotem());
		if (ens.isEmpty()) {
			ens.add(EntityType.ZOMBIE);
			ens.add(EntityType.SKELETON);
			ens.add(EntityType.WITHER_SKELETON);
		}
	}

	public ItemStack getTotem() {
		return totem("Unliving", plugin.totem);
	}
	
	public boolean isUnliving(ItemStack item) {
		if (item.isSimilar(getTotem())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public EntityType getRandomMob() {
		Random ran = new Random();
		int index = ran.nextInt(ens.size());
		return ens.get(index);
	}
	
	public int getUndeadSpawnCooldown() {
		return plugin.totem.getConfig().getInt("Unliving.Undead-Spawn-Cooldown");
	}
	
	public UUID getUndeadOwner(Entity en) {
		for (UUID id : plugin.ownerUndead.keySet()) {
			if (plugin.ownerUndead.get(id) == en) {
				return id;
			}
		}
		return null;
	}
	
}
