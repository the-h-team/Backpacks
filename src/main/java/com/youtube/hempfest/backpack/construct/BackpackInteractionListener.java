package com.youtube.hempfest.backpack.construct;

import com.github.sanctum.labyrinth.Labyrinth;
import com.github.sanctum.labyrinth.library.HUID;
import com.github.sanctum.labyrinth.library.Message;
import com.github.sanctum.labyrinth.library.StringUtils;
import com.youtube.hempfest.backpack.api.BackpackAPI;
import com.youtube.hempfest.backpack.event.BackpackCloseEvent;
import com.youtube.hempfest.backpack.event.BackpackCreateEvent;
import com.youtube.hempfest.backpack.event.BackpackOpenEvent;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class BackpackInteractionListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onOpen(PlayerInteractEvent e) {
		if (Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK).contains(e.getAction())) {
			if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.TRAPPED_CHEST) {
				ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
				if (BackpackAPI.getBackpack(item) == null) {
					if (item.hasItemMeta()) {
						if (item.getItemMeta().hasDisplayName()) {
							if (item.getItemMeta().getDisplayName().equals(StringUtils.translate("&f[Empty] &b&oBackpack"))) {
								BackpackCreateEvent event = new BackpackCreateEvent(e.getPlayer(), item);
								Bukkit.getPluginManager().callEvent(event);
								if (event.isCancelled()) {
									e.setCancelled(true);
									return;
								}
								item.setAmount(item.getAmount() - 1);
								Message msg = new Message(e.getPlayer(), "&7[&bBackpack&7]");
								msg.send("&aA new bag was obtained.");
								ItemStack newItem = new ItemStack(Material.TRAPPED_CHEST);
								ItemMeta meta = newItem.getItemMeta();
								HUID id = HUID.randomID();
								meta.setDisplayName(StringUtils.translate("&f[#&b&o" + id.toString() + "&f] &e" + e.getPlayer().getName()));
								meta.getPersistentDataContainer().set(new NamespacedKey(Labyrinth.getInstance(), "owner"), PersistentDataType.STRING, e.getPlayer().getUniqueId().toString());
								meta.getPersistentDataContainer().set(new NamespacedKey(Labyrinth.getInstance(), "id"), PersistentDataType.STRING, id.toString());
								newItem.setItemMeta(meta);
								if (!(e.getPlayer().getInventory().firstEmpty() == -1)) {
									e.getPlayer().getInventory().addItem(newItem);
								} else {
									e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), newItem);
								}
								Backpack backpack = new Backpack("Default-" + id.toString());
								backpack.setTitle(id.toString()).setSize(event.getSize()).setOwner(e.getPlayer().getUniqueId()).setItem(newItem).build();
								BackpackOpenEvent event2 = new BackpackOpenEvent(e.getPlayer(), backpack);
								Bukkit.getPluginManager().callEvent(event2);
								if (!event2.isCancelled()) {
									e.getPlayer().openInventory(BackpackAPI.getContents(backpack));
									e.getPlayer().playSound(e.getPlayer().getLocation(), event2.getOpenSound(), 10, 1);
									e.getPlayer().playSound(e.getPlayer().getLocation(), event2.getOpenSound(), 10, 4);
									e.getPlayer().playSound(e.getPlayer().getLocation(), event2.getOpenSound(), 10, 8);
								}
								e.setCancelled(true);
							}
						}
					}
				} else {
					Backpack backpack = BackpackAPI.getBackpack(item);
					BackpackOpenEvent event = new BackpackOpenEvent(e.getPlayer(), backpack);
					Bukkit.getPluginManager().callEvent(event);
					if (!event.isCancelled()) {
						e.getPlayer().openInventory(BackpackAPI.getContents(backpack));
						e.getPlayer().playSound(e.getPlayer().getLocation(), event.getOpenSound(), 10, 1);
						e.getPlayer().playSound(e.getPlayer().getLocation(), event.getOpenSound(), 10, 4);
						e.getPlayer().playSound(e.getPlayer().getLocation(), event.getOpenSound(), 10, 8);
					}
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player))
			return;
		if (e.getView().getTopInventory().getSize() != 27)
			return;
		if (BackpackAPI.getBackpack(e.getView().getTitle()) == null)
			return;
		Backpack backpack = BackpackAPI.getBackpack(e.getView().getTitle());
		if (e.getCurrentItem() != null & backpack.getItem().equals(e.getCurrentItem())) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPackClose(InventoryCloseEvent e) {
		if (!(e.getPlayer() instanceof Player))
			return;
		Player p = (Player) e.getPlayer();
		if (e.getView().getTopInventory().getSize() != 27)
			return;
		if (BackpackAPI.getBackpack(e.getView().getTitle()) == null)
			return;
		Backpack backpack = BackpackAPI.getBackpack(e.getView().getTitle());
		BackpackCloseEvent event = new BackpackCloseEvent(p, backpack);
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			BackpackAPI.updateItems(backpack, e.getView().getTopInventory().getContents());
			p.playSound(p.getLocation(), event.getCloseSound(), 10, 8);
			p.playSound(p.getLocation(), event.getCloseSound(), 10, 4);
			p.playSound(p.getLocation(), event.getCloseSound(), 10, 1);
		}
	}

}