package nl.thijsmolendijk.Condensers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Tools {
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
	public static void addMapToInventory(ItemStack[] map, Inventory inv) {
		inv.setContents(map);
	}
	@SuppressWarnings("unchecked")
	public static ItemStack[] get(String name, Plugin plg){
	        FileConfiguration cfg = null;
	        File file = new File(plg.getDataFolder()+"/condensers", name + ".condenser");
	        cfg = YamlConfiguration.loadConfiguration(file);
	        List<ItemStack> Inv = (List<ItemStack>) cfg.getList("CONTENT");
	        return (ItemStack[]) Inv.toArray();
	}
	public static float percentage(int one, int two) {
		float correct = (float) one;
		float questionNum = (float) two;
		return (correct/questionNum)*100.f;
	}
}
