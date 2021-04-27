package com.li.oop.web;

import com.li.oop.entity.Application;
import com.li.oop.exception.ApplicationNotFoundException;
import com.li.oop.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static com.li.oop.constant.consist.*;

@RestController
@RequestMapping("/service2")
public class Service2Controller {
    @GetMapping("/schedule/{n}")
    public String getSchedule(@PathVariable("n") String n) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(URL1 + n, String.class);
        FileIO.write("Schedule.json", responseEntity.getBody());
        return "Schedule.json saved!";
    }

    @GetMapping("/file/{name}")
    public String getFile(@PathVariable("name") String name) {
        return FileIO.read(name);
    }

    @PostMapping(value = "/result", consumes = "application/json", produces = "application/json")
    public String saveJson(@RequestBody String result) {
        FileIO.write("Result.json", result);
        return "Result.json saved!";
    }
}
