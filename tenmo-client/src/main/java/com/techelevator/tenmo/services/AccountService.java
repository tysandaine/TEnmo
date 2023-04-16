package com.techelevator.tenmo.services;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;

public class AccountService {

    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    private AuthenticatedUser currentUser;



    public AccountService(String url, AuthenticatedUser currentUser) {
        BASE_URL = url;
        this.currentUser = currentUser;
    }

    public BigDecimal viewCurrentBalance() {
        Account account = new Account();
        BigDecimal balance = new BigDecimal(0);
        try {
            account = restTemplate.exchange(BASE_URL + "account/balance", HttpMethod.GET,
                    makeAccountEntity(account), Account.class).getBody();
            System.out.println("Your current account balance is: $" + account.getBalance());
        } catch (RestClientException e) {
            System.out.println("Your account is somehow unbalanced!");
        }
        return balance;
    }

//        HttpEntity<Void> entity = new HttpEntity<>(headers);
//        ResponseEntity<Account>response = restTemplate.exchange(baseUrl + "account/balance", HttpMethod.GET, entity, Account.class);
//        return response.getBody().getBalance();


//    private HttpEntity<Account> makeAuthEntity() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(currentUser.getToken());
//        HttpEntity entity = new HttpEntity<>(headers);
//        return entity;
//    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Account> entity = new HttpEntity<>(account, headers);
        return entity;
    }


}
