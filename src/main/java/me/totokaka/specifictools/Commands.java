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

public class Commands implements CommandExecutor {
	
	SpecificTools plugin;
	Map<CommandSender, String> adds = new HashMap<CommandSender, String>();
	Map<CommandSender, String> removes = new HashMap<CommandSender, String>();
	
	public Commands(final SpecificTools plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd,
			final String alias, final String[] args) {
		if(args.length >= 1){
			if(args[0].equalsIgnoreCase("add")){
				
			}else if(args[0].equalsIgnoreCase("remove")){
				
			}else if(args[0].equalsIgnoreCase("list")){
				return list(sender, cmd, alias, args);
			}
		}
		return false;
	}
	
	public boolean add(final CommandSender sender, final Command cmd,
			final String alias, final String[] args){
		if (args.length >= 5){
		
			return true;
		}else{
			return false;
		}
	}
	
	public boolean remove(final CommandSender sender, final Command cmd,
			final String alias, final String[] args){
		return true;
	}
	
	public boolean list(final CommandSender sender, final Command cmd,
			final String alias, final String[] args){
		if(args.length == 1){
			sender.sendMessage("Here is a list of all tools with there actions");
			for(String world : plugin.replacements.replacements.keySet()){
				sender.sendMessage(world+":");
				for(String block : plugin.replacements.replacements.get(world).keySet()){
					sender.sendMessage("    "+block+":");
					for(String tool : plugin.replacements.replacements.get(world).get(block).keySet()){
						StringBuilder sb = new StringBuilder();
						sb.append(tool+": [");
						for(String action : plugin.replacements.replacements.get(world).get(block).get(tool)){
							sb.append(action+" ");
						}
						sb.append("]");
						sender.sendMessage(sb.toString());
					}
				}
			}
			return true;
		}
		return false;
	}
	
}
