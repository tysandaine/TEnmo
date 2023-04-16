package com.techelevator.tenmo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.http.HttpResponse;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Account not found.")
public class AccountNotFoundException extends Exception {

    public AccountNotFoundException() { super("Account not found."); }

}
