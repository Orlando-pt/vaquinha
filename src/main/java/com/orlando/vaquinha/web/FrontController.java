package com.orlando.vaquinha.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class FrontController {
    
    @GetMapping(value = {"", "/", "/index"})
    public String index(Model model) {
        model.addAttribute("balance", 220);
        return "index";
    }
}
