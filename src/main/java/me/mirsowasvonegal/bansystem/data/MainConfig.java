package me.mirsowasvonegal.bansystem.data;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainConfig {

    @Getter @Setter
    private MongoConfig mongoConfig;

    @Getter @Setter
    private Boolean VPNKick;

}
