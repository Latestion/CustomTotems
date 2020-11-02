package me.Latestion.Totem.Utils;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.Latestion.Totem.Main;

public class TimeBar extends BukkitRunnable {

	private double sec;
	private double counter;
	private BossBar bar;
	private Player player;
	private ItemStack item;
	private TotemUtils utils;
	
	public TimeBar(Main plugin, ItemStack item, Player player, BossBar bar, double seconds) {
		this.sec = seconds;
		this.bar = bar;
		this.counter = seconds;
		this.player = player;
		this.item = item;
		this.utils = new TotemUtils();
		bar.addPlayer(player);
		runTaskTimer(plugin, 0, 1);
	}

	public void setCounter(double d) {
		counter = d;
	}
	
	public double getCooldown() {
		return sec;
	}

	public double getRemainingSeconds() {
		return counter;
	}
	
	public BossBar getBar() {
		return bar;
	}
	
	public void stop() {
		this.cancel();
	}
	
	private void hideBar() {
		bar.setVisible(false);
	}
	
	private void showBar() {
		bar.setVisible(true);
	}
	
	private void counterSetProg() {
		double prog = counter / sec;
		bar.setProgress(prog);
		counter = counter + 0.05;
	}
	
	@Override
	public void run() {
		/*
		 * Sec == Cooldown
		 * 1 = Done!
		 * 
		 * Current Seconds / Total Seconds = Set Bar progress
		 */
		
		if (!player.isOnline()) {
			this.cancel();
		}
		
		if (utils.hasTotem(player)) {
			ItemStack pItem = utils.getTotem(player);
			if (pItem.isSimilar(item)) {	
				if (sec >= 2) {
					showBar();
				}
				else {
					hideBar();
				}
				if (counter == sec) {
					hideBar();
				}
			}
			else {
				hideBar();
			}
		}
		else {
			hideBar();
		}
		
		if ((counter / sec) < 1.0) {
			counterSetProg();
		}
	}
}
