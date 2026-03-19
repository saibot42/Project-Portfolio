package SOLO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadCSV {
    
   public static void updatePlayerPointsFromCSV(String filePath, List<Player> players) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String playerName = values[0];
                int week = Integer.parseInt(values[1]);
                double points = Double.parseDouble(values[2]);

                // find the player and update points
                for (Player p : players) {
                    if (p.getName().equals(playerName)) {
                        p.setPointsForWeek(week, points);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
