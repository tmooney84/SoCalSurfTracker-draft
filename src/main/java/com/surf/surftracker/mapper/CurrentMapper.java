
/*
CURRENT CONDITIONS:                      MAPPER CLASS INFORMATION!!!
 */
package com.surf.surftracker.mapper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import com.surf.surftracker.dto.SurfLine_rating_DTO;
import com.surf.surftracker.dto.SurfLine_sunlight_DTO;
import com.surf.surftracker.model.Current;
import com.surf.surftracker.util.TimeStampUtils;

//@Scheduled

public class CurrentMapper {
    private Current currentSpot;
    private SurfLine_rating_DTO currentRating;
    private SurfLine_sunlight_DTO currentSunlightDTO;

    Long nearestHour = TimeStampUtils.NearestHour();
    Long midnight = TimeStampUtils.Midnight();

    public CurrentMapper(Current currentSpot, SurfLine_rating_DTO currentRatingDTO, SurfLine_sunlight_DTO currentSunlightDTO) {
        this.currentSpot = currentSpot;
        this.currentRating = currentRatingDTO;
        this.currentSunlightDTO = currentSunlightDTO;
    }

    //@Scheduled
//current wave height

    //Current average wave height    String average of the 4 forecasts
    public void averageWaveHeight() {
    }

    //Current WaveQuality
    public void WaveQuality() {
        List<SurfLine_rating_DTO.Rating> ratings = currentRating.getData().getRating();
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

    //Sunrise					    String 6:53AM >>> find previous midnight and then search for sunrise
    public void sunrise() {
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
    public void sunset() {
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
    //SurfCaptain current		    String
    public void surfCaptainWaveHeight(){}

    //DeepSwell current			    String
    public void deepSwellWaveHeight(){}

    //Surf-Forecast current		    String
    public void surfForecastWaveHeight(){}

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