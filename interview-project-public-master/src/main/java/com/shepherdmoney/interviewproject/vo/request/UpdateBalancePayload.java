package com.shepherdmoney.interviewproject.vo.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class UpdateBalancePayload {

    private String creditCardNumber;

    // Changed to LocalDate class to facilitate with working only with the date and ignoring timestamp given
    // by Instant class or Date class
    // A change that does not affect basic function of Balance History
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM-dd-yyyy")
    private LocalDate transactionTime;

    private double transactionAmount;
}
