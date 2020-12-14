import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class StationIndex1 {
    HashMap<String, Line> number2line;
    TreeSet<Station> stations;
    TreeMap<Station, TreeSet<Station>> connections;


    public StationIndex1() {
        number2line = new HashMap<>();
        stations = new TreeSet<>();
        connections = new TreeMap<>();
    }
}
