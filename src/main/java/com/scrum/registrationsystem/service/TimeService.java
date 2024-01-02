/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scrum.registrationsystem.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;

/**
 *
 * @author patri
 */
public class TimeService {
    public LocalDateTime getCurrentDateTime() {
        String dateTimeString = "2024-01-01T17:01:00";
        return LocalDateTime.parse(dateTimeString);
    }

    public LocalDateTime getCurrentDateTime1() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://worldtimeapi.org/api/timezone/America/Guayaquil"))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = new JSONObject(response.body());
            String dateTimeString = jsonResponse.getString("datetime");

            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            OffsetDateTime odt = OffsetDateTime.parse(dateTimeString, formatter);
            return odt.toLocalDateTime();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    //timeService.getMinuteUnit()
    
    public int getMinuteUnit(LocalDateTime dateTime) {
        return dateTime.getMinute() % 10;
    }
}
