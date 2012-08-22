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
	
	public boolean load(SpecificTools plugin){
		this.plugin = plugin;
		this.config = plugin.getConfig();
		try{
			parseConfig(config);
		}catch(Exception ex){
			plugin.getLogger().warning("Something went terrebly wrong while parsing config.yml!\n" +
					"The error was:");
			ex.printStackTrace();
			plugin.getLogger().warning("Disabling plugin.");
			plugin.getPluginLoader().disablePlugin(plugin);
			return false;
		}
		return true;
	}
	
	public void parseConfig(FileConfiguration config){
		ConfigurationSection section = config.getConfigurationSection("Replacements");
		Set<String> wKeys = section.getKeys(false);// the worlds
		if(wKeys != null){
			for(String s : wKeys){
				Set<String> keys = section.getConfigurationSection(s).getKeys(false);// the blocks
				if(keys != null || Bukkit.getWorld(s) != null){
					for(String material : keys){
						Material m = Material.getMaterial(material);
						if(m != null && m.isBlock()){
							@SuppressWarnings("unchecked")
							List<String> list = (List<String>) section.getList(s+"."+material);
							Set<Material> tools = new HashSet<Material>();
							for(String tool : list){
								if(tool.equals("HAND")){
									tools.add(Material.AIR);
								}else if(Material.valueOf(tool) != null){
									tools.add(Material.valueOf(tool));
								}else{
									plugin.getLogger().warning("Looks like there is something wrong with your config.yml. "
											 +tool+" is not a valid tool!");
								}
							}
							if(replacements.get(s) == null){
								replacements.put(s, new HashMap<Material, Set<Material>>());
							}
							replacements.get(s).put(m, tools);
						}else{
							plugin.getLogger().warning("Looks like there is something wrong with your config.yml. "
									+material+" is not a valid block!");
						}
					}
				}else{
					plugin.getLogger().warning("Looks like there is something wrong with your config.yml. "
							+s+" is not a world or has no children!");
				}
			}
		}
	}
	
	public Set<Material> getToolsByBlock(Material m, World world){
		if(replacements.containsKey(world.getName())){
			if(replacements.get(world.getName()).containsKey(m)){
				return replacements.get(world.getName()).get(m);
			}
		}
		return null;
	}

	public FileConfiguration getConfig(){
		return config;
	}
	
	public void saveAndReloadConfig(){
		plugin.saveConfig();
		replacements = new HashMap<String, Map<Material, Set<Material>>>();
		this.load(plugin);
	}

}
