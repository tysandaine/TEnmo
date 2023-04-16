package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserDao dao;

    @RequestMapping(path = "/findall", method = RequestMethod.GET)
    public List<User> findAllPrivate() {
        List<User> users = dao.findAllPrivate();
        return users;
    }

    @RequestMapping(path = "/account/{accountId}", method = RequestMethod.GET)
    public String getUsernameByAccountId(@PathVariable long accountId){return dao.getUsernameByAccountId(accountId);}

}
