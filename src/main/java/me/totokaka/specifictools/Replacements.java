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
	
	public void load(SpecificTools plugin){
		FileConfiguration config = plugin.getConfig();
		ConfigurationSection section = config.getConfigurationSection("Replacements");
		Set<String> wKeys = section.getKeys(false);// the worlds
		if(wKeys != null){
			for(String s : wKeys){
				Set<String> keys = section.getConfigurationSection(s).getKeys(false);// the blocks
				if(keys != null || Bukkit.getWorld(s) != null){
					for(String material : keys){
						Material m = Material.getMaterial(material);
						if(m != null){
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
							if(replacements.get(s) != null){
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
		// TODO Per world
		if(replacements.containsKey(m)){
			return replacements.get(m);
		}
		return null;
	}
}
