package io.lionpa.lionpaitemapi;

import io.lionpa.lionpaitemapi.item.events.EventsInvoker;
import io.lionpa.lionpaitemapi.speedium.ItemBlockType;
import io.lionpa.speedium.Speedium;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class LionPaItemAPI extends JavaPlugin {

    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        new EventsInvoker().init();

        // Speedium support
        if (Bukkit.getPluginManager().isPluginEnabled("Speedium")) {
            new ItemBlockType().register();
            Speedium.dependencyLoaded();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getPlugin() {
        return plugin;
    }

}
