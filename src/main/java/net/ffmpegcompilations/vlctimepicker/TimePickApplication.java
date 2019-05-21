package net.ffmpegcompilations.vlctimepicker;

import java.io.IOException;

public class TimePickApplication {

    public static void main(String[] args) throws IOException, InterruptedException {

        TelnetVlcHandler handler = new TelnetVlcHandler();
        handler.openConnection();
        while (true) {
            boolean isPlaying = handler.isPlayingMedia();
            System.out.println("playing: " + isPlaying);
            if (isPlaying)
                System.out.println("time: " + handler.getPlayingTimeInSeconds());

            Thread.sleep(500);
        }
    }
}
