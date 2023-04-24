package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.service.BalanceHistoryService;
import com.shepherdmoney.interviewproject.service.CreditCardService;
import com.shepherdmoney.interviewproject.service.UserService;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class CreditCardController {

    // wire in CreditCard repository here (~1 line)
    private CreditCardService creditCardService;

    private UserService userService;

    private BalanceHistoryService balanceHistoryService;
    @Autowired
    private CreditCardController(CreditCardService theCreditCardService) {
        creditCardService = theCreditCardService;
    }

    @Autowired
    private void UserController(UserService theUserService) { userService = theUserService; }

    @Autowired
    private void BalanceHistoryController(BalanceHistoryService theBalanceHistoryService) {
        balanceHistoryService = theBalanceHistoryService;
    }




    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        // Create a credit card entity, and then associate that credit card with user with given userId
        // Return 200 OK with the credit card id if the user exists and credit card is successfully associated with the user
        // Return other appropriate response code for other exception cases
        // Do not worry about validating the card number, assume card number could be any arbitrary format and length

        // Create a new credit card instance
        CreditCard newCard = new CreditCard();
        newCard.setUserId(payload.getUserId());
        newCard.setNumber(payload.getCardNumber());
        newCard.setIssuanceBank(payload.getCardIssuanceBank());
        newCard.setBalanceHistory(new ArrayList<>());

        // Save the new credit card instance
        CreditCard card = creditCardService.save(newCard);

        // Pull up the user in given ID
        User user = userService.findById(payload.getUserId());

        // Return an error if there is no user to associate credit card with
        if (user == null) {
            return new ResponseEntity<>(card.getId(),
                    HttpStatus.BAD_REQUEST);
        }

        // Add credit card ID to user's list of cards
        user.getCreditCards().add(card.getId());
        user = userService.save(user);
        return new ResponseEntity<>(card.getId(), HttpStatus.OK);
    }

    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        // return a list of all credit card associated with the given userId, using CreditCardView class
        // if the user has no credit card, return empty list, never return null

        // Initialize a list of CreditCardView objects
        List<CreditCardView> CreditCards = new ArrayList<>();

        // Find user
        User user = userService.findById(userId);

        // Return an error if the user does not exist
        if (user == null) {
            return new ResponseEntity<>(CreditCards,
                    HttpStatus.BAD_REQUEST);
        }

        // Find each credit card by its ID, and use its Issuance Bank and Number to make a new CreditCardView of it
        for (Integer cardId : user.getCreditCards()) {
            CreditCard FindCardToAdd = creditCardService.findById(cardId);
            CreditCardView convertedCardtoAdd = new CreditCardView(FindCardToAdd.getIssuanceBank(),
                    FindCardToAdd.getNumber());
            CreditCards.add(convertedCardtoAdd);
        }

        return new ResponseEntity<>(CreditCards, HttpStatus.OK);
    }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        // Given a credit card number, efficiently find whether there is a user associated with the credit card
        // If so, return the user id in a 200 OK response. If no such user exists, return 400 Bad Request

        // Loop through each credit card, comparing each instance's creditCardNumber to the given creditCardNumber
        // If we find a match, we have found the user so we return the userId
        for (CreditCard creditCard: creditCardService.findAll()) {
            if ((Objects.equals(creditCard.getNumber(), creditCardNumber)) &&
                    (userService.findById(creditCard.getUserId()) != null)) {
                return new ResponseEntity<>(creditCard.getUserId(), HttpStatus.OK);
            }
        }

        // If the loop through every credit card produced no match, there is no user associated with the credit card
        // We simply return -1 since a userId can not be negative
        return new ResponseEntity<>(-1, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/credit-card:update-balance")
    public ResponseEntity<String> updateBalance(@RequestBody UpdateBalancePayload[] payload) {
        // Given a list of transactions, update credit cards' balance history.
        // For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
        // Given a transaction of {date: 4/10, amount: 10}, the new balanceHistory is
        // [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 110}]
        // Return 200 OK if update is done and successful, 400 Bad Request if the given card number
        //  is not associated with a card.

        // We will iterate through each payload in the input array, working out of each at a time
        for (UpdateBalancePayload load: payload) {
            int cardId = findIdGivenCreditCardNumber(load.getCreditCardNumber());
            if (cardId == -1) {
                return new ResponseEntity<>("No associated card for this number", HttpStatus.BAD_REQUEST);
            }
            // Find the credit card ID using the given credit card number with our helper method
            CreditCard creditCard = creditCardService.findById(cardId);

            // boolean used for detecting if a transaction for today's balance is present in the balance history
            Boolean containsToday = false;


            // Iterate through every transaction in the balance history and update the balance
            // for the transactions that happened on same date as payload or later
            for (BalanceHistory balance : creditCard.getBalanceHistory()) {

                // If true, increment balance by adding the balance of new transaction to current balance
                if (balance.getDate().isAfter(load.getTransactionTime()) ||
                        balance.getDate().equals(load.getTransactionTime())) {
                    balance.setBalance(balance.getBalance() + load.getTransactionAmount());
                }

                // Check if a balance history contains today's date.  If present, mark
                // containsToday to true
                if (balance.getDate().equals(LocalDate.now())) {
                    containsToday = true;
                }
            }

            // Create a new Balance History instance and add it to the credit card's balance history
            BalanceHistory newBalance = new BalanceHistory();
            newBalance.setDate(load.getTransactionTime());
            newBalance.setBalance(load.getTransactionAmount());
            creditCard.getBalanceHistory().add(newBalance);

            // Check if the balance history contains a transaction for today's date.  If it does not, then create a
            // new BalanceHistory for today's date and today's current balance
            if (!containsToday) {
                newBalance.setDate(LocalDate.now());
                newBalance.setBalance(load.getTransactionAmount());
                creditCard.getBalanceHistory().add(newBalance);
            }

            // One-liner that sorts the CreditCard's Balance History in chronological order, starting with today's date
            creditCard.getBalanceHistory().sort(Comparator.comparing(BalanceHistory::getDate).reversed());

            // Finally, save this credit card instance before moving on to another payload
            creditCard = creditCardService.save(creditCard);
        }
        return new ResponseEntity<>("Successfully updated balance history", HttpStatus.OK);
    }

    // Helper function for finding ID of a credit card based on Credit Card Number
    // Returns -1 if no credit card with the given number is found
    public int findIdGivenCreditCardNumber(String CreditCardNumber) {
        for (CreditCard creditCard: creditCardService.findAll()) {
            if (Objects.equals(creditCard.getNumber(), CreditCardNumber)) {
                return creditCard.getId();
            }
        }
        return -1;
    }

}
