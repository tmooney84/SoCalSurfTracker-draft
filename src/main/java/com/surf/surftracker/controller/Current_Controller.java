package com.surf.surftracker.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Current_Controller {
    @GetMapping("/fetchtest")

   public String fetchtest(){
        return "fetchmap";
    }
}
