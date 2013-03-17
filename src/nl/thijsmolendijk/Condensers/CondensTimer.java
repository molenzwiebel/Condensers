package nl.thijsmolendijk.Condensers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CondensTimer {
	public Main plugin;
	public int taskID;
	public CondensTimer(Main instance, int time) {
		this.plugin = instance;
		taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {			
			public void run() {
				for (Condenser c : plugin.condensers) {
					c.updateCurrentItem();
					if (c.currentItem == null || c.currentItem.getType() == Material.AIR) continue;
					if (c.maxEMC == 0) continue;
					boolean shouldStop = true;
					boolean found = false;
					do {
						for (int in = 0; in < 53; in ++) {
							if (found) continue;
							ItemStack i = c.inv.getItem(in);
							if (i == null || i.getType() == Material.AIR) continue;
							if (i.isSimilar(c.currentItem)) {
								continue;
							}
							if (Main.instance.conf.getEMC(i) == 0) continue;
							c.currentEMC = c.currentEMC + (int) Main.instance.conf.getEMC(i);
							c.addDyes();
							i.setAmount(i.getAmount()-1);
							c.inv.setItem(in, i);
							shouldStop = false;
							found = true;
						}
						shouldStop = false;
					} while (shouldStop);
					if (c.currentEMC >= c.maxEMC) {
						ItemStack i = new ItemStack(c.currentItem);
						i.setAmount(1);
						c.inv.addItem(i);
						c.currentEMC = c.currentEMC - c.maxEMC;
						c.addDyes();
					}
				}
			}
		}, 0L, (time * 2));
	}
}
