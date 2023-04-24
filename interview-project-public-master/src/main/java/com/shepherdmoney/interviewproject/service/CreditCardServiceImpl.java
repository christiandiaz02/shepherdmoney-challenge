package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    private CreditCardRepository creditCardRepository;

    @Autowired
    public CreditCardServiceImpl(CreditCardRepository theCreditCardRepository) {
        creditCardRepository = theCreditCardRepository;
    }

    @Override
    public List<CreditCard> findAll() {
        return creditCardRepository.findAll();
    }

    @Override
    public CreditCard findById(int theId) {
        Optional<CreditCard> result = creditCardRepository.findById(theId);
        CreditCard theCreditCard = null;
        if (result.isPresent()) {
            theCreditCard = result.get();
        }
        return theCreditCard;
    }

    @Override
    public CreditCard save(CreditCard creditCard) {
        return creditCardRepository.save(creditCard);
    }

    @Transactional
    @Override
    public void deleteById(int theId) {
        creditCardRepository.deleteById(theId);
    }
}
