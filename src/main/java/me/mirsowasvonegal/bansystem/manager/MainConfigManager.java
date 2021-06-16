package me.mirsowasvonegal.bansystem.manager;

import me.mirsowasvonegal.bansystem.data.MainConfig;
import me.mirsowasvonegal.bansystem.data.MessageConfig;
import me.mirsowasvonegal.bansystem.data.MongoConfig;

import java.util.ArrayList;
import java.util.Arrays;

public class MainConfigManager {

    ConfigManager configManager;

    public MainConfigManager() {
        this.configManager = new ConfigManager("config", MainConfig.class);
    }

    public void createConfig() {
        MainConfig mainConfig = new MainConfig();
        mainConfig.setMongoConfig(new MongoConfig("localhost", "admin", "Test1234", "BanSystem"));
        this.configManager.createConfig(mainConfig);
    }

    public MainConfig getConfig() {
        return configManager.getConfig();
    }


}
