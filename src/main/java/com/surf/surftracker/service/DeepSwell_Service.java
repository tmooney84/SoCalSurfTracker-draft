package com.surf.surftracker.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeepSwell_Service {
    public static void main(String[] args) {
        String[] DSurl = {
                "https://deepswell.com/surf-report/US/North-San-Diego/Ponto-at-South-Carlsbad-State-Beach/1004",
                "https://deepswell.com/surf-report/US/North-San-Diego/Tamarack/1003",
                "https://deepswell.com/surf-report/US/North-San-Diego/Oceanside-Pier-northside/1001",
                "https://deepswell.com/surf-report/US/South-Orange-County/San-Onofre-State-Beach/1059",
                "https://deepswell.com/surf-report/US/South-Orange-County/Lower-Trestles/1030",
                "https://deepswell.com/surf-report/US/South-Orange-County/Upper-Trestles/1029",
                "https://deepswell.com/surf-report/US/South-Orange-County/T-Street-Trafalgar-Street/1057",
                "https://deepswell.com/surf-report/US/South-Orange-County/Doheny-State-Beach/1027",
                "https://deepswell.com/surf-report/US/South-Orange-County/Salt-Creek/1026",
                "https://deepswell.com/surf-report/US/North-Orange-County/Newport-Beach/1055",
                "https://deepswell.com/surf-report/US/North-Orange-County/Huntington-Beach-Pier-Surf/1051",
                "https://deepswell.com/surf-report/US/North-Orange-County/Seal-Beach-Pier/1045"
        };

        try {
            for (int i = 0; i < DSurl.length; i++) {
                Document dsConnect = Jsoup.connect(DSurl[i])
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36").get();

                // Debugging: Print the document to ensure it's correctly fetched
                // System.out.println(scConnect);

                Elements div = dsConnect.getElementsByClass("panel-body");
                if (div != null) {
                    String text = div.text();
                   // System.out.println("Fetched text for location #" + i + ": " + text);

                    // Regular expression to match wave height patterns
                    Pattern pattern = Pattern.compile("(\\d+-\\d+\\+?|\\d+\\+?) ft");
                    Matcher matcher = pattern.matcher(text);

                    if (matcher.find()) {
                        String waveHeight = matcher.group(1);
                        System.out.println("Current Wave height at location #" + i + ": " + waveHeight);
                    } else {
                        System.out.println("Wave height not found at location #" + i);
                    }
                } else {
                    System.out.println("Element not found at location #" + i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
