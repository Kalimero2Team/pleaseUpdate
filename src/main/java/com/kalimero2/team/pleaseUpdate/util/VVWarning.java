package com.kalimero2.team.pleaseUpdate.util;

import com.kalimero2.team.pleaseUpdate.PleaseUpdatePlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class VVWarning implements Listener {

    private final PleaseUpdatePlugin plugin;

    public VVWarning(PleaseUpdatePlugin plugin) {
        this.plugin = plugin;
    }

    public void versionWarning(Player player) {
        if (com.viaversion.viaversion.api.Via.getAPI().getPlayerVersion(player.getUniqueId()) != plugin.getConfig().getInt("dont_annoy_for_version")) {
            Server server = player.getServer();
            NamespacedKey updatePlease = new NamespacedKey(plugin, "update_please");
            Advancement updateAdvancement = server.getAdvancement(updatePlease);
            if(updateAdvancement == null){
                updateAdvancement = server.getUnsafe().loadAdvancement(updatePlease, plugin.getConfig().getString("advancement_json"));
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
            }.runTaskLater(plugin, 20 * 2);
        }

    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (plugin.viaVersionIntegration) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    versionWarning(event.getPlayer());
                }
            }.runTaskLater(plugin, 20 * 5);
        }
    }

}
