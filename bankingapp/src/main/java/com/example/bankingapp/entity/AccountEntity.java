package com.example.bankingapp.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String accountName;
    private String accountNumber;
    private String accountSortCode;

    private double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private UserEntity userEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "accountFrom")
    @ToString.Exclude
    private List<TransactionEntity> transactionEntityList;

    public AccountEntity(String accountName, String accountNumber, String accountSortCode, UserEntity userEntity){
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.accountSortCode = accountSortCode;
        this.userEntity = userEntity;
        this.transactionEntityList = new ArrayList<>();
    }

}
