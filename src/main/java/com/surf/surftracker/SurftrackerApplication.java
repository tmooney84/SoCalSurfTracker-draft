package com.surf.surftracker;

import com.surf.surftracker.dto.*;
import com.surf.surftracker.model.Current;
import com.surf.surftracker.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SurftrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurftrackerApplication.class, args);

		Current lowerTrestlesCurrent = new Current();

		//right now hard coded with the trestles related url
		//pass in the url as a string to be used within each of these services
		SurfLine_rating_Service sl_rating_service = new SurfLine_rating_Service();
		SurfLine_sunlight_Service sl_sunlight_service = new SurfLine_sunlight_Service();
		SurfLine_surf_Service sl_surf_service = new SurfLine_surf_Service();
		SurfLine_swells_Service lt_swells_service = new SurfLine_swells_Service();
		SurfLine_tides_Service lt_tides_service = new SurfLine_tides_Service();
		SurfLine_weather_Service lt_weather_service = new SurfLine_weather_Service();
		SurfLine_wind_Service lt_wind_service = new SurfLine_wind_Service();
		DeepSwell_Service lt_ds_service = new DeepSwell_Service();

		try {
			SurfLine_rating_DTO ratingDTO = sl_rating_service.getSurfLineRating();
			SurfLine_sunlight_DTO sunlightDTO = sl_sunlight_service.getSurfLineSunlight();
			SurfLine_surf_DTO surfDTO = sl_surf_service.getSurfLineSurf();
			SurfLine_swells_DTO swellsDTO = lt_swells_service.getSurfLineSwells();
			SurfLine_tides_DTO tidesDTO = lt_tides_service.getSurfLineTides();
			SurfLine_wind_DTO windDTO = lt_wind_service.getSurfLineWind();
			SurfLine_weather_DTO weatherDTO = lt_weather_service.getSurfLineWeather();


			// Print out the filled DTO's
			System.out.println(ratingDTO);
			System.out.println(sunlightDTO);
			System.out.println(surfDTO);
			System.out.println(swellsDTO);
			System.out.println(tidesDTO);
			System.out.println(weatherDTO);
			System.out.println(windDTO);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

