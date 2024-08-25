package com.surf.surftracker;

import com.surf.surftracker.util.TimeStampUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeTester {
    public static void main(String[] args){
        System.out.println(TimeStampUtils.CurrentTime());
        System.out.println(TimeStampUtils.NearestHour());
        System.out.println(TimeStampUtils.Midnight());
        System.out.println(TimeStampUtils.ThreeAM());
        System.out.println(TimeStampUtils.SixAM());
        System.out.println(TimeStampUtils.NineAM());
        System.out.println(TimeStampUtils.TwelvePM());
        System.out.println(TimeStampUtils.ThreePM());
        System.out.println(TimeStampUtils.SixPM());
        System.out.println(TimeStampUtils.NinePM());
        System.out.println(TimeStampUtils.DayOne());
        System.out.println(TimeStampUtils.DayTwo());
        System.out.println(TimeStampUtils.DayThree());
        System.out.println(TimeStampUtils.DayFour());
        System.out.println(TimeStampUtils.DayFive());

        // Get the current epoch second
        long epochSecond = Instant.now().getEpochSecond();

        // Convert the epoch second to ZonedDateTime in the Pacific Time Zone
        ZonedDateTime pacificTime = Instant.ofEpochSecond(epochSecond)
                .atZone(ZoneId.of("America/Los_Angeles"));

        // Print the result
        System.out.println("Pacific Time: " + pacificTime);

    }

    }
