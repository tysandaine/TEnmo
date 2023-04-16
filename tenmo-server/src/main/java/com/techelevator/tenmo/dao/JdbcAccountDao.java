package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public  JdbcAccountDao(){}


    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public Long getCurrentUserId(Principal principal) throws AccountNotFoundException {
        String sql = "SELECT account_id, account.user_id, balance FROM account " +
                "JOIN tenmo_user using(user_id) " +
                "WHERE tenmo_user.username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, principal.getName());
        if (results.next()){
            return mapRowToAccount(results).getUserId();
        }
        throw new AccountNotFoundException();
    }

 /*   @Override
    public BigDecimal getBalanceByUserId(long userId) throws AccountNotFoundException {
        String sql = "SELECT account_id, user_id, balance " +
                "FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()){
            return mapRowToAccount(results).getBalance();
        }
        throw new AccountNotFoundException();
    }*/

    @Override
    public Account getBalanceByUserId(Long userId) throws AccountNotFoundException{
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?;";
        SqlRowSet results;
        results = jdbcTemplate.queryForRowSet(sql, userId);
        if(results.next()){
        return mapRowToAccount(results);
        }
        /*try {
            results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                return mapRowToAccount(results);
            }
        } catch (Exception e) {
            throw new AccountNotFoundException();
        }
        return null;*/
        return null;
    }


  /*  @Override
    *//*public boolean update(Account account) throws AccountNotFoundException {
        String sql = "UPDATE account SET balance = ? " +
                "WHERE account_id = ?;";
        boolean isSuccessful = false;
            int status = jdbcTemplate.update(sql, account.getUserId(), account.getBalance(), account.getAccountId());
            if (status == 1) {
                isSuccessful = true;
            } else {
                throw new AccountNotFoundException();
            }
        return isSuccessful;
    }*/
    @Override
    public BigDecimal addToBalance(BigDecimal amountAdded, long id) throws AccountNotFoundException {
        Account account = getAccountByAccountIdPublic(id);
        String sqlString = "UPDATE account SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sqlString, account.getBalance().add(amountAdded) , id);
        return account.getBalance();
    }
    @Override
    public BigDecimal subtractFromBalance(BigDecimal amountSubtracted, long id) throws AccountNotFoundException {
        Account account = getAccountByAccountIdPublic(id);
        String sqlString = "UPDATE account SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sqlString, account.getBalance().subtract(amountSubtracted) , id);
        return account.getBalance();
    }

    @Override
    public Account getAccountByAccountIdPrivate(long accountId){
        String sql = "Select account_id, user_id, balance " +
        "from account where account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if(results.next()){
            Account account = mapRowToAccount(results);
            account.setBalance(null);
            return account;
        }
        return null;
    }

    @Override
    public Account getAccountByAccountIdPublic(long accountId){
        String sql = "Select account_id, user_id, balance " +
                "from account where account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if(results.next()){
            Account account = mapRowToAccount(results);
            return account;
        }
        return null;
    }

    @Override
    public long getAccountIdByUserId(long userId){
        String sql = "Select account_id " +
                "from account where user_id = ?;";

        long results = jdbcTemplate.queryForObject(sql, Long.class, userId);
        return results;
    }



/*    @Override
    public Account create(Account account) {
        return null;
    }

    @Override
    public void delete(Account account) throws AccountNotFoundException {

    }*/

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountId(results.getLong("account_id"));
        account.setUserId(results.getLong("user_id"));
        account.setBalance(results.getBigDecimal("balance"));
        return account;
    }
}
