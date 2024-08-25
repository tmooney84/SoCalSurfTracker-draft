/*
Don't forget to define the variables dayOfWeek, dayOfMonth,timeOfDay!!!
 */

package com.surf.surftracker.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class SurfForecast_Service {
        public static void main(String[] args) {
            String[] SFurl = {"https://www.surf-forecast.com/breaks/Newport-Point/forecasts/latest",
                    "https://www.surf-forecast.com/breaks/Trestles_Lowers/forecasts/latest/",
                    "https://www.surf-forecast.com/breaks/Jimbaran-Beach/forecasts/latest/"
            };

            try {
                for (int i = 0; i < SFurl.length; i++) {
                    Document sfConnect = Jsoup.connect(SFurl[i])
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36")
                            .get();

                    // Select the <td> element with the specific class and data-date attribute

                    // Define your variables
                    String dayOfWeek = "Saturday";
                    String dayOfMonth = "24";
                    String timeOfDay = "5PM";

// Construct the query string with variables
                    String query = String.format(
                            "td.forecast-table__cell.forecast-table-wave-height__cell[data-date='%s %s %s']",
                            dayOfWeek, dayOfMonth, timeOfDay
                    );

// Use the query string in the selectFirst method
                    Element tdElement = sfConnect.selectFirst(query);

                    //System.out.println(tdElement);

                    if (tdElement != null) {
                        // Navigate to the <text> element inside the SVG and get the content
                        Element textElement = tdElement.selectFirst("text.swell-icon__val");
                        if (textElement != null) {
                            String waveHeight = textElement.text();
                            double wave = Double.parseDouble(waveHeight);
                            wave = Math.round(wave * 3.048);

                            System.out.println("Wave height: " + wave);
                        } else {
                            System.out.println("Wave height value not found");
                        }
                    } else {
                        System.out.println("Element with specified class and data-date not found");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

