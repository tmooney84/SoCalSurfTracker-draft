package com.surf.surftracker;

import com.surf.surftracker.model.Current;
import com.surf.surftracker.service.SurfForecast_Service;

public class SurfForecastTester {

    public static void main(String[] args) {
        // Create a Current object
        Current lowersCurrent = new Current();

        // Create an instance of SurfForecast_Service
        SurfForecast_Service surfService = new SurfForecast_Service(lowersCurrent);

        // Run the service to get surf forecast data
        surfService.getSurfForecastCurrent();

        // Output the results
        System.out.println("Surf Forecast Wave Height: " + lowersCurrent.getSurfForecastWaveHeight());
    }
}
