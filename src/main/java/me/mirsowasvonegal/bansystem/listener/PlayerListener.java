package me.mirsowasvonegal.bansystem.listener;

import me.mirsowasvonegal.bansystem.BanSystem;
import me.mirsowasvonegal.bansystem.manager.UserManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

    @EventHandler
    public void onConnection(LoginEvent event) {
        event.registerIntent(BanSystem.getInstance());
        BanSystem.getInstance().getProxy().getScheduler().runAsync(BanSystem.getInstance(), () -> {
            UserManager userManager = new UserManager(event.getConnection().getUniqueId());
            userManager.initializeUser(event);
            event.completeIntent(BanSystem.getInstance());
        });
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        UserManager userManager = new UserManager(player.getUniqueId());
        if(!(event.getMessage().startsWith("/"))) {
            if(userManager.isMuted())
                event.setCancelled(true);
        }
    }

}
