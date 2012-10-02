package me.totokaka.specifictools;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SpecificTools extends JavaPlugin implements Listener {
	
	Replacements replacements;
	Configuration config;
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(final BlockBreakEvent event) {
		// TODO faster breaking
		final Map<String, List<String>> tools = replacements.getToolsByBlock(event
				.getBlock().getType().toString(), event.getPlayer().getWorld());
		if(tools != null){
			Material itemInHand = event.getPlayer().getItemInHand().getType();
			String tool = itemInHand.toString();
			if(tool.equals("AIR")){
				tool = "HAND";
			}
			List<String> actions = tools.get(tool);
			if(actions != null){
				if(!(actions.contains("destroy") && actions.contains("drop"))){
					event.setCancelled(true);
					if(actions.contains("destroy")){
						event.getBlock().setType(Material.AIR);
					}
					if(actions.contains("drop")){
						for(ItemStack stack : event.getBlock().getDrops()){
							event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), stack);
						}
					}
				}
			}else{
				this.getLogger().info(itemInHand.toString() + " vs "+ itemInHand.name());
			}
		}/*else{
			this.getLogger().info(event
					.getBlock().getType().toString() + " vs " + event
					.getBlock().getType().name());
		}*/
	}
	
	@Override
	public void onDisable() {
		saveConfig();
	}
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		replacements = new Replacements(this);
		config = new Configuration(this);
		config.load();
		replacements.load();
		final Commands cmdExcecutor = new Commands(
				this);
		getCommand("SpecificTools").setExecutor(cmdExcecutor);
		getCommand("SpecificToolsAdd").setExecutor(cmdExcecutor);
		getCommand("SpecificToolsRemove").setExecutor(cmdExcecutor);
	}
	
	public Configuration getNewConfig(){
		return config;
	}
	
}
