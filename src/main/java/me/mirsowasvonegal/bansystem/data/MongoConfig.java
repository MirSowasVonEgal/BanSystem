package me.mirsowasvonegal.bansystem.data;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MongoConfig {

    @Getter
    @Setter
    private String host;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String password;

    @Getter @Setter
    private String database;

}
