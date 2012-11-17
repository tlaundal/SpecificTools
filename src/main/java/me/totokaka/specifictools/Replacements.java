package me.totokaka.specifictools;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Replacements
{
    //worlds      blocks       tools       actions
    Map<String, Map<String, Map<String, ToolMeta>>> replacements;
    SpecificTools plugin;

    public Replacements(final SpecificTools plugin)
    {
        this.plugin = plugin;
    }

    public Map<String, ToolMeta> getToolsByBlock(final String m, final World world)
    {
        if (replacements.containsKey(world.getName()))
        {
            if (replacements.get(world.getName()).containsKey(m))
            {
                return replacements.get(world.getName()).get(m);
            }
        }
        return null;
    }

    public boolean load()
    {
        replacements = new HashMap<String, Map<String, Map<String, ToolMeta>>>();
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
        final ConfigurationSection section = config
                .getConfigurationSection("replacements");
        final Set<String> worlds = section.getKeys(false);// the worlds
        if (worlds != null)
        {
            for (final String world : worlds)
            {

                final Set<String> blocks = section.getConfigurationSection(world).getKeys(false);
                if (blocks != null && Bukkit.getWorld(world) != null)
                {
                    for (final String block : blocks)
                    {
                        final Material m = Material.getMaterial(block);
                        if (m != null && m.isBlock())
                        {

                            Set<String> tools = section.getConfigurationSection(world).getConfigurationSection(block).getKeys(false);   //
                            HashMap<String, ToolMeta> toolMap = new HashMap<String, ToolMeta>();                                //This map contains all tools and their actions for the specific block in the specific world
                            for (String tool : tools)
                            {
                                if (tool.endsWith("-Time")) break;
                                if (!(tool.equals("HAND") || Material.getMaterial(tool) != null)) //Check if the tool is a valid tool
                                {
                                    plugin.getLogger().warning(tool + " Is not a tool!! the plugin will probably fuck up!! please stop the server and fix.");
                                }

                                List<String> actions = section.getConfigurationSection(world).getConfigurationSection(block).getStringList(tool);
                                for (String action : actions) // Iterate through the actions for the tool
                                {
                                    if (!(action.equals("drop") || action.equals("lightning") || action.equals("explode") || action.equals("destroy"))) //Check if the actions are valid
                                    {
                                        plugin.getLogger().warning(action + " Is not an action!! the plugin will probably fuck up!! please stop the server and fix.");
                                    }
                                }
                                int time = section.getConfigurationSection(world).getConfigurationSection(block).getInt(tool + "-time", -1);

                                toolMap.put(tool, new ToolMeta(actions, time));
                            }

                            //worlds
                            if (replacements.get(world) == null)
                            {
                                replacements.put(world, new HashMap<String, Map<String, ToolMeta>>());
                            }
                            //blocks
                            if (replacements.get(world).get(block) == null)
                            {
                                replacements.get(world).put(block, new HashMap<String, ToolMeta>());
                            }
                            replacements.get(world).get(block).putAll(toolMap);
                        }
                        else
                        {
                            plugin.getLogger().warning(
                                    "Looks like there is something wrong with your config.yml. "
                                            + block
                                            + " is not a valid block!");
                        }
                    }
                }
                else
                {
                    plugin.getLogger()
                            .warning(
                                    "Looks like there is something wrong with your config.yml. "
                                            + world
                                            + " is not a world or has no children!");
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
        public final List<String> actions;
        public final int time;

        public ToolMeta(List<String> actions, int time)
        {
            this.actions = actions;
            this.time = time;
        }
    }

}
