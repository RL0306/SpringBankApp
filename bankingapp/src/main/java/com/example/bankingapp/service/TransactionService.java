package com.example.bankingapp.service;

import com.example.bankingapp.dto.AccountDTO;
import com.example.bankingapp.dto.TransactionDTO;
import com.example.bankingapp.dto.TransactionRequest;
import com.example.bankingapp.entity.AccountEntity;
import com.example.bankingapp.entity.Action;
import com.example.bankingapp.entity.TransactionEntity;
import com.example.bankingapp.entity.UserEntity;
import com.example.bankingapp.repo.AccountRepository;
import com.example.bankingapp.repo.TransactionRepository;
import com.example.bankingapp.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    //repetitive, clean up
    public AccountDTO deposit(TransactionRequest transactionRequest) {
        AccountEntity accountEntity = accountRepository.getAccountEntityByAccountNameAndAccountNumberAndAccountSortCode(
                transactionRequest.getAccountName(),
                transactionRequest.getAccountNumber(),
                transactionRequest.getAccountSortCode()
        ).orElseThrow(() -> new IllegalStateException("Cannot find account"));        if (accountEntity.getAccountNumber().equals(transactionRequest.getAccountNumber()) && accountEntity.getAccountSortCode().equals(transactionRequest.getAccountSortCode())) {
            accountEntity.setBalance(accountEntity.getBalance() + transactionRequest.getAmount());
            accountRepository.save(accountEntity);

            TransactionEntity transactionEntity = new TransactionEntity(Action.DEPOSIT, transactionRequest.getAmount(), accountEntity, accountEntity);
            transactionRepository.save(transactionEntity);

            return mapper.map(accountEntity, AccountDTO.class);
        }

        throw new IllegalArgumentException("Cannot find account");

    }

    //    //repetitive, clean up
    public AccountDTO withdraw(TransactionRequest transactionRequest) {
        AccountEntity accountEntity = accountRepository.getAccountEntityByAccountNameAndAccountNumberAndAccountSortCode(
                transactionRequest.getAccountName(),
                transactionRequest.getAccountNumber(),
                transactionRequest.getAccountSortCode()
        ).orElseThrow(() -> new IllegalStateException("Cannot find account"));


            //VALIDATE THIS, CHECK IF USER HAS AMOUNT
            if((transactionRequest.getAmount() > accountEntity.getBalance())){
                throw new IllegalStateException("Cannot withdraw that amount");
            }

            accountEntity.setBalance(accountEntity.getBalance() - transactionRequest.getAmount());
            accountRepository.save(accountEntity);

            TransactionEntity transactionEntity = new TransactionEntity(Action.WITHDRAW, transactionRequest.getAmount(), accountEntity, accountEntity);
            transactionRepository.save(transactionEntity);

            return mapper.map(accountEntity, AccountDTO.class);
        }


    public AccountDTO transfer(Long id, TransactionRequest transactionRequest) {
        AccountEntity accountFrom = accountRepository.findById(id).orElseThrow(() -> new IllegalStateException("Cannot find account") );
        AccountEntity accountTo = accountRepository.getAccountEntityByAccountNameAndAccountNumberAndAccountSortCode(
                transactionRequest.getAccountName(),
                transactionRequest.getAccountNumber(),
                transactionRequest.getAccountSortCode()
        ).orElseThrow(() -> new IllegalStateException("Cannot find account"));

        if(accountFrom.getBalance() < transactionRequest.getAmount()){
            throw new IllegalStateException("Insufficient funds to make transfer.");
        }

        accountFrom.setBalance(accountFrom.getBalance() - transactionRequest.getAmount());
        accountTo.setBalance(accountTo.getBalance() + transactionRequest.getAmount());
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);

        TransactionEntity transactionEntity = new TransactionEntity(Action.TRANSFER, transactionRequest.getAmount(), accountTo, accountFrom);
        transactionRepository.save(transactionEntity);

        return mapper.map(accountFrom, AccountDTO.class);
        }

    public List<TransactionDTO> get(Principal principal) {
        UserEntity userEntity = userRepository.findUserByUsername(principal.getName()).orElseThrow();
        List<TransactionEntity> transactionEntityList = transactionRepository.getLastTenTransForUser(userEntity.getId());

        List<TransactionDTO> transactionDTOList = transactionEntityList.stream().map(transactionEntity -> mapper.map(transactionEntity, TransactionDTO.class)).collect(Collectors.toList());

        return transactionDTOList;
    }

//    public List<TransactionEntity> get(Principal principal) {
//
//    }
}

