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
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.*;

public class BanCommand extends Command implements TabExecutor {

    public BanCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            BanSystem.getInstance().getProxy().getScheduler().runAsync(BanSystem.getInstance(), () -> {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                MessageConfig messageConfig = new MessageManager().getConfig();

                if (args.length == 0 || args.length == 1) {
                    player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getBanListTitle())
                            .replace("&", "§")));
                    HashMap<Integer, Reason> reasons = new HashMap<>();

                    ReasonManager reasonManager = new ReasonManager();
                    reasonManager.getAllReasons().forEach(Reasons -> {
                        reasons.put(Reasons.getReasonId(), Reasons);
                    });


                    Map<Integer, Reason> sorted = new TreeMap<>(reasons);

                    sorted.forEach((ID, Reasons) -> {
                        player.sendMessage(new TextComponent(messageConfig.getBanListItem()
                                .replace("&", "§")
                                .replace("%id%", Reasons.getReasonId() + "")
                                .replace("%reason%", Reasons.getName())
                                .replace("%type%", Reasons.getType().toString())));
                    });

                    player.sendMessage(new TextComponent(" "));
                    player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getBanHelp())
                            .replace("&", "§")));
                } else if (args.length == 2) {
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                    if (!isStringInt(args[1])) {
                        player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getBanHelp())
                                .replace("&", "§")));
                        return;
                    }
                    if (target == null) {
                        UUID uuid = UUIDFetcher.getUUID(args[0]);
                        if (uuid == null) {
                            player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getPlayerNotFound())
                                    .replace("&", "§")));
                            return;
                        }
                        UserManager userManager = new UserManager(uuid);
                        userManager.initializeUser(args[0]);
                        if (!userManager.banUser(Integer.parseInt(args[1]), player.getUniqueId())) {
                            player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getReasonNotFound())
                                    .replace("&", "§")));
                            return;
                        }
                        player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getBanSuccessful())
                                .replace("&", "§")));
                    } else {
                        UserManager userManager = new UserManager(target.getUniqueId());
                        if (!userManager.banUser(Integer.parseInt(args[1]), player.getUniqueId())) {
                            player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getReasonNotFound())
                                    .replace("&", "§")));
                            return;
                        }
                        player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getBanSuccessful())
                                .replace("&", "§")));
                    }
                } else {
                    player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getBanHelp())
                            .replace("&", "§")));
                    return;
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
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
                {
                    if (player.getName().toLowerCase().startsWith(search))
                    {
                        matches.add(player.getName());
                    }
                }
                return matches;
            case 2:
                String search2 = args[1].toLowerCase();
                ReasonManager reasonManager = new ReasonManager();
                for (Reason reason : reasonManager.getAllReasons()) {
                    if ((reason.getReasonId()+"").startsWith(search2))
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
