package me.mirsowasvonegal.bansystem.data;

import lombok.*;

import java.util.ArrayList;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Getter
    @Setter
    private UUID uuid;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String ipaddress;

    @Getter @Setter
    private String firstLogin;

    @Getter @Setter
    private String lastLogin;

    @Getter @Setter
    private ArrayList<Ban> ban;

}

