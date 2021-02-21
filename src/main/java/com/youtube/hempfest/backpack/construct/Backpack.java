package com.youtube.hempfest.backpack.construct;

import com.github.ms5984.api.menuman.Menu;
import com.github.sanctum.labyrinth.data.container.DataContainer;
import com.github.sanctum.labyrinth.library.HUID;
import com.youtube.hempfest.backpack.api.BackpackAPI;
import java.io.Serializable;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

/**
 * A digital user-based share-able & physical representation of a backpack.
 */
public class Backpack implements Serializable {

	private static final long serialVersionUID = 5567341724467773889L;

	private final String name;

	private int size;

	private String title;

	private UUID owner;

	private ItemStack item;

	/**
	 * Create a unique name for the backpack object.
	 * Only (-_.) symbols are allowed.
	 * @param name The unique name of the backpack instance.
	 */
	public Backpack(String name) {
		this.name = name;
	}

	/**
	 * Define a size for the backpack object.
	 * @param size The size of the inventory.
	 * @return The builder object.
	 */
	public Backpack setSize(int size) {
		this.size = size;
		return this;
	}

	/**
	 * Define a size for the backpack object.
	 * @param rows The size of the inventory.
	 * @return The builder object.
	 */
	public Backpack setSize(Menu.InventoryRows rows) {
		this.size = rows.slotCount;
		return this;
	}

	/**
	 * Define a title that will be displayed every time the inventory for
	 * the backpack is opened.
	 * @param title The backpack's inventory title
	 * @return The builder object.
	 */
	public Backpack setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * Define a user owner for the backpack object.
	 * @param owner The user who owns the backpack.
	 * @return The builder object.
	 */
	public Backpack setOwner(UUID owner) {
		this.owner = owner;
		return this;
	}

	/**
	 * Define the exact item used for access with this backpack.
	 * @param backpack The itemstack to use
	 * @return The builder object.
	 */
	public Backpack setItem(ItemStack backpack) {
		this.item = backpack;
		return this;
	}

	/**
	 * Finalize and store the backpack object in cache.
	 */
	public void build() {
		BackpackAPI.backpacks.add(this);
		HUID id = DataContainer.getHuid(owner.toString() + "-bp-instance-" + name);
		if (id != null) {
			DataContainer.deleteInstance(id);
			DataContainer container = new DataContainer(owner.toString() + "-bp-instance-" + name);
			container.setValue(this);
			container.storeTemp();
			container.saveMeta();
		} else {
			DataContainer container = new DataContainer(owner.toString() + "-bp-instance-" + name);
			container.setValue(this);
			container.storeTemp();
			container.saveMeta();
		}
	}

	/**
	 * Save changes to the backpack object within cache.
	 */
	public void save() {
		HUID id = DataContainer.getHuid(owner.toString() + "-bp-instance-" + name);
		if (id != null) {
			DataContainer.deleteInstance(id);
			DataContainer container = new DataContainer(owner.toString() + "-bp-instance-" + name);
			container.setValue(this);
			container.storeTemp();
			container.saveMeta();
		} else {
			DataContainer container = new DataContainer(owner.toString() + "-bp-instance-" + name);
			container.setValue(this);
			container.storeTemp();
			container.saveMeta();
		}
	}

	/**
	 * Get the backpack object's inventory size
	 * @return The size of the backpack inventory.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Get the backpack object's unique name
	 * @return The unique name of the backpack from meta
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the backpack object's inventory title
	 * @return The title to be displayed every opening of the backpack.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Get the owner of the backpack. The person who initially obtained it.
	 * @return The owner of the backpack
	 */
	public UUID getOwner() {
		return owner;
	}

	/**
	 * Get the exact PDC-retained item for the backpack
	 * @return The item used to access to backpack's contents.
	 */
	public ItemStack getItem() {
		return item;
	}
}
