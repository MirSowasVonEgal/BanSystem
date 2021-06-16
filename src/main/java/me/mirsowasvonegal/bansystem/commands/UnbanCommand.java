package me.mirsowasvonegal.bansystem.commands;

import me.mirsowasvonegal.bansystem.BanSystem;
import me.mirsowasvonegal.bansystem.data.MessageConfig;
import me.mirsowasvonegal.bansystem.data.Reason;
import me.mirsowasvonegal.bansystem.manager.MessageManager;
import me.mirsowasvonegal.bansystem.manager.ReasonManager;
import me.mirsowasvonegal.bansystem.manager.UserManager;
import me.mirsowasvonegal.bansystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UnbanCommand extends Command {

    public UnbanCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            BanSystem.getInstance().getProxy().getScheduler().runAsync(BanSystem.getInstance(), () -> {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                MessageConfig messageConfig = new MessageManager().getConfig();

                switch (args.length) {
                    case 1:
                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                        if (target == null) {
                            UUID uuid = UUIDFetcher.getUUID(args[0]);
                            if (uuid == null) {
                                player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getPlayerNotFound())
                                        .replace("&", "§")));
                                return;
                            }
                            UserManager userManager = new UserManager(uuid);
                            userManager.initializeUser(args[0]);
                            userManager.unbanUser(player.getUniqueId());
                        } else {
                            UserManager userManager = new UserManager(target.getUniqueId());
                            userManager.unbanUser(player.getUniqueId());
                        }
                        break;
                    default:
                        player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getUnbanHelp())
                                .replace("&", "§")));
                        break;
                }
            });
        }
    }

}
