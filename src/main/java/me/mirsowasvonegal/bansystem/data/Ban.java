package me.mirsowasvonegal.bansystem.data;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ban {

    @Getter
    @Setter
    private int reasonId;

    @Getter @Setter
    private UUID by;

    @Getter @Setter
    private String start;

    @Getter @Setter
    private String end;

    @Getter @Setter
    private String banId;

    @Getter @Setter
    private UUID unbanned;

}
