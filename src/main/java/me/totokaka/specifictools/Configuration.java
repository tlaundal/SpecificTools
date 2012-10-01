package me.totokaka.specifictools;

import org.bukkit.configuration.file.FileConfiguration;

public class Configuration{
	FileConfiguration config;
	SpecificTools plugin;
	
	public Configuration(SpecificTools plugin){
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}
	
	public boolean load(){
		plugin.saveDefaultConfig();
		plugin.reloadConfig();
		this.config
		.options()
		.header("------------------SpecificTools Config------------------\n"
				+ "Here is an example of the format in this config:\n"
				+ "Replacements:\n"
				+ "    world:\n"
				+ "        DIRT:\n"
				+ "           HAND: [destroy]\n"
				+ "           DIAMOND_SHOVEL: [destroy, drop]\n"
				+ "This exaple makes the players hand and diamond shovel as the only \"tools\" "
				+ "capable of destroying Dirt in world\n"
				+ "Dirt will just destory the block, but a diamond shovel will destroy and drop it.\n"
				+ "--------------------------------------------------------");
		plugin.saveConfig();
		
		return true;
	}
	
	public void saveAndReloadReplacements() {
		plugin.saveConfig();
		plugin.replacements.load();
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
	
	public FileConfiguration getConfig(){
		return config;
	}
	
}
