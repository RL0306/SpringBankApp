package com.example.bankingapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountDTO {
    private Long id;
    private String accountName;
    private String accountNumber;
    private String accountSortCode;
    private double balance;
    private List<TransactionDTO> transactionEntityList;
}
