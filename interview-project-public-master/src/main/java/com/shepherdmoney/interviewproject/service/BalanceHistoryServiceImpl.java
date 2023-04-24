package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.repository.BalanceHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BalanceHistoryServiceImpl implements BalanceHistoryService {

    private BalanceHistoryRepository balanceHistoryRepository;

    @Autowired
    public BalanceHistoryServiceImpl (BalanceHistoryRepository theBalanceHistoryRepository) {
        balanceHistoryRepository = theBalanceHistoryRepository;
    }

    @Override
    public List<BalanceHistory> findAll() {
        return balanceHistoryRepository.findAll();
    }

    @Override
    public BalanceHistory findById(int theId) {
        Optional<BalanceHistory> result = balanceHistoryRepository.findById(theId);
        BalanceHistory theBalance = null;
        if (result.isPresent()) {
            theBalance = result.get();
        }
        return theBalance;
    }

    @Override
    public BalanceHistory save(BalanceHistory balanceHistory) {
        return balanceHistoryRepository.save(balanceHistory);
    }

    @Transactional
    @Override
    public void deleteById(int theId) {
        balanceHistoryRepository.deleteById(theId);
    }
}
