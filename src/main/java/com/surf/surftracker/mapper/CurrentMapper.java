
/*
CURRENT CONDITIONS:                      MAPPER CLASS INFORMATION!!!
 */
package com.surf.surftracker.mapper;

import com.surf.surftracker.model.Current;

//@Scheduled
public class CurrentMapper {
    private Current currentSurfBreak;

    public CurrentMapper(Current currentSurfBreak){
        this.currentSurfBreak = currentSurfBreak;
    }

    //@Scheduled
//current wave height






//Current average wave height    String average of the 4 forecasts
    public void averageWaveHeight(){}

    //Current wave quality	(MAKE IT COLOR CODED!!!)	 String from surfline may need to adjust using regex? for "_"
public void waveQuality(){
    //get current timestamp to convert to nearest hour for search
  //  long currentTime = Instant.now().getEpochSecond();


    //do a for each loop to cycle through to find the correct timestamp
    //and then access the key value>>> clean up "_" with regex
}

//@Scheduled... every hour
    //SurfLine current --> if plus add plus...String
    public void mapSurfLineWaveHeight(){
     //  String mappedSLWave = "3-4+"; "3" "4" "plus"
      //  currentSurfBreak.setSurfLineWaveHeight(mappedSLWave);
    }

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

    //Sunrise					    String 6:53AM >>> find previous midnight and then search for sunrise
    public void sunrise(){}

    //Sunset					        String 7:54PM >>> find previous midnight and then search for sunset
    public void sunset(){}

    //Weather					    String Search by time @hour and then get rid of "_" and all caps maybe regex?
    public void weather(){}

    //Current Swells				    String Top 1.6ft @ 14s SSW 193º three swells locate by hour and pull top 3; need logic for SSW, SSE etc. 251 degrees is WSW...so find 					 something for the logic
    public void swellOne(){}

    public void swellTwo(){}

    public void swellThree(){}
}