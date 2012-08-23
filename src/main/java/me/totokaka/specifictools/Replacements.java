package me.totokaka.specifictools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Replacements {
	
	Map<String, Map<Material, Set<Material>>> replacements = new HashMap<String, Map<Material, Set<Material>>>();
	SpecificTools plugin;
	FileConfiguration config;
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public Set<Material> getToolsByBlock(final Material m, final World world) {
		if (replacements.containsKey(world.getName())) {
			if (replacements.get(world.getName()).containsKey(m)) {
				return replacements.get(world.getName()).get(m);
			}
		}
		return null;
	}
	
	public boolean load(final SpecificTools plugin) {
		this.plugin = plugin;
		config = plugin.getConfig();
		try {
			parseConfig(config);
		} catch (final Exception ex) {
			plugin.getLogger().warning(
					"Something went terrebly wrong while parsing config.yml!\n"
							+ "The error was:");
			ex.printStackTrace();
			plugin.getLogger().warning("Disabling plugin.");
			plugin.getPluginLoader().disablePlugin(plugin);
			return false;
		}
		return true;
	}
	
	public void parseConfig(final FileConfiguration config) {
		final ConfigurationSection section = config
				.getConfigurationSection("Replacements");
		final Set<String> wKeys = section.getKeys(false);// the worlds
		if (wKeys != null) {
			for (final String s : wKeys) {
				final Set<String> keys = section.getConfigurationSection(s)
						.getKeys(false);// the blocks
				if (keys != null || Bukkit.getWorld(s) != null) {
					for (final String material : keys) {
						final Material m = Material.getMaterial(material);
						if (m != null && m.isBlock()) {
							@SuppressWarnings("unchecked")
							final List<String> list = (List<String>) section
									.getList(s + "." + material);
							final Set<Material> tools = new HashSet<Material>();
							for (String tool : list) {
								if (tool.equals("HAND")) {
									tools.add(Material.AIR);
								} else if (Material.valueOf(tool) != null) {
									tools.add(Material.valueOf(tool));
								} else {
									plugin.getLogger().warning(
											"Looks like there is something wrong with your config.yml. "
													+ tool
													+ " is not a valid tool!");
								}
							}
							if (replacements.get(s) == null) {
								replacements.put(s,
										new HashMap<Material, Set<Material>>());
							}
							replacements.get(s).put(m, tools);
						} else {
							plugin.getLogger().warning(
									"Looks like there is something wrong with your config.yml. "
											+ material
											+ " is not a valid block!");
						}
					}
				} else {
					plugin.getLogger()
							.warning(
									"Looks like there is something wrong with your config.yml. "
											+ s
											+ " is not a world or has no children!");
				}
			}
		}
	}
	
	public void saveAndReloadConfig() {
		plugin.saveConfig();
		replacements = new HashMap<String, Map<Material, Set<Material>>>();
		load(plugin);
	}
	
	public String parseMaterial(final String... old) {
		final StringBuilder builder = new StringBuilder();
		for (final String s : old) {
			if (s != null) {
				builder.append(s.toUpperCase() + " ");
			}
		}
		
		return builder.toString()
				.trim()
				.replace(" ", "_");
	}
	
}
