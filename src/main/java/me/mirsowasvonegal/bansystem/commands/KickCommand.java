package me.mirsowasvonegal.bansystem.commands;

import me.mirsowasvonegal.bansystem.BanSystem;
import me.mirsowasvonegal.bansystem.data.MessageConfig;
import me.mirsowasvonegal.bansystem.data.Reason;
import me.mirsowasvonegal.bansystem.manager.MessageManager;
import me.mirsowasvonegal.bansystem.manager.ReasonManager;
import me.mirsowasvonegal.bansystem.utils.Formatter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.HashSet;
import java.util.Set;

public class KickCommand extends Command implements TabExecutor {
    public KickCommand(String name, String permission, String... aliases) {
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
                        if(target == null) {
                            player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getPlayerNotFound())
                                    .replace("&", "§")));
                            return;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        MessageManager messageManager = new MessageManager();
                        messageManager.getConfig().getKickScreen().forEach(Line -> {
                            stringBuilder.append(Line);
                            stringBuilder.append("\n");
                        });
                        target.disconnect(new TextComponent(stringBuilder.toString()
                                .replace("&", "§")
                                .replace("%reason%", "/")));
                        break;
                    default:
                        ProxiedPlayer targetDefault = ProxyServer.getInstance().getPlayer(args[0]);
                        if(targetDefault == null) {
                            player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getPlayerNotFound())
                                    .replace("&", "§")));
                            return;
                        }
                        StringBuilder stringBuilderReason = new StringBuilder();
                        for (int i = 1; i<args.length; i++)
                            stringBuilderReason.append(args[i] + " ");

                        StringBuilder stringBuilderDefault = new StringBuilder();
                        MessageManager messageManagerDefault = new MessageManager();
                        messageManagerDefault.getConfig().getKickScreen().forEach(Line -> {
                            stringBuilderDefault.append(Line);
                            stringBuilderDefault.append("\n");
                        });
                        targetDefault.disconnect(new TextComponent(stringBuilderDefault.toString()
                                .replace("&", "§")
                                .replace("%reason%", stringBuilderReason.toString())));
                        break;
                    case 0:
                        player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getKickHelp())
                                .replace("&", "§")));
                        break;
                }
            });
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        Set<String> matches = new HashSet<>();
        switch (args.length) {
            case 1:
                String search = args[0].toLowerCase();
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
                {
                    if (player.getName().toLowerCase().startsWith(search))
                    {
                        matches.add(player.getName());
                    }
                }
                return matches;
            default:
                return matches;
        }
    }

}
