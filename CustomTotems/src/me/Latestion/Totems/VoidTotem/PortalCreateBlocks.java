package me.Latestion.Totems.VoidTotem;

import org.bukkit.entity.Player;

import me.Latestion.Totem.Main;

public class PortalCreateBlocks {

	private Main plugin;
	private Player player;
	 
	public PortalCreateBlocks(Main plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
	}
	
	public void openInventory() {
		player.openInventory(plugin.invs.get(0));
	}
}
