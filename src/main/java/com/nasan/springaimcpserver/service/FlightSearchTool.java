package com.nasan.springaimcpserver.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class FlightSearchTool {

    @Tool(name = "flight.searchByDate", description = "Belirli bir tarihteki uçuşları listeler (YYYY-MM-DD formatında)")
    public List<Map<String, Object>> searchFlightsByDate(String date) {
        List<Map<String, Object>> flights = new ArrayList<>();
        
        try {
            // Tarih formatını kontrol et
            LocalDate flightDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            
            // Örnek uçuş verileri oluştur (gerçek senaryoda web scraping veya API kullanılır)
            flights.addAll(generateSampleFlights(flightDate));
            
        } catch (DateTimeParseException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Geçersiz tarih formatı. YYYY-MM-DD formatında girin (örn: 2024-03-15)");
            flights.add(error);
        }
        
        return flights;
    }

    @Tool(name = "flight.searchByCity", description = "Belirli şehirler arası uçuşları listeler")
    public List<Map<String, Object>> searchFlightsByCity(String fromCity, String toCity) {
        List<Map<String, Object>> flights = new ArrayList<>();
        
        if (fromCity == null || toCity == null || fromCity.trim().isEmpty() || toCity.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Kalkış ve varış şehri belirtilmelidir");
            flights.add(error);
            return flights;
        }
        
        // Şehir isimlerini normalize et
        fromCity = normalizeCity(fromCity);
        toCity = normalizeCity(toCity);
        
        flights.addAll(generateCityFlights(fromCity, toCity));
        
        return flights;
    }

    @Tool(name = "flight.searchByAirline", description = "Belirli havayolu şirketinin uçuşlarını listeler")
    public List<Map<String, Object>> searchFlightsByAirline(String airlineName) {
        List<Map<String, Object>> flights = new ArrayList<>();
        
        if (airlineName == null || airlineName.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Havayolu şirketi adı belirtilmelidir");
            flights.add(error);
            return flights;
        }
        
        flights.addAll(generateAirlineFlights(airlineName));
        
        return flights;
    }

    @Tool(name = "flight.getPopularRoutes", description = "Popüler uçuş rotalarını listeler")
    public List<Map<String, Object>> getPopularRoutes() {
        List<Map<String, Object>> routes = new ArrayList<>();
        
        String[][] popularRoutes = {
            {"İstanbul", "Ankara", "1h 30m"},
            {"İstanbul", "İzmir", "1h 20m"},
            {"İstanbul", "Antalya", "1h 30m"},
            {"Ankara", "İzmir", "1h 10m"},
            {"İstanbul", "Trabzon", "1h 45m"},
            {"İstanbul", "Adana", "1h 35m"},
            {"İstanbul", "Gaziantep", "1h 40m"},
            {"Ankara", "Antalya", "1h 25m"}
        };
        
        for (String[] route : popularRoutes) {
            Map<String, Object> routeInfo = new HashMap<>();
            routeInfo.put("from", route[0]);
            routeInfo.put("to", route[1]);
            routeInfo.put("duration", route[2]);
            routeInfo.put("dailyFlights", (int)(Math.random() * 20) + 5);
            routeInfo.put("airlines", getAirlinesForRoute());
            routes.add(routeInfo);
        }
        
        return routes;
    }

    @Tool(name = "flight.getAirlines", description = "Mevcut havayolu şirketlerini listeler")
    public List<Map<String, String>> getAirlines() {
        List<Map<String, String>> airlines = new ArrayList<>();
        
        String[][] airlineData = {
            {"Turkish Airlines", "TK", "Türk Hava Yolları"},
            {"Pegasus Airlines", "PC", "Pegasus Hava Yolları"},
            {"AnadoluJet", "AJ", "AnadoluJet"},
            {"SunExpress", "XQ", "SunExpress"},
            {"Onur Air", "8Q", "Onur Air"},
            {"AtlasGlobal", "KK", "AtlasGlobal"}
        };
        
        for (String[] airline : airlineData) {
            Map<String, String> airlineInfo = new HashMap<>();
            airlineInfo.put("name", airline[0]);
            airlineInfo.put("code", airline[1]);
            airlineInfo.put("turkishName", airline[2]);
            airlines.add(airlineInfo);
        }
        
        return airlines;
    }

    // Yardımcı metodlar
    private List<Map<String, Object>> generateSampleFlights(LocalDate date) {
        List<Map<String, Object>> flights = new ArrayList<>();
        String[] cities = {"İstanbul", "Ankara", "İzmir", "Antalya", "Trabzon", "Adana"};
        String[] airlines = {"Turkish Airlines", "Pegasus", "AnadoluJet", "SunExpress"};
        
        for (int i = 0; i < 8; i++) {
            Map<String, Object> flight = new HashMap<>();
            flight.put("flightNumber", airlines[i % airlines.length].substring(0, 2) + String.format("%03d", 100 + i));
            flight.put("airline", airlines[i % airlines.length]);
            flight.put("from", cities[i % cities.length]);
            flight.put("to", cities[(i + 1) % cities.length]);
            flight.put("date", date.toString());
            flight.put("departureTime", String.format("%02d:%02d", 8 + (i * 2), (i * 15) % 60));
            flight.put("arrivalTime", String.format("%02d:%02d", 10 + (i * 2), (i * 15) % 60));
            flight.put("price", 150 + (i * 50));
            flight.put("currency", "TL");
            flight.put("duration", String.format("1h %02dm", 20 + (i * 10)));
            flights.add(flight);
        }
        
        return flights;
    }
    
    private List<Map<String, Object>> generateCityFlights(String fromCity, String toCity) {
        List<Map<String, Object>> flights = new ArrayList<>();
        String[] airlines = {"Turkish Airlines", "Pegasus", "AnadoluJet", "SunExpress"};
        
        for (int i = 0; i < 5; i++) {
            Map<String, Object> flight = new HashMap<>();
            flight.put("flightNumber", airlines[i % airlines.length].substring(0, 2) + String.format("%03d", 200 + i));
            flight.put("airline", airlines[i % airlines.length]);
            flight.put("from", fromCity);
            flight.put("to", toCity);
            flight.put("date", LocalDate.now().plusDays(i).toString());
            flight.put("departureTime", String.format("%02d:%02d", 9 + (i * 3), (i * 20) % 60));
            flight.put("arrivalTime", String.format("%02d:%02d", 11 + (i * 3), (i * 20) % 60));
            flight.put("price", 200 + (i * 75));
            flight.put("currency", "TL");
            flight.put("duration", calculateFlightDuration(fromCity, toCity));
            flight.put("available", true);
            flights.add(flight);
        }
        
        return flights;
    }
    
    private List<Map<String, Object>> generateAirlineFlights(String airlineName) {
        List<Map<String, Object>> flights = new ArrayList<>();
        String[] cities = {"İstanbul", "Ankara", "İzmir", "Antalya", "Trabzon"};
        
        for (int i = 0; i < 6; i++) {
            Map<String, Object> flight = new HashMap<>();
            flight.put("flightNumber", getAirlineCode(airlineName) + String.format("%03d", 300 + i));
            flight.put("airline", airlineName);
            flight.put("from", cities[i % cities.length]);
            flight.put("to", cities[(i + 2) % cities.length]);
            flight.put("date", LocalDate.now().plusDays(i % 7).toString());
            flight.put("departureTime", String.format("%02d:%02d", 7 + (i * 2), (i * 25) % 60));
            flight.put("arrivalTime", String.format("%02d:%02d", 9 + (i * 2), (i * 25) % 60));
            flight.put("price", 180 + (i * 60));
            flight.put("currency", "TL");
            flight.put("aircraft", getAircraftType(airlineName));
            flights.add(flight);
        }
        
        return flights;
    }
    
    private String normalizeCity(String city) {
        Map<String, String> cityMap = Map.of(
            "istanbul", "İstanbul",
            "ankara", "Ankara",
            "izmir", "İzmir",
            "antalya", "Antalya",
            "trabzon", "Trabzon",
            "adana", "Adana"
        );
        
        return cityMap.getOrDefault(city.toLowerCase(), 
            city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase());
    }
    
    private String calculateFlightDuration(String fromCity, String toCity) {
        Map<String, Integer> durations = Map.of(
            "İstanbul-Ankara", 90,
            "İstanbul-İzmir", 80,
            "İstanbul-Antalya", 90,
            "Ankara-İzmir", 70,
            "İstanbul-Trabzon", 105
        );
        
        String route = fromCity + "-" + toCity;
        int minutes = durations.getOrDefault(route, 
                     durations.getOrDefault(toCity + "-" + fromCity, 95));
        
        return String.format("%dh %02dm", minutes / 60, minutes % 60);
    }
    
    private List<String> getAirlinesForRoute() {
        List<String> airlines = Arrays.asList("Turkish Airlines", "Pegasus", "AnadoluJet");
        Collections.shuffle(airlines);
        return airlines.subList(0, 2);
    }
    
    private String getAirlineCode(String airlineName) {
        Map<String, String> codes = Map.of(
            "Turkish Airlines", "TK",
            "Pegasus", "PC", 
            "AnadoluJet", "AJ",
            "SunExpress", "XQ"
        );
        
        return codes.getOrDefault(airlineName, "XX");
    }
    
    private String getAircraftType(String airlineName) {
        String[] aircrafts = {"Boeing 737", "Airbus A320", "Boeing 777", "Airbus A330"};
        return aircrafts[Math.abs(airlineName.hashCode()) % aircrafts.length];
    }
    
    private String buildSearchUrl(String fromCity, String toCity, String date) {
        // Gerçek senaryoda bir uçuş arama sitesinin URL'si oluşturulur
        return String.format("https://example-flights.com/search?from=%s&to=%s&date=%s", 
                           fromCity.toLowerCase(), toCity.toLowerCase(), date);
    }
    
    private List<Map<String, Object>> generateSampleFlightsForRoute(String fromCity, String toCity, String date) {
        List<Map<String, Object>> flights = new ArrayList<>();
        
        for (int i = 0; i < 3; i++) {
            Map<String, Object> flight = new HashMap<>();
            flight.put("flightNumber", "TK" + String.format("%03d", 400 + i));
            flight.put("airline", "Turkish Airlines");
            flight.put("from", fromCity);
            flight.put("to", toCity);
            flight.put("date", date);
            flight.put("departureTime", String.format("%02d:%02d", 10 + (i * 4), 0));
            flight.put("price", 250 + (i * 100));
            flight.put("currency", "TL");
            flights.add(flight);
        }
        
        return flights;
    }
}
