package org.globemc.temppres;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public final class Temp_Pres extends JavaPlugin implements Listener {
    public Logger l = getLogger();
    public Server s = getServer();
    private int counter = 1;

    private double minDamageHeight = 0d;
    private double damage = 1;
    private int delay = 5;

    @Override
    public void onEnable() {
        l.info("Conditions plugin started");
        s.getPluginManager().registerEvents(this, this);
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                counter += 1;
                if (counter > delay) {
                    counter = 1;
                }
            }
        }, 0, 1L);
    }

    @EventHandler
    public void OnPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p.getLocation().y() <= minDamageHeight)
            if (counter == 1) p.damage(damage);
    }
}
