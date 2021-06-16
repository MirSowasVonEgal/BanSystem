package me.mirsowasvonegal.bansystem.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;

public class UUIDFetcher {


    private static final HashMap<String, UUID> UUID_CACHE = new HashMap();

    public static UUID getUUID(String username ) {
        if ( UUID_CACHE.containsKey( username ) )
            return UUID_CACHE.get( username );

        try {
            URL url = new URL( "https://api.mojang.com/users/profiles/minecraft/" + username );
            InputStream stream = url.openStream();

            InputStreamReader streamReader = new InputStreamReader( stream );
            BufferedReader bufferedReader = new BufferedReader( streamReader );

            String result = bufferedReader.lines().collect( Collectors.joining() );
            JsonElement jsonElement = new JsonParser().parse( result );
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String id = jsonObject.get( "id" ).toString();
            id = id.substring( 1 );
            id = id.substring( 0, id.length() - 1 );

            StringBuilder stringBuilder = new StringBuilder( id );
            stringBuilder.insert( 8, "-" ).insert( 13, "-" ).insert( 18, "-" ).insert( 23, "-" );

            UUID uuid = UUID.fromString(stringBuilder.toString());
            UUID_CACHE.put( username, uuid );

            return uuid;
        } catch ( IOException | IllegalStateException ignored ) { }
        return null;
    }

    public static String getName(String uuid) {
        uuid = uuid.replace("-", "");

        String output = callURL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);

        JsonElement jsonElement = new JsonParser().parse(output);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        return jsonObject.get("name").toString().replace("\"", "");
    }

    private static String callURL(String URL) {
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            URL url = new URL(URL);
            urlConn = url.openConnection();

            if (urlConn != null) urlConn.setReadTimeout(60 * 1000);

            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);

                if (bufferedReader != null) {
                    int cp;

                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }

                    bufferedReader.close();
                }
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}