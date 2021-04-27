package com.li.oop.web;

import com.li.oop.model.Generator;
import com.li.oop.model.Json;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service1")
public class Service1Controller {
    @GetMapping("/{n}")
    public String generateSchedule(@PathVariable("n") int n) {
        return Json.toJson(Generator.generateSchedule(n));
    }
}
