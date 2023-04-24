package com.shepherdmoney.interviewproject.service;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;

import java.util.List;

public interface CreditCardService {

    List<CreditCard> findAll();

    CreditCard findById(int theId);

    CreditCard save(CreditCard creditCard);

    void deleteById(int theId);
}
