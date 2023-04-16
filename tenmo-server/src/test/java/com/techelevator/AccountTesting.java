package com.techelevator;

import com.techelevator.tenmo.controller.AccountController;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;


public class AccountTesting {

    private AccountDao dao;
    public AccountController test;
    private JdbcTemplate jdbcTemplate;

    public AccountTesting() {
        dao = new JdbcAccountDao(jdbcTemplate);
    }

//    @Test
//    public void getShouldReturnSingleAuction() throws Exception {
//        BigDecimal balance = dao.getBalanceByUserId(1001);
//        System.out.println(balance);
//        Assert.assertEquals(1000.00, balance);
//    }
//x
//    @Test
//    public void getUserById() throws Exception {
//        Account account = dao.findByUserId(1001);
//        BigDecimal balance = account.getBalance();
//        Assert.assertEquals(1000.00, balance);
//    }

}
