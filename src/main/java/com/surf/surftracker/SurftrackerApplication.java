package com.surf.surftracker;

import com.surf.surftracker.dto.*;
import com.surf.surftracker.mapper.CurrentMapper;
import com.surf.surftracker.model.Current;
import com.surf.surftracker.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SurftrackerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SurftrackerApplication.class, args);
	}

	//  @Bean
//  CommandLineRunner commandLineRunner(UserService userService, BCryptPasswordEncoder encoder){
//     return args -> {
//
//        userService.save(new User("user", encoder.encode("password")));
//     };
//  }

		@Override
		public void run(String... args) throws Exception {

		//adjust Current object to include SurfSpotID and surfSpotName
		Current lowerTrestlesCurrent = new Current();

		//right now hard coded with the trestles related url
		//pass in the url as a string to be used within each of these services
		SurfLine_rating_Service lt_rating_service = new SurfLine_rating_Service();
		SurfLine_sunlight_Service lt_sunlight_service = new SurfLine_sunlight_Service();
		SurfLine_surf_Service lt_surf_service = new SurfLine_surf_Service();
		SurfLine_swells_Service lt_swells_service = new SurfLine_swells_Service();
		SurfLine_tides_Service lt_tides_service = new SurfLine_tides_Service();
		SurfLine_weather_Service lt_weather_service = new SurfLine_weather_Service();
		SurfLine_wind_Service lt_wind_service = new SurfLine_wind_Service();
		DeepSwell_Service lt_ds_service = new DeepSwell_Service(lowerTrestlesCurrent);
		Surf_Captain_Service lt_sc_service = new Surf_Captain_Service(lowerTrestlesCurrent);
		SurfForecast_Service lt_sf_service = new SurfForecast_Service(lowerTrestlesCurrent);

		try {
			//once the services are run, I could implement them here as a list
			SurfLine_rating_DTO ratingDTO = lt_rating_service.getSurfLineRating();
			SurfLine_sunlight_DTO sunlightDTO = lt_sunlight_service.getSurfLineSunlight();
			SurfLine_surf_DTO surfDTO = lt_surf_service.getSurfLineSurf();
			SurfLine_wind_DTO windDTO = lt_wind_service.getSurfLineWind();


			CurrentMapper ltCurrentMapper = new CurrentMapper(lowerTrestlesCurrent,ratingDTO,sunlightDTO,surfDTO,windDTO);

			//Running methods to map fields to Current object
			ltCurrentMapper.SL_WaveQuality();
			ltCurrentMapper.SL_Sunrise();
			ltCurrentMapper.SL_Sunset();
			ltCurrentMapper.SL_Surf();
			ltCurrentMapper.SL_Wind();

			SurfLine_swells_DTO swellsDTO = lt_swells_service.getSurfLineSwells();
			SurfLine_tides_DTO tidesDTO = lt_tides_service.getSurfLineTides();
			SurfLine_weather_DTO weatherDTO = lt_weather_service.getSurfLineWeather();

			lt_ds_service.getDeepSwellCurrent();
			lt_sc_service.getSurfCaptainCurrent();
			lt_sf_service.getSurfForecastCurrent();


			// Print out the filled DTO's
//			System.out.println(ratingDTO);
//			System.out.println(sunlightDTO);
//			System.out.println(surfDTO);
//			System.out.println(swellsDTO);
//			System.out.println(tidesDTO);
//			System.out.println(weatherDTO);
//			System.out.println(windDTO);

			//Prints out the Current Lower Trestles Pojo
			System.out.println("Lower Trestles: " + lowerTrestlesCurrent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

