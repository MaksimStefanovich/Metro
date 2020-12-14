import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class StationIndexDeserializer implements JsonDeserializer<StationIndex1> {


    @Override
    public StationIndex1 deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {

        JsonObject jsonObject = json.getAsJsonObject();
        StationIndex1 stationIndex1 = new StationIndex1();

        JsonArray lines = jsonObject.get("lines").getAsJsonArray();
        for (int i = 0; i < lines.size(); i++) {

            String number = lines.get(i).getAsJsonObject().get("number").getAsString();
            String name = lines.get(i).getAsJsonObject().get("name").getAsString();
            Line line = new Line(number, name);
            if (!stationIndex1.number2line.containsKey(number))
                stationIndex1.number2line.put(number, line);
        }


        JsonObject stations = jsonObject.get("stations").getAsJsonObject();

        for (Map.Entry entry : stations.entrySet()) {

            String number = (String) entry.getKey();

            JsonArray elements = (JsonArray) entry.getValue();
            elements.forEach(element -> {
                String name = element.getAsString();
                Station station = new Station(name, stationIndex1.number2line.get(number));
                stationIndex1.stations.add(station);
                stationIndex1.number2line.get(number).addStation(station);

            });
        }


        JsonArray connections = jsonObject.get("connections").getAsJsonArray();
        connections.forEach(elementConnections -> {

            JsonArray firstElement = elementConnections.getAsJsonArray();
            TreeSet<Station> set = new TreeSet<>();
            for (int i = 1; i < firstElement.size(); i++) {
                JsonObject jsonObject1 = firstElement.get(i).getAsJsonObject();

                for (Map.Entry entry1 : jsonObject1.entrySet()) {
                    if (entry1.getKey().toString().contains("station")) {
                        String stationName = entry1.getValue().toString();
                        Station end = getStationByName(stationIndex1.stations, stationName);
                        set.add(end);
                    }
                }
            }
            if (set.size() > 0) {
                for (int i = 0; i < 1; i++) {
                    JsonObject jsonObject1 = firstElement.get(i).getAsJsonObject();


                    for (Map.Entry entry1 : jsonObject1.entrySet()) {
                        if (entry1.getKey().toString().contains("station")) {
                            String stationName = entry1.getValue().toString();
                            Station end = getStationByName(stationIndex1.stations, stationName);
                            stationIndex1.connections.put(end, set);

                        }
                    }
                }
            }
        });


        return stationIndex1;
    }

    private Station getStationByName(TreeSet<Station> stations, String name) {

        Iterator<Station> it = stations.iterator();
        while (it.hasNext()) {
            Station str = it.next();
            if (("\"" + str.getName() + "\"").contains(name))
                return str;
        }

        return null;
    }
}
