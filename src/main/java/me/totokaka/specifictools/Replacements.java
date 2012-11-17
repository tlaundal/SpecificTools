package me.totokaka.specifictools;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.swing.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Replacements
{
    Map<World, WorldMeta> replacements;
    SpecificTools plugin;

    public Replacements(final SpecificTools plugin)
    {
        this.plugin = plugin;
    }

    public Map<Material, ToolMeta> getToolsByBlock(final Material block, final World world)
    {
        if (replacements.containsKey(world))
        {
            if (replacements.get(world.getName()).blocks.containsKey(block))
            {
                return replacements.get(world).blocks.get(block).tools;
            }
        }
        return null;
    }

    public boolean load()
    {
        replacements = new HashMap<World, WorldMeta>();
        try
        {
            parseConfig(plugin.getNewConfig().getConfig());
        }
        catch (final Exception ex)
        {
            plugin.getLogger().warning(
                    "Something went terribly wrong while parsing config.yml!\n"
                            + "The error was:");
            ex.printStackTrace();
            plugin.getLogger().warning("Disabling plugin.");
            plugin.getPluginLoader().disablePlugin(plugin);
            return false;
        }
        return true;
    }

    public void parseConfig(final FileConfiguration config)
    {
        ConfigurationSection worldSection = config.getConfigurationSection("replacements");
        if(worldSection == null) return;

        Set<String> worlds = worldSection.getKeys(false);
        if (worlds == null) return;
        if (worlds.isEmpty()) return;

        for (String w : worlds)
        {
            World world = Bukkit.getWorld(w);
            if (world == null) continue;

            ConfigurationSection blockSection = worldSection.getConfigurationSection(w);
            if(blockSection == null) continue;

            Set<String> blocks = blockSection.getKeys(false);
            if (blocks == null) continue;
            if (blocks.isEmpty()) continue;

            replacements.put(world, new WorldMeta(new HashMap<Material, BlockMeta>()));

            for (String b : blocks)
            {
                Material block = Material.getMaterial(b);
                if (block == null) continue;

                ConfigurationSection toolSection = blockSection.getConfigurationSection(b);
                if (toolSection == null) continue;

                Set<String> tools = toolSection.getKeys(false);
                if (tools == null) continue;
                if (tools.isEmpty()) continue;

                replacements.get(world).blocks.put(block, new BlockMeta(new HashMap<Material, ToolMeta>()));

                for (String t : tools)
                {
                    Material tool = Material.getMaterial(t);
                    if (tool == null) continue;

                    ConfigurationSection metaSection = toolSection.getConfigurationSection(t);
                    if (metaSection == null) continue;
                    if (!metaSection.contains("actions")) continue;
                    if (!metaSection.isList("actions")) continue;

                    List<String> stringActions = (List<String>) metaSection.getList("actions");
                    Set<Action>  actions = new HashSet<Action>();
                    for (String action : stringActions)
                    {
                        actions.add(Action.get(action));
                    }
                    int time = metaSection.getInt("time", -1);

                    replacements.get(world).blocks.get(block).tools.put(tool, new ToolMeta(actions, time));

                }
            }

        }
    }

    public class WorldMeta
    {
        private final Map<Material, BlockMeta> blocks;

        public WorldMeta(Map<Material, BlockMeta> blocks)
        {
            this.blocks = blocks;
        }
    }

    public class BlockMeta
    {
        private final Map<Material, ToolMeta> tools;

        public BlockMeta (Map<Material, ToolMeta> tools)
        {
            this.tools = tools;
        }
    }

    public class ToolMeta
    {
        public final Set<Action> actions;
        public final int time;

        public ToolMeta(Set<Action> actions, int time)
        {
            this.actions = actions;
            this.time = time;
        }
    }

    public enum Action
    {
        DROP, DESTROY, EXPLODE, LIGHTNING;

        public static Action get(String name)
        {
            if (name.equalsIgnoreCase("drop"))           return Action.DROP;
            else if (name.equalsIgnoreCase("destroy"))   return Action.DESTROY;
            else if (name.equalsIgnoreCase("explode"))   return Action.EXPLODE;
            else if (name.equalsIgnoreCase("lightning")) return Action.LIGHTNING;
            else                                         return null;
        }
    }

}
