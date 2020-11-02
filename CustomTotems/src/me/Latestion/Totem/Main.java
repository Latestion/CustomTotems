package me.Latestion.Totem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.Latestion.Totem.Commands.Commands;
import me.Latestion.Totem.Files.DataManager;
import me.Latestion.Totem.Files.TotemManager;
import me.Latestion.Totem.Utils.TimeBar;
import me.Latestion.Totems.TotemCheck;
import me.Latestion.Totems.TotemEvents;
import me.Latestion.Totems.EarthTotem.Earth;
import me.Latestion.Totems.EarthTotem.EarthEvents;
import me.Latestion.Totems.SpiritsTotem.Spirit;
import me.Latestion.Totems.SpiritsTotem.SpiritEvents;
import me.Latestion.Totems.StormsTotem.Storm;
import me.Latestion.Totems.StormsTotem.StormEvents;
import me.Latestion.Totems.TimeTotem.Time;
import me.Latestion.Totems.TimeTotem.TimeEvents;
import me.Latestion.Totems.Unliving.Unliving;
import me.Latestion.Totems.Unliving.UnlivingEvents;
import me.Latestion.Totems.VoidTotem.CreatePortalTeleportation;
import me.Latestion.Totems.VoidTotem.DeletePortal;
import me.Latestion.Totems.VoidTotem.VoidEvents;
import me.Latestion.Totems.VoidTotem.VoidInv;
import me.Latestion.Totems.VoidTotem.Voids;

public class Main extends JavaPlugin {
	
	public TotemManager totem;
	public DataManager data;
	
	public List<Inventory> invs = new ArrayList<>();
	
	public List<ItemStack> allTotems = new ArrayList<ItemStack>();
	
	public List<UUID> astralCooldown = new ArrayList<UUID>(); // Time
	public List<UUID> freezeCooldown = new ArrayList<UUID>(); // Time
	public Map<Player, TimeBar> astralBar = new HashMap<>(); // Time
	public Map<Player, TimeBar> freezeBar = new HashMap<>(); // Time
	
	public List<UUID> shadowSneakCooldown = new ArrayList<>(); // Void
	public List<UUID> teleportCooldown = new ArrayList<>(); // Void
	public Map<Player, VoidInv> voidInv = new HashMap<>(); // Void
	public Map<Player, ItemStack> itemAddInstance = new HashMap<>(); // Void
	public List<Player> cache = new ArrayList<>(); // Void
	public Map<Player, TimeBar> shadowBar = new HashMap<>(); // Void
	public Map<Player, TimeBar> warpBar = new HashMap<>(); // Void
	public Map<Player, CreatePortalTeleportation> portalLocation = new HashMap<>(); // Void
	public Map<Player, DeletePortal> deletePortal = new HashMap<Player, DeletePortal>(); // Void
	
	public List<UUID> weatherCooldown = new ArrayList<UUID>(); // Storm
	public List<UUID> strikeCooldown = new ArrayList<UUID>(); // Storm
	public Map<Player, TimeBar> weatherBar = new HashMap<>(); // Storm
	public Map<Player, TimeBar> strikeBar = new HashMap<>(); // Storm
	
	public List<UUID> undeadSpawnCooldown = new ArrayList<>(); // Unliving
	public Map<UUID, Entity> ownerUndead = new HashMap<UUID, Entity>(); // Unliving
	public Map<Player, TimeBar> undeadBar = new HashMap<>(); // Unliving
	
	public List<UUID> spiritWorlCooldown = new ArrayList<UUID>(); // Spirit
	public Map<UUID, Location> spiritWorld = new HashMap<UUID, Location>(); // Spirit
	public Map<UUID, Location> deadSpiritSpec = new HashMap<UUID, Location>(); // Spirit
	public Map<Player, TimeBar> spiritBar = new HashMap<>(); // Spirit
	
	public List<UUID> rockBlastCooldown = new ArrayList<UUID>(); // Earth
	public List<UUID> rippleCooldown = new ArrayList<UUID>(); // Earth
	public List<UUID> domeCooldown = new ArrayList<UUID>(); // Earth
	public List<UUID> wallCooldown = new ArrayList<UUID>(); // Earth
	public Map<Player, TimeBar> rockBlastBar = new HashMap<>(); // Earth
	public Map<Player, TimeBar> rippleBar = new HashMap<>(); // Earth
	public Map<Player, TimeBar> domeBar = new HashMap<>(); // Earth
	public Map<Player, TimeBar> wallBar = new HashMap<>(); // Earth
	
	@Override
	public void onEnable() {
		this.totem = new TotemManager(this);
		this.data = new DataManager(this);
		registerEvents();
		totems();
		setInv();
		this.getCommand("totems").setExecutor(new Commands(this));
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@SuppressWarnings("unused")
	private void totems() {
		
		// TotemCheck
		TotemCheck check = new TotemCheck(this);
		check.cast();
		
		// Time Totem
		Time t = new Time(this);
		
		// Void Totem
		Voids voids = new Voids(this);
		
		// Storm Totem
		Storm s = new Storm(this);
		
		// Unliving Totem
		Unliving unliving = new Unliving(this);
			
		// Spirit Totem
		Spirit spirit = new Spirit(this);
		
		// Earth Totem
		Earth earth = new Earth(this);
	}

	private void registerEvents() {
		this.getServer().getPluginManager().registerEvents(new TotemEvents(this), this);
		this.getServer().getPluginManager().registerEvents(new TimeEvents(this), this);
		this.getServer().getPluginManager().registerEvents(new VoidEvents(this), this);
		this.getServer().getPluginManager().registerEvents(new StormEvents(this), this);
		this.getServer().getPluginManager().registerEvents(new UnlivingEvents(this), this);
		this.getServer().getPluginManager().registerEvents(new SpiritEvents(this), this);
		this.getServer().getPluginManager().registerEvents(new EarthEvents(this), this);
	}
	
	private void setInv() {
		Inventory inv = Bukkit.createInventory(null, 54, "Select Your Block");
		inv.setItem(45, new ItemStack(Material.SPECTRAL_ARROW));
		inv.setItem(53, new ItemStack(Material.SPECTRAL_ARROW));
		int i = 0;
		for (Material mat : Material.values()) {
			if (mat.toString().contains("legacy")) {
				continue;
			}
			ItemStack item = new ItemStack(mat);
			inv.setItem(i, item);
			i++;
			if (i == 45) {
				Inventory set;
				set = inv;
				invs.add(set);
				inv = Bukkit.createInventory(null, 54, "Select Your Block");
				inv.setItem(45, new ItemStack(Material.SPECTRAL_ARROW));
				inv.setItem(53, new ItemStack(Material.SPECTRAL_ARROW));
				i = 0;
			}
		}
	}
}
