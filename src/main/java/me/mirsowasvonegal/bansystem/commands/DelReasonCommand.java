package me.mirsowasvonegal.bansystem.commands;

import me.mirsowasvonegal.bansystem.BanSystem;
import me.mirsowasvonegal.bansystem.data.MessageConfig;
import me.mirsowasvonegal.bansystem.data.Reason;
import me.mirsowasvonegal.bansystem.data.Type;
import me.mirsowasvonegal.bansystem.manager.MessageManager;
import me.mirsowasvonegal.bansystem.manager.ReasonManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.HashSet;
import java.util.Set;

public class DelReasonCommand extends Command implements TabExecutor {

    public DelReasonCommand(String name, String permission, String... aliases) {
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
                        if (isStringInt(args[0])) {
                            new ReasonManager().removeReason(Integer.parseInt(args[0]));
                                player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getReasonDelSuccessful())
                                        .replace("&", "§")));
                        } else {
                            player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getReasonDelHelp())
                                    .replace("&", "§")));
                            break;
                        }
                        break;
                    default:
                        player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getReasonDelHelp())
                                .replace("&", "§")));
                        break;
                }
            });
        }
    }

    public boolean isStringInt(String s)
    {
        try
        {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        Set<String> matches = new HashSet<>();
        switch (args.length) {
            case 1:
                String search = args[0].toLowerCase();
                ReasonManager reasonManager = new ReasonManager();
                for (Reason reason : reasonManager.getAllReasons()) {
                    if ((reason.getReasonId()+"").startsWith(search))
                    {
                        matches.add(reason.getReasonId()+"");
                    }
                }
                return matches;
            default:
                return matches;
        }
    }
}
