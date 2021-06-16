package me.mirsowasvonegal.bansystem.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ConfigManager {

    Gson gson;
    File file;
    Class classOfT;

    public <T> ConfigManager(String fileName, Class classOfT) {
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        this.file = new File("plugins//MSVE-BanSystem//" + fileName + ".json");
        this.classOfT = classOfT;
    }

    public <T> void createConfig(T target) {
        try {
            if(!file.exists()) {
                file.getParentFile().mkdir();
                file.createNewFile();
                try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
                    bufferedWriter.write(gson.toJson(target));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T getConfig() {
        try {
            return gson.fromJson(Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8), (Type) classOfT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
