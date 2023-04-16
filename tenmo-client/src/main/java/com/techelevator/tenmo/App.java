package com.techelevator.tenmo;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.security.Principal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";



    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
//    AccountService accountService = new AccountService(API_BASE_URL, currentUser);
/*    TransferService transferService = new TransferService(API_BASE_URL, currentUser);*/



    public static void main(String[] args) throws AccountNotFoundException {

        App app = new App();
        app.run();
    }

    private void run() throws AccountNotFoundException {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() throws AccountNotFoundException {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                currentUser = null;
                run();
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() throws AccountNotFoundException {
        AccountService accountService = new AccountService(API_BASE_URL, currentUser);
		accountService.viewCurrentBalance();
		
	}

	private void viewTransferHistory() throws AccountNotFoundException {
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);
		transferService.getAllTransfers();
		
	}

	private void viewPendingRequests() {
        System.out.println("This feature is under construction!");
		
	}

	private void sendBucks() throws AccountNotFoundException {
        TransferService transferService = new TransferService(API_BASE_URL, currentUser);
        transferService.sendBucks();

		
	}

	private void requestBucks() {
        System.out.println("This feature is under construction!");
		
	}
}
