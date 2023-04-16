package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface AccountDao {

    List<Account> findAll();

    Long getCurrentUserId(Principal principal) throws AccountNotFoundException;

    Account getBalanceByUserId(Long userId) throws AccountNotFoundException;

    BigDecimal addToBalance(BigDecimal amountAdded, long id) throws AccountNotFoundException;

    BigDecimal subtractFromBalance(BigDecimal amountSubtracted, long id) throws AccountNotFoundException;

    Account getAccountByAccountIdPrivate(long accountId);

    Account getAccountByAccountIdPublic(long accountId);

    long getAccountIdByUserId(long userId);

   /* boolean update(Account account) throws AccountNotFoundException;*/

    /*Account create(Account account);

    void delete(Account account) throws AccountNotFoundException;*/
}
