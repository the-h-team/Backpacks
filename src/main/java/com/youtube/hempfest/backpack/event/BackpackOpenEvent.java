package com.youtube.hempfest.backpack.event;

import com.youtube.hempfest.backpack.construct.Backpack;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called everytime a player open's a backpack inventory.
 */
public class BackpackOpenEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final Player opener;

	private final Backpack backpack;

	private boolean cancelled;

	private Sound openSound = Sound.ENTITY_PARROT_FLY;

	public BackpackOpenEvent(Player opener, Backpack backpack) {
		this.opener = opener;
		this.backpack = backpack;
	}

	public Player getOpener() {
		return opener;
	}

	public Backpack getBackpack() {
		return backpack;
	}

	public Sound getOpenSound() {
		return openSound;
	}

	public void setOpenSound(Sound openSound) {
		this.openSound = openSound;
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
