package com.techelevator.tenmo.services;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class TransferService {

    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    private AuthenticatedUser currentUser;


    public TransferService(String BASE_URL, AuthenticatedUser currentUser) {
        this.BASE_URL = BASE_URL;
        this.currentUser = currentUser;
    }
    private final ConsoleService cs = new ConsoleService();

    public void getAllTransfers() throws AccountNotFoundException {
       Transfer[] output = null;
/*       long userAccountId = restTemplate.exchange(BASE_URL + "/account/userid/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Long.class).getBody();*/
       try{
           output = restTemplate.exchange(BASE_URL + "/transfer/alltransfers", HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
           System.out.println("-------------------------------------------\r\n" +
                   "Transfers\r\n" +
                   "ID          From/To                 Amount\r\n" +
                   "-------------------------------------------\r\n");
           String fromOrTo = "";
           String userName = "";
           for(Transfer i: output){
                Account tempAccount = restTemplate.exchange(BASE_URL + "/account/" + i.getAccountFrom(), HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
               if(currentUser.getUser().getId() == tempAccount.getUserId()){
                   fromOrTo = "To: ";
                   /*long fromAccount = i.getAccountFrom();
                   Account account = restTemplate.exchange(BASE_URL + "/account/" + i.getAccountFrom(), HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
                   long fromId = account.getUserId();*/
                   userName = restTemplate.exchange(BASE_URL + "/user/account/" + i.getAccountTo(), HttpMethod.GET, makeAuthEntity(), String.class).getBody();
               } else{
                   fromOrTo = "From : ";
                   /*long toAccount = i.getAccountTo();
                   Account account = restTemplate.exchange(BASE_URL + "/account/" + i.getAccountTo(), HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
                   long toId = account.getUserId();*/
                   userName = restTemplate.exchange(BASE_URL + "/user/account/" + i.getAccountFrom(), HttpMethod.GET, makeAuthEntity(), String.class).getBody();
               }
               System.out.println(i.getTransferId() +"\t\t" + fromOrTo + userName + "\t\t\t$" + i.getAmount());
           }
           System.out.println("-------------------------------------------\r\n");
           long input = cs.promptForLong("Please enter transfer ID to view details (0 to cancel): ");
           if(input != 0){
               boolean foundTransferId = false;
               for (Transfer i: output) {
                   if(input == i.getTransferId()){
                       Transfer temp = restTemplate.exchange(BASE_URL + "/transfer/" + i.getTransferId(), HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
                       foundTransferId = true;
                       String type = "";
                       String status = "";
                       String userFrom = "";
                       String userTo = "";
                       switch (Long.toString(temp.getTransferTypeId())){
                           case "1":
                               type = "Request";
                               break;
                           case "2":
                               type = "Send";
                               break;
                       }
                       switch (Long.toString(temp.getTransferStatusId())){
                           case "1":
                               status = "Pending";
                               break;
                           case "2":
                               status = "Approved";
                               break;
                           case "3":
                               status= "Rejected";
                               break;
                       }
                       /*long fromAccount = temp.getAccountFrom();
                      *//* *//**//*Account senderAccount = restTemplate.exchange(BASE_URL + "/account/" + fromAccount, HttpMethod.GET, makeAuthEntity(), Account.class).getBody();*//**//*
                       long fromId = senderAccount.getUserId();*/
                       userFrom = restTemplate.exchange(BASE_URL + "/user/account/" + temp.getAccountFrom(), HttpMethod.GET, makeAuthEntity(), String.class).getBody();

                       /*long toAccount = temp.getAccountTo();
                       Account receiverAccount = restTemplate.exchange(BASE_URL + "/account/" + toAccount, HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
                       long toId = receiverAccount.getUserId();*/
                       userTo = restTemplate.exchange(BASE_URL + "/user/account/" + temp.getAccountTo(), HttpMethod.GET, makeAuthEntity(), String.class).getBody();

                       temp.setUserNameFrom(userFrom);
                       temp.setUserNameTo(userTo);
                       System.out.println("--------------------------------------------\r\n" +
                               "Transfer Details\r\n" +
                               "--------------------------------------------\r\n" +
                               " Id: "+ temp.getTransferId() + "\r\n" +
                               " From: " + userFrom + "\r\n" +
                               " To: " + userTo + "\r\n" +
                               " Type: " + type + "\r\n" +
                               " Status: " + status + "\r\n" +
                               " Amount: $" + temp.getAmount());
                   }
               }
               if(!foundTransferId){
                   System.out.println("Not a valid transfer ID");
               }
           }
        } catch (Exception e){
           System.out.println("Something went wrong");
       }
    }

    public void sendBucks(){
        User[] users = null;
        Transfer transfer = new Transfer();
        try{
          Scanner scanner = new Scanner(System.in);
          users = restTemplate.exchange(BASE_URL + "/user/findall", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            System.out.println("-------------------------------------------\r\n" +
                    "Users\r\n" +
                    "ID\t\tName\r\n" +
                    "-------------------------------------------");
            for (User i: users) {
                if(i.getId() != currentUser.getUser().getId()){
                    System.out.println(i.getId() + "\t\t" + i.getUsername());
                }
            }
            System.out.println("-------------------------------------------");
            long userIdInput = cs.promptForLong("Enter ID of user you are sending to (0 to cancel): ");
            long accountTo = restTemplate.exchange(BASE_URL + "/account/userid/" + userIdInput, HttpMethod.GET, makeAuthEntity(), Long.class).getBody();
            long accountFrom = restTemplate.exchange(BASE_URL + "/account/userid/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Long.class).getBody();
            transfer.setAccountTo(accountTo);
            transfer.setAccountFrom(accountFrom);
            if(transfer.getAccountTo() != 0){
                BigDecimal amount = cs.promptForBigDecimal("Enter amount: ");
                try{
                    transfer.setAmount(amount);
                } catch (NumberFormatException e){
                    System.out.println("Error when entering amount");
                }
                String output = restTemplate.exchange(BASE_URL + "/transfer/create", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
                System.out.println(output);
            }
        }catch (Exception e){
            System.out.println("Bad input");
        }

    }

    public Transfer createTransfer( BigDecimal amount, long accountTo) throws AccountNotFoundException{
        return null;
    }

    public Transfer getTransferById(long transferId){
        return null;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
