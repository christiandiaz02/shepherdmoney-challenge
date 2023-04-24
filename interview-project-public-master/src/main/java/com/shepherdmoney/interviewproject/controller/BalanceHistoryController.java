package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.service.BalanceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;

public class BalanceHistoryController {

    private BalanceHistoryService balanceHistoryService;

    @Autowired
    private BalanceHistoryController(BalanceHistoryService theBalanceHistoryService) {
        balanceHistoryService = theBalanceHistoryService;
    }
}
