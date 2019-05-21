package net.ffmpegcompilations.vlctimepicker;

import java.io.IOException;
import java.sql.SQLOutput;

public class TimePickApplication {

    public static void main(String[] args) throws IOException, InterruptedException {

        TelnetVlcHandler handler = new TelnetVlcHandler();
        handler.openConnection();
        while(true){
            System.out.println("playing: " + handler.isPlayingMedia());
            System.out.println("time: " + handler.getPlayingTimeInSeconds());

            Thread.sleep(500);
        }
    }
}
