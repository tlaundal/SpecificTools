package me.totokaka.specifictools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Replacements {
	
	Map<Material, Set<Material>> replacements = new HashMap<Material, Set<Material>>();
	
	public void load(SpecificTools plugin){
		FileConfiguration config = plugin.getConfig();
		ConfigurationSection section = config.getConfigurationSection("Replacements");
		Set<String> keys = section.getKeys(false);
		if(keys != null){
			for(String s : keys){
				Material m = Material.getMaterial(s);
				if(s != null){
					@SuppressWarnings("unchecked")
					List<String> list = (List<String>) section.getList(s);
					Set<Material> tools = new HashSet<Material>();
					for(String tool : list){
						if(tool.equals("HAND")){
							tools.add(null);
						}else if(Material.valueOf(tool) != null){
							tools.add(Material.valueOf(tool));
						}else{
							plugin.getLogger().warning("Looks like there is something wrong with your config.yml. "
									 +tool+" is not a valid tool!");
						}
					}
					replacements.put(m, tools);
				}else{
					plugin.getLogger().warning("Looks like there is something wrong with your config.yml. "
							+s+" is not a valid block!");
				}
			}
		}
	}
	
	public Set<Material> getToolsByBlock(Material m){
		if(replacements.containsKey(m)){
			return replacements.get(m);
		}
		return null;
	}
}
