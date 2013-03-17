package nl.thijsmolendijk.Condensers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public List<Condenser> condensers = new ArrayList<Condenser>();
	public EMCConfig conf;
	public static Main instance;
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		new CondensTimer(this, this.getConfig().getInt("condense-interval"));
		instance = this;
		try {
			this.loadCondensers();
		} catch (IOException e) {
			System.out.println("There was a error enabling Condensers! \n Condensers will disable itself.");
			this.setEnabled(false);
		}
		conf = new EMCConfig(this);
		try {
			conf.loadEMCConfig();
		} catch (Exception e) {
			System.out.println("There was a error enabling Condensers! \n Condensers will disable itself.");
			this.setEnabled(false);
		}
		new EventListener(this);
		ShapedRecipe condenserRecipe = new ShapedRecipe(this.condenser());
		condenserRecipe.shape("ODO", "DBD", "ODO");
		condenserRecipe.setIngredient('O', Material.OBSIDIAN);
		condenserRecipe.setIngredient('D', Material.DIAMOND);
		if (this.getConfig().getBoolean("expensive-recipe")) {
			condenserRecipe.setIngredient('B', Material.DIAMOND_BLOCK);
		} else {
			condenserRecipe.setIngredient('B', Material.CHEST);
		}
		this.getServer().addRecipe(condenserRecipe);
	}
	@Override
	public void onDisable() {
		this.saveCondensers();
	}
	
	
	//LOADING CONDENSERS
	public void loadCondensers() throws IOException {
		File dir = new File(this.getDataFolder()+"/condensers/");
		if (!dir.exists()) dir.mkdir();
		  for (File child : dir.listFiles()) {
		    if (child.getName().contains(".condenser")) {
		    	this.loadCondenser(child);
		    }
		  }
	}
	@SuppressWarnings("unchecked")
	public void loadCondenser(File f) {
		FileConfiguration cfg = null;
        cfg = YamlConfiguration.loadConfiguration(f);
        World world = this.getServer().getWorld((String) cfg.get("world"));
        Location l = new Location(world, Integer.parseInt(f.getName().split(":")[0]), Integer.parseInt(f.getName().split(":")[1]), Integer.parseInt(f.getName().split(":")[2]));
        int currentEmc = cfg.getInt("currentEmc");
        int maxEmc = cfg.getInt("maxEmc");
        ItemStack[] contents = new ItemStack[81];
        if (cfg.getList("CONTENT") instanceof List<?>) {
        	List<ItemStack> Inv = (List<ItemStack>) cfg.getList("CONTENT");
        	for (int i = 0; i < Inv.size(); i++) {
        		contents[i] = Inv.get(i);
        	}
        }
        this.condensers.add(new Condenser(l, contents, currentEmc, maxEmc));
	}
	
	
	//SAVING CONDENSERS
	public void saveCondensers() {
		for (Condenser c : this.condensers) {
			if (c.hoveringItem != null)
				c.hoveringItem.remove();
			writeCondenser(c.inv.getContents(), c.location, c.currentEMC, c.maxEMC, this);
		}
	}
	
	//CONDENSER ITEMSTACK
	public ItemStack condenser() {
		ItemStack i = new ItemStack(Material.ENDER_CHEST);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(ChatColor.GOLD+"Energy Condenser");
		i.setItemMeta(m);
		return i;
	}
	
	public Condenser getCondenserAtLoc(Location loc) {
		Condenser c = null;
		for (Condenser con : this.condensers) {
			if (con.location.equals(loc))
				c = con;
		}
		return c;
	}
	
	public Condenser getCondenserWithInv(Inventory inv) {
		Condenser c = null;
		for (Condenser con : this.condensers) {
			if (con.inv.equals(inv))
				c = con;
		}
		return c;
	}
	
	public void removeCondenser(Condenser c) {
		if (c.hoveringItem != null)
			c.hoveringItem.remove();
		this.condensers.remove(c);
		Location loc = c.location;
		String path = String.valueOf(loc.getBlockX())+":"+String.valueOf(loc.getBlockY())+":"+String.valueOf(loc.getBlockZ())+":.condenser";
		File f = new File(this.getDataFolder()+"/condensers", path);
		if (f.exists())
			f.delete();
	}
	
	
	public static void writeCondenser(ItemStack[] inv, Location loc, int currentEMC, int maxEMC, Main plugin) {
		String path = String.valueOf(loc.getBlockX())+":"+String.valueOf(loc.getBlockY())+":"+String.valueOf(loc.getBlockZ())+":";
		FileConfiguration cfg = null;
		File file = new File(plugin.getDataFolder()+"/condensers", path + ".condenser");
		cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("CONTENT", inv);
		cfg.set("currentEmc", currentEMC);
		cfg.set("maxEmc", maxEMC);
		cfg.set("world", loc.getWorld().getName());
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
