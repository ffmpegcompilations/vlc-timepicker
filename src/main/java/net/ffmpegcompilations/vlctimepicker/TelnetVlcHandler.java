package net.ffmpegcompilations.vlctimepicker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TelnetVlcHandler {

    Socket pingSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;

    public void openConnection() throws IOException {

        pingSocket = new Socket("localhost", 4212);
        out = new PrintWriter(pingSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));
        System.out.println("version: " + in.readLine());
        out.println("123");
        in.readLine();
        String passwordResponse = in.readLine();
        if (passwordResponse.equals("Wrong password")) {
            System.err.println("wrong vlc password, exiting");
            System.exit(1);
        } else if (passwordResponse.equals("Welcome, Master")) {
            System.out.println("login successful");
        } else {
            System.err.println("unknown error");
            System.exit(1);
        }
    }

    public boolean isPlayingMedia() throws IOException {

        out.println("is_playing");
        String response = trim(in.readLine());
        if(response.equals("1"))
            return true;
        return false;
    }

    private String trim(String s){

        s = s.replace(" ", "");
        s = s.replace(">", "");
        return s;
    }

    public int getPlayingTimeInSeconds() throws IOException {

        int seconds = 0;
        out.println("get_time");
        String response = trim(in.readLine());
        seconds = Integer.parseInt(response);
        return seconds;
    }

    public int getPlayingTimeInMiliseconds() throws IOException, InterruptedException {

        return getPlayingTimeInMiliseconds(50);
    }

    public int getPlayingTimeInMiliseconds(int stepSizeInMs) throws IOException, InterruptedException {

        int currentSeconds;
        int offsetToNextFullSecondInMs = 0;

        do {
            currentSeconds = getPlayingTimeInSeconds();
            out.println("play");
            Thread.sleep(stepSizeInMs);
            out.println("pause");

            if(getPlayingTimeInSeconds() != currentSeconds)
                break;
            offsetToNextFullSecondInMs += stepSizeInMs;
        } while (true);

        int totalPos = currentSeconds * 1000 - offsetToNextFullSecondInMs;

        out.println("seek " + (currentSeconds - 1));
        out.println("play");
        Thread.sleep(1000 - offsetToNextFullSecondInMs);
        out.println("pause");

        return totalPos;
    }
}