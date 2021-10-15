package com.example.bankingapp.repo;

import com.example.bankingapp.entity.AccountEntity;
import com.example.bankingapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<List<AccountEntity>> getAccountEntitiesByUserEntity(UserEntity userEntity);
    Optional<AccountEntity> getAccountEntityByAccountName(String accountName);
    Optional<AccountEntity> getAccountEntityByAccountNameAndAccountNumberAndAccountSortCode(String accountName, String accountNumber, String accountSortCode);
}
