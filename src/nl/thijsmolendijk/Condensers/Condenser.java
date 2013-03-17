package nl.thijsmolendijk.Condensers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class Condenser {
	public int currentEMC;
	public int maxEMC;
	public Location location;
	public ItemStack currentItem;
	public Inventory inv;
	public Item hoveringItem;
	public Condenser(Location loc) {
		this.inv = Bukkit.getServer().createInventory(null, 81, "Energy Condenser");
		this.inv.setContents(new ItemStack[81]);
		this.addTripWire();
		this.addDyes();
		this.currentEMC = 0;
		this.maxEMC = 0;
		this.location = loc;
		this.currentItem = new ItemStack(Material.AIR);
	}
	public Condenser(Location loc, ItemStack[] inv, int curEMC, int mEMC) {
		this.inv = Bukkit.getServer().createInventory(null, 81, "Energy Condenser");
		this.addTripWire();
		this.addDyes();
		this.inv.setContents(inv);
		this.location = loc;
		this.currentEMC = curEMC;
		this.maxEMC = mEMC;
		this.currentItem = inv[0];
	}
	public Inventory getInventory() {
		return inv;
	}
	public void addTripWire() {
		ItemStack[] stack = new ItemStack[81];
		for (int in = 54; in < 63; in++) {
			ItemStack i = new ItemStack(Material.TRIPWIRE);
			ItemMeta m = i.getItemMeta();
			m.setDisplayName(ChatColor.ITALIC+"");
			i.setItemMeta(m);
			stack[in] = i;
		}
		for (int inn = 64; inn < 72; inn++) {
			ItemStack i = new ItemStack(Material.PISTON_MOVING_PIECE);
			ItemMeta m = i.getItemMeta();
			m.setDisplayName(ChatColor.ITALIC+"");
			i.setItemMeta(m);
			stack[inn] = i;
		}
		this.inv.setContents(stack);
	}
	public void addDyes() {
		//73-80
		float percentage = 0;
		if (this.maxEMC != 0) {
			percentage = Tools.percentage(this.currentEMC, this.maxEMC);
		}
		if (percentage < 11.1) {
			this.addGreyDye(72, 81);
			return;
		}
		if (percentage < 22.2) {
			this.addGreenDye(72, 73);
			this.addGreyDye(73, 81);
			return;
		}
		if (percentage < 33.3) {
			this.addGreenDye(72, 74);
			this.addGreyDye(74, 81);
			return;
		}
		if (percentage < 44.4) {
			this.addGreenDye(72, 75);
			this.addGreyDye(75, 81);
			return;
		}
		if (percentage < 55.5) {
			this.addGreenDye(72, 76);
			this.addGreyDye(76, 81);
			return;
		}
		if (percentage < 66.6) {
			this.addGreenDye(72, 77);
			this.addGreyDye(77, 81);
			return;
		}
		if (percentage < 77.7) {
			this.addGreenDye(72, 78);
			this.addGreyDye(78, 81);
			return;
		}
		if (percentage < 88.8) {
			this.addGreenDye(72, 79);
			this.addGreyDye(79, 81);
			return;
		}
		if (percentage < 99.9) {
			this.addGreenDye(72, 81);
			return;
		}
	}
	public void addGreyDye(int start, int finish) {
		for (int in = start; in < finish; in++) {
			ItemStack i = new ItemStack(Material.INK_SACK, 1, (short) 8);
			ItemMeta m = i.getItemMeta();
			m.setDisplayName(ChatColor.GRAY+""+this.currentEMC+"/"+this.maxEMC);
			i.setItemMeta(m);
			this.inv.setItem(in, i);
		}
	}
	public void addGreenDye(int start, int finish) {
		for (int in = start; in < finish; in++) {
			ItemStack i = new ItemStack(Material.INK_SACK, 1, (short) 10);
			ItemMeta m = i.getItemMeta();
			m.setDisplayName(ChatColor.GREEN+""+this.currentEMC+"/"+this.maxEMC);
			i.setItemMeta(m);
			this.inv.setItem(in, i);
		}
	}
	public void updateCurrentItem() {
		this.currentItem = inv.getItem(63);
		if (Main.instance.getConfig().getBoolean("show-hovering-item")) {
			if (this.hoveringItem != null)
				this.hoveringItem.remove();
			if (this.currentItem != null) {
				this.hoveringItem = this.location.getWorld().dropItemNaturally(this.location, this.currentItem);
				this.hoveringItem.setVelocity(new Vector(0,.2,0));
			}
		}
		this.maxEMC = (int) Main.instance.conf.getEMC(this.currentItem);
		this.addDyes();
	}
	
	public ItemStack[] getChestContents() {
		ItemStack[] returning = new ItemStack[53];
		for (int i = 0; i < 53; i++) {
			returning[i] = this.inv.getItem(i);
		}
		return returning;
	}
}
