package me.mirsowasvonegal.bansystem.data;

import lombok.*;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageConfig {

    @Getter @Setter
    private String prefix;

    @Getter
    @Setter
    private ArrayList<String> banScreen;

    @Getter
    @Setter
    private ArrayList<String> kickScreen;

    @Getter
    @Setter
    private ArrayList<String> muteMessage;

    @Getter
    @Setter
    private ArrayList<String> banInfoMessage;

    @Getter
    @Setter
    private ArrayList<String> BanInfoBanIDMessage;

    @Getter @Setter
    private String kickHelp;

    @Getter @Setter
    private String banListTitle;

    @Getter @Setter
    private String banListItem;

    @Getter @Setter
    private String banHelp;

    @Getter @Setter
    private String banSuccessful;

    @Getter @Setter
    private String unbanHelp;

    @Getter @Setter
    private String unbanSuccessful;

    @Getter @Setter
    private String unbanIsNotBanned;

    @Getter @Setter
    private String reasonAddHelp;

    @Getter @Setter
    private String reasonAddSuccessful;

    @Getter @Setter
    private String reasonDelHelp;

    @Getter @Setter
    private String reasonDelSuccessful;

    @Getter @Setter
    private String reasonIdInUse;

    @Getter @Setter
    private String reasonNotFound;

    @Getter @Setter
    private String banInfoHelp;

    @Getter @Setter
    private String banInfoListItem;

    @Getter @Setter
    private String playerNotFound;

    @Getter @Setter
    private String activeSymbol;
}
