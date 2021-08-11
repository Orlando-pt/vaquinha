package com.orlando.vaquinha.web;

import com.orlando.vaquinha.paypal.ApiRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import urn.ebay.apis.eBLBaseComponents.PaymentTransactionSearchResultType;

@Controller
@RequestMapping("")
public class FrontController {

    @Autowired
    private ApiRequest paypalApiRequest;
    
    @GetMapping(value = {"", "/", "/index"})
    public String index(Model model) {
        model.addAttribute("balance", 220);
        return "index";
    }

    @GetMapping("/balance")
    public String balance() {
        var balance = paypalApiRequest.getBalance();
        System.out.println(balance.getBalanceTimeStamp());
        System.out.println(balance.getBalance().getValue());
        System.out.println(balance.getBalance().getCurrencyID());
        return "index";
    }

    @GetMapping("/transactions")
    public String transactions() {
        var transactions = paypalApiRequest.getTransactions();

        for (PaymentTransactionSearchResultType tran : transactions.getPaymentTransactions()) {
            System.out.println(tran.getPayer());
            System.out.println(tran.getPayerDisplayName());
            System.out.println(tran.getTimestamp());
            System.out.println(tran.getFeeAmount().getValue());
            System.out.println(tran.getGrossAmount().getValue());
            System.out.println(tran.getNetAmount().getValue());
            System.out.println(tran.getType());
        }

        return "index";
    }
}
