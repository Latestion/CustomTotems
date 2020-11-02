package me.Latestion.Totems;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

import me.Latestion.Totem.Main;

public class TotemEvents extends Totems implements Listener {

	private Main plugin;
	public TotemEvents(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void dead(EntityResurrectEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		if (hasTotem(player)) {
			if (plugin.allTotems.contains(getTotem(player))) {
				event.setCancelled(true);
			}
		}
	}
}
