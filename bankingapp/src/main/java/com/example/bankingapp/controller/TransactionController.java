package com.example.bankingapp.controller;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.dto.TransactionDTO;
import com.example.bankingapp.dto.TransactionRequest;
import com.example.bankingapp.entity.TransactionEntity;
import com.example.bankingapp.service.AccountService;
import com.example.bankingapp.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PatchMapping(value = {"/deposit"})
    public ResponseEntity<AccountDTO> deposit(@RequestBody TransactionRequest transactionRequest){
        AccountDTO accountDTO = transactionService.deposit(transactionRequest);

        return ResponseEntity.ok(accountDTO);
    }

    @PatchMapping(value = {"/withdraw"})
    public ResponseEntity<AccountDTO> withdraw(@RequestBody TransactionRequest transactionRequest){
        AccountDTO accountDTO = transactionService.withdraw(transactionRequest);
        return ResponseEntity.ok(accountDTO);
    }

    @GetMapping()
    public ResponseEntity<List<TransactionDTO>> getRecent(Principal principal){
        List<TransactionDTO> transactionDTOS = transactionService.get(principal);
        return ResponseEntity.ok(transactionDTOS);
    }

    @PatchMapping(value = "/transfer/{id}")
    public ResponseEntity<AccountDTO> transfer(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest){
        AccountDTO accountDTO = transactionService.transfer(id, transactionRequest);
        return ResponseEntity.ok(accountDTO);
    }
}
