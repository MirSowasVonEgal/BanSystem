package me.mirsowasvonegal.bansystem.manager;

import me.mirsowasvonegal.bansystem.data.MessageConfig;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageManager {

    ConfigManager configManager;

    public MessageManager() {
        this.configManager = new ConfigManager("message", MessageConfig.class);
    }

    public void createConfig() {
        MessageConfig messageConfig = new MessageConfig();
        messageConfig.setPrefix("&f&lMSVE-BanSystem &8➜ &7");
        messageConfig.setBanScreen(new ArrayList<>(Arrays.asList(
                "&f&lMSVE-BanSystem",
                "&cDu wurdest vom Netzwerk gebannt!",
                " ",
                "&7Grund: &b%reason%",
                "&7Endet in: &b%time%",
                "&7Ban ID: &b#%id%",
                " ",
                "&eDu kannst einen Entbannungsantrag unter &fforum.MSVE-BanSystem.net &eerstellen!",
                " ",
                "&fDas verbreiten deiner Ban ID kann deine Entbannung beeinträchtigen!")));
        messageConfig.setKickScreen(new ArrayList<>(Arrays.asList(
                "&f&lMSVE-BanSystem",
                "&cDu wurdest vom Netzwerk gekickt!",
                " ",
                "&7Grund: &b%reason%")));
        messageConfig.setMuteMessage(new ArrayList<>(Arrays.asList(
                "&f&lMSVE-BanSystem",
                "&cDu wurdest vom Netzwerk gemutet!",
                " ",
                "&7Grund: &b%reason%",
                "&7Endet in: &b%time%",
                "&7Ban ID: &b#%id%",
                " ",
                "&eDu kannst einen Entbannungsantrag unter &fforum.MSVE-BanSystem.net &eerstellen!",
                " ",
                "&fDas verbreiten deiner Ban ID kann deine Entbannung beeinträchtigen!")));
        messageConfig.setBanInfoMessage(new ArrayList<>(Arrays.asList(
                "&f&lMSVE-BanSystem",
                "&7Hier sind die Spieler Infos von: &3%username%",
                " ",
                "&7Username: &b%username%",
                "&7UUID: &b%uuid%",
                "&7Erster Login: &b%firstlogin%",
                "&7Letzter Login: &b%lastlogin%",
                "&7Bans: (Klicke auf einen Ban um weitere Infos zu erhalten!)")));
        messageConfig.setBanInfoBanIDMessage(new ArrayList<>(Arrays.asList(
                "&f&lMSVE-BanSystem",
                "&3%username% &7- &e%id%",
                " ",
                "&7Grund: &b%reason%",
                "&7Type: &b%type%",
                "&7Von: &b%by%",
                "&7Gebannt am: &b%start%",
                "&7Ende: &b%end%",
                "&7Entbannt: &b%unbanned%")));
        messageConfig.setKickHelp("&e/kick <Spieler> (Grund)");
        messageConfig.setBanListTitle("&eEs gibt folgende Ban IDs:");
        messageConfig.setBanListItem("&7- &e%id% &8➜ &c%reason% &7- &4&l%type%");
        messageConfig.setBanHelp("&e/Ban <Spieler> <ID>");
        messageConfig.setBanSuccessful("&7Dieser Spieler wurde &aerfolgreich &7gebannt!");
        messageConfig.setUnbanHelp("&e/Unban <Spieler>");
        messageConfig.setUnbanSuccessful("&7Dieser Spieler wurde &aerfolgreich &7entbannt!");
        messageConfig.setUnbanIsNotBanned("&7Dieser Spieler wurde &cnicht &7gebannt!");
        messageConfig.setReasonAddHelp("&e/AddReason <ID> <Name> <Dauer> <Days/Hours/Minutes/Seconds> <BAN/MUTE> <Permission>");
        messageConfig.setReasonAddSuccessful("&7Dieser Grund wurde &aerfolgreich &7hinzugefügt!");
        messageConfig.setReasonDelHelp("&e/DelReason <ID>");
        messageConfig.setReasonDelSuccessful("&7Dieser Grund wurde &aerfolgreich &cgelöscht&7!");
        messageConfig.setReasonIdInUse("&7Diese ID wird bereits &cverwendet&7!");
        messageConfig.setReasonNotFound("&7Dieser Grund wurde &cnicht &7gefunden!");
        messageConfig.setBanInfoHelp("&e/BanInfo <Spieler>");
        messageConfig.setBanInfoListItem("&7- &e%id% &8➜ &c%reason% &7- &4&l%type% %active%");
        messageConfig.setPlayerNotFound("&7Dieser Spieler wurde &cnicht &7gefunden!");
        messageConfig.setActiveSymbol("&8(&aAktiv&8)");
        this.configManager.createConfig(messageConfig);
    }

    public MessageConfig getConfig() {
        return configManager.getConfig();
    }

}
