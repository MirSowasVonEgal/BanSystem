package me.mirsowasvonegal.bansystem.commands;

import me.mirsowasvonegal.bansystem.BanSystem;
import me.mirsowasvonegal.bansystem.data.MessageConfig;
import me.mirsowasvonegal.bansystem.data.Reason;
import me.mirsowasvonegal.bansystem.data.User;
import me.mirsowasvonegal.bansystem.manager.MessageManager;
import me.mirsowasvonegal.bansystem.manager.ReasonManager;
import me.mirsowasvonegal.bansystem.manager.UserManager;
import me.mirsowasvonegal.bansystem.utils.Formatter;
import me.mirsowasvonegal.bansystem.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.*;

public class BanInfoCommand extends Command implements TabExecutor {

    public BanInfoCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            BanSystem.getInstance().getProxy().getScheduler().runAsync(BanSystem.getInstance(), () -> {
                ProxiedPlayer player = (ProxiedPlayer) sender;
                MessageConfig messageConfig = new MessageManager().getConfig();
                UUID uuid;

                HashMap<Integer, Reason> reasons = new HashMap<>();

                ReasonManager reasonManager = new ReasonManager();
                reasonManager.getAllReasons().forEach(Reasons -> {
                    reasons.put(Reasons.getReasonId(), Reasons);
                });

                Formatter formatter = new Formatter();

                switch (args.length) {
                    case 1:
                        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
                        if (target != null) {
                            uuid = target.getUniqueId();
                        } else {
                            uuid = UUIDFetcher.getUUID(args[0]);
                            if (uuid == null) {
                                player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getPlayerNotFound())
                                        .replace("&", "§")));
                                return;
                            }
                        }
                        UserManager userManager = new UserManager(uuid);
                        userManager.initializeUser(args[0]);
                        User user = userManager.getUser();

                        StringBuilder stringBuilder = new StringBuilder();
                        messageConfig.getBanInfoMessage().forEach(Line -> {
                            stringBuilder.append("\n");
                            stringBuilder.append(Line);
                        });

                        String firstLogin = "/";
                        if(!user.getFirstLogin().equals(""))
                            firstLogin = formatter.getDate(Long.parseLong(user.getFirstLogin()));

                        String lastLogin = "/";
                        if(!user.getFirstLogin().equals(""))
                            lastLogin = formatter.getDate(Long.parseLong(user.getLastLogin()));

                        player.sendMessage(new TextComponent(stringBuilder.toString()
                                .replace("&", "§")
                                .replace("%username%", user.getUsername())
                                .replace("%uuid%", user.getUuid().toString())
                                .replace("%firstlogin%", firstLogin)
                                .replace("%lastlogin%", lastLogin)));

                        user.getBan().forEach(Ban -> {
                            String active = "";
                            if((Ban.getUnbanned() == null) &&
                                    ((Long.parseLong(Ban.getEnd()) > System.currentTimeMillis()) || (Long.parseLong(Ban.getEnd()) == -1))) {
                                active = messageConfig.getActiveSymbol()
                                        .replace("&", "§");
                            }

                            if(reasons.get(Ban.getReasonId()) == null) return;

                            TextComponent item = new TextComponent(messageConfig.getBanInfoListItem()
                                    .replace("&", "§")
                                    .replace("%id%", Ban.getBanId())
                                    .replace("%reason%", reasons.get(Ban.getReasonId()).getName())
                                    .replace("%type%", reasons.get(Ban.getReasonId()).getType().toString())
                                    .replace("%active%", active));
                            item.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/baninfo " + args[0] + " " + Ban.getBanId()));

                            player.sendMessage(item);
                        });
                        break;
                    case 2:
                        ProxiedPlayer targetInfo = ProxyServer.getInstance().getPlayer(args[0]);
                        if (targetInfo != null) {
                            uuid = targetInfo.getUniqueId();
                        } else {
                            uuid = UUIDFetcher.getUUID(args[0]);
                            if (uuid == null) {
                                player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getPlayerNotFound())
                                        .replace("&", "§")));
                                return;
                            }
                        }
                        UserManager userManagerInfo = new UserManager(uuid);
                        userManagerInfo.initializeUser(args[0]);
                        User userInfo = userManagerInfo.getUser();

                        userInfo.getBan().forEach(Ban -> {
                            if(Ban.getBanId().equals(args[1])) {
                                if(reasons.get(Ban.getReasonId()) == null) return;

                                StringBuilder stringBuilderInfo = new StringBuilder();
                                messageConfig.getBanInfoBanIDMessage().forEach(Line -> {
                                    stringBuilderInfo.append("\n");
                                    stringBuilderInfo.append(Line);
                                });

                                String unbanned = "/";
                                if(Ban.getUnbanned() != null)
                                    unbanned = UUIDFetcher.getName(Ban.getUnbanned().toString());

                                player.sendMessage(new TextComponent(stringBuilderInfo.toString()
                                        .replace("&", "§")
                                        .replace("%username%", userInfo.getUsername())
                                        .replace("%id%", Ban.getBanId())
                                        .replace("%reason%", reasons.get(Ban.getReasonId()).getName())
                                        .replace("%by%", UUIDFetcher.getName(Ban.getBy().toString()))
                                        .replace("%type%",reasons.get(Ban.getReasonId()).getType().toString())
                                        .replace("%end%", formatter.getDate(Long.parseLong(Ban.getEnd())))
                                        .replace("%start%", formatter.getDate(Long.parseLong(Ban.getStart())))
                                        .replace("%unbanned%", unbanned)));
                            }
                        });
                        break;
                    default:
                        player.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getBanInfoHelp())
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
