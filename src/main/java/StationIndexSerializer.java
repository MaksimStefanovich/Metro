import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class StationIndexSerializer implements JsonSerializer<StationIndex> {


    @Override
    public JsonElement serialize(StationIndex src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        JsonObject stations = new JsonObject();
        JsonArray connections = new JsonArray();
        JsonArray lines = new JsonArray();


        for (Map.Entry entry : src.number2line.entrySet()) {

            JsonElement jsonElement = new JsonArray();
            String number = (String) entry.getKey();
            ArrayList<Station> list = new ArrayList<>();
            list.addAll(src.number2line.get(entry.getKey()).getStations());
            list.forEach(station -> jsonElement.getAsJsonArray().add(station.getName()));
            stations.add(number, jsonElement);
        }

        for (Map.Entry entry : src.connections.entrySet()) {

            JsonElement jsonElement = new JsonArray();
            JsonObject con = new JsonObject();

            Station station = (Station) entry.getKey();
            String name = station.getName();
            Line line = station.getLine();
            String string = line.getNumber();
            con.addProperty("line", string);
            con.addProperty("station", name);
            jsonElement.getAsJsonArray().add(con);

            TreeSet<Station> dest = src.connections.get(entry.getKey());
            Iterator<Station> iter = dest.iterator();
            while (iter.hasNext()) {
                if (iter.hasNext()) {
                    JsonObject another = new JsonObject();
                    Station station1 = iter.next();
                    String name1 = station1.getName();
                    Line line1 = station1.getLine();
                    String string1 = line1.getNumber();
                    another.addProperty("line", string1);
                    another.addProperty("station", name1);
                    jsonElement.getAsJsonArray().add(another);
                }
            }
            connections.add(jsonElement);
        }


        for (Map.Entry entry : src.number2line.entrySet()) {
            JsonObject con = new JsonObject();
            Line line = (Line) entry.getValue();
            con.addProperty("number", line.getNumber());
            con.addProperty("name", line.getName());

            lines.add(con);

        }


        result.add("stations", stations);
        result.add("connections", connections);
        result.add("lines", lines);


        return result;

    }
}
