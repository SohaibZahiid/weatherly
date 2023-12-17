import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;

import java.net.HttpURLConnection;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Weatherly {

    Weatherly() {
    }

    public static JSONObject getWeatherData(String name) {
        JSONArray locationData = getLocationData(name);

        JSONObject location = (JSONObject) locationData.get(0);
        double lat = (double) location.get("latitude");
        double lon = (double) location.get("longitude");

        String url = "https://api.open-meteo.com/v1/forecast?latitude=" +
                lat + "&longitude=" +
                lon + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=auto";

        try {
            HttpURLConnection conn = fetchApiRes(url);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuilder sb = new StringBuilder();
                Scanner sc = new Scanner(conn.getInputStream());
                while (sc.hasNext()) {
                    sb.append(sc.nextLine());
                }

                sc.close();
                conn.disconnect();


                JSONParser parser = new JSONParser();
                JSONObject resultJSON = (JSONObject) parser.parse(String.valueOf(sb));

                JSONObject hourly = (JSONObject) resultJSON.get("hourly");

                JSONArray time = (JSONArray) hourly.get("time");
                int index = findIndexOfCurretnTime(time);

                JSONArray temperatureDta = (JSONArray) hourly.get("temperature_2m");
                double temperature = (double) temperatureDta.get(index);

                JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
                String weatherCondition = convertWeatherCode((long) weatherCode.get(index));

                JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
                long humidity = (long) relativeHumidity.get(index);

                JSONArray windspeedData = (JSONArray) hourly.get("wind_speed_10m");
                double windspeed = (double) windspeedData.get(index);

                JSONObject weatherData = new JSONObject();
                weatherData.put("temperature", temperature);
                weatherData.put("weather_condition", weatherCondition);
                weatherData.put("humidity", humidity);
                weatherData.put("windspeed", windspeed);

                return weatherData;

            } else {
                System.out.println("API not found");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONArray getLocationData(String location) {

        try {
            HttpURLConnection conn = fetchApiRes("https://geocoding-api.open-meteo.com/v1/search?name="
                    + location
                    + "&count=10&language=en&format=json");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                StringBuilder sb = new StringBuilder();
                Scanner sc = new Scanner(conn.getInputStream());
                while (sc.hasNext()) {
                    sb.append(sc.nextLine());
                }

                sc.close();
                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultsJson = (JSONObject) parser.parse(String.valueOf(sb));

                JSONArray locationData = (JSONArray) resultsJson.get("results");

                return locationData;

            } else {
                System.out.println("API not found");
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static HttpURLConnection fetchApiRes(String urlString) {
        try {
            //create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //set method
            conn.setRequestMethod("GET");
            //connect
            conn.connect();
            return conn;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int findIndexOfCurretnTime(JSONArray timeList) {
        String currentTime = getCurrentTime();
        for (int i = 0; i < timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                return i;
            }
        }
        return 0;
    }

    public static String getCurrentTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }

    private static String convertWeatherCode(long weatherCode) {
        String weatherCondition = "";
        if (weatherCode == 0L) {
            weatherCondition = "Clear";
        } else if (weatherCode > 0L && weatherCode <= 3L) {
            weatherCondition = "Cloudy";
        } else if (weatherCode >= 51L && weatherCode <= 67L) {
            weatherCondition = "Rain";
        } else if (weatherCode >= 71L && weatherCode <= 77L) {
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }
}
