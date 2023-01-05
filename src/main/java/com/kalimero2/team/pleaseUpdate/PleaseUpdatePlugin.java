package com.kalimero2.team.pleaseUpdate;

import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PleaseUpdatePlugin extends JavaPlugin implements CommandExecutor, Listener {
    public boolean viaVersionIntegration = false;
    private NamespacedKey updatePlease;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        updatePlease = new NamespacedKey(this, "update_please");
        try {
            Class.forName("com.viaversion.viaversion.api.Via");
            viaVersionIntegration = true;
            getLogger().info("ViaVersion found, enabling version warning");
            getServer().getPluginManager().registerEvents(this, this);
        } catch (ClassNotFoundException e) {
            viaVersionIntegration = false;
            getLogger().warning("ViaVersion not found, disabling version warning");
        }

    }

    @Override
    public void onDisable() {
        removeAdvancement();
    }

    @SuppressWarnings("deprecation")
    private void removeAdvancement() {
        getServer().getUnsafe().removeAdvancement(updatePlease);
        // getServer().reloadData(); Not needed on Shutdown/maybe with Plugin reloads?
    }

    @SuppressWarnings("deprecation")
    public void versionWarning(Player player) {
        if (com.viaversion.viaversion.api.Via.getAPI().getPlayerVersion(player.getUniqueId()) != getConfig().getInt("dont_annoy_for_version")) {
            Server server = player.getServer();

            Advancement updateAdvancement = server.getAdvancement(updatePlease);
            if(updateAdvancement == null){
                updateAdvancement = server.getUnsafe().loadAdvancement(updatePlease, getConfig().getString("advancement_json"));
            }
            String next = updateAdvancement.getCriteria().iterator().next();
            player.getAdvancementProgress(updateAdvancement).awardCriteria(next);
            Advancement finalUpdateAdvancement = updateAdvancement;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isOnline()) {
                        player.getAdvancementProgress(finalUpdateAdvancement).revokeCriteria(next);
                    }
                }
            }.runTaskLater(this, 20 * 2);
        }

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (viaVersionIntegration) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    versionWarning(event.getPlayer());
                }
            }.runTaskLater(this, 20 * 5);
        }
    }
}
