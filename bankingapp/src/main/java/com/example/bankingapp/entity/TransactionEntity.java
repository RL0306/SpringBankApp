package com.example.bankingapp.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Action action;

    private double amount;

    private LocalDateTime actionTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private AccountEntity accountFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private AccountEntity accountTo;

    //constructor, needed with annotation
    public TransactionEntity(Action action, double amount, AccountEntity accountTo, AccountEntity accountFrom){
        this.action = action;
        this.amount = amount;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.actionTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TransactionEntity that = (TransactionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
