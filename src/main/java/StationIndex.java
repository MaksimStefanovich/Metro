import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class StationIndex {
    HashMap<String, Line> number2line;
    TreeSet<Station> stations;
    TreeMap<Station, TreeSet<Station>> connections;


    public StationIndex(String url) throws IOException {
        number2line = new HashMap<>();
        stations = new TreeSet<>();
        connections = new TreeMap<>();


        Document document = Jsoup.connect(url).maxBodySize(0).get();
        Elements elementsTr = document.select("table.standard>tbody>tr");
        elementsTr.forEach(element -> {

            Element spanNumber = element.select("td:nth-child(1) > span").first();
            if (spanNumber != null) {
                String name = element.select("td:nth-child(1) > span").attr("title");
                String number = spanNumber.text();
                Line line = new Line(number, name);

                if (!number2line.containsKey(number))
                    number2line.put(number, line);

            }
        });


        elementsTr.forEach(element -> {

            Element spanNumber = element.select("td:nth-child(1) > span").first();
            if (spanNumber != null) {
                String name = element.select("td:nth-child(1) > span").attr("title");
                String number = spanNumber.text();
                String stationName = element.select("td:nth-child(2) > span > a").text();
                Station station = new Station(stationName, number2line.get(number));
                if (!stationName.equals("")) {
                    stations.add(station);
                    number2line.get(number).addStation(station);


                }


            }

        });

        elementsTr.forEach(element -> {

            Element spanNumber = element.select("td:nth-child(1) > span").first();
            if (spanNumber != null) {
                String name = element.select("td:nth-child(1) > span").attr("title");
                String number = spanNumber.text();
                Line line = new Line(number, name);
                String stationName = element.select("td:nth-child(2) > span > a").text();
                Station station = new Station(stationName, number2line.get(number));
                if (!stationName.equals("")) {
                    stations.add(station);

                    Elements connectionsSpans = element.select("td:nth-child(4) > span");
                    TreeSet<Station> set = new TreeSet<>();

                    connectionsSpans.forEach(span -> {
                        if (span.hasClass("sortkey")) {
                            String numberStationConnection = span.text();
                            String nameStationConnection = span.nextElementSibling().attr("title")
                                    .replace("Переход на станцию ", "")
                                    .replace("Кросс-платформенная пересадка на станцию ", "")
                                    .split(" ")[0];

                            Station stationByName = getStationByName(numberStationConnection, nameStationConnection);


                            if (stationByName != null) {

                                set.add(stations.stream().filter(data -> Objects.equals(data, stationByName)).findFirst().get());


                            }


                        }
                    });
                    if (set.size() > 0) {
                        connections.put(station, set);
                    }
                }
            }


        });


    }


    private Station getStationByName(String lineNumber, String firstName) {
        List<Station> stations = this.number2line.get(lineNumber).getStations();
        for (Station station : stations) {
            if (station.getName().contains(firstName))
                return station;
        }
        return null;
    }


}



