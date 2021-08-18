package com.orlando.vaquinha.web;

import java.sql.Timestamp;

import com.orlando.vaquinha.dto.PaymentDto;
import com.orlando.vaquinha.model.Payment;
import com.orlando.vaquinha.paypal.ApiRequest;
import com.orlando.vaquinha.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class FrontController {

    @Autowired
    private ApiRequest paypalApiRequest;

    @Autowired
    private PaymentRepository paymentRepository;
    
    @GetMapping(value = {"", "/", "/index"})
    public String index(Model model) {
        model.addAttribute("balance", paypalApiRequest.getBalance());
        model.addAttribute("payment", new PaymentDto());
        model.addAttribute("contributions", paymentRepository.findTop12ByOrderByTimestampDesc());
        return "index";
    }

    @PostMapping("/contribution")
    public String payment(@ModelAttribute PaymentDto payment, Model model) {
        model.addAttribute("amount", payment.getAmount());
        if (!verifyPaymentParams(model, payment)) {
            var contribution = new Payment();

            contribution.setName(payment.getName());
            contribution.setAmount(payment.getAmount());
            contribution.setTimestamp(new Timestamp(System.currentTimeMillis()));

            paymentRepository.save(contribution);
        }
        return "contribution";
    }

    private Boolean verifyPaymentParams(Model model, PaymentDto payment) {
        if (payment.getAmount() == null || payment.getName() == null) {
            model.addAttribute("error_message", "Por favor, verifica os campos de submissão. Não são aceites campos vazios.");            
            System.out.println("Null values");
            return true;
        }
        if (payment.getName().isEmpty()) {
            model.addAttribute("error_message", "Por favor, verifica o nome da submissão.");
            System.out.println("Empty name");
            return true;
        }
        if (Double.compare(payment.getAmount(), Double.valueOf(0.0)) <= 0) {
            model.addAttribute("error_message", "O valor da contribuição deve ser superior a 0. Por favor, verifica novamente o campo.");
            System.out.println("Bad amount value");
            return true;
        }
        return false;
    }
}
