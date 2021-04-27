package com.li.oop.web;

import com.li.oop.model.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.li.oop.constant.consist.URL2;

@RestController
@RequestMapping("/service3")
public class Service3Controller {
    private String schedule;
    @GetMapping("/get")
    public String getSchedule() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL2 + "/file/Schedule.json", String.class);
        if (!responseEntity.getBody().equals("Error: File not found!")) {
            schedule = responseEntity.getBody();
            return "Got schedule!";
        } else {
            return "Schedule not found!";
        }
    }

    @GetMapping("/work")
    public String work() throws ExecutionException, InterruptedException {
        if (schedule != null) {
            RestTemplate restTemplate = new RestTemplate();
            Port port = Generator.generatePort(Json.toSchedule(schedule));
            port.getResult();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(Json.toJson(port), headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL2 + "/result", request, String.class);
            return "Work done!";
        } else {
            return "Schedule empty!";
        }
    }
}
