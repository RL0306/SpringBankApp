package com.example.bankingapp.dto;


import com.example.bankingapp.entity.Action;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Action action;
    private double amount;
    private LocalDateTime actionTime;
}
