package me.totokaka.specifictools;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
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
		final Set<Material> tools = replacements.getToolsByBlock(event
				.getBlock().getType(), event.getPlayer().getWorld());
		if (!event.getPlayer().hasPermission("SpecificTools.default")
				&& tools != null) {
			final ItemStack inHand = event.getPlayer().getItemInHand();
			if (!tools.contains(inHand.getType())) {
				event.setCancelled(true);
				if (config.notValid.equals("No drop")) {
					event.getBlock().setTypeId(0);
				} else if (config.notValid.equals("No break")) {
					event.setCancelled(true);
				} else if (config.notValid.equals("Drop")) {
					final Block block = event.getBlock();
					event.setCancelled(true);
					for (final ItemStack item : block.getDrops()) {
						getLogger().info(block.getDrops().toString());
						block.getWorld().dropItemNaturally(block.getLocation(),
								item);
					}
					event.getBlock().setTypeId(0);
				}
			} else { //the player has a valid tool
				if (config.valid.equals("No drop")) {
					event.getBlock().setTypeId(0);
				} else if (config.valid.equals("No break")) {
					event.setCancelled(true);
				} else if (config.valid.equals("Drop")) {
					final Block block = event.getBlock();
					event.setCancelled(true);
					for (final ItemStack item : block.getDrops()) {
						getLogger().info(block.getDrops().toString());
						block.getWorld().dropItemNaturally(block.getLocation(),
								item);
					}
					event.getBlock().setTypeId(0);
				}
			}
		}
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
