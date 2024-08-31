
/*
CURRENT CONDITIONS:                      MAPPER CLASS INFORMATION!!!
 */
package com.surf.surftracker.mapper;

import java.sql.Time;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import com.surf.surftracker.dto.*;
import com.surf.surftracker.model.Current;
import com.surf.surftracker.util.TimeStampUtils;

//@Scheduled

public class CurrentMapper {
    private final Current currentSpot;
    private final SurfLine_rating_DTO currentRatingDTO;
    private final SurfLine_sunlight_DTO currentSunlightDTO;
    private final SurfLine_surf_DTO currentSurfDTO;
    private final SurfLine_wind_DTO currentWindDTO;
    private final SurfLine_weather_DTO currentWeatherDTO;

    Long nearestHour = TimeStampUtils.NearestHour();
    Long midnight = TimeStampUtils.Midnight();

    public CurrentMapper(Current currentSpot, SurfLine_rating_DTO currentRatingDTO, SurfLine_sunlight_DTO currentSunlightDTO, SurfLine_surf_DTO currentSurfDTO, SurfLine_wind_DTO currentWindDTO, SurfLine_weather_DTO currentWeatherDTO) {
        this.currentSpot = currentSpot;
        this.currentRatingDTO = currentRatingDTO;
        this.currentSunlightDTO = currentSunlightDTO;
        this.currentSurfDTO = currentSurfDTO;
        this.currentWindDTO = currentWindDTO;
        this.currentWeatherDTO = currentWeatherDTO;
    }

    //@Scheduled
//current wave height

    //Current average wave height    String average of the 4 forecasts
    public void averageWaveHeight() {
    }

    //Current WaveQuality
    public void SL_WaveQuality() {
        List<SurfLine_rating_DTO.Rating> ratings = currentRatingDTO.getData().getRating();
        for (SurfLine_rating_DTO.Rating rating : ratings) {
            if (Long.valueOf(rating.getTimestamp()).equals(nearestHour)) {
                String currentRating = rating.getRatingDetails().getKey();
                currentRating = currentRating.replaceAll("_", " ");
                currentSpot.setWaveQuality(currentRating);
                return;
            }
        }
        throw new NoSuchElementException("No rating found for the timestamp: " + nearestHour);
    }

    //SurfLine Wave Height
    public void SL_Surf() {
        List<SurfLine_surf_DTO.Surf> surfList = currentSurfDTO.getData().getSurf();

        for (SurfLine_surf_DTO.Surf surf : surfList) {
            if (surf.getTimestamp() == TimeStampUtils.NearestHour()) {
                SurfLine_surf_DTO.SurfDetails surfDetails = surf.getSurfDetails();

                int min = (int)surfDetails.getMin();
                int max = (int)surfDetails.getMax();
                boolean plus = surfDetails.isPlus();
                String plusSign = "";
                if(plus){
                    plusSign = "+";
                }

                String waveHeight = String.format("%d-%d%s ft",min,max,plusSign);
                currentSpot.setSurfLineWaveHeight(waveHeight);
                //System.out.println("SL Wave Height is: " + waveHeight);
                return; // Exit after finding and processing the relevant surf details
            }
        }

        throw new NoSuchElementException("No surf details found for the timestamp: " + TimeStampUtils.NearestHour());
    }

    //Sunrise
    public void SL_Sunrise() {
        List<SurfLine_sunlight_DTO.Sunlight> sunlightList = currentSunlightDTO.getData().getSunlight();
        for (SurfLine_sunlight_DTO.Sunlight sun : sunlightList) {
            if (sun.getMidnight() == midnight) {
                long sunriseTimestamp = sun.getSunrise();
                ZonedDateTime sunriseTime = Instant.ofEpochSecond(sunriseTimestamp)
                        .atZone(ZoneId.of("America/Los_Angeles"));
                String formattedSunriseTime = sunriseTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                currentSpot.setSunrise(formattedSunriseTime);
                return;
            }
        }
        throw new NoSuchElementException("No sunrise found for the midnight timestamp: " + midnight);
    }

    //Sunset
    public void SL_Sunset() {
        List<SurfLine_sunlight_DTO.Sunlight> sunlightList = currentSunlightDTO.getData().getSunlight();
        for (SurfLine_sunlight_DTO.Sunlight sun : sunlightList) {
            if (sun.getMidnight() == midnight) {
                long sunriseTimestamp = sun.getSunset();

            //    System.out.println("Midnight: " + midnight);
                //    System.out.println("sunrise: " + sunriseTimestamp);

                ZonedDateTime sunriseTime = Instant.ofEpochSecond(sunriseTimestamp)
                        .atZone(ZoneId.of("America/Los_Angeles"));
                String formattedSunriseTime = sunriseTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                currentSpot.setSunset(formattedSunriseTime);
                return;
            }
        }
        throw new NoSuchElementException("No sunrise found for the midnight timestamp: " + midnight);
    }

    //Wind and Wind Direction
    public void SL_Wind() {
        List<SurfLine_wind_DTO.WindData.WindEntry> windEntries = currentWindDTO.getData().getWind();

        for (SurfLine_wind_DTO.WindData.WindEntry windEntry : windEntries) {
            if (windEntry.getTimestamp().equals(TimeStampUtils.NearestHour())) {
                int speed = windEntry.getSpeed().intValue();
                int direction = windEntry.getDirection().intValue();
                String formattedDirection = getCompassDirection(direction);
                String windSpeedDirection = String.format("%dkts %s", speed, formattedDirection);
                currentSpot.setWind(windSpeedDirection);
                return;
            }
        }
        throw new NoSuchElementException("No wind found for the timestamp: " + TimeStampUtils.NearestHour());
    }
    private String getCompassDirection(int direction) {
        String[] compassDirections = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
        int index = (int) Math.round(((double) direction % 360) / 22.5);
        return compassDirections[index % 16];
    }

    //Water temperature current
    public void SL_WaterTemp(){
        List<SurfLine_weather_DTO.DataData.WeatherEntry> weatherEntries = currentWeatherDTO.getData().getWeather();
        long nearestHour = TimeStampUtils.NearestHour();

        for (SurfLine_weather_DTO.DataData.WeatherEntry weatherEntry : weatherEntries) {
            if (weatherEntry.getTimestamp().equals(nearestHour)) {
                String temperature = String.format("%d°F", Math.round(weatherEntry.getTemperature()));
                currentSpot.setWaterTemperature(temperature);
                return;
            }
        }
        throw new NoSuchElementException("No temperature found for the timestamp: " + nearestHour);
    }

    //Weather					    String Search by time @hour and then get rid of "_" and all caps maybe regex?


    public void SL_WeatherConditons(){
        List<SurfLine_weather_DTO.DataData.WeatherEntry> condtionEntries = currentWeatherDTO.getData().getWeather();
        long nearestHour = TimeStampUtils.NearestHour();

        for (SurfLine_weather_DTO.DataData.WeatherEntry conditionEntry : condtionEntries) {
            if (conditionEntry.getTimestamp().equals(nearestHour)) {
                String weatherCondition = conditionEntry.getCondition().replace("_"," ");
                currentSpot.setWeatherConditions(weatherCondition);
                return;
            }
        }
        throw new NoSuchElementException("No temperature found for the timestamp: " + nearestHour);
    }



}


/*
    //@Scheduled... every hour
    //SurfLine current --> if plus add plus...String
    public void SurfLineWaveHeight() {
        //  String mappedSLWave = "3-4+"; "3" "4" "plus"
        //  currentSurfBreak.setSurfLineWaveHeight(mappedSLWave);

currentSpot.setSurfLineWaveHeight();
    }

}
*/   /*



    //Wind current				    String 5mph E
    public void wind(){}

    //Tide current				    String Low Tide @ 16:00 >>> will have to have some logic for finding the low or high tide and time
    public void tide(){}



     //Air temperature current  	    String 83ºF
    public void airTemperature(){}

    //Current Swells				    String Top 1.6ft @ 14s SSW 193º three swells locate by hour and pull top 3; need logic for SSW, SSE etc. 251 degrees is WSW...so find 					 something for the logic
    public void swellOne(){}

    public void swellTwo(){}

    public void swellThree(){}
} */