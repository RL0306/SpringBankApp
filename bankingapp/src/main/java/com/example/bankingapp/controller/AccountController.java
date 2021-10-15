package com.example.bankingapp.controller;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.dto.AccountRequest;
import com.example.bankingapp.dto.TransactionRequest;
import com.example.bankingapp.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @GetMapping()
    public ResponseEntity<List<AccountDTO>> getAll(Principal principal){


        List<AccountDTO> accounts = accountService.getAccounts(principal);

        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<AccountDTO> create(Principal principal, @RequestBody AccountRequest accountRequest){
        AccountDTO accountDTO = accountService.createAccount(principal, accountRequest);

        return ResponseEntity.ok(accountDTO);
    }
}
