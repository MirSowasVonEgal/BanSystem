package me.mirsowasvonegal.bansystem.commands;

import me.mirsowasvonegal.bansystem.BanSystem;
import me.mirsowasvonegal.bansystem.data.MessageConfig;
import me.mirsowasvonegal.bansystem.data.Reason;
import me.mirsowasvonegal.bansystem.data.Type;
import me.mirsowasvonegal.bansystem.manager.MessageManager;
import me.mirsowasvonegal.bansystem.manager.ReasonManager;
import me.mirsowasvonegal.bansystem.manager.UserManager;
import me.mirsowasvonegal.bansystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class AddReasonCommand extends Command implements TabExecutor {

    public AddReasonCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            BanSystem.getInstance().getProxy().getScheduler().runAsync(BanSystem.getInstance(), () -> {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                MessageConfig messageConfig = new MessageManager().getConfig();

                switch (args.length) {
                    case 6:
                        if (isStringInt(args[0]) && isStringInt(args[2])) {
                            if(args[4].equals("BAN") || args[4].equals("MUTE")) {
                                long time = Long.parseLong(args[2]);
                                if (args[3].equals("Days")) {
                                    time = time*86400000;
                                }
                                if (args[3].equals("Hours")) {
                                    time = time*3600000;
                                }
                                 if (args[3].equals("Minutes")) {
                                    time = time*60000;
                                }
                                if (args[3].equals("Seconds")) {
                                    time = time*1000;
                                }
                                if(!new ReasonManager().addReason(Integer.parseInt(args[0]), args[1], time, Type.valueOf(args[4]), args[5])) {
                                    player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getReasonIdInUse())
                                            .replace("&", "§")));
                                    return;
                                }
                                player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getReasonAddSuccessful())
                                        .replace("&", "§")));
                            } else {
                                player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getReasonAddHelp())
                                        .replace("&", "§")));
                                break;
                            }
                        } else {
                            player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getReasonAddHelp())
                                    .replace("&", "§")));
                            break;
                        }
                        break;
                    default:
                        player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getReasonAddHelp())
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
            case 4:
                String search4 = args[3].toLowerCase();
                if ("days".startsWith(search4))
                    matches.add("Days");
                if ("hours".startsWith(search4))
                    matches.add("Hours");
                if ("minutes".startsWith(search4))
                    matches.add("Minutes");
                if ("seconds".startsWith(search4))
                    matches.add("Seconds");
                return matches;
            case 5:
                String search5 = args[4].toUpperCase();
                if ("BAN".startsWith(search5))
                    matches.add("BAN");
                if ("MUTE".startsWith(search5))
                    matches.add("MUTE");
                return matches;
            case 6:
                String search6 = args[5].toLowerCase();
                if ("ban.".startsWith(search6))
                    matches.add("ban.");
                return matches;
            default:
                return matches;
        }
    }
}
