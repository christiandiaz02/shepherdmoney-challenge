package com.shepherdmoney.interviewproject.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    // Changed from Instant class to Data class for simplification
    private LocalDate date;

    private double balance;

}
