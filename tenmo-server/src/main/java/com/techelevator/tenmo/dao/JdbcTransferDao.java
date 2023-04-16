package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AccountDao dao;
    public JdbcTransferDao(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }


    @Override
    public List<Transfer> getAllTransfers(Principal principal) throws AccountNotFoundException {

        long currentAccountId = dao.getBalanceByUserId(dao.getCurrentUserId(principal)).getAccountId();
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id," +
                "account_from, account_to, amount " +
                "FROM transfer t " +
                "WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, currentAccountId, currentAccountId);
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getAllTransfersByAccountFrom(long accountFrom) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer" +
                "WHERE account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountFrom);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getAllTransfersByAccountTo(long accountTo) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer" +
                "WHERE account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountTo);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransfersByAmount(BigDecimal amount) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer" +
                "WHERE amount = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, amount);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransferById(long transferId) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE transfer_id = ?;";
        SqlRowSet results;
        results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        }
        return null;
    }

  /*  @Override
    public boolean sendBucks(Transfer transfer, Principal principal) throws AccountNotFoundException {
//    public Transfer sendBucks(Principal principal, BigDecimal amountSent, long accountTo) throws AccountNotFoundException {
//        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao();
//        long currentUserId = jdbcAccountDao.getCurrentUserId(principal);
        *//*AccountDao dao = new JdbcAccountDao();*//*
        BigDecimal currentBalance = dao.getBalanceByUserId(dao.getCurrentUserId(principal)).getBalance();
        BigDecimal zero = BigDecimal.valueOf(0.00);
        if (transfer.getAccountFrom() != transfer.getAccountTo()) {
            if ((currentBalance.compareTo(transfer.getAmount()) >= 0) && transfer.getAmount().compareTo(zero) > 0) {
                String withdrawSql = "UPDATE account SET balance = balance - ? " +
                        "WHERE account_id = ?;";
                int withdrawResults = jdbcTemplate.update(withdrawSql, transfer.getAmount(), transfer.getAccountFrom());

                if (withdrawResults != 1) {
                    transfer.setTransferStatusId(3);
                    return false;
                }
//        BigDecimal newPrincipalBalance = jdbcAccountDao.getBalanceByUserId(currentUserId).getBalance().subtract(amountSent);
//        BigDecimal newUserToBalance = jdbcAccountDao.getBalanceByUserId(userTo).getBalance().add(amountSent);
                String depositSql = "UPDATE account SET balance = balance + ? " +
                        "WHERE account_id = ?;";
                int depositResults = jdbcTemplate.update(depositSql, transfer.getAmount(), transfer.getAccountTo());
                if (depositResults != 1) {
                    transfer.setTransferStatusId(3);
                    return false;
                }
                transfer.setTransferStatusId(2);
                return true;
            }
            transfer.setTransferStatusId(3);
            return false;
        }
        transfer.setTransferStatusId(3);
        return false;
    }*/

    @Override
    public String createTransfer(Principal principal, BigDecimal amount, long accountTo) throws AccountNotFoundException {
        long currentAccountId = dao.getAccountIdByUserId(dao.getCurrentUserId(principal));
        if (currentAccountId == accountTo) {
            return "You cannot send money to yourself.";
        }
        if (amount.compareTo(dao.getBalanceByUserId(dao.getCurrentUserId(principal)).getBalance()) == -1 && amount.compareTo(new BigDecimal(0)) == 1) {
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, " +
                    "account_from, account_to, amount) VALUES (2, 2, ?, ?, ?);";
            /*            long tranId = jdbcTemplate.queryForObject(sql, Long.class, currentAccountId, accountTo, amount);*/
            jdbcTemplate.update(sql, currentAccountId, accountTo, amount);

            /*String sqlMap = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                    "account_from, account_to, (select username from tenmo_user join account using(user_id) where account_id = ?) " +
            "as username_from, (select username from tenmo_user join account using(user_id) where account_id = ?) " +
            "as username_to, amount FROM transfer t " +
            "JOIN account a ON t.account_from = a.account_id " +
            "JOIN tenmo_user tu using(user_id) " +
                    "WHERE transfer_id = ?;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sqlMap, currentAccountId, accountTo, tranId);
            if (results.next()) {*/
            dao.subtractFromBalance(amount, currentAccountId);
            dao.addToBalance(amount, accountTo);
            return "Transfer completed successfully!";

        } else {

            return "Transfer failed!";
        }
    }

    @Override
    public List<Transfer> getAllTransfersByType(long transferTypeId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer" +
                "WHERE transfer_type_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferTypeId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getAllTransfersByStatus(long transferStatusId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, " +
                "account_from, account_to, amount " +
                "FROM transfer" +
                "WHERE transfer_status_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferStatusId);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getLong("transfer_id"));
        transfer.setTransferTypeId(results.getLong("transfer_type_id"));
        transfer.setTransferStatusId(results.getLong("transfer_status_id"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }

    private Transfer mapRowToCreateTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getLong("transfer_id"));
        transfer.setTransferTypeId(results.getLong("transfer_type_id"));
        transfer.setTransferStatusId(results.getLong("transfer_status_id"));
        transfer.setAccountFrom(results.getLong("account_from"));
        transfer.setAccountTo(results.getLong("account_to"));
        transfer.setUserNameFrom(results.getString("username_from"));
        transfer.setUserNameTo(results.getString("username_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}
