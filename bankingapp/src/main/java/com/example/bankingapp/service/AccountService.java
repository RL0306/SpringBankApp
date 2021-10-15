package com.example.bankingapp.service;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.dto.AccountRequest;
import com.example.bankingapp.dto.TransactionRequest;
import com.example.bankingapp.entity.TransactionEntity;
import com.example.bankingapp.entity.AccountEntity;
import com.example.bankingapp.entity.Action;
import com.example.bankingapp.entity.UserEntity;
import com.example.bankingapp.repo.AccountRepository;
import com.example.bankingapp.repo.TransactionRepository;
import com.example.bankingapp.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public List<AccountDTO> getAccounts(Principal principal){
        String username = principal.getName();

        UserEntity userEntity = userRepository.findUserByUsername(username).orElseThrow();

        List<AccountEntity> accountEntities = accountRepository.getAccountEntitiesByUserEntity(userEntity).orElseThrow();

        List<AccountDTO> accountDTOS = accountEntities.stream().map(accountEntity -> mapper.map(accountEntity, AccountDTO.class)).collect(Collectors.toList());

        return accountDTOS;

    }

    public AccountDTO createAccount(Principal principal, AccountRequest accountRequest) {
        String username = principal.getName();

        UserEntity userEntity = userRepository.findUserByUsername(username).orElseThrow();

        AccountEntity accountEntity = new AccountEntity(accountRequest.getAccountName(), accountRequest.getAccountNumber(), accountRequest.getAccountSortCode(), userEntity);

        accountRepository.save(accountEntity);

        return mapper.map(accountEntity, AccountDTO.class);
    }


}
