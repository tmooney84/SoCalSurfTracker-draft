
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

import com.surf.surftracker.dto.SurfLine_rating_DTO;
import com.surf.surftracker.dto.SurfLine_sunlight_DTO;
import com.surf.surftracker.dto.SurfLine_surf_DTO;
import com.surf.surftracker.dto.SurfLine_wind_DTO;
import com.surf.surftracker.model.Current;
import com.surf.surftracker.util.TimeStampUtils;

//@Scheduled

public class CurrentMapper {
    private final Current currentSpot;
    private final SurfLine_rating_DTO currentRatingDTO;
    private final SurfLine_sunlight_DTO currentSunlightDTO;
    private final SurfLine_surf_DTO currentSurfDTO;
    private final SurfLine_wind_DTO currentWindDTO;

    Long nearestHour = TimeStampUtils.NearestHour();
    Long midnight = TimeStampUtils.Midnight();

    public CurrentMapper(Current currentSpot, SurfLine_rating_DTO currentRatingDTO, SurfLine_sunlight_DTO currentSunlightDTO, SurfLine_surf_DTO currentSurfDTO, SurfLine_wind_DTO currentWindDTO) {
        this.currentSpot = currentSpot;
        this.currentRatingDTO = currentRatingDTO;
        this.currentSunlightDTO = currentSunlightDTO;
        this.currentSurfDTO = currentSurfDTO;
        this.currentWindDTO = currentWindDTO;
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

    //Sunrise					    String 6:53AM >>> find previous midnight and then search for sunrise
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

    //Sunset					        String 7:54PM >>> find previous midnight and then search for sunset
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


    public void SL_Wind() {
        List<SurfLine_wind_DTO.WindData.WindEntry> currentWind = currentWindDTO.getData().getWind();
        long nearestHour = TimeStampUtils.NearestHour();
        System.out.println("Nearest Hour: " + nearestHour);
        for (SurfLine_wind_DTO.WindData.WindEntry wind : currentWind) {
            System.out.println("Wind Entry Timestamp: " + wind.getTimestamp());
            if (wind.getTimestamp() == TimeStampUtils.NearestHour()) {

                double currentWindSpeed = wind.getSpeed();
                int speed = (int)currentWindSpeed;
                double direction =  wind.getDirection();
                String formattedDirection = getCompassDirection((int) direction);
                String windSpeed = String.format("%dkts %s", speed, formattedDirection);
                System.out.println("Here is the current windspeed: " + windSpeed);
                currentSpot.setWind(windSpeed);
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
    /*
    //Wind current				    String 5mph E
    public void SL_Wind() {
        List<SurfLine_wind_DTO.WindData.WindEntry> currentWind = currentWindDTO.getData().getWind();
        for (SurfLine_wind_DTO.WindData.WindEntry wind : currentWind) {
            if (wind.getTimestamp() == TimeStampUtils.NearestHour()) {
                int speed = (int) wind.getSpeed();
                int direction = (int)wind.getDirection();
                String formattedDirection;

                String.format("%fKTS %s",speed,formattedDirection);
            }


            currentSpot.setWind(windSpeed);
            return;
        }
        throw new NoSuchElementException("No wind found for the timestamp: " + TimeStampUtils.NearestHour());
    }
*/
//6 KTS E


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

    //Water temperature current	    String 67ºF
    public void waterTemperature(){}

    //Air temperature current  	    String 83ºF
    public void airTemperature(){}



    //Weather					    String Search by time @hour and then get rid of "_" and all caps maybe regex?
    public void weather(){}

    //Current Swells				    String Top 1.6ft @ 14s SSW 193º three swells locate by hour and pull top 3; need logic for SSW, SSE etc. 251 degrees is WSW...so find 					 something for the logic
    public void swellOne(){}

    public void swellTwo(){}

    public void swellThree(){}
} */