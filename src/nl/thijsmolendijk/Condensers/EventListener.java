package nl.thijsmolendijk.Condensers;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EventListener implements Listener {
	public Main plugin;
	public EventListener(Main instance) {
		this.plugin = instance;
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (!e.getPlayer().getItemInHand().getItemMeta().hasDisplayName()) return;
		if (!e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GOLD+"Energy Condenser")) return;
		plugin.condensers.add(new Condenser(e.getBlockPlaced().getLocation()));
	}
	@EventHandler
	public void onInvOpen(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (e.getClickedBlock().getType() != Material.ENDER_CHEST) return;
		if (this.plugin.getCondenserAtLoc(e.getClickedBlock().getLocation()) == null) return;
		Condenser c = this.plugin.getCondenserAtLoc(e.getClickedBlock().getLocation());
		e.getPlayer().openInventory(c.getInventory());
		e.setCancelled(true);
	}
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		ItemStack i = e.getCurrentItem();
		if (i == null)
			i = e.getCursor();
		ItemMeta m = i.getItemMeta();
		if (i != null || m != null || i.getTypeId() != 0) {
			if (this.plugin.conf.getEMC(i) != 0) {
				if (i.getTypeId() != 36 || i.getType() != Material.TRIPWIRE) {
					if (i.getType() != Material.INK_SACK)
						m.setLore(Arrays.asList(ChatColor.GRAY+"Emc value: "+this.plugin.conf.getEMC(i)));
				}
			}
			i.setItemMeta(m);
		}
		if (e.getRawSlot() > 80) return;
		if (e.getRawSlot() == 63) {
			return;
		}
		if (e.getRawSlot() > 53) {
			e.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (this.plugin.getCondenserAtLoc(e.getBlock().getLocation()) == null) return;
		for (HumanEntity entity : this.plugin.getCondenserAtLoc(e.getBlock().getLocation()).inv.getViewers()) {
			entity.closeInventory();
		}
		for (ItemStack item : this.plugin.getCondenserAtLoc(e.getBlock().getLocation()).inv.getContents()) {
			if (item == null || item.getType() == Material.AIR) continue;
			if (item.getType() == Material.PISTON_MOVING_PIECE || item.getType() == Material.TRIPWIRE || item.getType() == Material.INK_SACK) continue;
			e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
		}
		this.plugin.removeCondenser(this.plugin.getCondenserAtLoc(e.getBlock().getLocation()));
	}
	@EventHandler
	public void onTryToPickup(PlayerPickupItemEvent e) {
		for (Condenser c : this.plugin.condensers) {
			if (c.hoveringItem == null) continue;
			if (c.hoveringItem.getEntityId() == e.getItem().getEntityId()) {
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent e) {
		for (Condenser c : this.plugin.condensers) {
			if (c.hoveringItem == null) continue;
			if (c.hoveringItem.getEntityId() == e.getEntity().getEntityId()) {
				e.setCancelled(true);
			}
		}
	}
}
