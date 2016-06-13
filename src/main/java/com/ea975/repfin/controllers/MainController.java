package com.ea975.repfin.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class MainController {

    @RequestMapping("/")
    public String index(Map<String, Object> model){
        model.put("message", "Hello World Lyra");
        return "index";
    }

}
