package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.security.Principal;

@RestController
@RequestMapping("/account")
@PreAuthorize("isAuthenticated()")
public class AccountController {
    @Autowired
    private AccountDao dao;

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Account get(Principal principal) throws AccountNotFoundException {
       Long userId = dao.getCurrentUserId(principal);
        return dao.getBalanceByUserId(userId);


    }

    @RequestMapping(path = "/{accountId}", method = RequestMethod.GET)
    public Account getAccountByAccountId(@PathVariable long accountId){return dao.getAccountByAccountIdPrivate(accountId);}

    @RequestMapping(path = "/userid/{userId}", method = RequestMethod.GET)
    public long getAccountIdByUserId(@PathVariable long userId){return dao.getAccountIdByUserId(userId);}



}
