package com.youtube.hempfest.backpack;

import com.github.sanctum.labyrinth.data.FileList;
import com.github.sanctum.labyrinth.data.FileManager;
import com.github.sanctum.labyrinth.data.container.DataStream;
import com.github.sanctum.labyrinth.event.EventBuilder;
import com.github.sanctum.labyrinth.library.HFEncoded;
import com.github.sanctum.labyrinth.library.Item;
import com.github.sanctum.labyrinth.library.StringUtils;
import com.youtube.hempfest.backpack.api.BackpackAPI;
import com.youtube.hempfest.backpack.construct.Backpack;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

public final class Backpacks extends JavaPlugin {

	public static FileList backpackSource;

	@Override
	public void onEnable() {
		// Plugin startup logic
		backpackSource = FileList.search(this);
		new EventBuilder(this).compileFields("com.youtube.hempfest.backpack.construct");
		new Item(Material.TRAPPED_CHEST, "&e[Empty] &b&oBackpack").
				setKey("owner").
				buildStack().
				attachLore(Collections.singletonList(StringUtils.translate("&3&oRight-click to open."))).
				addEnchant(Enchantment.VANISHING_CURSE, 1).
				setItem('O', Material.LEATHER).
				setItem('U', Material.CHEST).
				shapeRecipe("OOO", "OUO", "OOO").
				register();
		for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			for (DataStream stream : BackpackAPI.getData(p.getUniqueId().toString() + "-bp-instance-")) {
				try {
					Backpack backpack = (Backpack) new HFEncoded(stream.value()).deserialized();
					BackpackAPI.backpacks.add(backpack);
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		BackpackAPI.typeList.add(Material.TRAPPED_CHEST);
		if (!BackpackAPI.config.exists()) {
			InputStream stream = getResource("Config.yml");
			assert stream != null;
			FileManager.copy(stream, BackpackAPI.config.getFile());
		}
	}
}
