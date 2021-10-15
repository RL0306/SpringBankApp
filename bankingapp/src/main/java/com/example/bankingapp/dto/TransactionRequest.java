package com.example.bankingapp.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String accountName;
    private String accountNumber;
    private String accountSortCode;
    private double amount;
}
