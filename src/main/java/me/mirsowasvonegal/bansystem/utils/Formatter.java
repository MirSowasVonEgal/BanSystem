package me.mirsowasvonegal.bansystem.utils;

import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@NoArgsConstructor
public class Formatter {

    public String getRemainingTime(long remainingTime) {
        if (remainingTime <= -1) return "PERMANENT";
        long time = remainingTime;
        long Days = 0;
        long Hours = 0;
        long Minutes = 0;
        long Seconds = 0;
        if(time >= 1000*60*60*24) {
            Days = (long) Math.floor(time / (1000 * 60 * 60 * 24));
            time = time - ((1000 * 60 * 60 * 24) * Days);
        }
        if(time >= 1000*60*60) {
            Hours = (long) Math.floor(time / (1000 * 60 * 60));
            time = time - ((1000 * 60 * 60) * Hours);
        }
        if(time >= 1000*60) {
            Minutes = (long) Math.floor(time / (1000 * 60));
            time = time - ((1000 * 60) * Minutes);
        }
        if(time >= 1000) {
            Seconds = (long) Math.floor(time / (1000));
        }
        return Days + "d " + Hours + "h " + Minutes + "m " + Seconds + "s";
    }

    public String getDate(long time) {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        return df.format(time);
    }


}
