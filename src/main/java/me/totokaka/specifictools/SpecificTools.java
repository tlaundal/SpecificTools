package me.totokaka.specifictools;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SpecificTools extends JavaPlugin
{

    Replacements replacements;
    Configuration config;

    @Override
    public void onDisable()
    {
        saveConfig();
    }

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(new Listeners(this), this);
        replacements = new Replacements(this);
        config = new Configuration(this);
        config.load();
        replacements.load();
        final Commands cmdExcecutor = new Commands(
                this);
        getCommand("SpecificTools").setExecutor(cmdExcecutor);
    }

    public Configuration getNewConfig()
    {
        return config;
    }

}
