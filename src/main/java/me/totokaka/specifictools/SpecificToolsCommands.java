package me.totokaka.specifictools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpecificToolsCommands implements CommandExecutor {
	
	SpecificTools plugin;
	Map<CommandSender, String> adds = new HashMap<CommandSender, String>();
	Map<CommandSender, String> removes = new HashMap<CommandSender, String>();
	
	public SpecificToolsCommands(final SpecificTools plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd,
			final String alias, final String[] args) {
		if (args.length >= 1) {
			if (cmd.getName().equals("SpecificTools")) {
				return SpecificTools(sender, cmd, alias, args);
			} else if (cmd.getName().equals("SpecificToolsAdd")) {
				return SpecificToolsAdd(sender, cmd, alias, args);
			} else if (cmd.getName().equals("SpecificToolsRemove")) {
				return SpecificToolsRemove(sender, cmd, alias, args);
			}
		}
		return false;
	}
	
	public boolean SpecificTools(final CommandSender sender, final Command cmd,
			final String alias, final String[] args) {
		final String subCmd = args[0];
		if (subCmd.equals("list")) {
			final Map<String, Map<Material, Set<Material>>> replacements = plugin.replacements.replacements;
			sender.sendMessage("here is a list of all replacements: ");
			for (final String key : replacements.keySet()) {
				sender.sendMessage(key + ":");
				final Map<Material, Set<Material>> materials = replacements
						.get(key);
				for (final Material matKey : materials.keySet()) {
					sender.sendMessage("    " + matKey.toString() + ": ");
					final Set<Material> tools = materials.get(matKey);
					for (final Material mat : tools) {
						sender.sendMessage("        " + mat.toString());
					}
				}
			}
			return true;
		} else if (subCmd.equals("edit") && args.length == 3) {
			if (Bukkit.getWorld(args[1]) != null
					&& Material.getMaterial(args[2]) != null) {
				if (Material.getMaterial(args[2]).isBlock()) {
					adds.put(sender, "Replacements." + args[1] + "." + args[2]);
					removes.put(sender, "Replacements." + args[1] + "."
							+ args[2]);
					sender.sendMessage("All set! To add tools type: \"/sAdd [tool]\" and type \"/sAdd finished\" when you're done");
					sender.sendMessage("To remove tools type: \"/sRemove [tool]\" and type \"/sRemove finished\" when you're done");
					return true;
				}
			}
		} else if (subCmd.equals("create")) {
			if (Bukkit.getWorld(args[1]) != null
					&& Material.getMaterial(args[2]) != null) {
				if (Material.getMaterial(args[2]).isBlock()) {
					adds.put(sender, "Replacements." + args[1] + "." + args[2]);
					sender.sendMessage("All set! To add tools type: \"/sAdd [tool]\" and type \"/sAdd finished\" when you're done");
					return true;
				}
			}
		} else if (subCmd.equals("remove")) {
			if (Bukkit.getWorld(args[1]) != null
					&& Material.getMaterial(args[2]) != null) {
				if (Material.getMaterial(args[2]).isBlock()) {
					plugin.replacements.getConfig().set(
							"Replacements." + args[1] + "." + args[2], null);
					plugin.replacements.saveAndReloadConfig();
					sender.sendMessage("Entry removed");
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean SpecificToolsAdd(final CommandSender sender,
			final Command cmd, final String alias, final String[] args) {
		if (args.length == 1) {
			if (adds.containsKey(sender)) {
				if (args[0].equals("finished")) {
					adds.remove(sender);
					if (removes.containsKey(sender)) {
						removes.remove(sender);
					}
					plugin.replacements.saveAndReloadConfig();
					sender.sendMessage("Your session is over.");
				} else {
					String tool = plugin.replacements.parseMaterial(args);
					final List<String> list = plugin.replacements.getConfig()
							.getStringList(adds.get(sender));
					if (tool.equals("HAND")) {
						tool = "AIR";
					}
					if (Material.getMaterial(tool) != null) {
						list.add(tool);
						plugin.replacements.getConfig().set(adds.get(sender),
								list);
						sender.sendMessage(tool + " is added.");
					} else {
						sender.sendMessage(ChatColor.RED + tool
								+ " Is not a valid tool!");
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED
						+ "You are not in an add session!");
			}
			return true;
		}
		return false;
	}
	
	private boolean SpecificToolsRemove(final CommandSender sender,
			final Command cmd, final String alias, final String[] args) {
		if (args.length == 1) {
			if (removes.containsKey(sender)) {
				if (args[0].equals("finished")) {
					removes.remove(sender);
					if (adds.containsKey(sender)) {
						adds.remove(sender);
					}
					plugin.replacements.saveAndReloadConfig();
					sender.sendMessage("Your session is over.");
				} else {
					String tool = plugin.replacements.parseMaterial(args);
					final List<String> list = plugin.replacements.getConfig()
							.getStringList(removes.get(sender));
					if (tool.equals("HAND")) {
						tool = "AIR";
					}
					if (Material.getMaterial(tool) != null) {
						list.remove(tool);
						plugin.replacements.getConfig().set(
								removes.get(sender), list);
						sender.sendMessage(tool + " is removed.");
					} else {
						sender.sendMessage(ChatColor.RED + tool
								+ " Is not a valid tool!");
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED
						+ "You are not in an remove session!");
			}
			return true;
		}
		return false;
	}
	
}
