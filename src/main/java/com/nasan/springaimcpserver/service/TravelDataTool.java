package com.nasan.springaimcpserver.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class TravelDataTool {

    @Tool(name = "travel.getCityInfo", description = "Şehir hakkında seyahat bilgilerini getirir")
    public Map<String, Object> getCityInfo(String cityName) {
        Map<String, Object> cityInfo = new HashMap<>();
        
        if (cityName == null || cityName.trim().isEmpty()) {
            cityInfo.put("error", "Şehir adı belirtilmelidir");
            return cityInfo;
        }
        
        cityName = normalizeCity(cityName);
        
        cityInfo.put("name", cityName);
        cityInfo.put("country", "Türkiye");
        cityInfo.put("population", getCityPopulation(cityName));
        cityInfo.put("airports", getCityAirports(cityName));
        cityInfo.put("attractions", getCityAttractions(cityName));
        cityInfo.put("bestTimeToVisit", getBestTimeToVisit(cityName));
        cityInfo.put("averageTemperature", getAverageTemperature(cityName));
        cityInfo.put("currency", "Turkish Lira (TL)");
        cityInfo.put("timeZone", "UTC+3");
        cityInfo.put("language", "Turkish");
        
        return cityInfo;
    }

    @Tool(name = "travel.getWeatherForecast", description = "Şehir için hava durumu tahmini getirir")
    public Map<String, Object> getWeatherForecast(String cityName, String date) {
        Map<String, Object> weather = new HashMap<>();
        
        try {
            LocalDate forecastDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            long daysFromNow = ChronoUnit.DAYS.between(LocalDate.now(), forecastDate);
            
            if (daysFromNow > 7) {
                weather.put("warning", "7 günden fazla tahmin yapılamaz, genel bilgi verilmektedir");
            }
            
            cityName = normalizeCity(cityName);
            
            weather.put("city", cityName);
            weather.put("date", date);
            weather.put("temperature", generateTemperature(cityName, forecastDate));
            weather.put("humidity", 40 + (int)(Math.random() * 40));
            weather.put("condition", generateWeatherCondition());
            weather.put("windSpeed", 5 + (int)(Math.random() * 20));
            weather.put("precipitation", Math.random() * 10);
            weather.put("recommendation", generateTravelRecommendation(cityName));
            
        } catch (Exception e) {
            weather.put("error", "Geçersiz tarih formatı. YYYY-MM-DD formatında girin");
        }
        
        return weather;
    }

    @Tool(name = "travel.getTravelRoute", description = "İki şehir arası seyahat rotası ve seçenekleri getirir")
    public Map<String, Object> getTravelRoute(String fromCity, String toCity) {
        Map<String, Object> route = new HashMap<>();
        
        if (fromCity == null || toCity == null || fromCity.trim().isEmpty() || toCity.trim().isEmpty()) {
            route.put("error", "Kalkış ve varış şehirleri belirtilmelidir");
            return route;
        }
        
        fromCity = normalizeCity(fromCity);
        toCity = normalizeCity(toCity);
        
        route.put("from", fromCity);
        route.put("to", toCity);
        route.put("distance", calculateDistance(fromCity, toCity));
        route.put("travelOptions", generateTravelOptions(fromCity, toCity));
        route.put("estimatedCosts", generateTravelCosts(fromCity, toCity));
        route.put("recommendations", generateRouteRecommendations(fromCity, toCity));
        
        return route;
    }

    @Tool(name = "travel.getSeasonalInfo", description = "Şehir için mevsimsel seyahat bilgileri getirir")
    public Map<String, Object> getSeasonalInfo(String cityName, String season) {
        Map<String, Object> seasonalInfo = new HashMap<>();
        
        cityName = normalizeCity(cityName);
        season = season.toLowerCase();
        
        if (!Arrays.asList("spring", "summer", "autumn", "winter", "ilkbahar", "yaz", "sonbahar", "kış").contains(season)) {
            seasonalInfo.put("error", "Geçersiz mevsim. spring/yaz, summer/yaz, autumn/sonbahar, winter/kış kullanın");
            return seasonalInfo;
        }
        
        seasonalInfo.put("city", cityName);
        seasonalInfo.put("season", normalizeSeasonName(season));
        seasonalInfo.put("weatherInfo", getSeasonalWeather(cityName, season));
        seasonalInfo.put("crowdLevel", getSeasonalCrowdLevel(cityName, season));
        seasonalInfo.put("priceLevel", getSeasonalPriceLevel(cityName, season));
        seasonalInfo.put("activities", getSeasonalActivities(cityName, season));
        seasonalInfo.put("packingTips", getPackingTips(season));
        
        return seasonalInfo;
    }

    @Tool(name = "travel.getTransportation", description = "Şehir içi ulaşım seçenekleri getirir")
    public Map<String, Object> getTransportation(String cityName) {
        Map<String, Object> transportation = new HashMap<>();
        
        cityName = normalizeCity(cityName);
        
        transportation.put("city", cityName);
        transportation.put("publicTransport", getPublicTransport(cityName));
        transportation.put("taxi", getTaxiInfo(cityName));
        transportation.put("carRental", getCarRentalInfo(cityName));
        transportation.put("rideshare", getRideshareInfo(cityName));
        transportation.put("bikeRental", getBikeRentalInfo(cityName));
        
        return transportation;
    }

    @Tool(name = "travel.getBudgetEstimate", description = "Şehir için günlük bütçe tahmini getirir")
    public Map<String, Object> getBudgetEstimate(String cityName, String budgetType) {
        Map<String, Object> budget = new HashMap<>();
        
        cityName = normalizeCity(cityName);
        
        if (!Arrays.asList("budget", "mid-range", "luxury").contains(budgetType.toLowerCase())) {
            budget.put("error", "Bütçe tipi: budget, mid-range, luxury olmalıdır");
            return budget;
        }
        
        budget.put("city", cityName);
        budget.put("type", budgetType);
        budget.put("dailyBudget", generateBudgetBreakdown(cityName, budgetType));
        budget.put("currency", "TL");
        budget.put("tips", getBudgetTips(budgetType));
        
        return budget;
    }

    // Yardımcı metodlar
    private String normalizeCity(String city) {
        Map<String, String> cityMap = Map.of(
            "istanbul", "İstanbul",
            "ankara", "Ankara",
            "izmir", "İzmir", 
            "antalya", "Antalya",
            "bursa", "Bursa",
            "adana", "Adana",
            "trabzon", "Trabzon",
            "gaziantep", "Gaziantep"
        );
        
        return cityMap.getOrDefault(city.toLowerCase(), 
            city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase());
    }
    
    private String getCityPopulation(String cityName) {
        Map<String, String> populations = Map.of(
            "İstanbul", "15.5 milyon",
            "Ankara", "5.7 milyon", 
            "İzmir", "4.4 milyon",
            "Antalya", "2.6 milyon",
            "Bursa", "3.1 milyon",
            "Adana", "2.2 milyon"
        );
        
        return populations.getOrDefault(cityName, "Bilinmiyor");
    }
    
    private List<String> getCityAirports(String cityName) {
        Map<String, List<String>> airports = Map.of(
            "İstanbul", Arrays.asList("İstanbul Havalimanı (IST)", "Sabiha Gökçen (SAW)"),
            "Ankara", Arrays.asList("Esenboğa Havalimanı (ESB)"),
            "İzmir", Arrays.asList("Adnan Menderes Havalimanı (ADB)"),
            "Antalya", Arrays.asList("Antalya Havalimanı (AYT)"),
            "Trabzon", Arrays.asList("Trabzon Havalimanı (TZX)")
        );
        
        return airports.getOrDefault(cityName, Arrays.asList("Yerel havalimanı"));
    }
    
    private List<String> getCityAttractions(String cityName) {
        Map<String, List<String>> attractions = Map.of(
            "İstanbul", Arrays.asList("Ayasofya", "Sultanahmet Camii", "Kapalıçarşı", "Galata Kulesi", "Boğaz Turu"),
            "Ankara", Arrays.asList("Anıtkabir", "Ankara Kalesi", "Etnografya Müzesi", "Atatürk Orman Çiftliği"),
            "İzmir", Arrays.asList("Konak Meydanı", "Kemeraltı Çarşısı", "Alsancak", "İzmir Saat Kulesi"),
            "Antalya", Arrays.asList("Kaleiçi", "Düden Şelalesi", "Aspendos", "Perge", "Konyaaltı Plajı")
        );
        
        return attractions.getOrDefault(cityName, Arrays.asList("Yerel turistik yerler"));
    }
    
    private String getBestTimeToVisit(String cityName) {
        Map<String, String> bestTimes = Map.of(
            "İstanbul", "Nisan-Haziran, Eylül-Kasım",
            "Ankara", "Mayıs-Ekim",
            "İzmir", "Nisan-Kasım", 
            "Antalya", "Nisan-Kasım",
            "Trabzon", "Mayıs-Ekim"
        );
        
        return bestTimes.getOrDefault(cityName, "İlkbahar-Sonbahar");
    }
    
    private String getAverageTemperature(String cityName) {
        Map<String, String> temps = Map.of(
            "İstanbul", "Yıllık ortalama: 14°C",
            "Ankara", "Yıllık ortalama: 12°C",
            "İzmir", "Yıllık ortalama: 17°C",
            "Antalya", "Yıllık ortalama: 18°C"
        );
        
        return temps.getOrDefault(cityName, "Ortalama: 15°C");
    }
    
    private Map<String, Object> generateTemperature(String cityName, LocalDate date) {
        Map<String, Object> temp = new HashMap<>();
        
        // Mevsime göre sıcaklık tahmini
        int month = date.getMonthValue();
        int baseTemp = getBaseTempForMonth(month);
        
        temp.put("min", baseTemp - 5 + (int)(Math.random() * 5));
        temp.put("max", baseTemp + 5 + (int)(Math.random() * 5));
        temp.put("unit", "Celsius");
        
        return temp;
    }
    
    private int getBaseTempForMonth(int month) {
        int[] monthlyTemps = {5, 7, 12, 17, 22, 27, 30, 30, 25, 19, 13, 8};
        return monthlyTemps[month - 1];
    }
    
    private String generateWeatherCondition() {
        String[] conditions = {"Güneşli", "Parçalı Bulutlu", "Bulutlu", "Yağmurlu", "Karlı"};
        return conditions[(int)(Math.random() * conditions.length)];
    }
    
    private String generateTravelRecommendation(String cityName) {
        String[] recommendations = {
            "Seyahat için ideal bir gün",
            "Hafif yağmur olabilir, şemsiye alın",
            "Sıcak hava, bol su için",
            "Soğuk hava, kalın giyinin"
        };
        
        return recommendations[(int)(Math.random() * recommendations.length)];
    }
    
    private String calculateDistance(String fromCity, String toCity) {
        // Basit mesafe hesaplama
        Map<String, Map<String, String>> distances = Map.of(
            "İstanbul", Map.of("Ankara", "350 km", "İzmir", "340 km", "Antalya", "500 km"),
            "Ankara", Map.of("İstanbul", "350 km", "İzmir", "400 km", "Antalya", "350 km")
        );
        
        if (distances.containsKey(fromCity) && distances.get(fromCity).containsKey(toCity)) {
            return distances.get(fromCity).get(toCity);
        }
        
        return "400-600 km (tahmini)";
    }
    
    private List<Map<String, Object>> generateTravelOptions(String fromCity, String toCity) {
        List<Map<String, Object>> options = new ArrayList<>();
        
        // Uçak
        Map<String, Object> flight = new HashMap<>();
        flight.put("type", "Uçak");
        flight.put("duration", "1-2 saat");
        flight.put("price", "200-500 TL");
        flight.put("comfort", "Yüksek");
        options.add(flight);
        
        // Otobüs
        Map<String, Object> bus = new HashMap<>();
        bus.put("type", "Otobüs");
        bus.put("duration", "4-8 saat");
        bus.put("price", "50-150 TL");
        bus.put("comfort", "Orta");
        options.add(bus);
        
        // Araba
        Map<String, Object> car = new HashMap<>();
        car.put("type", "Araba");
        car.put("duration", "4-6 saat");
        car.put("price", "100-200 TL (yakıt)");
        car.put("comfort", "Yüksek");
        options.add(car);
        
        return options;
    }
    
    private Map<String, String> generateTravelCosts(String fromCity, String toCity) {
        Map<String, String> costs = new HashMap<>();
        costs.put("flight", "200-500 TL");
        costs.put("bus", "50-150 TL");
        costs.put("car", "100-200 TL");
        costs.put("accommodation", "150-800 TL/gece");
        costs.put("food", "50-200 TL/gün");
        
        return costs;
    }
    
    private List<String> generateRouteRecommendations(String fromCity, String toCity) {
        return Arrays.asList(
            "Erken rezervasyon yapın",
            "Mevsimsel fiyat değişimlerini takip edin", 
            "Birden fazla ulaşım seçeneğini karşılaştırın",
            "Konaklama için şehir merkezini tercih edin"
        );
    }
    
    private String normalizeSeasonName(String season) {
        Map<String, String> seasonMap = Map.of(
            "spring", "İlkbahar", "ilkbahar", "İlkbahar",
            "summer", "Yaz", "yaz", "Yaz",
            "autumn", "Sonbahar", "sonbahar", "Sonbahar",
            "winter", "Kış", "kış", "Kış"
        );
        
        return seasonMap.getOrDefault(season, season);
    }
    
    private String getSeasonalWeather(String cityName, String season) {
        // Mevsim ve şehre göre hava durumu
        return "Bu mevsimde ortalama sıcaklık " + (15 + (int)(Math.random() * 15)) + "°C";
    }
    
    private String getSeasonalCrowdLevel(String cityName, String season) {
        String[] levels = {"Az", "Orta", "Yoğun", "Çok Yoğun"};
        return levels[(int)(Math.random() * levels.length)];
    }
    
    private String getSeasonalPriceLevel(String cityName, String season) {
        String[] levels = {"Düşük", "Orta", "Yüksek", "Çok Yüksek"};
        return levels[(int)(Math.random() * levels.length)];
    }
    
    private List<String> getSeasonalActivities(String cityName, String season) {
        return Arrays.asList("Müze gezisi", "Yürüyüş", "Fotoğraf çekimi", "Yerel lezzetler");
    }
    
    private List<String> getPackingTips(String season) {
        Map<String, List<String>> tips = Map.of(
            "spring", Arrays.asList("Katmanlı giyim", "Hafif ceket", "Yağmurluk"),
            "summer", Arrays.asList("Güneş kremi", "Şapka", "Hafif giysiler"),
            "autumn", Arrays.asList("Ceket", "Bot", "Şemsiye"),
            "winter", Arrays.asList("Kalın mont", "Eldiven", "Kar botu")
        );
        
        return tips.getOrDefault(season.toLowerCase(), Arrays.asList("Mevsime uygun giyim"));
    }
    
    private Map<String, Object> getPublicTransport(String cityName) {
        Map<String, Object> transport = new HashMap<>();
        transport.put("metro", cityName.equals("İstanbul") || cityName.equals("Ankara"));
        transport.put("bus", true);
        transport.put("tram", cityName.equals("İstanbul"));
        transport.put("price", "5-15 TL");
        
        return transport;
    }
    
    private Map<String, String> getTaxiInfo(String cityName) {
        Map<String, String> taxi = new HashMap<>();
        taxi.put("startingFee", "8 TL");
        taxi.put("perKm", "3-5 TL");
        taxi.put("availability", "24/7");
        
        return taxi;
    }
    
    private Map<String, String> getCarRentalInfo(String cityName) {
        Map<String, String> rental = new HashMap<>();
        rental.put("dailyPrice", "100-300 TL/gün");
        rental.put("companies", "Avis, Hertz, Budget");
        rental.put("requirement", "Ehliyet + Kredi Kartı");
        
        return rental;
    }
    
    private Map<String, String> getRideshareInfo(String cityName) {
        Map<String, String> rideshare = new HashMap<>();
        rideshare.put("uber", cityName.equals("İstanbul") ? "Mevcut" : "Mevcut değil");
        rideshare.put("bitaksi", "Mevcut");
        rideshare.put("priceRange", "Taksi fiyatları ile benzer");
        
        return rideshare;
    }
    
    private Map<String, String> getBikeRentalInfo(String cityName) {
        Map<String, String> bike = new HashMap<>();
        bike.put("availability", cityName.equals("İstanbul") || cityName.equals("Ankara") ? "Mevcut" : "Sınırlı");
        bike.put("hourlyPrice", "5-15 TL/saat");
        bike.put("dailyPrice", "30-50 TL/gün");
        
        return bike;
    }
    
    private Map<String, Object> generateBudgetBreakdown(String cityName, String budgetType) {
        Map<String, Object> breakdown = new HashMap<>();
        
        int multiplier = budgetType.equals("budget") ? 1 : budgetType.equals("mid-range") ? 2 : 4;
        
        breakdown.put("accommodation", 100 * multiplier + " TL");
        breakdown.put("food", 50 * multiplier + " TL");
        breakdown.put("transportation", 20 * multiplier + " TL");
        breakdown.put("activities", 30 * multiplier + " TL");
        breakdown.put("total", (200 * multiplier) + " TL");
        
        return breakdown;
    }
    
    private List<String> getBudgetTips(String budgetType) {
        Map<String, List<String>> tips = Map.of(
            "budget", Arrays.asList("Hostel konaklama", "Street food", "Toplu taşıma"),
            "mid-range", Arrays.asList("3-4 yıldız otel", "Yerel restoranlar", "Karışık ulaşım"),
            "luxury", Arrays.asList("5 yıldız otel", "Fine dining", "Özel transfer")
        );
        
        return tips.getOrDefault(budgetType, Arrays.asList("Dengelenmiş harcama"));
    }
}
