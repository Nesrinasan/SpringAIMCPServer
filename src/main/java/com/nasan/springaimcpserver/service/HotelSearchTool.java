package com.nasan.springaimcpserver.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class HotelSearchTool {

    @Tool(name = "hotel.searchByCity", description = "Belirli şehirdeki otellerin isimlerini listeler")
    public List<Map<String, Object>> searchHotelsByCity(String cityName) {
        List<Map<String, Object>> hotels = new ArrayList<>();
        
        if (cityName == null || cityName.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Şehir adı belirtilmelidir");
            hotels.add(error);
            return hotels;
        }
        
        cityName = normalizeCity(cityName);
        hotels.addAll(generateHotelsForCity(cityName));
        
        return hotels;
    }

    @Tool(name = "hotel.searchByRating", description = "Belirli puan ve üzeri otelleri listeler")
    public List<Map<String, Object>> searchHotelsByRating(String cityName, double minRating) {
        List<Map<String, Object>> hotels = new ArrayList<>();
        
        if (cityName == null || cityName.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Şehir adı belirtilmelidir");
            hotels.add(error);
            return hotels;
        }
        
        List<Map<String, Object>> allHotels = generateHotelsForCity(normalizeCity(cityName));
        
        for (Map<String, Object> hotel : allHotels) {
            double rating = (Double) hotel.get("rating");
            if (rating >= minRating) {
                hotels.add(hotel);
            }
        }
        
        return hotels;
    }

    @Tool(name = "hotel.searchByPriceRange", description = "Belirli fiyat aralığındaki otelleri listeler")
    public List<Map<String, Object>> searchHotelsByPriceRange(String cityName, int minPrice, int maxPrice) {
        List<Map<String, Object>> hotels = new ArrayList<>();
        
        if (cityName == null || cityName.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Şehir adı belirtilmelidir");
            hotels.add(error);
            return hotels;
        }
        
        List<Map<String, Object>> allHotels = generateHotelsForCity(normalizeCity(cityName));
        
        for (Map<String, Object> hotel : allHotels) {
            int price = (Integer) hotel.get("pricePerNight");
            if (price >= minPrice && price <= maxPrice) {
                hotels.add(hotel);
            }
        }
        
        return hotels;
    }

    @Tool(name = "hotel.getHotelDetails", description = "Belirli bir otelin detaylı bilgilerini getirir")
    public Map<String, Object> getHotelDetails(String hotelName, String cityName) {
        Map<String, Object> hotelDetails = new HashMap<>();
        
        if (hotelName == null || hotelName.trim().isEmpty()) {
            hotelDetails.put("error", "Otel adı belirtilmelidir");
            return hotelDetails;
        }
        
        // Örnek detaylı otel bilgisi
        hotelDetails.put("name", hotelName);
        hotelDetails.put("city", cityName);
        hotelDetails.put("address", generateAddress(cityName));
        hotelDetails.put("rating", 4.0 + (Math.random() * 1.0));
        hotelDetails.put("pricePerNight", 200 + (int)(Math.random() * 800));
        hotelDetails.put("currency", "TL");
        hotelDetails.put("amenities", generateAmenities());
        hotelDetails.put("description", generateDescription(hotelName));
        hotelDetails.put("rooms", generateRoomTypes());
        hotelDetails.put("contact", generateContact());
        hotelDetails.put("checkinTime", "14:00");
        hotelDetails.put("checkoutTime", "12:00");
        
        return hotelDetails;
    }

    @Tool(name = "hotel.searchByDate", description = "Belirli tarih aralığında müsait otelleri listeler")
    public List<Map<String, Object>> searchHotelsByDate(String cityName, String checkInDate, String checkOutDate) {
        List<Map<String, Object>> hotels = new ArrayList<>();
        
        try {
            LocalDate checkIn = LocalDate.parse(checkInDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate checkOut = LocalDate.parse(checkOutDate, DateTimeFormatter.ISO_LOCAL_DATE);
            
            if (checkOut.isBefore(checkIn)) {
                Map<String, Object> error = new HashMap<>();
                error.put("error", "Çıkış tarihi giriş tarihinden önce olamaz");
                hotels.add(error);
                return hotels;
            }
            
            List<Map<String, Object>> allHotels = generateHotelsForCity(normalizeCity(cityName));
            
            for (Map<String, Object> hotel : allHotels) {
                // Müsaitlik kontrolü (örnek)
                boolean available = Math.random() > 0.3; // %70 müsaitlik oranı
                if (available) {
                    hotel.put("checkInDate", checkInDate);
                    hotel.put("checkOutDate", checkOutDate);
                    hotel.put("available", true);
                    hotels.add(hotel);
                }
            }
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Geçersiz tarih formatı. YYYY-MM-DD formatında girin");
            hotels.add(error);
        }
        
        return hotels;
    }


    @Tool(name = "hotel.getPopularHotels", description = "Popüler otelleri şehirlere göre listeler")
    public Map<String, List<String>> getPopularHotels() {
        Map<String, List<String>> popularHotels = new HashMap<>();
        
        popularHotels.put("İstanbul", Arrays.asList(
            "Çırağan Palace Kempinski", "Four Seasons Sultanahmet", "Swissotel The Bosphorus",
            "Conrad Istanbul", "The Ritz Carlton Istanbul", "Park Hyatt Istanbul"
        ));
        
        popularHotels.put("Ankara", Arrays.asList(
            "JW Marriott Ankara", "Sheraton Ankara", "Hilton Ankara",
            "Divan Çukurhan", "Gordon Hotel Ankara", "Best Western Khan Hotel"
        ));
        
        popularHotels.put("İzmir", Arrays.asList(
            "Swissotel Büyük Efes", "Hilton İzmir", "Movenpick Hotel İzmir",
            "Wyndham Grand İzmir", "Key Hotel", "Park Inn by Radisson İzmir"
        ));
        
        popularHotels.put("Antalya", Arrays.asList(
            "Titanic Beach Lara", "Delphin Imperial", "Concorde De Luxe Resort",
            "Club Hotel Sera", "Akra Hotel", "Su Hotel"
        ));
        
        return popularHotels;
    }

    // Yardımcı metodlar
    private List<Map<String, Object>> generateHotelsForCity(String cityName) {
        List<Map<String, Object>> hotels = new ArrayList<>();
        String[] hotelChains = {"Hilton", "Marriott", "Hyatt", "Sheraton", "Conrad", "Swissotel"};
        String[] localHotels = {"Grand", "Palace", "Boutique", "Central", "Royal", "Elite"};
        
        // Zincir oteller
        for (int i = 0; i < 4; i++) {
            Map<String, Object> hotel = new HashMap<>();
            hotel.put("name", hotelChains[i % hotelChains.length] + " " + cityName);
            hotel.put("city", cityName);
            hotel.put("type", "Chain Hotel");
            hotel.put("rating", 4.0 + (Math.random() * 1.0));
            hotel.put("pricePerNight", 300 + (i * 150));
            hotel.put("currency", "TL");
            hotel.put("starRating", 4 + (i % 2));
            hotel.put("amenities", generateAmenities());
            hotel.put("distance", String.format("%.1f km", 1.0 + (i * 2.5)));
            hotels.add(hotel);
        }
        
        // Yerel oteller
        for (int i = 0; i < 4; i++) {
            Map<String, Object> hotel = new HashMap<>();
            hotel.put("name", localHotels[i % localHotels.length] + " Hotel " + cityName);
            hotel.put("city", cityName);
            hotel.put("type", "Local Hotel");
            hotel.put("rating", 3.5 + (Math.random() * 1.0));
            hotel.put("pricePerNight", 150 + (i * 100));
            hotel.put("currency", "TL");
            hotel.put("starRating", 3 + (i % 2));
            hotel.put("amenities", generateAmenities().subList(0, 4));
            hotel.put("distance", String.format("%.1f km", 0.5 + (i * 1.8)));
            hotels.add(hotel);
        }
        
        return hotels;
    }
    
    private String normalizeCity(String city) {
        Map<String, String> cityMap = Map.of(
            "istanbul", "İstanbul",
            "ankara", "Ankara", 
            "izmir", "İzmir",
            "antalya", "Antalya",
            "bursa", "Bursa",
            "adana", "Adana",
            "gaziantep", "Gaziantep"
        );
        
        return cityMap.getOrDefault(city.toLowerCase(), 
            city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase());
    }
    
    private String generateAddress(String cityName) {
        String[] districts = {"Merkez", "Çankaya", "Beşiktaş", "Kadıköy", "Şişli", "Beyoğlu"};
        String[] streets = {"Atatürk Bulvarı", "İnönü Caddesi", "Cumhuriyet Meydanı", "Bağdat Caddesi"};
        
        return String.format("%s, %s, %s", 
            streets[(int)(Math.random() * streets.length)],
            districts[(int)(Math.random() * districts.length)],
            cityName);
    }
    
    private List<String> generateAmenities() {
        String[] allAmenities = {
            "WiFi", "Spa", "Fitness Center", "Pool", "Restaurant", "Bar",
            "Room Service", "Concierge", "Valet Parking", "Business Center",
            "Conference Rooms", "Airport Shuttle", "Pet Friendly", "AC"
        };
        
        List<String> availableAmenities = Arrays.asList(allAmenities);
        Collections.shuffle(availableAmenities);
        
        int count = 6 + (int)(Math.random() * 4); // 6-9 özellik
        return availableAmenities.subList(0, Math.min(count, availableAmenities.size()));
    }
    
    private String generateDescription(String hotelName) {
        String[] descriptions = {
            " konforlu konaklama imkanı sunan lüks bir oteldir.",
            " modern tasarımı ve kaliteli hizmetiyle öne çıkan bir tesistir.",
            " şehir merkezinde yer alan premium bir konaklama tesisidir.",
            " unutulmaz bir konaklama deneyimi sunan butik bir oteldir."
        };
        
        return hotelName + descriptions[(int)(Math.random() * descriptions.length)];
    }
    
    private List<Map<String, Object>> generateRoomTypes() {
        List<Map<String, Object>> rooms = new ArrayList<>();
        
        String[][] roomData = {
            {"Standard Room", "250", "2"},
            {"Deluxe Room", "350", "2"},
            {"Executive Suite", "500", "4"},
            {"Presidential Suite", "800", "6"}
        };
        
        for (String[] room : roomData) {
            Map<String, Object> roomType = new HashMap<>();
            roomType.put("type", room[0]);
            roomType.put("pricePerNight", Integer.parseInt(room[1]));
            roomType.put("capacity", Integer.parseInt(room[2]));
            roomType.put("currency", "TL");
            rooms.add(roomType);
        }
        
        return rooms;
    }
    
    private Map<String, String> generateContact() {
        Map<String, String> contact = new HashMap<>();
        contact.put("phone", "+90 " + (200 + (int)(Math.random() * 300)) + " " + (100 + (int)(Math.random() * 800)) + " " + (10 + (int)(Math.random() * 80)) + " " + (10 + (int)(Math.random() * 80)));
        contact.put("email", "info@hotel-example.com");
        contact.put("website", "www.hotel-example.com");
        
        return contact;
    }
    
    private String buildHotelSearchUrl(String cityName) {
        return String.format("https://example-hotels.com/search?city=%s", cityName.toLowerCase());
    }
}
