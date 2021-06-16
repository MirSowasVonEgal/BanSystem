package me.mirsowasvonegal.bansystem.manager;

import com.mongodb.client.model.Filters;
import me.mirsowasvonegal.bansystem.BanSystem;
import me.mirsowasvonegal.bansystem.data.*;
import me.mirsowasvonegal.bansystem.utils.Formatter;
import me.mirsowasvonegal.bansystem.utils.Random;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserManager {

    UUID uuid;
    CollectionManager collectionManager;

    public UserManager(UUID uuid) {
        this.uuid = uuid;
        collectionManager = new CollectionManager(BanSystem.getMongoManger().getBanCollection());
    }

    public void initializeUser(LoginEvent loginEvent) {
        User user = getUser();
        if (user == null) {
            user = new User(this.uuid, loginEvent.getConnection().getName(), loginEvent.getConnection().getAddress().getAddress().getHostAddress(), System.currentTimeMillis()+"", System.currentTimeMillis()+"", new ArrayList<>());
            collectionManager.add(user);
        } else {
            user.getBan().forEach(Ban -> {
                if (Ban.getUnbanned() != null) return;
                if((Long.parseLong(Ban.getEnd()) > System.currentTimeMillis()) || (Long.parseLong(Ban.getEnd()) == -1)) {
                    ReasonManager reasonManager = new ReasonManager();
                    Reason reason = reasonManager.getReason(Ban.getReasonId());
                    if(reason == null) return;
                    if(reason.getType().equals(Type.BAN)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        MessageManager messageManager = new MessageManager();
                        messageManager.getConfig().getBanScreen().forEach(Line -> {
                            stringBuilder.append(Line);
                            stringBuilder.append("\n");
                        });

                        loginEvent.setCancelled(true);
                        loginEvent.setCancelReason(new TextComponent(stringBuilder.toString()
                                .replace("&", "§")
                                .replace("%time%", new Formatter().getRemainingTime(Long.parseLong(Ban.getEnd()) - System.currentTimeMillis()))
                                .replace("%reason%", reason.getName())
                                .replace("%id%", Ban.getBanId())));
                        return;
                    }
                }
            });
            if (user.getFirstLogin() == null)
                user.setFirstLogin(System.currentTimeMillis()+"");
            user.setUsername(loginEvent.getConnection().getName());
            user.setLastLogin(System.currentTimeMillis()+"");
            user.setIpaddress(loginEvent.getConnection().getAddress().getAddress().getHostAddress());
            collectionManager.update(Filters.eq("uuid", this.uuid.toString()), user);
        }
    }

    public void initializeUser(String username) {
        User user = getUser();
        if (user != null) return;
        user = new User(this.uuid, username, "", "", "",new ArrayList<>());
        collectionManager.add(user);
    }

    public User getUser() {
        return collectionManager.find(Filters.eq("uuid", this.uuid.toString()), User.class);
    }

    public void unbanUser(UUID by) {
        User user = getUser();
        AtomicBoolean unbanned = new AtomicBoolean(false);
        user.getBan().forEach(Ban -> {
            if(!((Long.parseLong(Ban.getEnd()) > System.currentTimeMillis())))
                return;
            if(Ban.getUnbanned() == null) {
                Ban.setUnbanned(by);
                unbanned.set(true);
            }
        });
        ProxiedPlayer byPlayer = BanSystem.getInstance().getProxy().getPlayer(by);
        if(byPlayer == null) return;
        MessageConfig messageConfig = new MessageManager().getConfig();
        if(!unbanned.get()) {
            byPlayer.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getUnbanIsNotBanned())
                    .replace("&", "§")));
            return;
        }
        byPlayer.sendMessage(new TextComponent((messageConfig.getPrefix() + messageConfig.getUnbanSuccessful())
                .replace("&", "§")));
        collectionManager.update(Filters.eq("uuid", this.uuid.toString()), user);
    }


    public boolean isMuted() {
        User user = getUser();
        if (user == null) {
            user = new User(this.uuid,"", "", System.currentTimeMillis() + "", System.currentTimeMillis() + "", new ArrayList<>());
            collectionManager.add(user);
            return true;
        } else {
            for (Ban Ban : user.getBan()) {
                ReasonManager reasonManager = new ReasonManager();
                Reason reason = reasonManager.getReason(Ban.getReasonId());
                if(reason != null) {
                    if (reason.getType().equals(Type.MUTE)) {
                        if (Ban.getUnbanned() == null) {
                            if ((Long.parseLong(Ban.getEnd()) > System.currentTimeMillis()) || (Long.parseLong(Ban.getEnd()) == -1)) {
                                ProxiedPlayer player = BanSystem.getInstance().getProxy().getPlayer(uuid);
                                if (player == null) return true;
                                StringBuilder stringBuilder = new StringBuilder();
                                MessageManager messageManager = new MessageManager();
                                messageManager.getConfig().getMuteMessage().forEach(Line -> {
                                    stringBuilder.append(Line);
                                    stringBuilder.append("\n");
                                });

                                player.sendMessage(new TextComponent(stringBuilder.toString()
                                        .replace("&", "§")
                                        .replace("%time%", new Formatter().getRemainingTime(Long.parseLong(Ban.getEnd()) - System.currentTimeMillis()))
                                        .replace("%reason%", reason.getName())
                                        .replace("%id%", Ban.getBanId())));
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    public Boolean banUser(int reasonId, UUID by) {
        ReasonManager reasonManager = new ReasonManager();
        Reason reason = reasonManager.getReason(reasonId);
        if (reason == null) {
            return false;
        }
        User user = getUser();
        String BanId = new Random().generateString(8);
        Long end = (Long.parseLong(reason.getTime()) + System.currentTimeMillis());
        if(Long.parseLong(reason.getTime()) == -1)
            end = -1L;
        user.getBan().add(new Ban(reasonId, by, System.currentTimeMillis() + "", end + "", BanId, null));
        collectionManager.update(Filters.eq("uuid", this.uuid.toString()), user);
        ProxiedPlayer player = BanSystem.getInstance().getProxy().getPlayer(uuid);
        if(reason.getType().equals(Type.BAN)) {
            if (player != null) {
                StringBuilder stringBuilder = new StringBuilder();
                MessageManager messageManager = new MessageManager();
                messageManager.getConfig().getBanScreen().forEach(Line -> {
                    stringBuilder.append(Line);
                    stringBuilder.append("\n");
                });
                player.disconnect(new TextComponent(stringBuilder.toString()
                        .replace("&", "§")
                        .replace("%time%", new Formatter().getRemainingTime(Long.parseLong(reason.getTime())))
                        .replace("%reason%", reason.getName())
                        .replace("%id%", BanId)));
            }
        }
        if(reason.getType().equals(Type.MUTE)) {
            if (player != null) {
                StringBuilder stringBuilder = new StringBuilder();
                MessageManager messageManager = new MessageManager();
                messageManager.getConfig().getMuteMessage().forEach(Line -> {
                    stringBuilder.append(Line);
                    stringBuilder.append("\n");
                });

                player.sendMessage(new TextComponent(stringBuilder.toString()
                        .replace("&", "§")
                        .replace("%time%", new Formatter().getRemainingTime(Long.parseLong(reason.getTime())))
                        .replace("%reason%", reason.getName())
                        .replace("%id%", BanId)));
            }
        }
        return true;
    }

}
