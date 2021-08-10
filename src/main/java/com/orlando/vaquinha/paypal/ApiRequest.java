package com.orlando.vaquinha.paypal;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import urn.ebay.api.PayPalAPI.GetBalanceReq;
import urn.ebay.api.PayPalAPI.GetBalanceRequestType;
import urn.ebay.api.PayPalAPI.GetBalanceResponseType;
import urn.ebay.api.PayPalAPI.PayPalAPIInterfaceServiceService;
import urn.ebay.api.PayPalAPI.TransactionSearchReq;
import urn.ebay.api.PayPalAPI.TransactionSearchRequestType;
import urn.ebay.api.PayPalAPI.TransactionSearchResponseType;

@Component
public class ApiRequest {
    
    private PayPalAPIInterfaceServiceService paypal;

    public ApiRequest() {
        configPaypalAccount();
    }

    /**
     * Config the paypal account with all the necessary params
     * This method will allow the establishment of a connection to the paypal servers
     */
    private void configPaypalAccount() {
        Map<String, String> customConfigurationMap = new HashMap<String, String>();
        customConfigurationMap.put("mode", "live");
        customConfigurationMap.put("acct1.UserName", System.getenv("USER_ID"));
        customConfigurationMap.put("acct1.Password", System.getenv("PWD_ID"));
        customConfigurationMap.put("acct1.Signature", System.getenv("SIGNATURE_ID"));
        paypal = new PayPalAPIInterfaceServiceService(customConfigurationMap);
    }

    /**
     * get paypal account balance
     * @return GetBalanceResponseType object
     */
    public GetBalanceResponseType getBalance() {
        var balance = new GetBalanceReq();
        var reqType = new GetBalanceRequestType();

        reqType.setReturnAllCurrencies("0");
        balance.setGetBalanceRequest(reqType);

        try {
            return paypal.getBalance(balance);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    /**
     * get all the transactions of the account
     * since a specific date
     * @return TransactionSearchResponseType object
     */
    public TransactionSearchResponseType getTransactions() {
        var req = new TransactionSearchReq();
        var reqType = new TransactionSearchRequestType();

        reqType.setStartDate("2021-08-00");
        req.setTransactionSearchRequest(reqType);

        try {
            return paypal.transactionSearch(req);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
}
