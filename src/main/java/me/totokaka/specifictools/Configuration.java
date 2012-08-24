package me.totokaka.specifictools;

import org.bukkit.configuration.file.FileConfiguration;

public class Configuration{
	FileConfiguration config;
	SpecificTools plugin;
	
	public String notValid = "No drop";
	public String valid = "Drop";
	
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
				+ "           - HAND\n"
				+ "           - DIAMOND_SHOVEL\n"
				+ "This exaple makes the players hand and Diamond shovel as the only \"tools\" "
				+ "capable of destroying Dirt in world\n\n"
				+ "The valid and notValid options are configured in the same way, you can set them to: Drop, No drop or No break\n"
				+ "These are pretty self explaning. the 'valid' option is used when a player breaks a block with a valid tool,\n"
				+ "if the player does with a non valid tool notValid is used"
				+ "--------------------------------------------------------");
		plugin.saveConfig();
		
		this.notValid = config.getString("Settings.notValid");
		this.valid = config.getString("Settings.valid");
		
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
