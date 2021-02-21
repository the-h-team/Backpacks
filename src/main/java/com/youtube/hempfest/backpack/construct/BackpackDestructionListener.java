package com.youtube.hempfest.backpack.construct;

import com.github.sanctum.labyrinth.Labyrinth;
import com.youtube.hempfest.backpack.api.BackpackAPI;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BackpackDestructionListener implements Listener {


	@EventHandler(priority = EventPriority.NORMAL)
	public void onDestroy(EntityDamageByBlockEvent e) {
		if (e.getEntity() instanceof Item) {
			ItemStack item = ((Item) e.getEntity()).getItemStack();
			if (BackpackAPI.getBackpack(item) != null) {
				Backpack pack = BackpackAPI.getBackpack(item);
				BackpackAPI.remove(pack);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onDestroy(ItemDespawnEvent e) {
		ItemStack item = e.getEntity().getItemStack();
		if (BackpackAPI.getBackpack(item) != null) {
			Backpack pack = BackpackAPI.getBackpack(item);
			BackpackAPI.remove(pack);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onDestroy(PlayerDeathEvent e) {
		Player p = e.getEntity();
		final Location death = p.getLocation().add(0, 2, 0);
		final ItemStack[] items = p.getInventory().getContents();
		for (ItemStack item : items) {
			if (BackpackAPI.getBackpack(item) != null) {
				final Backpack pack = BackpackAPI.getBackpack(item);
				for (ItemStack backpackItem : BackpackAPI.getContents(pack)) {
					if (backpackItem != null) {
						new BukkitRunnable() {
							@Override
							public void run() {
								p.getWorld().dropItemNaturally(death, backpackItem);
							}
						}.runTaskLater(Labyrinth.getInstance(), 20);
					}
				}
				BackpackAPI.remove(pack);
				item.setAmount(0);
			}
		}
	}

}
