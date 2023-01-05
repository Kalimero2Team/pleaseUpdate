package eu.byquanton.plugins.pleaseUpdate;

import eu.byquanton.plugins.pleaseUpdate.util.VVWarning;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class PleaseUpdatePlugin extends JavaPlugin implements CommandExecutor {
    public boolean viaVersionIntegration = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new VVWarning(this), this);

        try {
            Class.forName("com.viaversion.viaversion.api.Via");
            viaVersionIntegration = true;
            getLogger().info("ViaVersion found, enabling version warning");
        } catch (ClassNotFoundException e) {
            viaVersionIntegration = false;
            getLogger().info("ViaVersion not found, disabling version warning");
        }

    }

    @Override
    public void onDisable() {
        getServer().getUnsafe().removeAdvancement(new NamespacedKey(this, "update_please"));
        getServer().reloadData();
    }
}
