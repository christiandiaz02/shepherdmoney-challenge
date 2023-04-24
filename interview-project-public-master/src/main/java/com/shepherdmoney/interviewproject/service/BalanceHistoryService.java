package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.model.BalanceHistory;

import java.util.List;

public interface BalanceHistoryService {

    List<BalanceHistory> findAll();

    BalanceHistory findById(int theId);

    BalanceHistory save(BalanceHistory balanceHistory);

    void deleteById(int theId);
}
