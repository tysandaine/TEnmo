package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
/*    @NotNull(message = "Account To field cannot be empty")*/
    private long transferId;
/*    @NotNull(message = "Account To field cannot be empty")*/
    private long transferTypeId;
/*    @NotNull(message = "Account To field cannot be empty")*/
    private long transferStatusId;
/*    @NotNull(message = "Account To field cannot be empty")*/
    private long accountFrom;
/*    @NotNull(message = "Account To field cannot be empty")*/
    private long accountTo;
    private BigDecimal amount;
/*    @NotNull(message = "Username from field cannot be empty")*/
    private String userNameFrom;
/*    @NotNull(message = "Username to field cannot be empty")*/
    private String userNameTo;

    public Transfer(long transferId, long accountFrom, long accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Transfer(){
    }

    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(long accountTo) { this.accountTo = accountTo; }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getTransferTypeId() { return transferTypeId; }

    public void setTransferTypeId(long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getUserNameFrom() {
        return userNameFrom;
    }

    public void setUserNameFrom(String userNameFrom) {
        this.userNameFrom = userNameFrom;
    }

    public String getUserNameTo() {
        return userNameTo;
    }

    public void setUserNameTo(String userNameTo) {
        this.userNameTo = userNameTo;
    }
}
