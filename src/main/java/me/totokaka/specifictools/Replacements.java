package me.totokaka.specifictools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Replacements {
	//worlds         blocks            tools         actions
	Map<String, Map<String, Map<String, List<String>>>> replacements;
	SpecificTools plugin;
	
	public Replacements(final SpecificTools plugin){
		this.plugin = plugin;
	}
	
	public Map<String, List<String>> getToolsByBlock(final String m, final World world) {
		if(replacements.containsKey(world.getName())){
			if(replacements.get(world.getName()).containsKey(m)){
				return replacements.get(world.getName()).get(m);
			}
		}
		return null;
	}
	
	public boolean load() {
		replacements = new HashMap<String, Map<String, Map<String, List<String>>>>();
		try {
			parseConfig(plugin.getNewConfig().getConfig());
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
				.getConfigurationSection("replacements");
		final Set<String> worlds = section.getKeys(false);// the worlds
		if (worlds != null) {
			for (final String world : worlds) {
				final Set<String> blocks = section.getConfigurationSection(world).getKeys(false);
				if(blocks != null && Bukkit.getWorld(world) != null){
					for(final String material : blocks){
						final Material m = Material.getMaterial(material);
						if (m != null && m.isBlock()) {
							Set<String> tools = section.getConfigurationSection(world).getConfigurationSection(material).getKeys(false);
							HashMap<String, List<String>> toolmap = new HashMap<String, List<String>>();
							for(String tool : tools){
								//replacements.get(world).get(material).put(tool, config.getConfigurationSection(world).getConfigurationSection(material).getStringList(tool));
								if(!(tool.equals("HAND") || Material.getMaterial(tool) != null)){
									plugin.getLogger().warning(tool+" Is not a tool!! the plugin will probably fuck up!! please stop the server and fix.");
								}
								List<String> actions = section.getConfigurationSection(world).getConfigurationSection(material).getStringList(tool);
								for(String action : actions){
									if(!(action.equals("drop") || action.equals("explode") || action.equals("destroy"))){
										plugin.getLogger().warning(action+" Is not an action!! the plugin will probably fuck up!! please stop the server and fix.");
									}
								}
								toolmap.put(tool, actions);
							}
							//worlds
							if(replacements.get(world) == null){
								replacements.put(world, new HashMap<String, Map<String,List<String>>>());
							}
							//blocks
							if(replacements.get(world).get(material) == null){
								replacements.get(world).put(material, new HashMap<String, List<String>>());
							}
							replacements.get(world).get(material).putAll(toolmap);
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
								+ world
								+ " is not a world or has no children!");
				}
			}
		}
	}

	
}
