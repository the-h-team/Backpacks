package com.youtube.hempfest.backpack.api;

import com.github.sanctum.labyrinth.data.FileManager;
import com.github.sanctum.labyrinth.data.container.DataContainer;
import com.github.sanctum.labyrinth.data.container.DataStream;
import com.github.sanctum.labyrinth.library.HFEncoded;
import com.github.sanctum.labyrinth.library.HUID;
import com.github.sanctum.labyrinth.library.StringUtils;
import com.sun.deploy.config.Config;
import com.youtube.hempfest.backpack.Backpacks;
import com.youtube.hempfest.backpack.construct.Backpack;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * The one and only implementation for Backpack API usage.
 */
public class BackpackAPI {

	public static FileManager config  = Backpacks.backpackSource.find("Config", "Backpacks");

	public static final LinkedList<Material> typeList = new LinkedList<>();

	/**
	 * Get the entire cache for backpacks.
	 */
	public static final LinkedList<Backpack> backpacks = new LinkedList<>();

	/**
	 * Get a backpack object by comparing a user id to that off a cached reference.
	 * @param owner The user to search for.
	 * @return A backpack object if not null.
	 */
	public static Backpack getBackpack(UUID owner) {
		return backpacks.stream().filter(b -> b.getOwner().equals(owner)).findFirst().orElse(null);
	}

	/**
	 * Get a backpack object by comparing an inventory title to that of a cached reference
	 * @param title The inventory title to search for.
	 * @return A backpack object if not null.
	 */
	public static Backpack getBackpack(String title) {
		return backpacks.stream().filter(b -> ChatColor.stripColor(b.getTitle()).equals(ChatColor.stripColor(title))).findFirst().orElse(null);
	}

	/**
	 * Get a backpack object by comparing a specified item's persistent data container
	 * to that of a cached reference
	 * @param item The item w/ PDC to look for
	 * @return A backpack object if not null.
	 */
	public static Backpack getBackpack(ItemStack item) {
		return backpacks.stream().filter(b -> b.getItem().equals(item)).findFirst().orElse(null);
	}

	/**
	 * Get an inventory view with the exact contents from a backpack object.
	 * @param backpack The backpack object to query from.
	 * @return The inventory to view.
	 */
	public static Inventory getContents(Backpack backpack) {
		Inventory inv = Bukkit.createInventory(null, backpack.getSize(), StringUtils.translate(backpack.getTitle()));
		inv.setContents(BackpackAPI.getItems(backpack) != null ? BackpackAPI.getItems(backpack) : new ItemStack[backpack.getSize()]);
		return inv;
	}

	/**
	 * Update a backpacks storage contents within cache.
	 * @param backpack The backpack object to query from
	 * @param newItems The new item's to update the cache with
	 */
	public static void updateItems(Backpack backpack, ItemStack[] newItems) {
		HUID id = DataContainer.getHuid(backpack.getOwner().toString() + "-bp-" + backpack.getName());
		if (id != null) {
			DataContainer.deleteInstance(id);
			DataContainer container = new DataContainer(backpack.getOwner().toString() + "-bp-" + backpack.getName());
			container.setValue(newItems);
			container.storeTemp();
			container.saveMeta();
		} else {
			DataContainer container = new DataContainer(backpack.getOwner().toString() + "-bp-" + backpack.getName());
			container.setValue(newItems);
			container.storeTemp();
			container.saveMeta();
		}
	}

	/**
	 * Remove a specified backpack object from existence.
	 * @param backpack The backpack object to remove.
	 */
	public static void remove(Backpack backpack) {
		HUID id = DataContainer.getHuid(backpack.getOwner().toString() + "-bp-" + backpack.getName());
		HUID id2 = DataContainer.getHuid(backpack.getOwner().toString() + "-bp-instance-" + backpack.getName());
		backpacks.remove(backpack);
		DataContainer.deleteInstance(id);
		DataContainer.deleteInstance(id2);
	}

	/**
	 * Get all storage contents from a specified backpack
	 * @param backpack The backpack object to query from.
	 * @return The backpacks storage contents or null.
	 */
	public static ItemStack[] getItems(Backpack backpack) {
		ItemStack[] array = null;
		HUID id = DataContainer.getHuid(backpack.getOwner().toString() + "-bp-" + backpack.getName());
		if (id != null) {
			DataStream data = DataContainer.loadInstance(id, true);
			try {
				array = (ItemStack[]) new HFEncoded(data.value()).deserialized();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	/**
	 * Query the Labyrinth Persistent Data Container for all meta entries
	 * containing a specified regex.
	 * @param context The regex to query for.
	 * @return A list of Data retaining all its originally stored values.
	 */
	public static List<DataStream> getData(String context) {
		List<DataStream> data = new ArrayList<>();
		for (HUID id : DataContainer.get()) {
			DataStream stream = DataContainer.loadInstance(id, true);
			if (stream.getMetaId().contains(context)) {
				data.add(stream);
			}
		}
		return data;
	}

}
