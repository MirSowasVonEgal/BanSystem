package me.mirsowasvonegal.bansystem.data;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reason {

    @Getter
    @Setter
    private int reasonId;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String time;

    @Getter @Setter
    private Type type;

    @Getter @Setter
    private String perms;

}
