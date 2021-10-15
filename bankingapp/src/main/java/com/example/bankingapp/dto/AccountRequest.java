package com.example.bankingapp.dto;

import lombok.Data;

@Data
public class AccountRequest {
    private String accountName;
    private String accountNumber;
    private String accountSortCode;
}
