package com.shepherdmoney.interviewproject.vo.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class UpdateBalancePayload {

    private String creditCardNumber;

    // Change to Date class to facilitate testing with Postman
    // Does not affect basic function of Balance History
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM-dd-yyyy")
    private LocalDate transactionTime;

    private double transactionAmount;
}
