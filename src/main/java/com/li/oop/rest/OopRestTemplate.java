package com.li.oop.rest;

import com.li.oop.model.Generator;
import com.li.oop.model.Json;
import com.li.oop.model.Port;
import com.li.oop.model.Schedule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

import static com.li.oop.constant.consist.*;

public class OopRestTemplate {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL2 + "/schedule/100", String.class);

        responseEntity = restTemplate.getForEntity(URL2 + "/file/Schedule.json", String.class);

        Port port = Generator.generatePort(Json.toSchedule(responseEntity.getBody()));
        port.getResult();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(Json.toJson(port), headers);
        responseEntity = restTemplate.postForEntity(URL2 + "/result", request, String.class);
    }
}
