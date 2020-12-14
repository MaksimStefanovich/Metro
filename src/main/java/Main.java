import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        try {
            StationIndex stationIndex = new StationIndex("http://ru.wikipedia.org/wiki/Список_станций_Московского_метрополитена");
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(StationIndex.class, new StationIndexSerializer())
                    .create();
            String json = gson.toJson(stationIndex);
            BufferedWriter writer = new BufferedWriter(new FileWriter("out.json"));
            writer.write(json);
            writer.close();


            gson = new GsonBuilder()
                    .registerTypeAdapter(StationIndex1.class, new StationIndexDeserializer())
                    .create();
            StationIndex1 stationIndex1 = gson.fromJson(json, StationIndex1.class);

            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            for (Map.Entry entry : stationIndex1.number2line.entrySet()) {
                List<Station> list = new ArrayList<>();

                list.addAll(stationIndex1.number2line.get(entry.getKey()).getStations());
                if(list.size() > 0)
                System.out.println("Количество станций на " + entry.getKey() + " линии =  " + list.size());
            }




        } catch (IOException e) {
            System.out.println("Error");
        }
    }


}
