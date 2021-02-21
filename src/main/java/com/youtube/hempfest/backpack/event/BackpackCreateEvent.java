package com.youtube.hempfest.backpack.event;

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

	public BackpackCreateEvent(Player creator, ItemStack empty) {
		this.opener = creator;
		this.empty = empty;
	}

	public Player getOpener() {
		return opener;
	}

	public ItemStack getEmpty() {
		return empty;
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
