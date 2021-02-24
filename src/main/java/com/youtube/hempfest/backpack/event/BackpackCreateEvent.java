package com.youtube.hempfest.backpack.event;

import com.github.sanctum.labyrinth.library.StringUtils;
import java.util.UUID;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called everytime a player finalizes ownership of a new backpack.
 */
public class BackpackCreateEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Player opener;

	private final ItemStack empty;

	private boolean cancelled;

	private int size = 27;

	private String itemName;

	private UUID owner;

	public BackpackCreateEvent(Player creator, String itemName, ItemStack empty) {
		this.opener = creator;
		this.itemName = itemName;
		this.empty = empty;
		this.owner = creator.getUniqueId();
	}

	public Player getOpener() {
		return opener;
	}

	public ItemStack getEmpty() {
		return empty;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public UUID getOwner() {
		return owner;
	}

	public String getItemName() {
		return StringUtils.translate(itemName);
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
