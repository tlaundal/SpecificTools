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
	
	Replacements replacements = new Replacements();
	private boolean noDrop = true;
	
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
				if (noDrop) {
					event.getBlock().setTypeId(0);
				}
			} else { //the player has a valid tool
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
	
	@Override
	public void onDisable() {
		saveConfig();
	}
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		reloadConfig();
		noDrop = getConfig().getBoolean("NoDrop");
		getConfig()
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
						+ "If you set NoDrop to false the block will not break if the player tries to destroy it with a tool that is not valid\n"
						+ "If you set it to true the block will not drop anything.."
						+ "");
		saveConfig();
		replacements.load(this);
		final SpecificToolsCommands cmdExcecutor = new SpecificToolsCommands(
				this);
		getCommand("SpecificTools").setExecutor(cmdExcecutor);
		getCommand("SpecificToolsAdd").setExecutor(cmdExcecutor);
		getCommand("SpecificToolsRemove").setExecutor(cmdExcecutor);
	}
	
}
