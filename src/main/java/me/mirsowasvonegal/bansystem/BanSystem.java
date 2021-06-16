package me.mirsowasvonegal.bansystem;

import lombok.Getter;
import me.mirsowasvonegal.bansystem.commands.*;
import me.mirsowasvonegal.bansystem.data.MongoConfig;
import me.mirsowasvonegal.bansystem.listener.PlayerListener;
import me.mirsowasvonegal.bansystem.manager.MainConfigManager;
import me.mirsowasvonegal.bansystem.manager.MessageManager;
import me.mirsowasvonegal.bansystem.manager.MongoManger;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class BanSystem extends Plugin {

    @Getter
    private static MongoManger mongoManger;

    @Getter
    private static BanSystem instance;

    @Override
    public void onEnable() {
        instance = this;



        new MessageManager().createConfig();
        MainConfigManager mainConfigManager = new MainConfigManager();
        mainConfigManager.createConfig();
        MongoConfig mongoConfig = mainConfigManager.getConfig().getMongoConfig();

        this.mongoManger = new MongoManger(mongoConfig.getUsername(), mongoConfig.getDatabase(), mongoConfig.getPassword(), mongoConfig.getHost());
        this.mongoManger.connect();

        PluginManager pm = ProxyServer.getInstance().getPluginManager();
        pm.registerListener(this, new PlayerListener());

        pm.registerCommand(getInstance(), new BanCommand("ban", "bansystem.command.ban","mute"));
        pm.registerCommand(getInstance(), new UnbanCommand("unban", "bansystem.command.unban", "unmute"));
        pm.registerCommand(getInstance(), new AddReasonCommand("addreason", "bansystem.command.addreason"));
        pm.registerCommand(getInstance(), new DelReasonCommand("delreason", "bansystem.command.delreason", "removereason"));
        pm.registerCommand(getInstance(), new KickCommand("kick", "bansystem.command.kick"));
        pm.registerCommand(getInstance(), new BanInfoCommand("baninfo", "bansystem.command.baninfo"));
    }

    @Override
    public void onDisable() {

    }
}
