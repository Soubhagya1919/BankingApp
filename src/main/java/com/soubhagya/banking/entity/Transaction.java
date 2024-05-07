package com.soubhagya.banking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //Primary key field should be of Object type because the update
    //method in Jpa are used in case of both insert and update operations
    //The operation will be decided based on whether the primary key field is
    //null or not
    private Long accountId;
    private double amount;
    private String transactionType; // DEPOSIT, WITHDRAW, TRANSFER
    private LocalDateTime timestamp;
}
