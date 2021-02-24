package com.youtube.hempfest.backpack.event;

import com.github.sanctum.labyrinth.library.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * Called everytime a player finalizes ownership of a new backpack.
 */
public class BackpackAttachLoreEvent extends Event{

	private static final HandlerList handlers = new HandlerList();

	private final Player opener;

	private final List<String> additions = new ArrayList<>();

	public BackpackAttachLoreEvent(Player creator) {
		this.opener = creator;
	}

	public Player getOwner() {
		return opener;
	}

	public void addLore(String text) {
		if (!additions.contains(text)) {
			additions.add(text);
		}
	}

	public void addLore(Collection<String> add) {
		additions.addAll(add);
	}

	public List<String> getAdditions() {
		return additions;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
