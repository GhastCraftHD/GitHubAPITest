package de.leghast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

  private static volatile String tag;

  public static void main(String[] args) {

    Thread updateChecker = new Thread(
        () -> {
          try{
            URL url = new URL("https://api.github.com/repos/LeGhast/Miniaturise/releases/latest");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try{
              connection.setRequestProperty("Content-Type", "application/json");

              if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) return;

              BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
              String input;
              StringBuilder response = new StringBuilder();

              while ((input = in.readLine()) != null){
                response.append(input);
              }

              in.close();
              connection.disconnect();

              if(!response.toString().contains("tag_name")) return;

              tag = response.toString().split("\"tag_name\":\"")[1].split("\",")[0];

            }finally {
              connection.disconnect();
            }

          }catch (Exception ignore){}
        }
    );

    updateChecker.start();

    while (tag == null){}

    System.out.println(tag);

  }
}