package com.orlando.vaquinha.web;

import com.orlando.vaquinha.paypal.ApiRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class FrontController {

    @Autowired
    private ApiRequest paypalApiRequest;
    
    @GetMapping(value = {"", "/", "/index"})
    public String index(Model model) {
        model.addAttribute("balance", paypalApiRequest.getBalance());
        model.addAttribute("transactions", paypalApiRequest.getTransactions().getPaymentTransactions());
        return "index";
    }
}
