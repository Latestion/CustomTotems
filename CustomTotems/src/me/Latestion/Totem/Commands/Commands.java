package me.Latestion.Totem.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Latestion.Totem.Main;
import me.Latestion.Totems.TotemCheck;
import me.Latestion.Totems.EarthTotem.Earth;
import me.Latestion.Totems.SpiritsTotem.Spirit;
import me.Latestion.Totems.StormsTotem.Storm;
import me.Latestion.Totems.TimeTotem.Time;
import me.Latestion.Totems.Unliving.Unliving;
import me.Latestion.Totems.VoidTotem.Voids;

public class Commands implements CommandExecutor {
	
	private Main plugin;
	public Commands(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("totems")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "/totems give {player} {totemname}" 
					+ ChatColor.WHITE + ": Gives the mention playerd the item!");
			}
			else {
				if (sender.hasPermission("totems.give")) {
					if (args[0].equalsIgnoreCase("give")) {
						if (args.length == 3) {
							Player player;
							try {
								player = Bukkit.getPlayerExact(args[1]);
							}
							catch (Exception e) {
								sender.sendMessage(ChatColor.RED + "Invalid Player!");
								return false;
							}
							if (args[2].equalsIgnoreCase("time")) {
								Time time  = new Time(plugin);
								player.getInventory().addItem(time.getTotem());
							}
							if (args[2].equalsIgnoreCase("storm")) {
								Storm storm = new Storm(plugin);
								player.getInventory().addItem(storm.getTotem());
							}
							if (args[2].equalsIgnoreCase("belt")) {
								TotemCheck check = new TotemCheck(plugin);
								player.getInventory().addItem(check.belt());
							}
							if (args[2].equalsIgnoreCase("spirit")) {
								Spirit spirit = new Spirit(plugin);
								player.getInventory().addItem(spirit.getTotem());
							}
							if (args[2].equalsIgnoreCase("void")) {
								Voids voids = new Voids(plugin);
								player.getInventory().addItem(voids.getTotem());
							}
							if (args[2].equalsIgnoreCase("unliving")) {
								Unliving unliving = new Unliving(plugin);
								player.getInventory().addItem(unliving.getTotem());
							}
							if (args[2].equalsIgnoreCase("earth")) {
								Earth unliving = new Earth(plugin);
								player.getInventory().addItem(unliving.getTotem());
							}
						}
					}
				}
			}
			
		}
		return false;
	}
}
