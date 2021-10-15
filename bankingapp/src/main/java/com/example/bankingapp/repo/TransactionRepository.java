package com.example.bankingapp.repo;

import com.example.bankingapp.entity.AccountEntity;
import com.example.bankingapp.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findTop10ByAccountFromOrderByActionTimeDesc(AccountEntity accountEntity);

    @Query(value = "SELECT * FROM transaction_entity INNER JOIN account_entity on account_from_id = transaction_entity.account_from_id INNER JOIN user_entity on user_entity.id = :userId ORDER BY transaction_entity.action_time DESC LIMIT 10;", nativeQuery = true)
    List<TransactionEntity>getLastTenTransForUser(@Param("userId") Long userId);
}
